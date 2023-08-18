package future;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScheduledExecutorServiceExample {
    public static void main(String[] args) throws InterruptedException {
       /* work1();
        Thread.sleep(10000);
        work2();*/

       ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
       work1();
       scheduledExecutorService.schedule(ScheduledExecutorServiceExample::work2, 10, java.util.concurrent.TimeUnit.SECONDS);
       scheduledExecutorService.shutdown();
    }

    private static void work1() {
        System.out.println("Hello from work1");
    }
    private static void work2() {
        System.out.println("Hello from work2");
    }
}
