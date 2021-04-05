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

        if (fromX == move.fromX &&
            fromY == move.fromY &&
            toX == move.toX &&
            toY == move.toY) { return true; }

        return false;
    }

    public void show() {

        //show the move in a friendly way
        //index cords need to be converted to chess notation

        return;
    }
    
}