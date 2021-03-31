import BoardState;

public static class Tests {

    public static boolean testSetupInitialBoard() {

        //ensures that the correct IDs are placed into 
        //the positions array
        //see "Piece IDs & Positions.txt"

        BoardState boardState = new BoardState();
        boardState.testSetupInitialBoard();
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

        return true;
    }

}