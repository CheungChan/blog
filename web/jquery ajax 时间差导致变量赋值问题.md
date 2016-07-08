ajax异步请求，在各种特效方面，做出了不少的贡献，有了它让用户体验更好。下面说一下曾今遇到过的一个问题，今天又遇到了，又花了我一点时间，小问题，但是特别容易忽视，并且不容易想到是什么原因产生的。废话不多说，举个例子大家就明白了。
### 一，准备测试文件test.php和test.html
1，test.php

```php
<?php  
echo "1";  
?>  

```
2,test.html

```html
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />  
<script type="text/javascript" src="jquery-1.3.2.min.js"></script>  
</head>  
<body>  
<div>blog.51yip.com</div>  
</body>  
</html>  
<script type="text/javascript">  
js代码放到这里  
</script> 
```
### 二，问题举例

```
<script type="text/javascript">  
function test(value){                //定义一个function  
 error = false;                      //定义一个测试变量  
 $.ajax({                            //ajax异步请求  
 type: "POST",                       //传值方式post  
 url: "test.php",                    //异步请求的文件  
 data: "name="+value,                //异步请求的参数  
 success: function(msg){             //请求成功的回调函数  
 if($.trim(msg) == '1'){  
 error = true;                       //返回值为1，把error变成true  
 }  
 }  
 });  
 alert(error);                       //打印一下error的内容，在这里你知道它会弹出什么吗？  
 if(error == true){  
 $("div").remove();  
 }  
}  
test("good");  
</script>  
```
代码中的alert(error);不管msg返回什么，都只会弹出false，按javascript的执行原理，一般情况下都是顺序执行的，那为什么这个error的值没有被改变呢？原因就在于异步请求是有一个时间差的，为了验证这个时间差，在举个例子，可以让你清楚的看到，这个时间差。
### 三，验证ajax异步请求的时间差

```
<script type="text/javascript">  
function test(value){  
 error = false;  
 $.ajax({  
 type: "POST",  
 url: "test.php",  
 data: "name="+value,  
 success: function(msg){  
 if($.trim(msg) == '1'){  
 error = true;  
 alert(error);                        //在这里打印一下error  
 }  
 }  
 });  
 alert(error);                        //在这里打印一下error  
 if(error == true){  
 $("div").remove();  
 }  
}  
test("good");  
</script>  
```
当你刷新页面后，问题就很清楚，它首先弹出的是false,然后弹出了true，二次弹出之间的时间差，就是ajax异步请求的时间差。从表面上看，这段js代码的执行顺序是这样的上--下--中，其实不是这样的，代码执行的顺序还是上--中--下。为什么会先执行下面的代码呢？那是因为ajax异步请求，需要时间，而js并没有去等待，所以在这里有一个时间差。
### 四，解决方法
1，把实际要操作的动作放到回调函数中，逃避这个时间差

```
<script type="text/javascript">  
function test(value){  
 $.ajax({  
 type: "POST",  
 url: "test.php",  
 data: "name="+value,  
 success: function(msg){  
 $("div").remove();                 //实际要操作的动作  
 }  
 });  
}  
test("good");  
</script> 
```
前面几个例子，是为了举例，真正写代码，不会那样写，哈哈。
2，进行同步请求

```
<script type="text/javascript">  
function test(value){  
 error = false;  
 $.ajax({  
 type: "POST",  
 url: "test.php",  
 async: false,                         //进行同步请求，默认是true的  
 data: "name="+value,  
 success: function(msg){  
 if($.trim(msg) == '1'){  
 error = true;  
 alert(error);                         //弹一下error  
 }  
 }  
 });  
 alert(error);                         //在弹一下error  
 if(error == true){  
 $("div").remove();  
 }  
}  
test("good");  
</script>  
```
当你刷新页面时，这里是弹出二个true,为什么会这样呢？加了async:false后，就会有一个等待的过程，也就是说ajax不执行完，不执行下面的代码。用这个方法有个问题，如果等待的时间过长，用户体验很不好的。

转载请注明
作者:海底苍鹰
地址:http://blog.51yip.com/jsjquery/1209.html


