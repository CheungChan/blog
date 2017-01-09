# python中super()的用法。  
python中的super()和java中的super()方法是不同的 ，当在python中使用super()的时候，尤其是之前是做java开发的，可能会认为这东西在python里怎么这么丑呢？看起来python里的``` super(cls,obj) ```和java里的``` super()``` 是一样的，但是当多继承存在的时候，事情可能就不对劲了。事实上在python里super()是一个类而不是一个方法。

#### 举例：  
![origin](image/origin.png)  
然后  
![LigerDetail](image/LigerDetail.png)  
输出  
![LigerOutput](image/LigerOutput.png)  
看起来什么都没有发生，但是当我们改变第一个参数的时候  
![LionDetail](image/LionDetail.png)  
输出  
![LionOutput](image/LionOutput.png)  
看起来哪里不对  
![TigerDetail](image/TigerDetail.png)  
输出  
![TigerOutput](image/TigerOutput.png)  
继续  
![AnimalDetail](image/AnimalDetail.png)  
输出  
![AnimalOutput](image/AnimalOutput.png)
所以说，正如我们看到的，存在一个 Cls.mro().事实上，第二个参数决定了这个list，第一个参数决定了在这个list中的什么位置开始。  
最后  
![thinkargs1](image/thinkargs1.png)  
输出  
![thinkargs1Output](image/thinkargs1Output.png)
#### 所以说，super()的伪代码是这样的
```
def super(arg1,arg2):
    _list = arg2.__class__mro()
    return _list[index(arg1.__class__) + 1]
```