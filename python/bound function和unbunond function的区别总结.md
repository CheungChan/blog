# bound function和unbound function有什么区别》

读了[stackoverflow关于bound和unbound functions的问答](http://stackoverflow.com/questions/114214/class-method-differences-in-python-bound-unbound-and-static)两遍，总算读懂了，下面写一点观后感。
#### 不要按照java对于方法的分类去解释python里对于方法的分类。java里的方法大致分为静态方法和非静态方法。静态方法用类名访问，非静态方法用实例来访问。静态方法只能访问静态属性和方法，非静态方法可以访问所有。但是用这种分类方法去看待python里的静态方法类方法就会有问题。  
####  python里的分类是按照bound和unbound来划分的。类里面的方法第一个参数是self的那种方法是bound的方法，第一个参数对应绑定的实例。  
e.g  
```
class Test(object):
    def method_one(self):
        print "Called method_one"
a_test = Test()
a_test.method_one()
```
这种调用就相当于
```
Test().method_one(a_test)
```
因为调用方法的时候是采用元类里定义的__getattribute__方法，里面相当于这样。
```
Test.__dict__['method_one'].__get__(a_test,Test)
```
也可以绑定一个类对象
```
class Test(object):
    @classmethod
    def method_two(cls):
        print "Called method_two"
Test().method_two()
```
这种调用相当于
```
method_two(Test)

```
因为调用方法的时候是采用元类里定义的__getattribute__方法，里面相当于这样。
```
Test.__dict__['method_two'].__get__(None,Test)
```
####  而classmethod操作类属性是所有实例共享的，classmethod也可以被实例调用。但是类不能调用self那种方法。
```
a_test = Test()
a_test.method_two()
```
也是可以的。就相当于用类调用了。  
非绑定方法是这样
```
class Test(object):
    @staticmethod
    def method_three():
        print "Called method_three"
```
####  非绑定方法可以被类调用，也可以被实例调用 
```
Test.method_three()
Test().method_three()
```
#### 都可以。
#### 这种静态方法不会直接绑定类也不会绑定实例，属于非绑定方法。

#### 带self的方法和classmethod是探讨的是python处理的东西  是第一个参数默认处理成什么的问题  是跟调用有关系的 跟方法体无关的
