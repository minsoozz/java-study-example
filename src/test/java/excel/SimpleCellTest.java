package excel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCellTest {

    @Test
    void test(){

        SimpleCell c1 = new SimpleCell( "C1");
        SimpleCell c2 = new SimpleCell( "C2");
        SimpleCell c3 = new SimpleCell( "C3");

        c1.subscribe(c3);

        c1.onNext(10);
        c2.onNext(20);
    }
}