public class Player {

    private boolean myTurn;
    private final Symbol symbol;
    private boolean myWin;

    public Player(boolean myTurn, Symbol symbol) {
        this.myTurn = myTurn;
        this.symbol = symbol;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public boolean isMyWin() {
        return myWin;
    }

    public void setMyWin(boolean myWin) {
        this.myWin = myWin;
    }

}
