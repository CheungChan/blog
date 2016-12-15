## Java 反射之私有字段和方法详细介绍
尽管我们通常认为通过JAVA的反射机制来访问其它类的私有字段和私有方法是可行的，其实并没有那么困难。 
注释：只有在单独的JAVA程序中运行该代码才有效，就像你做一些单元测试或者常规的程序。如果你尝试在JAVA APPLET内使用该方法，你需要稍稍修改SecurityManager。但是，因为你不是经常需要与它打交道，这里也就不再赘述了。 
这里是本次内容的列表: 
1.访问私有字段。 
2.访问私有方法。 
访问私有字段: 
为了访问私有字段，你需要调用Class.getDeclaredField(String name)或者Class.getDeclaredFields()方法。方法Class.getField(String name)和Class.getFields()仅仅返回共有的字段，所以它们都无法起到作用。这里有一个例子，该例子中有一个包含私有字段的类，在该类下面有通过反射访问私有字段的代码。 
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
这个代码会打印出文本"fieldValue = The Private Value"，而该值正好是对象PrivateObject的私有字段privateString的值。 
注意到我们使用了方法PrivateObject.class.getDeclaredfield("privateString")。正是这个调用这个方法返回了私有字段。这个方法仅仅根据指定的类返回字段，不会返回父类申明的字段。 
另外仔细观察加粗的语句。通过调用 Field.setAccessible(true)，你关掉了对于这个指定字段实例的访问检查，仅仅对反射有效。现在你能访问它了，不管它是私有的，保护的或是默认的(default)，即时调用者并不在该范围中。你仍然不能通过常规方法访问该字段，因为编译器不允许。 
访问私有方法 
为了访问一个私有方法，你需要调用Class.getDeclaredMethod(String name,Class[] parameterTypes)或者Class.getDeclaredMethods()方法。方法Class.getMethod(String name,Class[] parameterTypes)和Class.getMethods()仅仅返回公有方法，所以它们不会起到作用。下面是一个简单的例子，该例子中有一个拥有私有方法的类，类下面是通过反射机制访问私有方法的代码。 
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
String returnValue = (String) 
privateStringMethod.invoke(privateObject, null); 
System.out.println("returnValue = " + returnValue); 
```
这个代码例子会打印出文本"returnValue = The private Value"，该值正好是私有方法的返回值。

转自 [http://www.jb51.net/article/32170.htm](http://www.jb51.net/article/32170.htm)
## 由ibatis中的逆向工程的bug引起空指针异常的血案：操作符优先级问题
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
## ArrayList的默认值是多少，怎样扩容
1.6是默认值是10，见ArrayList类的ArrayList()方法中有this(10)。扩容方法是原数组满了之后加上原来长度的一半加1，见ArrayList()的ensureCapacity(int minCapacity)方法。int newCapacity = (oldCapacity*3)/2+1;同时创建一个新的newCapacity长度的数组讲原来的数组迁移到新数组在放弃原数组。  
1.7是初始值是0，然后add的时候改成默认值10，增长见grow(int minCapacity)方法int newCapacity = oldCapacity + (oldCapacity >> 1);if (newCapacity - minCapacity < 0)newCapacity = minCapacity;所以是当数组满了之后增长为原来的3/2，也就是oldCapacity + (oldCapacity >> 1)
## HashMap的默认值是多少，怎样扩容
hashMap的默认值是16.见HashMap类的HashMap()方法中table = new Entry[DEFAULT_INITIAL_CAPACITY]，而此常量值为16.扩容是长度乘以2.见addEntry(int hash,K key,V value,int bucketIndex)方法。里面有resize(2*table.length)但是不是满了就扩容。而是达到原长度乘以扩容因子的乘积之后扩容。见addEntry方法中的if(size++ >= threshold) resize(2*table.length)。而threshold的值是DEFAULT_INITIAL_CAPACITY*DEFAULT_LOAD_FACTOR。扩容因子的值是0.75。
## 子类集成父类的属性。修改了父类属性的类型导致运行时子类报NoSuchFieldException
由于之前父类的这个属性是一个Service，在开事务的情况下，注入属性的时候写实体类的ServiceImpl类型会切不到事务，所以改bug讲这个属性类型改为了Service接口类型来注入。而子类中引用了这个属性，调用这个service上的add方法，综合环境报了NoSuchFieldException异常。原因是综合功能环境是采用的class文件增量更新。父类修改了导致重新编译了class。本地环境是全部编译，所以子类没有修改内容也重新编译了，所以没有报错。然后综合环境是class文件替换，如果子类没有修改过就不会替换class。而子类编译class的时候如果有引用父类的变量在编译的时候就会将对父类的引用转换为子类中的变量。而没有重新编译所以综合环境上子类还是持有的原来父类的引用。由此引申到如果定义一个static final int age=1的话，其他的类使用了这个final属性的引用的话，编译的时候会将这个引用替换为常量的值。所以反编译class的时候这时将没有这个常量的引用，而只有1。
## AtomicLong、AtomicInteger在高并发的情况下可以保证线程安全而long不可以。
高并发的情况下long i的i++这种跟原来的值有关系的赋值操作可能会出错，而使用AtomicLong就可以了。他的底层使用的不是乐观锁也不是悲观锁，而是Cas锁，就是copyandswap算法的锁，是一种无锁算法。采用的是c语言写的。用native修饰的方法。而c语言的代码是直接控制了cpu在执行这个属性简单的赋值操作的时候不允许cpu切换到别的线程干别的事情，是c语言直接控制了cpu。
## ThreadLocal中的get()的时候可能会引起空指针问题
由于ThreadLocal中的set()和get()方法都是操作的内部的叫做threadLocalmap的属性。这个属性以ThreadLocal类型的this对象为键以传入的Object类型的变量为值。但是这个ThreadLocal类型的this对象采用的是虚引用WeakedReference，所以在gc的时候就被回收了导致键没有了，导致get()空指针，而且出现了内存泄漏（键被gc了，值还存在，值不可达）。所以jdk采用的办法是在下一次调用get()set()remove()方法的时候会检查有没有内存泄漏的，有的话就清除一下。而jdk采用虚引用作为键官方解释是说为了防止时间长有大的对象被ThreadLocalmap长期占用，导致内存溢出。而为了防止空指针，可以采取的办法是在工具类里面设置静态的ThreadLocal对象=new TheadLocal()作为强引用。由于是静态变量，知道程序结束，才会被回收，这样就可以不检查get()是否返回空指针。
举例
```

