## 1.在js中获取velocity变量
要在vm文件的js中使用velocity的变量可以先var jq = jQuery.noConflict(); 讲$的控制权交出来之后jQuery的$用jq代替。velocity变量直接$
## 2.各种jQuery选择器
### （一）层次选择器：
$("body div")是body内的所有div  
$("body > div")是body内的子元素  
$(".one + div")是.one后面的一个div，相当于$(".one").next("div")  
$(".one ~ div")是.one后面的所有div，相当于$(".one").nextAll("div")  
$(".one~div")是.one后面的所有同辈div，$(".one").siblings("div")是.one的所有同辈div
### （二）过滤选择器：
#### ①基本过滤选择器
$("div:not(.one)")是div中所有不是.one的  
$(":header")是所有标题元素  
$(":animated")是当前执行动画的元素  
$(":focus")是当前获取焦点的元素。  
#### ②内容过滤选择器
$("div:contains('我')")是div中包含文本我的元素  
$("div:empty")是div中不包含子元素的（包括文本元素）的  
$("div:parent")是div中包含子元素的（包括文本元素）的  
$("div:has('.mini')")是class为mini的div元素
#### ③可见性过滤选择器
$("div:visible")是可见的div元素
$("div:hidden").show(3000)是样式display为none的和input   type="hidden"的和visibility:hidden"的元素 show是展示元素单位是毫秒  
#### ④属性过滤选择器
$("div[title]")是div中有title属性的元素  
$("div[title=test]")是div中title属性为test的元素,不为test的元素用!=,以test开头的元素，结尾用$=,包含用*=，等于或者以test为前缀字符串以-连接用|=，用空格分隔的值中含有的用~=，复合元素写多个[]  
#### ⑤子元素过滤选择器
$("div:nth-child(even)")是每个div的索引值是偶数的元素  
nth-child(odd)奇数 :nth-child(2)索引值为2的，:nth_child(3n)索引值是3的倍数的:nth-child(3n+1)索引值是3n+1的。 

==:nth-child和eq()的区别::nth-child为每一个父元素匹配子元素。eq只匹配当前集合的一个元素。并且eq()索引从0开始，:nth-child索引从1开始==

$("div:first-child")匹配div中的第一个子元素  
$("div:last-child")匹配div中的最后一个子元素
``` $("div:only-child") ```
匹配div中唯一的子元素。如果div只有一个子元素才会被匹配
#### ⑥表单对象属性过滤选择器
$("form1 :enabled")是form1中所有可用元素,:disabled是所有不可用元素  
$("input:checked")是input中所有被选中的元素（包括单选框和复选框）

``` $("select option:selected"）```
选取所有选中的元素（下拉列表）
### （三）表单选择器
:input 是选取所有的<input>,<textarea>,<select>和<button>元素  
:text 选取所有的单行文本框  
:password选取所有的密码框  
:radio选取所有的单选框  
:checkbox选取所有的多选框  
``` :submit ```选取所有的提交按钮  
:image选取所有的图像按钮  
:reset选取所有的重置按钮  
:button选取所有的按钮  
:file选取所有的上传域  
:hidden 选取所有的不可见元素  

#### 选择器注意事项  

