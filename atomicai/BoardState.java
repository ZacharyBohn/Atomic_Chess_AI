package atomicai;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.stream.IntStream;

//used for reading/writing to/from disk
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class BoardState  implements Serializable {
    //must be defined to implement serializable
    private static final long serialVersionUID = 44005L;
    public Move initiatingMove;
    public int desirability;
    public BoardState parent;
    public int[][] positions;
    public ArrayList<Move> validMoves;
    //this is the map that stores what moves turn into what
    //board states
    public ArrayList<BoardState> knownMoves;
    public boolean whitesTurn;
    //these are used to determine if a player can castle
    public boolean whiteKingMoved;
    public boolean blackKingMoved;
    public boolean whiteRook0Moved;
    public boolean blackRook0Moved;
    public boolean whiteRook7Moved;
    public boolean blackRook7Moved;
    public boolean validMovesGenerated;
    //only used to determine if a piece is black or white
    public static int[] whitePieces;
    public static int[] blackPieces;
    static {
        blackPieces = new int[6];
        whitePieces = new int[6];
        blackPieces[0] = 1; //rook
        blackPieces[1] = 2; //knight
        blackPieces[2] = 3; //bishop
        blackPieces[3] = 4; //king
        blackPieces[4] = 5; //queen
        blackPieces[5] = 6; //pawn
        whitePieces[0] = 7; //pawn
        whitePieces[1] = 8; //rook
        whitePieces[2] = 9; //knight
        whitePieces[3] = 10; //bishop
        whitePieces[4] = 11; //king
        whitePieces[5] = 12; //queen
    }

    public BoardState() {
        //initialize variables in default states
        desirability = 1000;
        positions = new int[8][8];
        knownMoves = new ArrayList<BoardState>();
        validMoves = new ArrayList<Move>();
        whitesTurn = true;
        whiteKingMoved = false;
        blackKingMoved = false;
        whiteRook0Moved = false;
        whiteRook7Moved = false;
        blackRook0Moved = false;
        blackRook7Moved = false;
        validMovesGenerated = false;
        return;
    }

    public void setupInitialBoard() {
        //set starting positions for the chess game

        //black back rank
        positions[0][0] = 1;
        positions[1][0] = 2;
        positions[2][0] = 3;
        positions[3][0] = 4;
        positions[4][0] = 5;
        positions[5][0] = 3;
        positions[6][0] = 2;
        positions[7][0] = 1;

        //black pawns
        for (int x=0; x<8; x++) {
            positions[x][1] = 6; 
        }

        //white back rank
        positions[0][7] = 8;
        positions[1][7] = 9;
        positions[2][7] = 10;
        positions[3][7] = 11;
        positions[4][7] = 12;
        positions[5][7] = 10;
        positions[6][7] = 9;
        positions[7][7] = 8;

        //white pawns
        for (int x=0; x<8; x++) {
            positions[x][6] = 7; 
        }

        return;
    }

    public void move(Move move) {
        move(move.fromX, move.fromY, move.toX, move.toY);
        return;
    }

    public void move(int oldX, int oldY, int newX, int newY) {
        //move piece from oldX, oldY to newX, newY
        //this does not take into account legality

        boolean opposingPieces = !(IntStream.of(whitePieces).anyMatch(
                x -> x == positions[oldX][oldY]) == 
            IntStream.of(whitePieces).anyMatch(
                x -> x == positions[newX][newY]
            ));

        if (opposingPieces) { explode(newX, newY); }
        else{
            positions[newX][newY] = positions[oldX][oldY];
        }

        positions[oldX][oldY] = 0;

        return;
    }

    public BoardState branch(Move branchingMove) {
        //branch the map for the given move
        BoardState subState = new BoardState();
        copyTo(subState);
        subState.initiatingMove = branchingMove;
        subState.move(branchingMove);
        subState.whitesTurn = !whitesTurn;
        knownMoves.add(subState);
        return subState;
    }

    public void explode(int x, int y) {
        //remove the piece from x, y and
        //explode all pieces within 1 space of x, y
        //except pawns
        positions[x][y] = 0;
        //need to create a sequence of numbers to loop through in order
        //to check the spaces around the given x,y
        //
        //however, checking spaces that dont exist (ie -1) needs to be
        //avoided.  The xList and yList below represent which spaces
        //to check relative to the given x,y.  These lists will not
        //go outside the board
        ArrayList<Integer> xList = new ArrayList<Integer>();
        ArrayList<Integer> yList = new ArrayList<Integer>();
        xList.add(0);
        yList.add(0);
        //0 is the left most space of the board
        if (x == 0) { xList.add(1); }
        //7 is the right most space of the board
        else if (x == 7) { xList.add(-1); }
        else {
            xList.add(-1);
            xList.add(1);
        }
        if (y == 0) { yList.add(1); }
        else if (y == 7) { yList.add(-1); }
        else {
            yList.add(-1);
            yList.add(1);
        }

        for (int spaceX : xList) {
            for (int spaceY : yList) {
                //6 and 7 are pawns id's
                if (positions[x + spaceX][y + spaceY] != 6 &&
                positions[x + spaceX][y + spaceY] != 7) {
                    positions[x + spaceX][y + spaceY] = 0;
                }
            }
        }

        return;
    }

    public boolean checkVictory() {
        //check to see if the board is currently within a victory state
        //either because a king is dead, or a king is in checkmate

        //check to see if both kings are on the board
        boolean whiteKingPresent = false;
        boolean blackKingPresent = false;

        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                //11 is the white king
                //4 is the black king
                if (positions[x][y] == 11) { whiteKingPresent = true; }
                if (positions[x][y] == 4) { blackKingPresent = true; }
            }
        }

        if (!whiteKingPresent || !blackKingPresent) { return true; }

        //if the turn player has no valid moves, then they are in checkmate
        generateValidMoves();
        if (validMoves.size() == 0) { return true; }

        return false;
    }

    public boolean lookAhead(int turns) {
        //see if victory is possible with X turns ahead
        //this does NOT mean forced checkmate, just possible checkmate
        //
        //warning!  recursion ahead! =0

        //base case
        if (turns == 1) {
            BoardState nextMove;
            for (Move m : validMoves) {
                nextMove = new BoardState();
                copyTo(nextMove);
                nextMove.move(m);
                if (nextMove.checkVictory()) { return true; }
            }
            return false;
        }

        //recursive part
        BoardState nextMove;
        for (Move m : validMoves) {
            nextMove = new BoardState();
            copyTo(nextMove);
            nextMove.generateValidMoves();
            nextMove.move(m);
            if (nextMove.lookAhead(turns - 1)) {
                return true;
            }
        }

        return false;
    }

    public boolean isValid(Move move) {
        //check if a move is within the valid moves list
        if (!validMovesGenerated) { generateValidMoves(); }
        if (containsMove(validMoves, move)) { return true; }
        return false;
    }

    public boolean opponentMoveTo(int x, int y) {
        //check if the non-turn player can move any piece to
        //x, y

        BoardState b = new BoardState();
        copyTo(b);
        b.whitesTurn = !whitesTurn;
        b.generateValidMoves();
        for (Move m : b.validMoves) {
            if (m.toX == x && m.toY == y) { return true; }
        }

        return false;
    }

    public boolean inCheck() {
        //checks if the turn player is in check
        //if the king is not on the board, this will return false

        int turnPlayerKingId;
        if (whitesTurn) { turnPlayerKingId = 11; }
        if (!whitesTurn) { turnPlayerKingId = 4; }
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                if (positions[x][y] == turnPlayerKingId) {
                    if (opponentMoveTo(x, y)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean kingAlive(boolean whitesKing) {
        //checks if a specific king is still on the board

        int turnPlayerKingId;
        if (whitesTurn) { turnPlayerKingId = 11; }
        if (!whitesTurn) { turnPlayerKingId = 4; }
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                if (positions[x][y] == turnPlayerKingId) {
                    return true;
                }
            }
        }

        return false;
    }

    public void generateValidMoves() {
        //generate all valid moves for the turn player
        validMoves = new ArrayList<Move>();

        int[] turnPlayerPieces = new int[6];

        if (whitesTurn) { turnPlayerPieces = whitePieces; }
        if (!whitesTurn) { turnPlayerPieces = blackPieces; }

        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                if (containsPiece(turnPlayerPieces, positions[x][y])) {
                    for (Move m : createMoves(x, y)) { validMoves.add(m); }
                }
            }
        }

        removeSuicideMoves(validMoves);
        validMovesGenerated = true;

        return;
    }

    private ArrayList<Move> removeSuicideMoves(ArrayList<Move> moves) {
        //the moves given will be semi-valid, some of them may include
        //moves that kill one's own king or place one's own king in check.
        //remove these

        ArrayList<Move> trueValidMoves = new ArrayList<Move>();
        BoardState nextMove;
        for (Move m : moves) {
            nextMove = new BoardState();
            copyTo(nextMove);
            nextMove.move(m);
            if (nextMove.kingAlive(nextMove.whitesTurn) && !nextMove.inCheck()) {
                trueValidMoves.add(m);
            }
        }

        return trueValidMoves;
    }

    public ArrayList<Move> createMoves(int x, int y) {
        //generate the moves for the piece at location x,y
        return new ArrayList<Move>();
    }

    private ArrayList<Move> pawnMoves (int x, int y) {
        //generate all valid moves for a pawn at x,y
        return new ArrayList<Move>();
    }

    private ArrayList<Move> rookMoves (int x, int y) {
        //generate all valid moves for a rook at x,y
        return new ArrayList<Move>();
    }

    private ArrayList<Move> knightMoves (int x, int y) {
        //generate all valid moves for a knight at x,y
        return new ArrayList<Move>();
    }

    private ArrayList<Move> bishopMoves (int x, int y) {
        //generate all valid moves for a bishop at x,y
        return new ArrayList<Move>();
    }

    private ArrayList<Move> queenMoves (int x, int y) {
        //generate all valid moves for a queen at x,y
        return new ArrayList<Move>();
    }

    private ArrayList<Move> kingMoves (int x, int y) {
        //generate all valid moves for a king at x,y
        return new ArrayList<Move>();
    }

    private ArrayList<Move> directionalMoves (int x, int y, int dirX, int dirY) {
        //generate all valid moves for a piece at x,y moving along
        //the given direction
        return new ArrayList<Move>();
    }

    public boolean containsMove(ArrayList<Move> arrayList, Move move) {
        //return whether or not the given move is in the given list
        return true;
    }

    public boolean containsPiece(int[] pieceArray, int piece) {
        //returns whether the given piece is in the given array
        for (int x : pieceArray) {
            if (x == piece) { return true; }
        }
        return false;
    }

    public void copyTo(BoardState otherState) {
        //copy all relevent values from self to the given board state
        //this will NOT copy known moves list or the valid moves list

        //copy each position indivitually
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                otherState.positions[x][y] = positions[x][y];
            }
        }

        otherState.whitesTurn = whitesTurn;
        otherState.whiteKingMoved = whiteKingMoved;
        otherState.blackKingMoved = blackKingMoved;
        otherState.whiteRook0Moved = whiteRook0Moved;
        otherState.blackRook0Moved = blackRook0Moved;
        otherState.whiteRook7Moved = whiteRook7Moved;
        otherState.blackRook7Moved = blackRook7Moved;

        return;
    }

    public void serialize() {
        //this will prepare the board state (and all children)
        //to be written to the disk
        return;
    }

    public void deserialize() {
        //this is called after the baordstate has been read from the disk
        //and placed in memory, it re-creates the structure of the board state map
        return;
    }
}