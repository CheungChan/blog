## tomcat对servlet使用单例多线程，为什么，应该注意什么？
tomcat对于web.xml中配置的servlet类名通过反射创建实例，但仅在第一次请求到来时创建一个实例，之后就一直使用这个实例了。  
#### 开发要注意的
原因是对于多个请求创建多个servlet开销太大。但是不注意可能引起线程安全问题。对于实例变量是在堆内存中的，所有线程都共用一个对象所以实例变量有线程安全问题，应该尽量不适用实例变量，或者使用线程安全的实例变量如Vector、HashTable但是开启同步代价太大，尽量不要使用实例变量。静态变量在方法区中，也是被共享的，所以也不要使用。    
而局部变量是在堆栈中创建的，每个线程都有自己的堆栈变量，所以没有线程安全问题。  
如果在方法中有操作共享数据的，如读写同一个文件，要加syncronized锁起来。  
还有一个办法是让servelt类去实现SingleThreadModel标记接口，但是这个接口已经在servlet2.4规范之后废弃了，原因是这会导致每个请求都创建一个实例，开销太大。  
#### 框架处理办法
Struts2的Action是原型的，非单例的，会对每一个请求产生一个Action实例  
Struts1 Action是单例的，spring mvc的controller也是如此。因此开发时要求必须是线程安全的，Action只有一个实例来处理所有的请求。单例策略限制了struts1 Action能做的事，并且开发时要特别小心。Action资源必须是线程安全的或同步的。  
spring的ioc容器管理的bean默认也是单例的。当spring管理Struts2的Action时，bean默认是单例的，可以通过配置设置为原型(scope="prototype")  
#### servlet容器如何同时处理多个请求。
java的内存模型JMM(Java Memory Model)  
JMM主要规定了线程和内存之间的一些关系。根据JMM的设计，系统存在一个主内存（Main Memory），Java中实例变量都储存在主存中，对于所有线程都是共享的。每天线程都有自己工作主存(Working Memory)，工作主存由缓存和堆栈两部分组成，缓存中保存的是主存中变量的拷贝，缓存可能并不总和主存同步，也可能缓存中变量的修改可能没有立刻写入主存中。堆栈中保存的是线程的局部变量，线程之间无法通过相互直接访问堆栈中的变量。  
工作者线程（Working Thread):执行代码的一组线程。  
调度线程（Dispatcher Thread）：每个线程都具有分配给他的线程优先级，线程根据优先级调度执行的。  
Servlet采用多线程来处理多个请求同时访问。servlet依赖一个线程池来服务请求。调度线程是单例的负责管理线程池，线程池实际上是一系列工作线程的集合。  
当容器收到一个servlet请求，调度线程从线程池中选出一个工作者线程，将请求传递给该工作者线程来执行servlet的service方法。当这个线程正在执行的时候，容器收到另一个请求，调度线程同样从线程池中选出另一个工作者线程去服务新的请求。容器并不关心这个请求访问的是不是同一个servlet。  
servlet容器默认采用单例多线程来处理请求，也可以改变可在tomcat的server.xml中通过```<Connector>```元素设置线程池中线程的数目。  

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

## rose启动报错jdk版本低于1.5
### 报错：
Caused by: java.lang.IllegalStateException: Context namespace element 'annotation-config' and its parser class [org.springframework.context.annotation.AnnotationConfigBeanDefinitionParser] are only available on JDK 1.5 and higher
### 原因：
使用的rose1.0引用的spring版本是2.5.6，该版本的spring的org.springframework.core.jdkVersion中只对jdk的1.6,1.7.版本进行了大于1.5的判断，没有1.8，不识别所以报此错误。
### 解决办法：
方法一：更改jdkVersion.java编译成class再替换到jar包中。
方法二：安装jdk1.7，使用1.7不用1.8
方法三：使用rose2.0-SNAPCHAT,如果从http://repos.fenqi.d.xiaonei.com/nexus/content/groups/public-snapshots中下载不下来此版本。可以先从svn上下载rose2.0版本的项目，再用mvn的install命令安装到版本库中再使用就ok了。