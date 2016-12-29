## html中字体过大导致上下重叠
解决办法：在重叠的行上加入style="line-height:150%;padding-top:10px;margin-top:30px;display:-webkit-inline-box;padding-bottom:22px;"
## 设置顶层固定的菜单并居中
```css
#box a{
    color:greenyellow;
    text-decoration:none;
    display:block;
    width:25%;
    float:left;
    text-align:center;
    height:50px;
    line-height:50px;
}
#box a:hover{
    color:yellow;
    text-decoration:underline;
    background:hotpink;
}
#box {
    position:fixed;
    top:0;
    left:0;
    width:100%;
    background:black;
    font-size:25px;
}
```
设置菜单固定在最上面：将position设为固定。top:0使得菜单在最上面。left:0或者right:0和width:100%使得整个box行被占满。   
设置菜单居中，导航栏均分设置元素的宽度为百分数，由于a是行内元素没有宽度，所以设置width=25%之前必须设为块级元素才能生效。设置成块级元素之后会从上到下排列，所以又设置了float:left使得元素从左到右排列。设置text-align:center使得文字水平居中。设置height与line-height相同的值使得文字垂直居中。