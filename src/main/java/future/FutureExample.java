package future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int x = 1337;

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<Integer> leftFuture = executorService.submit(() -> f(x));
        Future<Integer> rightFuture = executorService.submit(() -> g(x));

        executorService.shutdown();

        int leftResult = leftFuture.get();
        int rightResult = rightFuture.get();

        System.out.println(leftResult + rightResult);
    }

    private static int g(int x) {
        return x;
    }

    private static int f(int x) {
        return x;
    }

    public static class Result {
        private int left;
        private int right;
    }
}
