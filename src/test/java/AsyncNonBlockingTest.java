import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AsyncNonBlockingTest {
    static class WorkerA {

        Consumer<String> workForA = (message) -> {
            for (int index = 0; index < 5; index++) {
                for (int subIndex = Integer.MIN_VALUE; subIndex < Integer.MAX_VALUE; subIndex++) {
                }
                System.out.println("A: doing something.");
            }
            System.out.println("A: " + message);
        };

        Consumer<String> workForB = (message) -> {
            for (int index = 0; index < 5; index++) {
                for (int subIndex = Integer.MIN_VALUE; subIndex < Integer.MAX_VALUE; subIndex++) {
                }
                System.out.println("B: doing something.");
            }
            System.out.println("B: " + message);
        };

        void doMyWork() {
            workForA.accept("I'm worker A. And I'm done.");
        }

        Consumer<String> getWorkForB() {
            return workForB;
        }
    }

    static class WorkerB {

        CompletableFuture<Void> takeMyWorkAndDoMyWork(Consumer<String> myWork) {
            return CompletableFuture.runAsync(() -> myWork.accept("I'm worker B. And I'm done."));
        }
    }

    /**
     * WorkerA는 자신이 해야하는 일과 WorkerB가 해야하는 일을 모두 가지고 있습니다.
     * WorkerA는 WorkerB에게 일을 건내면, WorkerB는 전달받은 일을 수행합니다.
     * CompletableFuture.runAsync() 메소드에 의해 새로운 스레드가 WorkerB의 일을 수행합니다.
     * WorkerA는 WorkerB의 일이 끝나는 것을 기다리지 않고 자신의 일을 수행합니다.
     *
     * 결과
     * WorkerA와 WorkerB가 동시에 일하는 구간이 생깁니다.
     * 여러 번 실행시 업무를 먼저 마치는 Worker가 매번 바뀝니다.
     */
    @Test
    void test() {
        WorkerA a = new WorkerA();
        WorkerB b = new WorkerB();
        CompletableFuture<Void> joinPoint = b.takeMyWorkAndDoMyWork(a.getWorkForB());
        a.doMyWork();
        // WorkerB가 일을 마치지 않았는데 메인(main) 스레드가 종료되는 경우 어플리케이션이 종료되므로 이런 현상을 방지하는 코드 추가
        joinPoint.join();
        System.out.println("All workers done.");
    }
}
