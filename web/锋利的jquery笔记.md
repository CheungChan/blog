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
$("div:hidden").show(3000)是样式display为none的和input type="hidden"的和visibility:hidden"的元素 show是展示元素单位是毫秒
#### ④属性过滤选择器
$("div[title]")是div中有title属性的元素
$("div[title=test]")是div中title属性为test的元素,不为test的元素用!=,以test开头的元素，结尾用$=,包含用*=，等于或者以test为前缀字符串以-连接用|=，用空格分隔的值中含有的用~=，复合元素写多个[]
#### ⑤子元素过滤选择器
$("div:nth-child(even)")是每个div的索引值是偶数的元素
nth-child(odd)奇数 :nth-child(2) 索引值为2的，:nth_child(3n)索引值是3的倍数的:nth-child(3n+1)索引值是3n+1的。 

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
### 表单选择器
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

==选择器注意事项==

1.如果表达式中含有特殊字符# .  (  [,等

如id="id#b" 获取这样的元素采用$("#id\\\\#b")
如id="id[1]" 获取这样的元素采用$("#id\\\\[1\\\\]")

2.要注意空格
如$(".test :hidden")表示.test的hidden的后代元素，而$(".test:hidden")表示.test中hidden的元素，加空格是层次选择器中的后代选择器 不加空格是过滤选择器中的可见性过滤选择器。


