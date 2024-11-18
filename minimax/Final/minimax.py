import chess
import random
from datetime import datetime

# Resources Used: Chess FEN Viewer - https://www.redhotpawn.com/chess/chess-fen-viewer.php

scores = {'p': 1, 'n': 3, 'b': 3, 'r': 5, 'q': 9,
          'P': -1, 'N': -3, 'B': -3, 'R': -5, 'Q': -9}


def evaluate_board(board):
    # Simple numerical evaluation of the current state of the board
    if board.is_game_over():
        if board.is_checkmate():
            return float('-inf') if board.turn else float('inf')
        return 0  # Draw

    value = 0
    for square in chess.SQUARES:
        piece = board.piece_at(square)
        if piece:
            value += scores.get(str(piece), 0)
    return value


def minimax(board, depth, maxPlayer, initial_depth=None):
    if initial_depth is None:
        initial_depth = depth

    if depth == 0 or board.is_game_over():
        return evaluate_board(board)

    if maxPlayer:
        best_score = float('-inf')
        best_move = None
        for move in board.legal_moves:
            board.push(move)  # tryit
            eval = minimax(board, depth - 1, False, initial_depth)
            board.pop()  # undo trial
            if eval > best_score:
                best_score = eval
                best_move = move
        if depth == initial_depth:
            return best_move
        return best_score
    else:
        best_opp_score = float('inf')
        best_move = None
        for move in board.legal_moves:
            board.push(move)
            eval = minimax(board, depth - 1, True, initial_depth)
            board.pop()
            if eval < best_opp_score:
                best_opp_score = eval
                best_move = move
        if depth == initial_depth:
            return best_move
        return best_opp_score


def bot_move(board):
    valid_moves = list(board.legal_moves)
    capture_moves = [move for move in valid_moves if board.is_capture(move)]
    if capture_moves:
        move = random.choice(capture_moves)
    else:
        move = random.choice(valid_moves)
    return move


def test_bot_move(fen, bot_is_white, description, continue_game=True):
    print(f"\nTest: {description}")
    board = chess.Board(fen)
    user_color = "Black" if bot_is_white else "White"

    if (board.turn == chess.WHITE) == bot_is_white:
        move = minimax(board, 3, True, 3)
        board.push(move)
        print(f"Bot moved: {move}")
    else:
        print(f"It's {user_color}'s turn to move")
        while True:
            user_move = input(f"{user_color}: ")
            try:
                move = chess.Move.from_uci(user_move)
                if move in board.legal_moves:
                    board.push(move)
                    break
                else:
                    print("Invalid move. Try again.")
            except ValueError:
                print("Format should be in UCI (e.g., e2e4). Try again.")

    print(f"New position: {board.fen()}")

    if continue_game:
        print("\nContinuing with normal game...")
        game(starting_fen=board.fen(), starting_color="w" if bot_is_white else "b")


def test_case_bot_standard_white():
    fen = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1'
    bot_is_white = True
    description = "Bot starts as white from the standard position"
    test_bot_move(fen, bot_is_white, description)


def test_case_bot_standard_black():
    fen = 'rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1'
    bot_is_white = False
    description = "Bot starts as black from the standard position"
    test_bot_move(fen, bot_is_white, description)


def test_case_bot_puts_opponent_in_check():
    fen = "rnbqkbnr/ppp2pp1/7p/3pp3/3P1Q2/8/PPP1PPPP/RNB1KBNR w KQkq - 0 3"
    bot_is_white = True
    description = "Bot (white) is about to put black in check. White queen is diagonally one step from capturing a pawn that is two steps in front of the king. It *should* want to capture the pawn with the queen to put the black king in check."
    test_bot_move(fen, bot_is_white, description, continue_game=False)


def test_case_bot_is_in_check():
    fen = "rnbqkbnr/pppp1ppp/8/4Q3/8/8/PPPP1PPP/RNB1KBNR b KQkq - 1 2"
    bot_is_white = False
    description = "Bot (black) is currently in check. White queen could immediately capture black king"
    test_bot_move(fen, bot_is_white, description, continue_game=False)


def print_game_header():
    print("=====================================================")
    print("            CSC 290 Chess Bot Version 0.1            ")
    print("=====================================================")
    print(f"Time: {datetime.now()}")


def game(starting_fen=None, starting_color=None):
    print_game_header()

    if starting_color is None:
        computer_color = input(
            "Computer Player? (w=white/b=black): ").strip().lower()
    else:
        computer_color = starting_color

    if computer_color not in ["w", "b"]:
        print("Invalid input. Please enter 'w' or 'b'.")
        return
    else:
        bot_is_white = True if computer_color == "w" else False
        bot_color = "white" if bot_is_white else "black"
        user_color = "Black" if bot_is_white else "White"

    if starting_fen is None:
        fen_position = input(
            "Starting FEN position? (hit ENTER for standard starting position): ").strip()
    else:
        fen_position = starting_fen

    if fen_position:
        board = chess.Board(fen_position)
    else:
        board = chess.Board()

    while not board.is_game_over():
        if (board.turn == chess.WHITE and bot_is_white) or (board.turn == chess.BLACK and not bot_is_white):
            initial_depth = 3  # set the search depth.. variable so we can change it
            move = minimax(board, initial_depth, True, initial_depth)
            board.push(move)
            print(f"Bot (as {bot_color}): {move}")
        else:
            user_move = input(f"{user_color}: ")
            try:
                move = chess.Move.from_uci(user_move)
                if move in board.legal_moves:
                    board.push(move)
                else:
                    print("Invalid move. Try again.")
            except ValueError:
                print("Format should be in UCI (e.g., e2e4). Try again.")
                continue

        print(f"New FEN position: {board.fen()}")

        if board.is_checkmate():
            winner = "Bot" if board.turn != (
                chess.WHITE if bot_is_white else chess.BLACK) else user_color
            print(f"Checkmate! {winner} wins!")
            return


if __name__ == "__main__":
    print("\nChoose a starting situation:")
    print("1. New game from scratch")
    print("2. Test: Bot starts as white from standard position")
    print("3. Test: Bot starts as black after e4")
    print("4. Test: Bot (white) can put black in check")
    print("5. Test: Bot (black) must escape check")

    choice = input("\nEnter choice (1-5): ").strip()

    match choice:
        case "1":
            game()
        case "2":
            test_case_bot_standard_white()
            # played this out a bit and it works. put the black king in check in just 3 moves!!
        case "3":
            test_case_bot_standard_black()
            # played this out a bit and also seems to work ✅
        case "4":
            test_case_bot_puts_opponent_in_check()
            # played this out. white queen puts black king in check... although not in the way we expected.
        case "5":
            test_case_bot_is_in_check()
            # played this out. bot successfully escapes check by protecting king with the horse. ✅
        case _:
            print("Invalid choice. Please run again and select 1-5.")
