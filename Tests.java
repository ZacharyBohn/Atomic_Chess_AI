import java.util.ArrayList;
import manifold.ext.api.Jailbreak;

public class Tests {

    public static void main(String[] args) {
        runAllTests();
    }

    //NOT IMPLEMENTED
    public static void runAllTests() {

        //tests return true if the code being tested works
        //false, if the code failed

        System.out.println();

        System.out.println("Testing setupInitialBoard()");
        if (testSetupInitialBoard()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing move()");
        if (testMove()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing explode()");
        if (testExplode()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing checkVictory()");
        if (testCheckVictory()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing generateValidMoves()");
        if (testGenerateValidMoves()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing removeSuicideMoves()");
        if (testRemoveSuicideMoves()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing opponentMoveTo()");
        if (testOpponentMoveTo()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing lookAhead()");
        if (testLookAhead()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing pawnMoves()");
        if (testPawnMoves()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing rookMoves()");
        if (testRookMoves()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing knightMoves()");
        if (testKnightMoves()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing bishopMoves()");
        if (testBishopMoves()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing queenMoves()");
        if (testQueenMoves()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing kingMoves()");
        if (testKingMoves()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing directionalMoves()");
        if (testDirectionalMoves()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing saveToDisk()");
        if (testSaveToDisk()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        System.out.println("Testing readFromDisk()");
        if (testReadFromDisk()) { System.out.println("ok"); }
        else { System.out.println("test failed"); }
        System.out.println();

        return;
    }

    public static boolean testSetupInitialBoard() {

        //ensures that the correct IDs are placed into 
        //the positions array
        //see "Piece IDs & Positions.txt"

        BoardState boardState = new BoardState();
        boardState.setupInitialBoard();
        if (boardState.positions == null) { return false; }
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
        boardState.setupInitialBoard();
        if (boardState.positions == null) { return false; }
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
        boardState.positions = new int[8][8];
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
        boardState.positions = new int[8][8];
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

        //add both kings to the board and check victory
        //it should come back as false
        for (int x=0; x<8; x++) {
            for (int y=0; y<8;y++) {
                boardState.positions[x][y] = 0;
            }
        }
        //white king = 11
        boardState.positions[0][0] = 11;
        //black king = 4
        boardState.positions[7][7] = 4;
        if (boardState.checkVictory() == true) {
            return false;
        }

        //all sub tests passed, for this unit test passes
        return true;
    }

    public static boolean testGenerateValidMoves() {

        BoardState boardState = new BoardState();
        boardState.positions = new int[8][8];
        //there should be no valid moves before any pieces are on the board
        boardState.generateValidMoves();
        if (boardState.validMoves.size() != 0) {
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
        boardState.generateValidMoves();
        if (boardState.validMoves.size() != 14) {
            return false;
        }

        return true;
    }

    public static boolean testRemoveSuicideMoves() {
        
        BoardState boardState = new BoardState();
        boardState.setupInitialBoard();
        //move the white queen to a position that pins a
        //black pawn to the black king
        //white queen = 11
        boardState.positions[4][7] = 0;
        boardState.positions[7][3] = 11;
        boardState.whitesTurn = false;
        boardState.generateValidMoves();
        //move pawn up once
        Move suicideMove1 = new Move(3, 1, 3, 2);
        //move pawn up twice
        Move suicideMove2 = new Move(3, 1, 3, 3);
        if (boardState.containsMove(boardState.validMoves, suicideMove1) ||
            boardState.containsMove(boardState.validMoves, suicideMove2)) {
                return false;
            }
        //there should now be 17 moves left that are valid for black
        if (boardState.validMoves.size() != 17) {
            return false;
        }

        return true;
    }

    public static boolean testOpponentMoveTo() {

        //setup a board state instance with only a few
        //moves for black with a couple obscure rules coming
        //into play
        BoardState boardState = new BoardState();
        boardState.positions = new int[8][8];
        //set a black king and black rook on the board
        //black king = 4
        //black rook = 1
        boardState.positions[0][0] = 4;
        boardState.positions[1][0] = 1;
        //place a white king, white pawn and white rook on the board
        //white king = 11
        //white pawn = 7
        //white rook = 8
        boardState.positions[1][7] = 11;
        boardState.positions[1][6] = 7;
        boardState.positions[2][0] = 8;
        //black should be able to move to 4 squares
        //1,6
        //2,0
        //1,1
        //0,1
        boardState.whitesTurn = true;
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                //check if the current square should be movable to by black
                if (x == 1 && y == 6 && boardState.opponentMoveTo(x, y) == false) {
                    return false;
                }
                else if (x == 2 && y == 0 && boardState.opponentMoveTo(x, y) == false) {
                    return false;
                }
                else if (x == 1 && y == 1 && boardState.opponentMoveTo(x, y) == false) {
                    return false;
                }
                else if (x == 0 && y == 1 && boardState.opponentMoveTo(x, y) == false) {
                    return false;
                }
                else if (boardState.opponentMoveTo(x, y) == true) {
                    //black should NOT be able to move here
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean testLookAhead() {

        BoardState boardState = new BoardState();
        boardState.setupInitialBoard();
        boardState.whitesTurn = true;
        if (boardState.lookAhead(2) == true ||
            boardState.lookAhead(3) == false ||
            boardState.lookAhead(4) == false) {
                //then look ahead is not functioning correctly
                return false;
            }
        
        //move the white queen to a position to blow up the black king
        boardState.positions[3][7] = 0;
        boardState.positions[2][3] = 12;
        if (boardState.lookAhead(1) == false) {
            return false;
        }

        return true;
    }

    public static boolean testPawnMoves() {

        BoardState boardState = new BoardState();
        boardState.positions = new int[8][8];
        //place white pawn in its initial square
        boardState.positions[0][6] = 7;
        //place the black pawn on a square other than its initial one
        boardState.positions[0][2] = 6;
        //create something for the black pawn to attack
        boardState.positions[1][3] = 12;
        ArrayList<Move> validMoves = boardState.generateValidMoves();
        //create the two valid moves for the white pawn
        Move move1 = new Move(0, 6, 0, 5);
        Move move2 = new Move(0, 6, 0, 4);
        //black pawn moves
        Move move3 = new Move(0, 2, 0, 3);
        Move move4 = new Move(0, 2, 1, 3);

        //now valid moves should contain all 4 of the explicityly defined moves
        //and no other moves
        if (validMoves.size() != 4 ||
            boardState.containsMove(validMoves, move1) == false ||
            boardState.containsMove(validMoves, move2) == false ||
            boardState.containsMove(validMoves, move3) == false ||
            boardState.containsMove(validMoves, move4) == false) {
                return false;
            }

        return true;
    }

    public static boolean testRookMoves() {

        BoardState boardState = new BoardState();
        boardState.positions = new int[8][8];
        boardState.positions[0][0] = 8;
        boardState.generateValidMoves();
        if (boardState.validMoves != 14) {
            return false;
        }

        return true;
    }

    public static boolean testKnightMoves() {

        BoardState boardState = new BoardState();
        boardState.positions = new int[8][8];
        boardState.positions[4][3] = 9;
        boardState.generateValidMoves();
        if (boardState.validMoves != 8) {
            return false;
        }

        return true;
    }

    public static boolean testBishopMoves() {

        BoardState boardState = new BoardState();
        boardState.positions = new int[8][8];
        boardState.positions[1][1] = 10;
        boardState.generateValidMoves();
        if (boardState.validMoves != 9) {
            return false;
        }

        return true;
    }

    public static boolean testQueenMoves() {

        BoardState boardState = new BoardState();
        boardState.positions = new int[8][8];
        boardState.positions[0][0] = 12;
        boardState.generateValidMoves();
        if (boardState.validMoves != 21) {
            return false;
        }

        return true;
    }

    public static boolean testKingMoves() {

        BoardState boardState = new BoardState();
        boardState.positions = new int[8][8];
        boardState.positions[3][3] = 11;
        boardState.generateValidMoves();
        if (boardState.validMoves != 8) {
            return false;
        }

        return true;
    }

    public static boolean testDirectionalMoves() {

        BoardState boardState = new BoardState();
        ArrayList<Move> moves = boardState.directionalMoves(0, 0, 1, 0);
        if (moves.size() != 7) {
            return false;
        }
        ArrayList<Move> moves2 = boardState.directionalMoves(7, 7, -1, -1);
        if (moves2.size() != 7) {
            return false;
        }

        return true;
    }

    public static boolean testContainsMove() {

        ArrayList<Move> moves = new ArrayList<Move>();
        Move move = new Move(0, 0, 1 ,1);
        moves.add(move);
        BoardState boardState = new BoardState();
        if (boardState.containsMove(moves, move) == false) {
            return false;
        }

        return true;
    }

    //NOT IMPLEMENTED
    public static boolean testSaveAndRead() {

        //save something to the disk
        //then read it and see if it is the same

        return true;
    }

}