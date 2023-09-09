import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

public class SyncBlockingTest {

    static class WorkerA {
        Consumer<String> workForA = (message) -> {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 100000000; j++) {

                }
                System.out.println("A: doing something...");
            }
            System.out.println("A = " + message);
        };

        Consumer<String> workForB = (message) -> {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 100000000; j++) {

                }
                System.out.println("B: doing something...");
            }
            System.out.println("B = " + message);
        };

        void doMyWork() {
            workForA.accept("I'm worker A. And I'm done.");
        }

        Consumer<String> giveWorkToB() {
            return workForB;
        }
    }

    static class WorkerB {

        void takeMyWorkAndDoMyWork(Consumer<String> myWork) {
            myWork.accept("I'm worker B. And I'm done.");
        }
    }

    /**
     * WorkerA는 자신이 해야하는 일과 WorkerB가 해야하는 일을 모두 가지고 있습니다.
     * WorkerA는 WorkerB에게 일을 건내면, WorkerB는 전달받은 일을 수행합니다.
     * WorkerA는 WorkerB가 일을 마친 후에 자신의 일을 수행합니다.
     *
     * 결과
     * 항상 WorkerB가 일을 마친 뒤 WorkerA가 일을 수행합니다.
     */
    @Test
    void test() {
        WorkerA workerA = new WorkerA();
        WorkerB workerB = new WorkerB();

        workerB.takeMyWorkAndDoMyWork(workerA.giveWorkToB());
        workerA.doMyWork();
    }
}
