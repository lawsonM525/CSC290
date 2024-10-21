import chess
import random
from datetime import datetime

def bot_move(board):
    valid_moves = list(board.legal_moves)
    capture_moves = [move for move in valid_moves if board.is_capture(move)]
    if capture_moves:
        move = random.choice(capture_moves)
    else:
        move = random.choice(valid_moves)
    return move


def print_game_header():
    print("=====================================================")
    print("            CSC 290 Chess Bot Version 0.1            ")
    print("=====================================================")
    print(f"Time: {datetime.now()}")

def game():
    print_game_header()

    computer_color = input("Computer Player? (w=white/b=black): ").strip().lower()
    if computer_color not in ["w", "b"]:
        print("Invalid input. Please enter 'w' or 'b'.")
        return
    else:
        bot_is_white = True if computer_color == "w" else False
        bot_color = "white" if bot_is_white else "black"
        user_color = "Black" if bot_is_white else "White"

    fen_position = input("Starting FEN position? (hit ENTER for standard starting position): ").strip()

    if fen_position:
        board = chess.Board(fen_position)
    else:
        board = chess.Board()

    while not board.is_game_over():
        if (board.turn == chess.WHITE and bot_is_white) or (board.turn == chess.BLACK and not bot_is_white):
            move = bot_move(board)
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
            winner = "Bot" if board.turn != (chess.WHITE if bot_is_white else chess.BLACK) else user_color
            print(f"Checkmate! {winner} wins!")
            return

if __name__ == "__main__":
    game()
