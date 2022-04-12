import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class WinTest {

    private final Win win = new Win();
    String[] positions;
    String[] positionsFull;

    @Test
    void checkForWin_MyWin() {
//        Given
        positions = new String[]{"X", "O", "X", null, "O", "X", null, "O", null};
        positionsFull = new String[]{"X", "O", "O", "O", "X", "O", "X", "X", "X"};
//        When
        int winNumber = win.checkForWin(Symbol.CIRCLE,positions);
        int winNumberFull = win.checkForWin(Symbol.CROSS,positionsFull);
//        Then
        assertEquals(1,winNumber);
        assertEquals(1,winNumberFull);
    }
    @Test
    void checkForWin_EnemyWin() {
//        Given
        positions = new String[]{"X", "O", null, null, "X", "O", null, null, "X"};
        positionsFull = new String[]{"O", "X", "X","X", "O", "X", "O", "O", "O"};
//        When
        int winNumber = win.checkForWin(Symbol.CIRCLE,positions);
        int winNumberFull = win.checkForWin(Symbol.CROSS,positionsFull);
//        Then
        assertEquals(2,winNumber);
        assertEquals(2,winNumberFull);
    }
    @Test
    void checkForWin_Tie() {
//        Given
        positions = new String[]{"X", "O", "X", "X", "O", "O", "O", "X", "X"};
//        When
        int winNumber = win.checkForWin(Symbol.CIRCLE,positions);
//        Then
        assertEquals(3,winNumber);
    }

    @Test
    void checkForWin_NoWin() {
//        Given
        positions = new String[]{"X", null, "X", "X", "O", "O", "O", "X", "X"};
        String[] positionsEmpty = new String[9];
//        When
        int winNumber = win.checkForWin(Symbol.CIRCLE,positions);
        int winNumberEmpty = win.checkForWin(Symbol.CIRCLE,positionsEmpty);
//        Then
        assertEquals(0,winNumber);
        assertEquals(0,winNumberEmpty);
    }
}