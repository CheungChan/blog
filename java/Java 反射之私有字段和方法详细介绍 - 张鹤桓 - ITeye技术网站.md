# Java 反射之私有字段和方法详细介绍 - 张鹤桓 - ITeye技术网站
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
