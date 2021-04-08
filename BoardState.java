import java.util.ArrayList;
import java.io.Serializable;

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
        return;
    }

    public void setupInitialBoard() {
        //set starting positions for the chess game
        return;
    }

    public void move(int oldX, int oldY, int newX, int newY) {
        //move piece from oldX, oldY to newX, newY
        //this does not take into account legality
        return;
    }

    public void branch(Move branchingMove) {
        //branch the map for the given move
        return;
    }

    public void explode(int x, int y) {
        //remove the piece from x, y and
        //explode all pieces within 1 space of x, y
        //except pawns
        return;
    }

    public boolean checkVictory() {
        //check to see if the board is currently within a victory state
        //either because a king is dead, or a king is in checkmate
        return false;
    }

    public boolean lookAhead(int turns) {
        //see if victory is possible with X turns ahead
        //this does NOT mean forced checkmate, just possible checkmate
        return false;
    }

    public boolean isValid(Move move) {
        //check if a move is within the valid moves list
        return true;
    }

    public boolean opponentMoveTo(int x, int y) {
        //check if the non-turn player can move any piece to
        //x, y
        return true;
    }

    public void generateValidMoves() {
        //generate all valid moves for the turn player
        return;
    }

    private ArrayList<Move> removeSuicideMoves(ArrayList<Move> moves) {
        //the moves given will be semi-valid, some of them may include
        //moves that kill one's own king or place one's own king in check
        //remove these
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