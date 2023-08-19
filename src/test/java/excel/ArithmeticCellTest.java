package excel;

import org.junit.jupiter.api.Test;

class ArithmeticCellTest {

    @Test
    void test() {

            ArithmeticCell c3 = new ArithmeticCell("C3");
            SimpleCell c1 = new SimpleCell("C1");
            SimpleCell c2 = new SimpleCell("C2");

            c1.subscribe(c3::setLeft);
            c2.subscribe(c3::setRight);

            c1.onNext(10);
            c2.onNext(20);
            c1.onNext(15);
    }
}