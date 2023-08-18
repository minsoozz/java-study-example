package future;

public class CallbackStyleExample {
    public static void main(String[] args) {
        int x = 1337;
        Result result = new Result();

        f(x, (int y) -> {
            result.left = y;
            System.out.println((result.left + result.right));
        });

        g(x, (int z) -> {
            result.right = z;
            System.out.println(result.left + result.right);
        });
    }

    private static void g(int x, IntCallback callback) {
        int z = x; // Placeholder implementation
        callback.call(z);
    }

    private static void f(int x, IntCallback callback) {
        int y = x; // Placeholder implementation
        callback.call(y);
    }

    public static class Result {
        private int left;
        private int right;
    }

    interface IntCallback {
        void call(int result);
    }
}

