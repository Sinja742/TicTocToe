public class Win {

    public final int noWin = 0;
    public final int myWin = 1;
    public final int yourWin = 2;
    public final int tie = 3;

    private final int[][] winConstellation = new int[][] {{0,1,2},
                                                          {3,4,5},
                                                          {6,7,8},    //
                                                          {0,3,6},    //0,1,2
                                                          {1,4,7},    //3,4,5
                                                          {2,5,8},    //6,7,8
                                                          {0,4,8},    //
                                                          {2,4,6}};

    private int firstSpot = -1;
    private int secondSpot = -1;

    private String[] positions;

    public int getFirstSpot() {
        return this.firstSpot;
    }

    public void initSpots() {
        this.firstSpot = -1;
        this.secondSpot = -1;
    }

    public int getSecondSpot() {
        return this.secondSpot;
    }

    public int checkForWin(Symbol playerSymbol, String[] positions) {
        this.positions = positions;
        if(checkForMyWin(playerSymbol)) {
            return this.myWin;
        } else if(checkForEnemyWin(playerSymbol)) {
            return this.yourWin;
        } else if(checkForTie()) {
            return this.tie;
        } else {
            return this.noWin;
        }
    }

    private boolean checkForMyWin(Symbol playerSymbol) {
        for (int i = 0; i < winConstellation.length; i++) {
            if (this.positions[winConstellation[i][0]] == playerSymbol.getSymbol() && this.positions[winConstellation[i][1]] == playerSymbol.getSymbol() && this.positions[winConstellation[i][2]] == playerSymbol.getSymbol()) {
                this.firstSpot = winConstellation[i][0];
                this.secondSpot = winConstellation[i][2];
                return true;
            }
        }
        return false;
    }

    private boolean checkForEnemyWin(Symbol playerSymbol) {
        Symbol enemyPlayerSymbol;
        if(playerSymbol.equals(Symbol.CROSS)) {
            enemyPlayerSymbol = Symbol.CIRCLE;
        } else {
            enemyPlayerSymbol = Symbol.CROSS;
        }

        for (int i = 0; i < winConstellation.length; i++) {
            if (this.positions[winConstellation[i][0]] == enemyPlayerSymbol.getSymbol() && this.positions[winConstellation[i][1]] == enemyPlayerSymbol.getSymbol() && this.positions[winConstellation[i][2]] == enemyPlayerSymbol.getSymbol()) {
                firstSpot = winConstellation[i][0];
                secondSpot = winConstellation[i][2];
                return true;
            }
        }
        return false;
    }

    private boolean checkForTie() {
        for (int i = 0; i < winConstellation.length; i++) {
            if (this.positions[i] == null) {
                return false;
            }
        }
        return true;
    }
}
