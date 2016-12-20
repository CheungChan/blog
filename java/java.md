## Java 反射之私有字段和方法详细介绍
尽管我们通常认为通过JAVA的反射机制来访问其它类的私有字段和私有方法是可行的，其实并没有那么困难。   
注释：只有在单独的JAVA程序中运行该代码才有效，就像你做一些单元测试或者常规的程序。如果你尝试在JAVA APPLET内使用该方法，你需要稍稍修改SecurityManager。但是，因为你不是经常需要与它打交道，这里也就不再赘述了。   
这里是本次内容的列表:   
1.访问私有字段。   
2.访问私有方法。   
访问私有字段:   
为了访问私有字段，你需要调用```Class.getDeclaredField(String name)```或者```Class.getDeclaredFields()```方法。方法```Class.getField(String name)```和```Class.getFields()```仅仅返回共公开的字段，所以它们都无法起到作用。这里有一个例子，该例子中有一个包含私有字段的类，在该类下面有通过反射访问私有字段的代码。   
复制代码 代码如下:
```java
public class PrivateObject { 
private String privateString = null; //声明为私有字段 
public PrivateObject(String privateString) { 
    this.privateString = privateString; 
} 
} 
```
复制代码 代码如下:
```java
PrivateObject privateObject = new PrivateObject("The Private Value");//实例化对象 
Field privateStringField = PrivateObject.class. 
getDeclaredField("privateString"); 
privateStringField.setAccessible(true);//允许访问私有字段 
String fieldValue = (String) privateStringField.get(privateObject);//获得私有字段值 
System.out.println("fieldValue = " + fieldValue); 
```
这个代码会打印出文本```"fieldValue = The Private Value"```，而该值正好是对象```PrivateObject```的私有字段```privateString```的值。 
注意到我们使用了方法```PrivateObject.class.getDeclaredfield("privateString")```。正是这个调用这个方法返回了私有字段。这个方法仅仅根据指定的类返回字段，不会返回父类声明的字段。 
另外仔细观察加粗的语句。通过调用 ```Field.setAccessible(true)```，你关掉了对于这个指定字段实例的访问检查，仅仅对反射有效。现在你能访问它了，不管它是私有的，保护的或是默认的(default)，即时调用者并不在该范围中。你仍然不能通过常规方法访问该字段，因为编译器不允许。 
访问私有方法 
为了访问一个私有方法，你需要调用```Class.getDeclaredMethod(String name,Class[] parameterTypes)```或者```Class.getDeclaredMethods()```方法。方法```Class.getMethod(String name,Class[] parameterTypes)```和```class.getMethods()```仅仅返回公有方法，所以它们不会起到作用。下面是一个简单的例子，该例子中有一个拥有私有方法的类，类下面是通过反射机制访问私有方法的代码。 
复制代码 代码如下:
```java
public class PrivateObject { 
private String privateString = null; 
public PrivateObject(String privateString) { 
    this.privateString = privateString; 
} 
private String getPrivateString(){//私有方法 
    return this.privateString; 
} 
} 
```
复制代码 代码如下:
```java
PrivateObject privateObject = new PrivateObject("The Private Value"); 
Method privateStringMethod = PrivateObject.class. 
getDeclaredMethod("getPrivateString", null); 
privateStringMethod.setAccessible(true); 
String returnValue = (String)privateStringMethod.invoke(privateObject, null); 
System.out.println("returnValue = " + returnValue); 
```
这个代码例子会打印出文本```"returnValue = The private Value"```，该值正好是私有方法的返回值。

