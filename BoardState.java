import java.util.ArrayList;

public class BoardState {
    public Move initiatingMove;
    public BoardState parent;
    public int[][] positions;
    public ArrayList<Move> validMoves;
    public boolean whitesTurn;
    //used to determine if a player can castle
    public boolean whiteKingMoved;
    public boolean blackKingMoved;
    public boolean whiteRook0Moved;
    public boolean blackRook0Moved;
    public boolean whiteRook7Moved;
    public boolean blackRook7Moved;

    public BoardState() {
        //initialize variables in default states
        positions = new int[8][8];
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
        return;
    }

    public void move(int oldX, int oldY, int newX, int newY) {
        return;
    }

    public void branch(Move branchingMove) {
        return;
    }

    public void explode(int x, int y) {
        return;
    }

    public boolean checkVictory() {
        return false;
    }

    public boolean lookAhead(int turns) {
        return false;
    }

    public boolean isValid(Move move) {
        return true;
    }

    public boolean opponentMoveTo(int x, int y) {
        return true;
    }

    public void generateValidMoves() {
        return;
    }

    private ArrayList<Move> removeSuicideMoves(ArrayList<Move> moves) {
        return new ArrayList<Move>();
    }

    private ArrayList<Move> pawnMoves (int x, int y) {
        return new ArrayList<Move>();
    }

    private ArrayList<Move> rookMoves (int x, int y) {
        return new ArrayList<Move>();
    }

    private ArrayList<Move> knightMoves (int x, int y) {
        return new ArrayList<Move>();
    }

    private ArrayList<Move> bishopMoves (int x, int y) {
        return new ArrayList<Move>();
    }

    private ArrayList<Move> queenMoves (int x, int y) {
        return new ArrayList<Move>();
    }

    private ArrayList<Move> kingMoves (int x, int y) {
        return new ArrayList<Move>();
    }

    private ArrayList<Move> directionalMoves (int x, int y, int dirX, int dirY) {
        return new ArrayList<Move>();
    }

    public boolean containsMove(ArrayList<Move> arrayList, Move move) {
        return true;
    }


}