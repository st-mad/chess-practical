import org.junit.Test;

import cs1002.checker.FileTest;
import cs1002.junit.FileTestRunner;

public class Tests {
    @Test
    public void TestInput_00_startEmpty() {
        FileTest.run(W08Practical::main, "00_startEmpty");
    }

    @Test
    public void TestInput_01_startEmpty() {
        FileTest.run(W08Practical::main, "01_startEmpty");
    }

    @Test
    public void TestInput_02_validMove() {
        FileTest.run(W08Practical::main, "02_validMove");
    }

    @Test
    public void TestInput_03_validMove() {
        FileTest.run(W08Practical::main, "03_validMove");
    }
    
    @Test
    public void TestInput_04_validMove() {
        FileTest.run(W08Practical::main, "04_validMove");
    }

    @Test
    public void TestInput_05_wrongTurn() {
        FileTest.run(W08Practical::main, "05_wrongTurn");
    }

    @Test
    public void TestInput_06_wrongTurn() {
        FileTest.run(W08Practical::main, "06_wrongTurn");
    }
    
    @Test
    public void TestInput_07_outOfBoard() {
        FileTest.run(W08Practical::main, "07_outOfBoard");
    }
    
    @Test
    public void TestInput_08_outOfBoard() {
        FileTest.run(W08Practical::main, "08_outOfBoard");
    }

    @Test
    public void TestInput_09_takePiece() {
        FileTest.run(W08Practical::main, "09_takePiece");
    }
    
    @Test
    public void TestInput_10_takePiece() {
        FileTest.run(W08Practical::main, "10_takePiece");
    }

    @Test
    public void TestInput_11_takeOwnPiece() {
        FileTest.run(W08Practical::main, "11_takeOwnPiece");
    }
    
    @Test
    public void TestInput_12_takeOwnPiece() {
        FileTest.run(W08Practical::main, "12_takeOwnPiece");
    }
    
    @Test
    public void TestInput_13_jumpOverPiece() {
        FileTest.run(W08Practical::main, "13_jumpOverPiece");
    }

    @Test
    public void TestInput_14_whiteWins() {
        FileTest.run(W08Practical::main, "14_whiteWins");
    }
    
    @Test
    public void TestInput_15_blackWins() {
        FileTest.run(W08Practical::main, "15_blackWins");
    }

    @Test
    public void TestInput_16_invalidMove() {
        FileTest.run(W08Practical::main, "16_invalidMove");
    }
    
    @Test
    public void TestInput_17_invalidMove() {
        FileTest.run(W08Practical::main, "17_invalidMove");
    }
    
    @Test
    public void TestInput_18_quit() {
        FileTest.run(W08Practical::main, "18_quit");
    }    

    public static void main(String[] args) {
        FileTestRunner.run(Tests.class, args);
    }
}
