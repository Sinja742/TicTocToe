import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUI {

    private final int WIDTH = 506;
    private final int HEIGHT = 527;

    private JFrame frame;
    private Thread thread;
    private Painter painter;

    private BufferedImage board;
    private BufferedImage redX;
    private BufferedImage blueX;
    private BufferedImage redCircle;
    private BufferedImage blueCircle;

    private final int lengthOfSpace = 160;

    private final Font font = new Font("Verdana", Font.BOLD, 32);
    private final Font smallerFont = new Font("Verdana", Font.BOLD, 20);
    private final Font largerFont = new Font("Verdana", Font.BOLD, 50);

    private final String waitingString = "Waiting for another player";
    private final String unableToCommunicateWithOpponentString = "Unable to communicate with opponent.";
    private final String wonString = "You won!";
    private final String enemyWonString = "Opponent won!";
    private final String tieString = "Game ended in a tie.";

    private final TicTacToe ttt;

    public GUI(TicTacToe ttt) {
        this.ttt = ttt;
        this.loadImages();
        this.painter = new Painter();
        this.painter.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public int getLengthOfSpace() {
        return this.lengthOfSpace;
    }

    public void newFrame() {
        this.frame = new JFrame();
        this.frame.setTitle("Tic-Tac-Toe");
        this.frame.setContentPane(this.painter);
        this.frame.setSize(WIDTH, HEIGHT);
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
    }

    public void closeFrame(){
        this.frame.dispose();
    }

    public void newThread(TicTacToe ttt) {
        this.thread = new Thread(ttt, "TicTacToe");
        this.thread.start();
    }

    public void repaint() {
        this.painter.repaint();
    }


    private void loadImages() {
        try {
            board = ImageIO.read(getClass().getResourceAsStream("/board.png"));
            redX = ImageIO.read(getClass().getResourceAsStream("/redX.png"));
            redCircle = ImageIO.read(getClass().getResourceAsStream("/redCircle.png"));
            blueX = ImageIO.read(getClass().getResourceAsStream("/blueX.png"));
            blueCircle = ImageIO.read(getClass().getResourceAsStream("/blueCircle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Painter extends JPanel implements MouseListener {
        private static final long serialVersionUID = 1L;

        public Painter() {
            setFocusable(true);
            requestFocus();
            setBackground(Color.WHITE);
            addMouseListener(this);
        }

        public void render(Graphics g) {
            g.drawImage(board, 0, 0, null);
            if (ttt.isUnableToCommunicateWithOpponent()) {
                renderProblem(g);
                return;
            }
            if (ttt.isServerConnectionAccepted()) {
                renderServerConnectionAccepted(g);
            } else {
                renderServerConnectionNotAccepted(g);
            }
        }

        private void renderProblem(Graphics g) {
            g.setColor(Color.BLACK);
            g.setFont(smallerFont);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int stringWidth = g2.getFontMetrics().stringWidth(unableToCommunicateWithOpponentString);
            g.drawString(unableToCommunicateWithOpponentString, (GUI.this.WIDTH - stringWidth) / 2, GUI.this.HEIGHT / 2);
        }


        private void renderServerConnectionAccepted(Graphics g) {
            drawGame(g);
            if (ttt.isTie()) {
                Graphics2D g2 = (Graphics2D) g;
                g.setColor(Color.BLACK);
                g.setFont(font);
                int stringWidth = g2.getFontMetrics().stringWidth(tieString);
                g.drawString(tieString, (GUI.this.WIDTH - stringWidth) / 2, GUI.this.HEIGHT / 2);
            } else if(ttt.isGameOver()) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(10));
                g.setColor(Color.DARK_GRAY);
                g.drawLine(ttt.getFirstSpot() % 3 * lengthOfSpace + 10 * ttt.getFirstSpot() % 3 + lengthOfSpace / 2, (int) (ttt.getFirstSpot() / 3) * lengthOfSpace + 10 * (int) (ttt.getFirstSpot() / 3) + lengthOfSpace / 2, ttt.getSecondSpot() % 3 * lengthOfSpace + 10 * ttt.getSecondSpot() % 3 + lengthOfSpace / 2, (int) (ttt.getSecondSpot() / 3) * lengthOfSpace + 10 * (int) (ttt.getSecondSpot() / 3) + lengthOfSpace / 2);

                g.setColor(Color.BLACK);
                g.setFont(largerFont);
                if (ttt.isMyWin()) {
                    int stringWidth = g2.getFontMetrics().stringWidth(wonString);
                    g.drawString(wonString, (GUI.this.WIDTH - stringWidth) / 2, GUI.this.HEIGHT / 2);
                } else if (ttt.isGameOver() && !ttt.isMyWin()) {
                    int stringWidth = g2.getFontMetrics().stringWidth(enemyWonString);
                    g.drawString(enemyWonString, (GUI.this.WIDTH - stringWidth) / 2, GUI.this.HEIGHT / 2);
                }
            }
        }

        private void drawGame(Graphics g) {
            String[] positions = ttt.getPositions();
            for (int i = 0; i < positions.length; i++) {
                if (positions[i] != null) {
                    if (positions[i].equals("X")) {
                        if (ttt.getMySymbol().equals(Symbol.CIRCLE)) {
                            g.drawImage(redX, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3), null);
                        } else {
                            g.drawImage(blueX, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3), null);
                        }
                    } else if (positions[i].equals("O")) {
                        if (ttt.getMySymbol().equals(Symbol.CIRCLE)) {
                            g.drawImage(blueCircle, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3), null);
                        } else {
                            g.drawImage(redCircle, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3), null);
                        }
                    }
                }
            }
        }

        private void renderServerConnectionNotAccepted(Graphics g) {
            g.setColor(Color.BLACK);
            g.setFont(font);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int stringWidth = g2.getFontMetrics().stringWidth(waitingString);
            g.drawString(waitingString, (GUI.this.WIDTH - stringWidth) / 2, GUI.this.HEIGHT / 2);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            render(g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            ttt.mouseClicked(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

    }

}