1.如果表达式中含有特殊字符# .  (  [,等  
如id="id#b" 获取这样的元素采用$("#id\\\\#b")  
如id="id[1]" 获取这样的元素采用$("#id\\\\[1\\\\]")  
2.要注意空格
如$(".test :hidden")表示.test的hidden的后代元素，而$(".test:hidden")表示.test中hidden的元素，加空格是层次选择器中的后代选择器 不加空格是过滤选择器中的可见性过滤选择器。
## 3.jQuery中的DOM操作
#### 节点操作
①：创建节点  
使用jQuery工厂函数即可$("<p>你好</p>")  
②：插入节点  
append() 向每个匹配的元素内部追加内容  
appendTo()将所有匹配的内容追加到指定元素中，与append()相反  
prepend()向每个匹配的元素内部前置内容  
prependTo()将所有匹配的元素前置到指定的元素中，与prepend()相反  
after()向每个匹配的元素之后插入内容  
insertAfter()将所有匹配的内容插入到指定元素后面，与after()相反  
before()在每个匹配的内容之前插入内容  
insertBefore()将所有匹配的内容插入到指定元素前面，与before()相反  
③：删除节点  
remove()将匹配的元素删除（包括它的所有后代节点），并且会删除这些元素绑定的事件，可以加参数过滤节点，返回值是此节点的引用，后面可以用appendTo()再添加进去。  
detach()方法，将匹配的元素删除（包括它的所有后代节点），不会删除这些元素绑定的事件。
empty()清空节点里的东西。如果没有后代节点则清空节点的内容。 
④：复制节点  
clone()方法复制节点不会复制绑定的事件，但可以传入参数true来使副本具有绑定的事件。如复制的节点不具有再复制的功能，传入true可以再复制。  
⑤：替换节点
a.replaceWith(b);
将所有符合条件的a替换为b
a.replaceAll(b);
将a替换掉符合条件的a，与replaceWith相反
⑥：包裹节点
a.wrap(b)；将每一个符合条件的a被b包裹
a.wrapAll(b)；将所有符合条件的a被一个b包裹
a.wrapInner(b);将a的每一个子元素被b包裹
#### 属性操作
获取设置属性attr(),删除removeAttr()
样式操作：追加样式addClass(),删除样式removeClass(),切换样式toggleClass("another")如果有则增加没有则去掉。判断是否有样式hasClass()就相当于is(".anthor")  
jQuery 1.6 中prop()用来获取匹配元素集中第一个元素的属性值，removeProp()用来删除
#### 设置和获取值
单选框多选框的val()返回值为一个数组，也可以用数组设置值
#### 遍历节点
children()方法返回所有子元素不包括孙元素。
###### parent();parents("ul");closet("ul")的区别：
parent()返回匹配元素的父元素（一个）  
parents("ul")返回匹配元素的祖先元素，即父元素父元素的父元素。。。。。. 
closet("ul")是逐层向上查找父元素，再查找父元素的父元素，返回第一个匹配的祖先元素。
#### CSS-DOM操作
$("p").css("color");获取值  
$("p").css("color","red");设置值  
$("p").css({"fontSize":"30px","backgroundColor":"#888888"});设置多个值  
可以写成"fontSize"也可以写成"font-size",可以写成"30px"也可以写成30，传数字默认为px,如改单位只能写成"30em"  
offset()用来获取元素在当前视窗中的相对偏移，其中返回的对象包含两个属性，top和left。  
var offset=$("p").offset();  
var left = offset.left();  
var top = offset.top():  
postion()方法获取相对于最近的一个position属性为relative()或者absolute()的祖父及诶单的相对偏移，也是有两个属性top 和left  
scrollTop()和scrollLeft()方法用来获取滚动条相对顶端和左边的距离，也可以设置。

### 案例:
因为title属性相应太慢，用jquery实现效果，代码如下：
``` 
$(function(){
    var x = 10;
    var y = 20;
    $("a.tooltip").mouseover(function(e){
        this.myTitle = this.title;
        this.title="";
        //创建div元素
        var tooltip = "<div id='tooltip'>this.myTitle</div>";
        $("body").append(tooltip);
        $("#tooltip").
            css({
                "top":(e.pageY + y) + "px",
                "left":(e.pageX + x) + "px"
            }).show("fast");
    }).mouseout(function(){
        this.title = this.myTitle;
        $("tooltip").remove();
    }).mousemove(function(e){
        $("#tooltip").css({
            "top":(e.pageY + y)+"px",
            "left":(e.pageX + x) + "px"
        });
    });
});

```


