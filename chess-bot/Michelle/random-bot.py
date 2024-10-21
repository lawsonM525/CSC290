import chess
import chess.pgn
import chess.svg
import random
import datetime

#  header with time and date
print("=" * 50)
print("             CS 290 Chess Bot Version 0.1            ")
print("=" * 50)
print(f"Time: {datetime.datetime.now()}")

# user input which side (white or black)
bot_color = input("Computer Player? (w=white/b=black): ").strip().lower()
while bot_color not in ("w", "b"):
    print("Invalid input! Please enter 'w' for white or 'b' for black.")
    bot_color = input("Computer Player? (w=white/b=black): ").strip().lower()

# starting FEN position
'''
FEN is a type of notation - forsyth edwards notation
describes current position of all the pieces, whose turn t is, who has rights to do certain moves, etc
to be able to test well, we need to understand FEN
'''
starting_fen = input("Starting FEN position? (hit ENTER for standard starting position): ").strip()
board = chess.Board(starting_fen) if starting_fen else chess.Board()

# Random-playing  function
def choose_random_move(board):
    legal_moves = list(board.legal_moves)
    # Get capture moves
    # capture moves = kill moves like when a piece can take another piece off the board
    capture_moves = [move for move in legal_moves if board.is_capture(move)]
    # If capture moves exist, choose one at random, otherwise, choose any legal move at random
    if capture_moves:
        return random.choice(capture_moves)
    else:
        return random.choice(legal_moves)

def print_board_status(board):
    print(f"New FEN position: {board.fen()}")
    print(board)

# Main game loop

# determine who plays first
# if bot is black, user moves first because user is white and white goes first
user_to_move = bot_color != "w" 

while not board.is_game_over():

    if user_to_move:
        user_move = input(f"{'White' if board.turn == chess.WHITE else 'Black'}: ").strip()
        try:
            ## using UCI notation to declare moves
            move = chess.Move.from_uci(user_move)
            if move in board.legal_moves:
                board.push(move)
                user_to_move = False
            else:
                print("Invalid move! Try again.")
        except ValueError:
            print("Invalid input! Enter move in UCI notation (e.g., e2e4).")
    else:
        # Bot makes a move
        move = choose_random_move(board)
        board.push(move)
        print(f"Bot ({'White' if board.turn == chess.BLACK else 'Black'}): {move}")
        user_to_move = True
    print_board_status(board)

# Print game over status
print("=" * 50)
print("Game over!")
print(board.result())
if board.is_checkmate():
    print("Checkmate!")
elif board.is_stalemate():
    print("Stalemate!")
elif board.is_insufficient_material():
    print("Draw by insufficient material!") # not enoguh pieces for any player to get a checkmate
else:
    print("The game has ended in a draw.")
