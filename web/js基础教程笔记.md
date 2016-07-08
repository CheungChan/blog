1.防止页面显示在框架中，或者说防止别人拦截自己的网页，采用if(top.location!=self.location){top.location.replace(self.location)}这样会使得替换浏览器历史而可以用back按钮如果用直接将top.location设置为self.location不能使用。

2.如需点一个链接加载一个iframe，只需将a链接的target设置为iframe的name或者id(针对不同的浏览器)

3.用js打开一个新窗口，var catWindow=window.open("images/pixels1.jpg","catWin","resizable=no,width=350,height=260,toobar=yes,location=yes,scrollbars=yes"); catWindow.focus;return false;第一个参数是内容指向，第二个是标题。其他参数不写等于写成=no

4.js没有replaceAll方法，可以用replace的正则代替。如替换换行。str.replace(new RegExp("<br>","igm"),"\n").i代表ignoreCase不区分大小写。g代表global，m代表multiline