##  工作中遇到的oracle行转列，多行记录合并一条的sql语句
``` sql
select 
    wmsys.wm_concat(d.prod_prop_name||':'||d.prod_prop_val) sku_name,
    c.prod_sku_id,
    c.sku_price,
    c.sku_origin_price,
    c.sku_foreign_currency_origin_price,
    c.sku_foreign_currency_price,
    c.prod_name 
from(
    select 
        a.prod_sku_id,
        a.sku_price,
        a.sku_origin_price,
        a.foreign_currency_origin_price,a.foreign_currency_price,
        b.prod_name
    from 
        B2C_PROD_SKU a,
        B2C_PRODUCT b
    where
        b.merchant_id= #merchantId:VARCHAR# and a.prod_id=b.prod_id) c,
    B2C_PROD_SKU_PROP_INFO d 
where 
    c.prod_sku_id=d.prod_sku_id
group by 
    c.prod_sku_id,
    c.sku_price,
    c.sku_origin_price,
    c.sku_foreign_currency_origin_price,
    c.sku_foreign_currency_price,
    c.prod_name;
```
这里有一个坑就是wmsys.wm_comcat()函数返回值说clob，不是varchar所以在resultMap里要定义成\<result column="SKU_PROPERTY" property="skuProperty" jdbcType="CLOB" javaType="java.lang.String"/>

## oracle中decode()函数用法(oracle中的ifelse)
decode(条件,值1,返回值1,值2,返回值2...值n,返回值n,缺省值)  
该函数含义如下：  
IF 条件=值1 THEN  RETURN(返回值1)  
ELIF 条件=值2 THEN RETURN(返回值2)   
...  
ELSE RETURN（缺省值)  
END IF
## oracle中的sign函数，比较大小
sign(变量1-变量2)返回0,-1,1 可结合decode使用
