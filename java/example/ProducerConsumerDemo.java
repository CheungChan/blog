/**
 * 需求：保证生产一个消费一个，生产一个消费一个。现在提供两个生产者两个消费者，开启4个线程。
 */

class Resource{
    private String name;
    private int count = 1;
    private boolean flag;

    public synchronized void set(String name){
        while(flag){//不用if判断，防止唤醒同类线程之后不判断又生产一个
            try{this.wait();}catch(Exception e){}
        }
        this.name = name + "--" + count++;
        System.out.println(Thread.currentThread() + "生产者" + this.name);
        flag = true;
        this.notifyAll();//不用notify而用notifyAll防止唤醒的是同类线程判断后再次沉睡，最终四个线程全部沉睡
    }

    public synchronized void out(){
        while(!flag){//不用if判断，防止唤醒同类线程之后不判断又生产一个
            try{this.wait();}catch(Exception e){}
        }
        System.out.println(Thread.currentThread() + "...消费者..." + this.name);
        flag = false;
        this.notifyAll();//不用notify而用notifyAll防止唤醒的是同类线程判断后再次沉睡，最终四个线程全部沉睡
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
            res.set("商品");
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
            res.out();
        }
    }
}
public class ProducerConsumerDemo{
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