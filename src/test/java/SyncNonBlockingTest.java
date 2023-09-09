import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SyncNonBlockingTest {

    static class WorkerA {
        Consumer<String> ownJob = (message) -> {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 100000000; j++) {

                }
                System.out.println("A: doing something...");
            }
            System.out.println("A: = " + message);
        };

        Consumer<String> workForB = (message) -> {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 100000000; j++) {

                }
                System.out.println("B: doing something...");
            }
            System.out.println("B: " + message);
        };

        void doMyWork() {
            ownJob.accept("I'm worker A. And I'm done.");
        }

        public Consumer<String> getWorkForB() {
            return workForB;
        }

        void isWorkForBFinished(CompletableFuture<Void> joinPoint) {
            while (!joinPoint.isDone()) {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("A: Worker B is still working. Continue check what B is finished.");
            }
            System.out.println("A: Worker B is done. Im gonna doing my work.");
        }
    }

    static class WorkerB {
        CompletableFuture<Void> doMyWork(Consumer<String> myWork) {
            return CompletableFuture.runAsync(() -> myWork.accept("I'm worker B. And I'm done."));
        }
    }

    /**
     * 전달한 일은 논블로킹으로 처리되지만, 전달한 일이 끝났는지 확인 후 자신의 업무를 진행하므로 동기 처리가 됩니다.
     *
     * WorkerA는 WorkerB에게 업무를 전달합니다.
     * WorkerB는 즉각 응답 후 자신의 일을 수행합니다.
     * CompletableFuture.runAsync() 메소드를 통해 새로운 스레드가 WorkerB의 일을 수행합니다.
     * WorkerA는 WorkerB의 일이 끝났는지 지속적으로 확인합니다.
     * WorkerB의 일이 끝나지 않았다면 일정 시간 대기 후 다시 확인합니다.
     * WorkerB의 일이 끝났다면 자신의 남은 업무를 수행합니다.
     *
     * 결과
     * “B: doing something.” - 일을 전달받은 WorkerB는 즉각 응답 후 자신의 일을 수행합니다.
     * “A: Worker B is still working. Continue check what B is finished.” - WorkerA는 자신의 일을 수행하지 않고 WorkerB의 일이 끝났는지 지속적으로 확인합니다.
     * “B: I’m worker B. And I’m done.” - WorkerB의 일이 끝났습니다.
     * “A: Worker B is done. Im gonna doing my work.” - WorkerA는 WorkerB의 일이 끝났음을 확인 후 자신의 업무를 수행합니다.
     * “A: I’m worker A. And I’m done.” - WorkerA는 자신의 업무를 마무리 짓습니다.
     * 논블로킹 형태로 WorkerA와 WorkerB는 동시에 일을 수행하지만, 업무 관계상 필연적으로 WorkerA는 WorkerB의 일이 마치면 자신의 일을 마무리합니다.
     */
    @Test
    void test() {
        WorkerA a = new WorkerA();
        WorkerB b = new WorkerB();
        Consumer<String> workForB = a.getWorkForB();
        CompletableFuture<Void> joinPoint = b.doMyWork(workForB);
        a.isWorkForBFinished(joinPoint);
        a.doMyWork();
    }
}
