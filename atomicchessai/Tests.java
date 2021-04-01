package atomicchessai;

import java.util.ArrayList;


public class Tests {

    public static void main(String[] args) {
        System.out.println("ai test");
        return;
    }

    public static void runAllTests() {

        //tests return true if the code being tested works
        //false, if the code failed

        return;
    }

    public static boolean testSetupInitialBoard() {

        //ensures that the correct IDs are placed into 
        //the positions array
        //see "Piece IDs & Positions.txt"

        BoardState boardState = new BoardState();
        boardState.setupInitialBoard();
        //if any of these are false, then the test fails
        if (
            boardState.positions[0][0] != 1 ||
            boardState.positions[0][1] != 2 ||
            boardState.positions[0][2] != 3 ||
            boardState.positions[0][3] != 4 ||
            boardState.positions[0][4] != 5 ||
            boardState.positions[7][0] != 8 ||
            boardState.positions[7][1] != 9 ||
            boardState.positions[7][2] != 10 ||
            boardState.positions[7][3] != 11 ||
            boardState.positions[7][4] != 12
            ) {
                return false;
            }

        //now make sure all pawns are in place
        for (int x=0; x<8; x++) {
            if (
                boardState.positions[x][1] != 6 ||
                boardState.positions[x][6] != 7) {
                    return false;
                }
        }

        //now make sure that all the empty squares are indeed empty
        for (int x=0; x<8; x++) {
            for (int y=2; y<6; y++) {
                if (boardState.positions[x][y] != 0) {
                    return false;
                }
            }
        }

        //all sub tests passed, therefore the entire test passes
        return true;
    }

    public static boolean testMove() {

        BoardState boardState = new BoardState();
        //place a piece, them move the piece
        boardState.positions[0][0] = 1;
        boardState.move(0, 0, 7, 7);

        //make sure that the piece isn't in the old location
        //and that it IS is the new location
        if (
            boardState.positions[0][0] == 1 ||
            boardState.positions[7][7] == 0
            ) {
                return false;
            }

        return true;
    }

    public static boolean testExplode() {
        BoardState boardState = new BoardState();
        //setup a 9x9 square to explode
        //0 = empty, 6 = pawn, 5 = queen
        //
        // 6 6 6
        // 5 6 5  
        // 5 0 0
        boardState.positions[0][0] = 6;
        boardState.positions[0][1] = 6;
        boardState.positions[0][2] = 6;
        boardState.positions[1][0] = 5;
        boardState.positions[1][1] = 6;
        boardState.positions[1][2] = 5;
        boardState.positions[2][0] = 5;
        boardState.positions[2][1] = 0;
        boardState.positions[2][2] = 0;

        //cause an explosion and make sure that the queens
        //were removed and that the pawns were not
        //the pawn in the middle should be removed though
        boardState.explode(1, 1);
        if (
            boardState.positions[0][0] != 6 ||
            boardState.positions[0][1] != 6 ||
            boardState.positions[0][2] != 6 ||
            boardState.positions[1][0] != 0 ||
            boardState.positions[1][1] != 0 ||
            boardState.positions[1][2] != 0 ||
            boardState.positions[2][0] != 0 ||
            boardState.positions[2][1] != 0 ||
            boardState.positions[2][2] != 0 
        ) {
            return false;
        }

        return true;
    }

    public static boolean testCheckVictory() {

        //the board is in victory state, if the TURN player
        //has no valid moves
        
        //first test to see if when the king is removed, then it
        //correctly determines that the game is over
        //
        //initialize board as empty
        //so technically both white and black should be considered
        //victorious
        BoardState boardState = new BoardState();
        //check for black is checkmate
        boardState.whitesTurn = false;
        if (boardState.checkVictory() == false) {
            return false;
        }
        //check for white in checkmate
        boardState.whitesTurn = true;
        if (boardState.checkVictory() == false) {
            return false;
        }

        //place the black king is checkmate
        //by placing the king in the back rank
        //k. r
        //q. r
        //will place the king in checkmate, but only in atomic rules
        //to make sure that the algo takes into account explosions
        //black king = 4
        //black queen = 5
        //white rook = 8
        
        boardState.positions[0][0] = 4;
        boardState.positions[0][1] = 5;
        boardState.positions[1][0] = 8;
        boardState.positions[1][1] = 8;
        //black king is now in checkmate
        //so make sure that it is blacks turn
        boardState.whitesTurn = false;
        if (boardState.checkVictory() == false) {
            return false;
        }

        //white king = 11
        //white queen = 12
        //black rook = 1
        boardState.positions[0][0] = 11;
        boardState.positions[0][1] = 12;
        boardState.positions[1][0] = 1;
        boardState.positions[1][1] = 1;
        //white king is now in checkmate
        //so make sure that it is whites turn
        boardState.whitesTurn = true;
        if (boardState.checkVictory() == false) {
            return false;
        }
        //all sub tests passed, for this unit test passes
        return true;
    }

    public static boolean testGenerateValidMoves() {

        BoardState boardState = new BoardState();

        //there should be no valid moves before any pieces are on the board
        if (boardState.generateValidMoves().size() != 0) {
            return false;
        }

        //place a pawn in a starting position and a rook on the board
        //white pawn = 7, in starting position
        boardState.positions[0][6] = 7;
        //white rook = 8, right staring position
        boardState.positions[7][7] = 8;

        //there should be 2 moves for the pawn
        //and 14 moves for the rook
        boardState.whitesTurn = true;
        if (boardState.generateValidMoves().size() != 14) {
            return false;
        }

        return true;
    }

    public static boolean testRemoveSuicideMoves() {
        //create a list of moves that contains exactly 1 suicide
        //move
        ArrayList<Move> moveList = new ArrayList<Move>();

        //test to make sure that the suicide move was removed and
        //that no OTHER move was removed

        return true;
    }

}