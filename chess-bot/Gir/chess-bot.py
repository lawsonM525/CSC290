import chess
import datetime
import random

#Global Variables
board = chess.Board()
gameIsActive = False
isBotFirst = True
isFirstRound = True

#Beginning the Game
def start():
    print("=====================================================")
    print("             CS 290 Chess Bot Version 0.1")
    print("=====================================================")
    print(datetime.datetime.now())
    #determine who goes first
    computerColor = input("Computer Player? (w=white/b=black): ")
    if(computerColor == 'b'):
        print("Computer goes second.")	
        isBotFirst = False
    if(computerColor == 'w'):
        print("Computer goes first.")
    else:
        print("Invalid input, Computer will be White.")
    #determine starting position
    startFENPosition = input("Starting FEN position? (hit ENTER for standard starting postion): ")
    if (startFENPosition == ''):
        board = chess.Board() #redundant, but resets the board
    else:
        board = chess.Board(startFENPosition)
    #making the game active
    gameIsActive = True

#Check the Board Every Turn
def boardChecker():
    if(board.is_checkmate()):
        print("Checkmate!")
        gameIsActive = False
    if(board.is_stalemate()):
        print("Stalemate! Draw!")
        gameIsActive = False
    if(board.is_check()):
        print("The board is currently in Check!")
        #there may be special rules for after check

#Print the Board
def printBoard():
    print("New FEN position: ")
    print(board.fen())
    boardChecker()

#Bot's turn
def botTurn():
    botMove = "" #empty
    max = 1 #placeholder

    #generate legal captures to prioritze
    if (board.generate_legal_captures.count() != 0):
        #randomly pick from the legal captures
        botMove = random.choice(board.generate_legal_captures)
    elif(board.generate_legal_moves.count() != 0):
        #randomly pick from the legal moves
        botMove = random.choice(board.generate_legal_moves)
    else:
        print("Something is Wrong")
        return


    # skip if the first move is for the Player. continue otherwise
    if (isFirstRound and not isBotFirst): 
        isFirstRound = False #turn it off
        return
    else:
        isFirstRound = False #turn it off
    
    if isBotFirst: #White
        board.push(botMove)
        print(f"Bot (as white): {botMove}")
    elif not isBotFirst: #Black
        board.push(botMove)
        print(f"Bot (as black): {botMove}")

    #display New FEN position
    printBoard()

def playerTurn():
    #Ask Input
    if isBotFirst: #Black
        move = input("White: ")
    else: #White
        board.push(move)
        move = input("White: ")
    #Make move
    board.push(move)

    #display New FEN position
    printBoard()

def game():
    start()
    while(gameIsActive):
        botTurn()
        playerTurn()

game()
