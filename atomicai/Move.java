package atomicai;

public class Move {

    public int fromX;
    public int fromY;
    public int toX;
    public int toY;

    public Move(int FromX, int FromY, int ToX, int ToY) {
        fromX = FromX;
        fromY = FromY;
        toX = ToX;
        toY = ToY;
        return;
    }

    public boolean equals(Move move) {

        if (move == null) { return false; }

        if (fromX == move.fromX &&
            fromY == move.fromY &&
            toX == move.toX &&
            toY == move.toY) { return true; }

        return false;
    }

    public void show() {
        
        System.out.println("Move from: " + fromX + ", " + fromY);
        System.out.println("To: " + toX + ", " + toY);

        return;
    }
    
}