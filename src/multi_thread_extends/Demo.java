package multi_thread_extends;


public class Demo {
    public static void main(String[] args) {
        MyThread t1 = new MyThread("haha");
        MyThread t2 = new MyThread("hehe");

        t1.start();
        t2.start();
    }
}