```
另外的问题是当线程创建的时候把当前线程作为key，如果线程被销毁，则这个ThreadLocal被销毁，但是在使用线程池的时候，线程永生，可能会导致没有set值的时候调用get方法会返回上一次线程使用的值，所以应该在线程不用之前手动调用remove()方法。
## java中的四种引用类型
按照从强到弱顺序是强引用，软引用，弱引用，虚引用。强引用是我们在编程过程中使用的最简单的引用，如代码String s=”abc”中变量s就是字符串对象”abc”的一个强引用。任何被强引用指向的对象都不能被垃圾回收器回收，这些对象都是在程序中需要的。而软引用是在内存快要溢出之前会被gc掉，常用于缓存的场景。而弱引用会在任意次gc的时候，不一定内存要溢出的时候被gc掉，常用于性能优化为目的的缓存而不是为了防止内存溢出。虚引用是影子引用，用的很少。
## Person p = new Person("zhangsan",20) 这句话都做了什么事情？
1.因为new用到了Person.class,所以会先找到Person.class文件并加载到内存中。  
2.执行该类的static代码块，如果有的话，给Person.class类进行初始化。  
3.在堆内存开辟内存空间，分配内存地址。  
4.在堆内存中建立对象的特有属性，并进行默认初始化。（赋予安全值0，null等）  
5.对属性进行显示初始化（实例变量初始化器）  
6.对该对象进行构造代码块初始化。（实例构造器）
7.对对象进行构造函数初始化。  
7.将内存地址付给栈内存中的p变量。
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
```
Outer.Inner in = new Outer().new Inner();  
```
2.当内部类在外部类的成员位置上时，就可以被成员修饰符所修饰。比如  
private:将内部类在外部类中进行封装。  
static：讲内部类变成静态内部类。
当内部类被静态修饰时，只能访问外部的静态成员。  
在外部其他类中，如何直接访问静态内部类的非静态成员呢？  
```
new Outer.Inner().function();  
```
在外部其他类中，如何直接访问static内部类的静态成员呢？ 
``` 
Outer.Inner.function();  
```
注意：当内部类中定义了静态成员，该内部类必须是静态内部类。  
当外部类中的静态方法访问内部类时，内部类也必须是静态内部类。
