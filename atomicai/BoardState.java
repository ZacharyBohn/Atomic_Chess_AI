package atomicai;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
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
    public Move lastMove;
    public boolean validMovesGenerated;
    public boolean currentlyGeneratingValidMoves;
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
    public static HashMap<Integer, String> friendlyIds;
    static {
        friendlyIds = new HashMap<Integer, String>();
        friendlyIds.put(0, "- ");
        friendlyIds.put(1, "r.");
        friendlyIds.put(2, "n.");
        friendlyIds.put(3, "b.");
        friendlyIds.put(4, "k.");
        friendlyIds.put(5, "q.");
        friendlyIds.put(6, "p.");
        friendlyIds.put(7, "p ");
        friendlyIds.put(8, "r ");
        friendlyIds.put(9, "n ");
        friendlyIds.put(10, "b ");
        friendlyIds.put(11, "k ");
        friendlyIds.put(12, "q ");
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
        currentlyGeneratingValidMoves = false;
        return;
    }

    public void setupInitialBoard() {
        //set starting positions for the chess game

        //black back rank
        positions[0][0] = 1;
        positions[1][0] = 2;
        positions[2][0] = 3;
        positions[3][0] = 5;
        positions[4][0] = 4;
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
        positions[3][7] = 12;
        positions[4][7] = 11;
        positions[5][7] = 10;
        positions[6][7] = 9;
        positions[7][7] = 8;

        //white pawns
        for (int x=0; x<8; x++) {
            positions[x][6] = 7; 
        }

        return;
    }

    public void printBoard() {

        System.out.println("    A  B  C  D  E  F  G  H");
        System.out.println("    ----------------------");
        for (int y=0; y<8; y++) {
            System.out.print((8-y) + " | ");
            for (int x=0; x<8; x++) {
                System.out.print(friendlyIds.get(positions[x][y]) + " ");
            }
            System.out.print("\n");
        }

        return;
    }

    public boolean moveAndPassTurn(int oldX, int oldY, int newX, int newY) {
        Move move = new Move(oldX, oldY, newX, newY);
        return moveAndPassTurn(move.fromX, move.fromY, move.toX, move.toY);
    }

    public boolean moveAndPassTurn(Move move) {
        //check if the move is valid
        //if it is, then apply it, pass the turn and return true
        //if not return false
        if (containsMove(validMoves, move)) {
            move(move);
            whitesTurn = !whitesTurn;
            validMovesGenerated = false;
            return true;
        }
        return false;
    }

    public void move(Move move) {
        move(move.fromX, move.fromY, move.toX, move.toY);
        return;
    }

    public void move(int oldX, int oldY, int newX, int newY) {
        //move piece from oldX, oldY to newX, newY
        //this does not take into account legality

        
        boolean opposingPieces = opposingPieces(oldX, oldY, newX, newY);

        if (opposingPieces) { explode(newX, newY); }
        else {
            //apply the actual move
            positions[newX][newY] = positions[oldX][oldY];
            //apply castling if applicable
            //white queen side castle: 4,7 - 2,7
            if (oldX == 4 && oldY == 7 && newX == 2 && newY == 7) {
                //move the white rook
                positions[0][7] = 0;
                positions[3][7] = 8;
            }
            //white king side castle: 4,7 - 6,7
            if (oldX == 4 && oldY == 7 && newX == 6 && newY == 7) {
                //move the white rook
                positions[7][7] = 0;
                positions[5][7] = 8;
            }
            //black queen side castle: 4,0 - 2,0
            if (oldX == 4 && oldY == 0 && newX == 2 && newY == 0) {
                //move the black rook
                positions[0][0] = 0;
                positions[3][0] = 1;
            }
            //black king side castle: 4,0 - 6,0
            if (oldX == 4 && oldY == 0 && newX == 6 && newY == 0) {
                //move the black rook
                positions[7][0] = 0;
                positions[5][0] = 1;
            }

        }

        positions[oldX][oldY] = 0;
        lastMove = new Move(oldX, oldY, newX, newY);

        //keep track of which pieces moved, so that castling
        //can be calculated
        if (oldX == 4 && oldY == 7) { whiteKingMoved = true; }
        if (oldX == 0 && oldY == 7) { whiteRook0Moved = true; }
        if (oldX == 7 && oldY == 7) { whiteRook7Moved = true; }
        if (oldX == 4 && oldY == 0) { blackKingMoved = true; }
        if (oldX == 0 && oldY == 0) { blackRook0Moved = true; }
        if (oldX == 7 && oldY == 0) { blackRook7Moved = true; }

        return;
    }

    private boolean opposingPieces(int fromX, int fromY, int toX, int toY) {

        //check if either space is empty
        if (positions[fromX][fromY] == 0 || positions[toX][toY] == 0) {
            return false;
        }

        return (
            containsPiece(whitePieces, positions[fromX][fromY]) ==
            containsPiece(whitePieces, positions[toX][toY]));
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
        generateValidMoves(true);
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
            nextMove.generateValidMoves(true);
            nextMove.move(m);
            if (nextMove.lookAhead(turns - 1)) {
                return true;
            }
        }

        return false;
    }

    public boolean isValid(Move move) {
        //check if a move is within the valid moves list
        if (!validMovesGenerated) { generateValidMoves(true); }
        if (containsMove(validMoves, move)) { return true; }
        return false;
    }

    public boolean opponentMoveTo(int x, int y) {
        //check if the non-turn player can move any piece to
        //x, y

        BoardState b = new BoardState();
        copyTo(b);
        b.whitesTurn = !whitesTurn;
        b.generateValidMoves(false);
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
        else { turnPlayerKingId = 4; }
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
        else { turnPlayerKingId = 4; }
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                if (positions[x][y] == turnPlayerKingId) {
                    return true;
                }
            }
        }

        return true;
    }

    public void generateValidMoves(boolean removeSuicideMoves) {
        //generate all valid moves for the turn player
        if (validMovesGenerated) { return; }
        currentlyGeneratingValidMoves = true;
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

        if (removeSuicideMoves) { removeSuicideMoves(validMoves); }
        validMovesGenerated = true;
        currentlyGeneratingValidMoves = false;
    
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
            if (nextMove.kingAlive(nextMove.whitesTurn)) {
                if (!nextMove.inCheck() || nextMove.kingAlive(!nextMove.whitesTurn)) {
                    trueValidMoves.add(m);
                }
            }
        }

        return trueValidMoves;
    }

    public ArrayList<Move> createMoves(int x, int y) {
        //generate the moves for the piece at location x,y
        int pieceId = positions[x][y];
        //pawns 6, 7
        if (pieceId == 6 || pieceId == 7) { return pawnMoves(x, y); }
        //rooks 1, 8
        if (pieceId == 1 || pieceId == 8) { return rookMoves(x, y); }
        //knights 2, 9
        if (pieceId == 2 || pieceId == 9) { return knightMoves(x, y); }
        //bishops 3, 10
        if (pieceId == 3 || pieceId == 10) { return bishopMoves(x, y); }
        //kings 4, 11
        if (pieceId == 1 || pieceId == 11) { return kingMoves(x, y); }
        //queens 5, 12
        if (pieceId == 5 || pieceId == 12) { return queenMoves(x, y); }
        //default return value
        return new ArrayList<Move>();
    }

    private ArrayList<Move> pawnMoves(int x, int y) {
        //generate all valid moves for a pawn at x,y

        //initial setup of variables
        ArrayList<Move> moves = new ArrayList<Move>();
        int[] opponentPieces;
        int initialPositionY;
        int direction;
        int enPassantPositionY;
        if (whitesTurn) {
            opponentPieces = blackPieces;
            initialPositionY = 6;
            //the square that a pawn must be on to perform en passant
            enPassantPositionY = 3;
            //the direction the given pawn can move
            direction = -1;
        }
        else {
            opponentPieces = whitePieces;
            initialPositionY = 1;
            enPassantPositionY = 4;
            direction = 1;
        }

        //moving forward 1 space
        if (positions[x][y + direction] == 0) { moves.add(new Move(x, y, x, y + direction)); }
        //moving forward 2 spaces
        if (y == initialPositionY) {
            //this if statement must be called AFTER the previous one to avoid
            //a potential out of index access for positions
            if (positions[x][y + direction] == 0 && positions[x][y + 2*direction] == 0) {
                moves.add(new Move(x, y, x, y + 2*direction));
            }
        }
        //attacking left
        if (x != 0) {
            if (containsPiece(opponentPieces, positions[x - 1][y + direction])) {
                moves.add(new Move(x, y, x - 1, y + direction));
            }
        }
        //attacking right
        if (x != 7) {
            if (containsPiece(opponentPieces, positions[x + 1][y + direction])) {
                moves.add(new Move(x, y, x + 1, y + direction));
            }
        }
        //en passant
        //first, define what the last move must have been, for this pawn
        //to be able to en passant, either to the left or right
        Move leftPawnDoubleMove = new Move(x-1, y+(2*direction), x-1, y);
        Move rightPawnDoubleMove = new Move(x+1, y+(2*direction), x+1, y);
        if (y == enPassantPositionY && lastMove.equals(leftPawnDoubleMove)) {
            moves.add(new Move(x, y, x-1, y+direction));
        }
        if (y == enPassantPositionY && lastMove.equals(rightPawnDoubleMove)) {
            moves.add(new Move(x, y, x+1, y+direction));
        }

        return moves;
    }

    private ArrayList<Move> rookMoves(int x, int y) {
        //generate all valid moves for a rook at x,y

        ArrayList<Move> moves = new ArrayList<Move>();
        //left
        for (Move m : directionalMoves(x, y, -1, 0)) { moves.add(m); }
        //right
        for (Move m : directionalMoves(x, y, 1, 0)) { moves.add(m); }
        //forward
        for (Move m : directionalMoves(x, y, 0, -1)) { moves.add(m); }
        //backwards
        for (Move m : directionalMoves(x, y, 0, 1)) { moves.add(m); }

        return moves;
    }

    private ArrayList<Move> knightMoves(int x, int y) {
        //generate all valid moves for a knight at x,y

        ArrayList<Move> moves = new ArrayList<Move>();
        int[] turnPlayerPieces;
        if (whitesTurn) { turnPlayerPieces = whitePieces; }
        else { turnPlayerPieces = blackPieces; }
        
        //move to -2, -1
        if (x >= 2 && y >= 1) {
            if (!containsPiece(turnPlayerPieces, positions[x-2][y-1])) {
                moves.add(new Move(x, y, x-2, y-1));
            }
        }
        //move to -1, -2
        if (x >= 2 && y >= 1) {
            if (!containsPiece(turnPlayerPieces, positions[x-1][y-2])) {
                moves.add(new Move(x, y, x-1, y-2));
            }
        }
        //move to 1, -2
        if (x <= 1 && y >= 2) {
            if (!containsPiece(turnPlayerPieces, positions[x+1][y-2])) {
                moves.add(new Move(x, y, x+1, y-2));
            }
        }
        //move to 2, -1
        if (x <= 2 && y >= 1) {
            if (!containsPiece(turnPlayerPieces, positions[x+2][y-1])) {
                moves.add(new Move(x, y, x+2, y-1));
            }
        }
        //move to 2, 1
        if (x <= 2 && y <= 1) {
            if (!containsPiece(turnPlayerPieces, positions[x+2][y+1])) {
                moves.add(new Move(x, y, x+2, y+1));
            }
        }
        //move to 1, -2
        if (x <= 1 && y >= 2) {
            if (!containsPiece(turnPlayerPieces, positions[x+1][y-2])) {
                moves.add(new Move(x, y, x+1, y-2));
            }
        }
        //move to -1, -2
        if (x >= 1 && y >= 2) {
            if (!containsPiece(turnPlayerPieces, positions[x-1][y-2])) {
                moves.add(new Move(x, y, x-1, y-2));
            }
        }
        //move to -2, 1
        if (x >= 2 && y <= 1) {
            if (!containsPiece(turnPlayerPieces, positions[x-2][y+1])) {
                moves.add(new Move(x, y, x-2, y+1));
            }
        }

        return moves;
    }

    private ArrayList<Move> bishopMoves(int x, int y) {
        //generate all valid moves for a bishop at x,y

        ArrayList<Move> moves = new ArrayList<Move>();
        //left forward
        for (Move m : directionalMoves(x, y, -1, -1)) { moves.add(m); }
        //left backwards
        for (Move m : directionalMoves(x, y, -1, 1)) { moves.add(m); }
        //right forward
        for (Move m : directionalMoves(x, y, 1, -1)) { moves.add(m); }
        //right backwards
        for (Move m : directionalMoves(x, y, 1, 1)) { moves.add(m); }

        return moves;
    }

    private ArrayList<Move> queenMoves(int x, int y) {
        //generate all valid moves for a queen at x,y

        ArrayList<Move> moves = new ArrayList<Move>();
        //left forward
        for (Move m : directionalMoves(x, y, -1, -1)) { moves.add(m); }
        //forward
        for (Move m : directionalMoves(x, y, 0, -1)) { moves.add(m); }
        //right forward
        for (Move m : directionalMoves(x, y, 1, -1)) { moves.add(m); }
        //right
        for (Move m : directionalMoves(x, y, 1, 0)) { moves.add(m); }
        //right backward
        for (Move m : directionalMoves(x, y, 1, 1)) { moves.add(m); }
        //backward
        for (Move m : directionalMoves(x, y, 0, 1)) { moves.add(m); }
        //left backward
        for (Move m : directionalMoves(x, y, -1, 1)) { moves.add(m); }
        //left
        for (Move m : directionalMoves(x, y, -1, 0)) { moves.add(m); }

        return moves;
    }

    private ArrayList<Move> kingMoves(int x, int y) {
        //generate all valid moves for a king at x,y

        ArrayList<Move> moves = new ArrayList<Move>();
        ArrayList<Integer> seqX = new ArrayList<Integer>();
        ArrayList<Integer> seqY = new ArrayList<Integer>();
        //this avoids out of index error for positions if the
        //king is on the side of the board
        if (x != 0) { seqX.add(-1); }
        if (x != 7) { seqX.add(1); }
        if (y != 0) { seqY.add(-1); }
        if (y != 7) { seqY.add(1); }

        //normal moves
        for (int moveX : seqX) {
            for (int moveY : seqY) {
                if (positions[x+moveX][y+moveY] == 0) {
                    moves.add(new Move(x, y, x+moveX, y+moveY));
                }
            }
        }

        //white castling
        if (whitesTurn &&
            !whiteKingMoved) {
                //queens side
                if (positions[0][7] == 8 &&
                    !whiteRook0Moved &&
                    !opponentMoveTo(1, 7) &&
                    !opponentMoveTo(2, 7) &&
                    !opponentMoveTo(3, 7) &&
                    positions[1][7] == 0 &&
                    positions[2][7] == 0 &&
                    positions[3][7] == 0) {
                        moves.add(new Move(x, y, 2 ,7));
                    }
                //kings side
                if (positions[7][7] == 8 &&
                    !whiteRook7Moved &&
                    !opponentMoveTo(5, 7) &&
                    !opponentMoveTo(6, 7) &&
                    positions[5][7] == 0 &&
                    positions[6][7] == 0) {
                        moves.add(new Move(x, y, 6, 7));
                    }

            }
        //black castling
        if (!whitesTurn &&
            !blackKingMoved) {
                //queens side
                if (positions[0][0] == 1 &&
                    !blackRook0Moved &&
                    !opponentMoveTo(1, 0) &&
                    !opponentMoveTo(2, 0) &&
                    !opponentMoveTo(3, 0) &&
                    positions[1][0] == 0 &&
                    positions[2][0] == 0 &&
                    positions[3][0] == 0) {
                        moves.add(new Move(x, y, 2 ,0));
                    }
                //kings side
                if (positions[7][0] == 1 &&
                    !blackRook7Moved &&
                    !opponentMoveTo(5, 0) &&
                    !opponentMoveTo(6, 0) &&
                    positions[5][0] == 0 &&
                    positions[6][0] == 0) {
                        moves.add(new Move(x, y, 6, 0));
                    }
            }

        return moves;
    }

    private ArrayList<Move> directionalMoves(int x, int y, int dirX, int dirY) {
        //generate all valid moves for a piece at x,y moving along
        //the given direction

        ArrayList<Move> moves = new ArrayList<Move>();
        int[] turnPlayerPieces;
        if (whitesTurn) { turnPlayerPieces = whitePieces; }
        else { turnPlayerPieces = blackPieces; }
        int cursorX = x + dirX;
        int cursorY = y + dirY;
        while (true) {
            if (cursorX < 0 || cursorX > 7) { break; }
            if (cursorY < 0 || cursorY > 7) { break; }
            if (containsPiece(turnPlayerPieces, positions[cursorX][cursorY])) { break; }
            Move move = new Move(x, y, cursorX, cursorY);
            moves.add(move);
            //can't skip over opponent's piece
            if (positions[x][y] != 0) { break; }
        }

        return moves;
    }

    public boolean containsMove(ArrayList<Move> arrayList, Move move) {
        //return whether or not the given move is in the given list

        for (Move m : arrayList) {
            if (move.fromX == m.fromX &&
                move.fromY == m.fromY &&
                move.toX == m.toX &&
                move.toY == m.toY) {
                    return true;
                }
        }

        return false;
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

    //NOT YET IMPLEMENTED
    public void serialize() {
        //this will prepare the board state (and all children)
        //to be written to the disk
        return;
    }

    //NOT YET IMPLEMENTED
    public void deserialize() {
        //this is called after the baordstate has been read from the disk
        //and placed in memory, it re-creates the structure of the board state map
        return;
    }
}