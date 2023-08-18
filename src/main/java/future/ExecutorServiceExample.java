package future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceExample {
    public static void main(String[] args) {
        int x = 1337;
        Result result = new Result();

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            result.left = f(x);
        });

        executorService.submit(() -> {
            result.right = g(x);
        });

        executorService.shutdown();
        System.out.println(result.left + result.right);
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
