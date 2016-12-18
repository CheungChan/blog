import java.util.concurrent.locks.*;
/**
 * 需求：保证生产一个消费一个，生产一个消费一个。现在提供两个生产者两个消费者，开启4个线程。
 */


class Resource{
    private String name;
    private int count = 1;
    private boolean flag;
    private Lock lock = new ReentrantLock();
    private Condition condition_pro = lock.newCondition();
    private Condition condition_con = lock.newCondition();

    public  void set(String name) throws InterruptedException{
        lock.lock();
        try{
            while(flag){
                condition_pro.await();
            }
            this.name = name + "--" + count++;
            System.out.println(Thread.currentThread() + "生产者" + this.name);
            flag = true;
            condition_con.signal();//只唤醒消费者的线程
        }finally{
            lock.unlock();
        }
    }

    public  void out() throws InterruptedException{
        lock.lock();
        try{
            while(!flag){
                condition_con.await();
            }
            System.out.println(Thread.currentThread() + "...消费者..." + this.name);
            flag = false;
            condition_pro.signal();//只唤醒生产者的线程
        }finally{
            lock.unlock();
        }
    }
}
class Producer implements Runnable{
    private Resource res;
    Producer(Resource res){
        this.res = res;
    }
    @Override
    public void run(){
        while(true){
            try{res.set("商品");}catch(InterruptedException e){}
        }
    }
}
class Consumer implements Runnable{
    private Resource res;
    Consumer(Resource res){
        this.res = res;
    }
    @Override
    public void run(){
        while(true){
            try{res.out();}catch(InterruptedException e){}
        }
    }
}
public class ProducerConsumerDemo2{
    public static void main(String[] args){
        Resource res = new Resource();
        Producer prod = new Producer(res);
        Consumer con = new Consumer(res);
        Thread t1 = new Thread(prod);
        Thread t2 = new Thread(prod);
        Thread t3 = new Thread(con);
        Thread t4 = new Thread(con);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}