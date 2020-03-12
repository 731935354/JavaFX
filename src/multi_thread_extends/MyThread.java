package multi_thread_extends;

// 通过继承Thread来实现多线程
public class MyThread extends Thread {
    private String name;

    public MyThread(String name) {
        this.name = name;
    }

    // 开启线程之后的工作函数
    public void run() {
        System.out.println("My name is: " + this.name);
    }
}
