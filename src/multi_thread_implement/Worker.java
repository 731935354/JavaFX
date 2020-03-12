package multi_thread_implement;

// 通过实现Runnable接口来实现多线程
public class Worker implements Runnable {
    private String name;

    public Worker(String name) {
        this.name = name;
    }
    // 创建线程后的工作函数
    public void run() {
        System.out.println("My name is: " + this.name);
    }
}
