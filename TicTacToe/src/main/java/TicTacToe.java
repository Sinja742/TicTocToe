import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class TicTacToe implements Runnable {

    private String[] spaces = new String[9];

    private boolean unableToCommunicateWithOpponent = false;

    private boolean gameOver = false;
    private boolean tie = false;

    private int maxErrors = 10;

    private final ServerConnection sc = new ServerConnection();
    private final Player me;
    private final Win w = new Win();
    private final GUI gui;

    private boolean serverPlayerStarts;


    public TicTacToe() {

        sc.findServer();

        gui = new GUI(this);

        if (!sc.connect()) {
            sc.initializeServer();
            this.me = new Player(true, Symbol.CROSS);
        } else {
            this.me = new Player(false, Symbol.CIRCLE);
        }

        this.gui.newFrame();
        this.gui.newThread(this);
    }

    /**
     * maxErrors wird bei jeder neuen Runde um 5 erhöht, da mit steigender Spieldauer eine größere Gesamtzahl an Fehlern akzeptiert wird
     */
    public void initNewGame(boolean serverPlayerStarts) {
        this.gameOver = false;
        this.tie = false;
        this.spaces = new String[9];
        this.maxErrors += 5;

        this.me.setMyWin(false);
        if (serverPlayerStarts && this.me.getSymbol().equals(Symbol.CROSS)) {
            this.me.setMyTurn(true);
        } else if (!serverPlayerStarts && this.me.getSymbol().equals(Symbol.CIRCLE)) {
            this.me.setMyTurn(true);
        } else {
            this.me.setMyTurn(false);
        }

        this.w.initSpots();
        this.gui.closeFrame();
        this.gui.newFrame();
    }

    public boolean isUnableToCommunicateWithOpponent() {
        return this.unableToCommunicateWithOpponent;
    }

    public boolean isServerConnectionAccepted() {
        return this.sc.isAccepted();
    }

    public String[] getPositions() {
        return this.spaces;
    }

    public Symbol getMySymbol() {
        return this.me.getSymbol();
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public boolean isTie() {
        return this.tie;
    }

    public boolean isMyWin() {
        return this.me.isMyWin();
    }

    public int getFirstSpot() {
        return this.w.getFirstSpot();
    }

    public int getSecondSpot() {
        return this.w.getSecondSpot();
    }

    /**
     * Spielablauf
     * ständiges neu zeichnen der Oberfläche
     * Prüfen auf neue Server Requests, wenn kein Gegenspieler gefunden wurde
     */
    public void run() {
        serverPlayerStarts = true;
        while (true) {
            while (!this.gameOver) {
                tick();
                gui.repaint();

                if (this.me.getSymbol().equals(Symbol.CROSS) && !sc.isAccepted()) {
                    sc.listenForServerRequest();
                }
            }
        }
    }

    private void tick() {
        if (sc.getErrors() >= this.maxErrors) this.unableToCommunicateWithOpponent = true;

        if (!me.isMyTurn() && !this.unableToCommunicateWithOpponent) {
            try {
                int space = sc.getDis().readInt();
                if (this.me.getSymbol().equals(Symbol.CIRCLE)) spaces[space] = "X";
                else spaces[space] = "O";
                this.checkEndOfGame();
                this.me.setMyTurn(true);
            } catch (IOException e) {
                e.printStackTrace();
                sc.incErrors();
            }
        }
    }

    private void checkEndOfGame() {
        int winStatus = this.w.checkForWin(this.me.getSymbol(), this.spaces);
        if (winStatus == this.w.noWin) {
            this.gameOver = false;
        } else if (winStatus == this.w.myWin) {
            this.me.setMyWin(true);
            this.gameOver = true;
        } else if (winStatus == this.w.tie) {
            this.tie = true;
            this.gameOver = true;
        } else if (winStatus == this.w.yourWin) {
            this.gameOver = true;
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (this.sc.isAccepted()) {
            if (gameOver) {
                serverPlayerStarts = !serverPlayerStarts;
                this.initNewGame(serverPlayerStarts);
            } else if (this.me.isMyTurn() && !this.unableToCommunicateWithOpponent && !gameOver) {
                int x = e.getX() / gui.getLengthOfSpace();
                int y = e.getY() / gui.getLengthOfSpace();
                y *= 3;
                int position = x + y;

                if (spaces[position] == null) {
                    if (me.getSymbol().equals(Symbol.CROSS)) spaces[position] = "X";
                    else spaces[position] = "O";
                    me.setMyTurn(false);
                    gui.repaint();
                    Toolkit.getDefaultToolkit().sync();

                    try {
                        sc.getDos().writeInt(position);
                        sc.getDos().flush();
                    } catch (IOException ex) {
//                        Errors werden gezählt, wenn diese eine bestimmte Anzahl überschreiten, wird die Verbindung abgebrochen
                        sc.incErrors();
                        ex.printStackTrace();
                    }

                    System.out.println("DATA WAS SENT");
                    checkEndOfGame();

                }
            }

        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        TicTacToe ticTacToe = new TicTacToe();
    }

}