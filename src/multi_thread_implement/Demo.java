package multi_thread_implement;



public class Demo {
    public static void main(String[] args) {
        Worker w1 = new Worker("haha");
        Worker w2 = new Worker("hehe");

        Thread t1 = new Thread(w1);
        Thread t2 = new Thread(w2);

        t1.start();
        t2.start();
    }
}
