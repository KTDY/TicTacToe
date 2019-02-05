package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ModelOfGameTest {

    @Test
    public void isCellBusy() {
        ModelOfGame m = new ModelOfGame(3,"X");
        boolean test =  m.isCellBusy(0,0);
        boolean test2 = true;

    }
}