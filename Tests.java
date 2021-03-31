import BoardState;

public static class Tests {

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

        

        return true;
    }

}