转自 [http://www.jb51.net/article/32170.htm](http://www.jb51.net/article/32170.htm)
## 由```ibatis```中的逆向工程的bug引起空指针异常的血案：操作符优先级问题
工作中同事采用ibatis的逆向工程根据数据库表生成了java代码，model、dao、sqlmap这些东西。其中的model层复写的equals方法是这样的
``` java
public boolean equals(User user1,User user2){
    return user1.getUserName() == null ? false : user1.getUserName().equals(user2.getUserName())
    && user1.getPassword() == null ? false: user1.getPassword.equals(user2.getPassword)
    && user1.getGender() == null ? false: user1.getGender().equals(user2.getGender())
}
```
bug在哪里呢？那我们看下面的例子
``` java
return true? false: true
    && true? false:true
```
这句我们可能认为```true?false:true```返回的是```false```然后```false&&```无论什么都是```false```，所以最终结果返回```false```,但实际返回的是```true```。  
怎么回事呢？事实上```&&```的优先级要比三目运算符```xx?xx:xx```要高，所以实际运算顺序是```true?false:true```这一步返回的是```false```，然后```false && xx```，这时候```user1.getUserName==null```根本没有进行判断而结果是```false```,然后是```false?false:true```,所以最好返回结果是```true```。   
于是乎引发了这个血案，就是```user1.getName==null```是正确的所以是```false```，这时候```user1.getPassword()==null```没有进行判断就走到了```user1.getPassword.equals(user2.getPassword())```，而导致了空指针异常```NullPointerException```
## ```ArrayList```的默认值是多少，怎样扩容
1.6是默认值是```10```，见```ArrayList```类的```ArrayList()```方法中有```this(10)```。扩容方法是原数组满了之后加上原来长度的一半加1，见```ArrayList()```的```ensureCapacity(int minCapacity)```方法。```int newCapacity = (oldCapacity * 3) / 2 + 1```;同时创建一个新的```newCapacity```长度的数组讲原来的数组迁移到新数组在放弃原数组。  
1.7是初始值是```0```，然后```add```的时候改成默认值```10```，增长见```grow(int minCapacity)```方法```int newCapacity = oldCapacity + (oldCapacity >> 1);if (newCapacity - minCapacity < 0)newCapacity = minCapacity;```所以是当数组满了之后增长为原来的3/2，也就是```oldCapacity + (oldCapacity >> 1)```  
```Vector``` 是jdk1.1就有的，那时候还没有集合框架，集合框架是jdk1.2诞生的，```Vector``` 线程安全操作慢，```ArrayList``` 线程不安全操作快，而且```Vector``` 满了之后扩增一倍，浪费空间。
## ```HashMap```的默认值是多少，怎样扩容
```hashMap```的默认值是```16```.见```HashMap```类的```HashMap()```方法中```table = new Entry[DEFAULT_INITIAL_CAPACITY]```，而此常量值为16.扩容是长度乘以2.见```addEntry(int hash,K key,V value,int bucketIndex)```方法。里面有```resize(2 * table.length)```但是不是满了就扩容。而是达到原长度乘以扩容因子的乘积之后扩容。见```addEntry```方法中的```if(size++ >= threshold) resize(2 * table.length)```。而```threshold```的值是```DEFAULT_INITIAL_CAPACITY*DEFAULT_LOAD_FACTOR```。扩容因子的值是```0.75```。
## 子类集成父类的属性。修改了父类属性的类型导致运行时子类报```NoSuchFieldException```
由于之前父类的这个属性是一个```Service```，在开事务的情况下，注入属性的时候写实体类的```ServiceImpl```类型会切不到事务，所以改bug讲这个属性类型改为了```Service```接口类型来注入。而子类中引用了这个属性，调用这个```service```上的```add```方法，综合环境报了```NoSuchFieldException```异常。原因是综合功能环境是采用的```class```文件增量更新。父类修改了导致重新编译了```class```。本地环境是全部编译，所以子类没有修改内容也重新编译了，所以没有报错。然后综合环境是```class```文件替换，如果子类没有修改过就不会替换```class```。而子类编译```class```的时候如果有引用父类的变量在编译的时候就会将对父类的引用转换为子类中的变量。而没有重新编译所以综合环境上子类还是持有的原来父类的引用。由此引申到如果定义一个```static final int age=1```的话，其他的类使用了这个```final```属性的引用的话，编译的时候会将这个引用替换为常量的值。所以反编译```class```的时候这时将没有这个常量的引用，而只有```1```。
## ```AtomicLong```、```AtomicInteger```在高并发的情况下可以保证线程安全而```long```不可以。
高并发的情况下```long i```的```i++```这种跟原来的值有关系的赋值操作可能会出错，而使用```AtomicLong```就可以了。他的底层使用的不是乐观锁也不是悲观锁，而是```Cas锁```，就是```copyandswap```算法的锁，是一种无锁算法。采用的是```c```语言写的。用```native```修饰的方法。而```c```语言的代码是直接控制了cpu在执行这个属性简单的赋值操作的时候不允许cpu切换到别的线程干别的事情，是c语言直接控制了cpu。
## ```ThreadLocal```中的```get()```的时候可能会引起空指针问题
由于```ThreadLocal```中的```set()```和```get()```方法都是操作的内部的叫做```threadLocalmap```的属性。这个属性以```ThreadLocal```类型的```this```对象为键以传入的```Object```类型的变量为值。但是这个```ThreadLocal```类型的```this```对象采用的是虚引用```WeakedReference```，所以在```gc```的时候就被回收了导致键没有了，导致```get()```空指针，而且出现了内存泄漏（键被gc了，值还存在，值不可达）。所以jdk采用的办法是在下一次调用```get()set()remove()```方法的时候会检查有没有内存泄漏的，有的话就清除一下。而jdk采用虚引用作为键官方解释是说为了防止时间长有大的对象被```ThreadLocalmap```长期占用，导致内存溢出。而为了防止空指针，可以采取的办法是在工具类里面设置静态的```ThreadLocal```对象```=new TheadLocal()```作为强引用。由于是静态变量，直到程序结束，才会被回收，（静态变量随着类的加载而加载，随着类的消失而消失）这样就可以不检查```get()```是否返回空指针。
举例
```java
//退款需要操作的库存
private static ThreadLocal<List<ProdStorage>> refundSuncessProdStorageHolder = new ThreadLocal<List<ProdStorage>>(){
    @Override
    protected List<ProdStorage> initialValue(){
        return new ArrayList<ProdStorage>();
    }
}
//商户是否使用redis存储库存
private static ThreadLocal<Map<String,Boolean>> redisSupportHolder = new ThreadLocal<Map<String,Boolean>>(){
    @Override
    protected Map<String,Boolean> initialValue(){
        return new HashMap<String,Boolean>();
    }
}
public static ThreadLocal<List<ProdStorage>> getRefundSuccessProdStorageHolder(){
    return refundSuncessProdStorageHolder;
}
public static ThreadLocal<Map<String,Boolean>> getRedisSupportHolder(){
    return redisSupportHolder;
}
```
另外的问题是当线程创建的时候把当前线程作为key，如果线程被销毁，则这个```ThreadLocal```被销毁，但是在使用线程池的时候，线程永生，可能会导致没有set值的时候调用get方法会返回上一次线程使用的值，所以应该在线程不用之前手动调用```remove()```方法。
## ```java```中的四种引用类型
按照从强到弱顺序是强引用，软引用，弱引用，虚引用。```强引用```是我们在编程过程中使用的最简单的引用，如代码```String s="abc"```中变量```s```就是字符串对象```"abc"```的一个强引用。任何被强引用指向的对象都不能被垃圾回收器回收，这些对象都是在程序中需要的。而```软引用```是在内存快要溢出之前会被gc掉，常用于缓存的场景。而```弱引用```会在任意次gc的时候，不一定内存要溢出的时候被gc掉，常用于性能优化为目的的缓存而不是为了防止内存溢出。```虚引用```是影子引用，用的很少。
## ```Person p = new Person("zhangsan",20)``` 这句话都做了什么事情？
1.因为new用到了```Person.class```,所以会先找到```Person.class```文件并加载到内存（方法区）中。  
2.执行该类的```static```代码块，如果有的话，给```Person.class```类进行初始化（先将类代码静态内容放入静态方法区，再将类中非静态方法加入非静态方法区）。  
3.在堆内存开辟内存空间，分配内存地址。  
4.在堆内存中建立对象的特有属性，并进行```默认初始化```。（赋予安全值0，null等）  
5.对属性进行显示初始化（```实例变量初始化器```）  
6.对该对象进行构造代码块初始化。（```实例初始化器```）
7.对对象进行```构造函数初始化```。  
7.将内存地址付给栈内存中的p变量。
## ```p.setName("lisi")```这句话又做了什么？
![image](image/p.setName()过程.png)
如上图所示，从上面的7步继续，栈内存中原来有```main```方法，现在有引用```p```(指向堆内存中对象的地址0x0023），方法```setName```，```name```变量和```this```。```p```调用```setName```方法，这时候```p```就是```this```，所以将```p```的地址0x0023赋值给```this```，而```this```调用```setName```所以找到堆内存中的```setName```方法，讲堆内存中的name赋值为lisi，而再次调用```P p1 = new P1();p1.setName("qq");```时也是在栈内存产生引用```p```,```name```,```this```,方法```setName```，先给```this```赋值为```p```的地址，然后通过```this```找到堆内存中的对象，再将堆内存中的```name```赋值修改。
## 单例模式中的懒汉模式和饿汉模式
### 饿汉模式：先初始化对象
``` java
class Single{
    private Single(){};
    private static final Single s = new Single();
    public static Single getInstance(){
        return s;
    }
}
```
### 懒汉模式：对象方法被调用时再初始化，延时加载。
``` java
class Single{
    private Single(){};
    private static Single s = null;
    public static Single getInstance(){
        if(s == null){
            synchronized(Single.class){
                if(s == null){
                    s = new Single();
                }
            }
        }
        return s;
        
    }
}
```
两次判断，外层判断是为了已经有了对象不再去拿锁提高效率，内层判断是为了防止多线程并发出现错误，有可能在外层判断加锁之前程序切换。另外懒汉模式中锁是类的字节码对象因为此方法是静态方法。
## 多态总结
### 1.多态的体现
父类的引用指向了自己的子类对象。  
父类的引用也可以接收自己的子类对象。
### 2.多态的前提
必须是类与类之间有关系，要么继承，要么实现。  
通常还有一个前提：存在覆盖。
### 3.多态的好处
大大提高了程序的扩展性
### 4.多态的弊端
虽然提高了程序的扩展性，但是只能使用父类引用中的父类成员。
### 5.在多态中成员函数的特点
在编译期间，参阅引用型变量所属的类是否有调用的方法，如果有，编译通过，如果没有编译失败。  
在运行期间，参阅对象所属的类中是否有调用的方法。  
简单总结就是：成员函数在多态调用时，编译看左边，运行看右边。（动态绑定）
### 6.在多态中成员变量的特点
无论编译还是运行，都参考左边（引用型变量所属的类）。
### 7.在多态中，静态成员函数的特点：
无论编译还是运行，都参考左边。（静态绑定）
## 内部类的访问规则
1.内部类可以访问外部类中的成员，包括私有。  
之所以可以直接访问外部类中的成员，是因为内部类持有了一个外部类的引用。格式 ```外部类名.this  ```
2.外部类要访问内部类，必须建立内部类对象。  
## 内部类的访问格式
1.当内部类定义在外部类的成员位置上，而且非私有，可以在外部其他类中可以直接访问内部类对象。  
格式：  
外部类名.内部类名 变量名 = 外部类对象.内部类对象  
```java
Outer.Inner in = new Outer().new Inner();  
```
2.当内部类在外部类的成员位置上时，就可以被成员修饰符所修饰。比如  
private:将内部类在外部类中进行封装。  
static：讲内部类变成静态内部类。
当内部类被静态修饰时，只能访问外部的静态成员。  
在外部其他类中，如何直接访问静态内部类的非静态成员呢？  
```java
new Outer.Inner().function();  
```
在外部其他类中，如何直接访问static内部类的静态成员呢？ 
``` java
Outer.Inner.function();  
```
注意：当内部类中定义了静态成员，该内部类必须是静态内部类。  
当外部类中的静态方法访问内部类时，内部类也必须是静态内部类。  
3.当内部类定义在局部时  
1）不可以b被成员修饰符修饰  
2）可以直接访问外部类中的成员，因为还持有外部类的引用。但是不可以访问它在局部定义的变量,只能访问被final修饰的局部变量。
## 如何给javac设置编译后class文件的存放位置，如何运行别的位置处的class文件？
```java
javac -d c:\myclass   packa.DemoA.java
```
就会将编译后的class文件存放到制定目录下。  
在当前目录下运行```c:\myclass```下的class文件
```java
set classpath=c:\myclass
java packa.DemoA
```
## 多线程的运行出现安全问题的原因和解决办法
### 原因：
多条语句在操作同一线程共享数据时，一个线程对多条语句只执行了一部分，还没有执行完，另一个线程进来执行，导致共享数据的错误。
### 解决办法：
对多条操作共享数据的语句，只能有一个线程执行完，在执行过程中其他线程不可以参与执行。
## 多线程中方法的锁是什么？
非静态方法的锁是this，静态方法的锁是类的字节码文件。同步的关键是锁必须是同一个锁才能同步。
## 写一个死锁Demo
<a href="example/DeadLockTest.java">死锁示例</a>
## ```wait()、notify()、notifyAll()```的使用场景，为什么定义在Object中?

```wait()、notify()、notifyAll()```都是用在同步中，因为要对持有监视器（锁）的线程操作，  所以要使用在同步中，因为只有同步才具有锁。  
为什么这些操作线程的方法要定义在```Object```中呢？  
因为这些方法在操作线程的同时，都必须标识他们所操作的线程持有的锁，只有同一锁上的被等待线程，可以被同一锁上的```notify```唤醒。不可以对不同锁中的线程进行唤醒。  
也就是说，等待和唤醒必须是同一个锁。  
而锁可以使任意对象，所以可以被任意对象调用的方法定义在```object```中。
## 生产者、消费者模型在多线程模式下为什么需要用```while```判断标记，为什么定义 ```notifyAll```？
使用```while```原因：让被唤醒的线程再一次判断标记。  
使用```notifyAll```原因：因为只用```notify```，容易出现只唤醒本方线程的情况，导致程序中所有的线程被等待。  
代码示例：<a href="example/ProducerConsumerDemo.java" target="_blank">生产者消费者代码示例</a>  
jdk1.5中提供了多线程的升级解决方案。  
讲同步```syncronized```替换成为显式的```Lock```操作。  
将```Object```中的```wait、notify、notifyAll```替换成了```Condition```对象。该对象可以通过```Lock```锁进行获取。该示例中，实现了本方只唤醒对方操作。  
代码示例：<a href="example/ProducerConsumerDemo2.java" target="_blank">生产者消费者代码示例5.0升级版</a>
## 如何停止一个线程？
```stop()``` 方法由于安全问题已经过时。  
如何停止线程？  
只有一种，```run()```方法结束。  
开启多线程运行，运行代码通常是循环结构。  
只要控制住循环，就可以让```run()```方法结束，也就是线程结束。  
特殊情况：  
当线程处于冻结状态(```wait()或者sleep()```)，就不会读取到标记，那么线程就不会结束。  
当没有指定的方式让冻结的线程恢复到运行状态时，这时需要对冻结进行清除。  
强制让线程恢复到运行状态中来，这样就可以操作标记让线程结束。  
```Thread``` 类提供了该方法```interrupt()```；相当于拍了睡的人一砖头，虽然醒了但是受伤了，产生了异常```InterruptedException```  
```interrupt()```方法如果在```sleep()```之前被调用，则线程执行到```sleep()```时就会马上产出```InterruptedException```，此情此景叫做 决断中断。 
示 例：  
```java
public class PendingInterrupt extends Object {  
    public static void main(String[] args){  
        //如果输入了参数，则在main线程中中断当前线程（亦即main线程）  
        if( args.length > 0 ){  
            Thread.currentThread().interrupt();  
        }   
        //获取当前时间  
        long startTime = System.currentTimeMillis();  
        try{  
            Thread.sleep(2000);  
            System.out.println("was NOT interrupted");  
        }catch(InterruptedException x){  
            System.out.println("was interrupted");  
        }  
        //计算中间代码执行的时间  
        System.out.println("elapsedTime=" + ( System.currentTimeMillis() - startTime));  
    }  
}
```
执行结果  
![image](image/决断中断.png)
## 使用 isInterrupted()方法判断中断状态
可以在 ```Thread``` 对象上调用 ```isInterrupted()```方法来检查任何线程的中断状态。这里需要注意：线程一旦被中断，```isInterrupted()```方法便会返回 true，而一旦 ```sleep()```方法抛出异常，它将清空中断标志，此时```isInterrupted()```方法将返回 ```false```。
下面的代码演示了 isInterrupted()方法的使用：
```java
public class InterruptCheck extends Object{  
    public static void main(String[] args){  
        Thread t = Thread.currentThread();  
        System.out.println("Point A: t.isInterrupted()=" + t.isInterrupted());  
        //待决中断，中断自身  
        t.interrupt();  
        System.out.println("Point B: t.isInterrupted()=" + t.isInterrupted());  
        System.out.println("Point C: t.isInterrupted()=" + t.isInterrupted());  

        try{  
            Thread.sleep(2000);  
            System.out.println("was NOT interrupted");  
        }catch( InterruptedException x){  
            System.out.println("was interrupted");  
        }  
        //抛出异常后，会清除中断标志，这里会返回false  
        System.out.println("Point D: t.isInterrupted()=" + t.isInterrupted());  
    }  
}  
```
运行结果如下：  
![image](image/isInterrupted.png)
## 使用 Thread.interrupted()方法判断中断状态
可以使用 ```Thread.interrupted()```方法来检查当前线程的中断状态（并隐式重置为 ```false```）。又由于它是静态方法，因此不能在特定的线程上使用，而只能报告调用它的线程的中断状态，如果线程被中断，而且中断状态尚不清楚，那么，这个方法返回 ```true```。与 ```isInterrupted()```不同，它将自动重置中断状态为 ```false```，第二次调用 ```Thread.interrupted()```方法，总是返回 ```false```，除非中断了线程。
如下代码演示了 Thread.interrupted()方法的使用：  
```java
public class InterruptReset extends Object {  
    public static void main(String[] args) {  
        System.out.println(  
            "Point X: Thread.interrupted()=" + Thread.interrupted());  
        Thread.currentThread().interrupt();  
        System.out.println(  
            "Point Y: Thread.interrupted()=" + Thread.interrupted());  
        System.out.println(  
            "Point Z: Thread.interrupted()=" + Thread.interrupted());  
    }  
}
```
运行结果如下：  
![image](image/interrupted.png)
## 守护线程介绍
```setDaemon(true)``` 将线程可以设置为守护线程（后台线程），当正在运行的线程都是守护线程时，java虚拟机退出。该方法必须在```start```方法之前被调用。这个方法可使得守护线程不中断而自动结束，就像是圣斗士星矢哥五个守护雅典娜，雅典娜挂了哥五个就失业了。
## ```join``` 方法介绍
当A线程执行到了B线程的```join()```方法时，A线程就会等待。等B线程都执行完，A才会执行。  
```join``` 方法可以临时加入线程执行。
## ```String s1 = "abc" ```与 ```String s2 = new String("abc") ```有什么区别？
就一个区别，s1在内存中有一个对象，s2在内存中有两个对象，分别是abc和new的对象。
## 获取两个字符串中最大的相同子串,```String s1="abcwerthelloyuiodef",s2="cvhellobnm";```
思路：  
1.将短的子串按照长度递减的方式获取到  
2.将获取到的子串去长串中判断是否包含，如果包含，已经找到。
代码实现：
```java
public String getMaxSubString(String s1,String s2){
    String max = "",min = "";
    max = (s1.length() > s2.length()) ? s1 : s2;
    min = (max==s1) ? s2 : s1;
    for(int x=0; x<min.length(); x++){
        for(int y=0,z=min.length()-x; z!=min.length()+1; y++,z++){
            String temp = min.substring(y,z);
            if(max.contains(temp)){
                return temp;
            }
        }
    }
    return "";
}
```
## 集合框架中迭代器的注意事项
迭代器```Iterator```是一个接口，所有集合框架类里面都有```iterator()```方法（实现的```Collection```接口中的方法），该方法返回各自集合类中定义的迭代器内部类。
```List``` 集合特有的迭代器,```ListIterator``` 是```Iterator``` 的子接口。  
在迭代时，不可以通过集合对象中的方法操作集合中的元素。因为会发生```ConcurrentModificationException```异常。所以,在迭代器时，只能用迭代器的方法操作元素，可是```Iterator```方法是有限的,只能对元素进行判断、取出、删除操作。如果想要其他的操作，如增加、修改等，就要使用其子接口```ListIterator```，该接口只能通过```List```集合的```listIterator()```方法获取。  
```Vector``` 有一个方法```Enumeration en = v.elements()```,枚举是```Vector``` 特有的取出方式。发现枚举和迭代器很像，其实枚举和迭代器是一样的。因为枚举的名称以及方法的名称都过长，所以枚举被迭代器取代了，枚举郁郁而终了。
