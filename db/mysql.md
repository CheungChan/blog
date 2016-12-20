## MySQL批量数据插入脚本优化

项目中有需要插入30万行数据到RDS（阿里的Mysql产品）的一张表，一般从外部导出的语句都是如下结构：
 ![image](image/原始批量插入.png)  
也就是有30万行INSERT语句，我们尝试执行一下看看
![image](image/开始执行原始批量.png)
大概13分钟后执行完毕，插入308801行数据
![image](image/原始批量插入结果.png)
我们把SQL改写成如下格式
![image](image/改进批量插入.png)
再执行一下看看
![image](image/改进批量插入结果.png)
可以看到，插入了308803行数据只需要3.91秒，效率提高了很多  
虽然mysql支持此种方式，但是oracle不支持。oracle插入多条数据的方式是
```sql 
insert into 表名(字段1，字段2) 
select 值1，值2 from dual 
union all 
select 值11,值22 from dual
```
可同样起到插入时间优化的效果。