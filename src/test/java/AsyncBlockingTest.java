import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AsyncBlockingTest {

    static class WorkerA {
        boolean isWorkBFinished;

        Consumer<String> ownJob = (message) -> {
            for (int index = 0; index < 5; index++) {
                for (int subIndex = Integer.MIN_VALUE; subIndex < Integer.MAX_VALUE; subIndex++) {
                }
                System.out.println("A: doing something.");
            }
            System.out.println("A: " + message);
        };

        Consumer<Void> callMeLater = (Void) -> {
            isWorkBFinished = true;
            System.out.println("B: Hey, Worker A. I'm done.");
        };

        void waitWorkBFinished() {
            while (!isWorkBFinished) {
                System.out.println("A: Waiting for Worker B.");
                for (int subIndex = 0; subIndex < 1000; subIndex++) {
                }
            }
        }

        void doMyWork() {
            ownJob.accept("I'm worker A. And I'm done my first job.");
            waitWorkBFinished();
            ownJob.accept("I'm worker A. And I'm done my second job.");
        }

        Consumer<Void> getCallMeLater() {
            return callMeLater;
        }
    }

    static class WorkerB {

        Consumer<String> ownJob = (message) -> {
            for (int index = 0; index < 5; index++) {
                for (int subIndex = Integer.MIN_VALUE; subIndex < Integer.MAX_VALUE; subIndex++) {
                }
                System.out.println("B: doing something.");
            }
            System.out.println("B: " + message);
        };

        CompletableFuture<Void> doWorkAndCallToALater(Consumer<Void> callBack) {
            return CompletableFuture.runAsync(() -> {
                ownJob.accept("I'm worker B. And I'm my first job.");
                callBack.accept(null);
                ownJob.accept("I'm worker B. And I'm my second job.");
            });
        }
    }

    /**
     * WorkerA는 자신의 일을 수행하기 전에 WorkerB에게 callBack 메소드를 전달합니다.
     * callBack 메소드는 WorkerB가 자신의 일을 일부 마치면 WorkerA에게 이를 알리는 용도로 사용됩니다.
     * WorkerA와 WorkerB 모두 각자 자신의 일을 수행합니다.
     * CompletableFuture.runAsync() 메소드를 통해 새로운 스레드가 WorkerB의 일을 수행합니다.
     * WorkerA는 업무를 수행 중에 WorkerB의 일이 끝나기를 기다리는 구간이 존재합니다. 블로킹 구간입니다.
     * WorkerB는 자신의 업무 일부가 종료되면 callBack 메소드를 통해 workerA에게 이를 알리고, 자신의 업무를 마저 진행합니다.
     * 블로킹 되어있던 WorkerA는 WorkerB의 업무 일부가 종료되는 시점부터 자신의 남은 업무를 수행합니다.
     *
     * 결과
     * WorkerA와 WorkerB가 동시에 업무를 진행합니다.
     * “A: Waiting for Worker B.” - WorkerA가 WorkerB의 첫 업무 종료를 기다립니다.
     * “B: Hey, Worker A. I’m done.” - WorkerB가 WorkerA에게 자신의 첫 업무 종료를 알립니다.
     * WorkerA와 WorkerB가 동시에 업무를 마무리합니다.
     * 최종적으로 업무를 종료하는 순서는 실행시마다 달라질 수 있습니다.
     */
    @Test
    void test() {
        WorkerA a = new WorkerA();
        WorkerB b = new WorkerB();
        CompletableFuture<Void> joinPoint = b.doWorkAndCallToALater(a.getCallMeLater());
        a.doMyWork();
        // WorkerB가 일을 마치지 않았는데 메인(main) 스레드가 종료되는 경우 어플리케이션이 종료되므로 이런 현상을 방지하는 코드 추가
        joinPoint.join();
        System.out.println("All workers done.");
    }
}
