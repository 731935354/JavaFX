package multi_thread_lambda;

public class Demo {
    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            Thread t = new Thread(() -> worker_run());
            t.start();
        }
    }

    public static void worker_run() {
        System.out.println("This is a new thread worker.");
    }
}
