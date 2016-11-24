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
默认值是10，见ArrayList类的ArrayList()方法中有this(10)。扩容方法是原数组满了之后加上原来长度的一半加1，见ArrayList()的ensureCapacity(int minCapacity)方法。int newCapacity = (oldCapacity*3)/2+1;同时创建一个新的newCapacity长度的数组讲原来的数组迁移到新数组在放弃原数组。
## HashMap的默认值是多少，怎样扩容
hashMap的默认值是16.见HashMap类的HashMap()方法中table = new Entry[DEFAULT_INITIAL_CAPACITY]，而此常量值为16.扩容是长度乘以2.见addEntry(int hash,K key,V value,int bucketIndex)方法。里面有resize(2*table.length)但是不是满了就扩容。而是达到原长度乘以扩容因子的乘积之后扩容。见addEntry方法中的if(size++ >= threshold) resize(2*table.length)。而threshold的值是DEFAULT_INITIAL_CAPACITY*DEFAULT_LOAD_FACTOR。扩容因子的值是0.75。
