## 通过了解float在计算机中的表示形式来理解为什么浮点数会可能算错。
10个0.1相加结果不是1，为什么？  
float由符号数、尾数、基数、指数四部分组成。其值结果为符号数 尾数*（基数的指数次方）。其中基数固定为2。所以实际计算机表示只需要符号数、尾数、指数三部分。  
对于单精度浮点数（float）而言，共32位。第0位表示符号数，只有0和1两种分别表示正负。1-8表示指数部分，9-31表示尾数部分。  
对于双精度浮点数（double）而言，共64位。第0位表示符号数，只有0和1两种分别表示正负。1-11表示指数部分，12-63表示尾数部分。  
由于浮点数的尾数和基数有多种表示方式，所以规定了一种国际标准。其中十进制的小数的尾数遵循小数点前面为0，小数点后面第一位不为0的规则。二进制的小数遵循小数点前面的值固定为1。而实际中1.xxxx的1不需要保存，这样可以表示的数值范围更多。而指数部分采用的是EXCESS系统表现。也就是将指数的中间部分值设为0，使得负数不需要用符号来表示。比如当指数用8位的数表示的时候，最大值11111111=255的1/2，即01111111=127（小数部分舍弃）表示0，所以126可以用来表示-1。

```java
public class Test{
    public static void main(String[] args) {
        float f = 0.75f;
        System.out.println("存入的小数： " + f);
        int in = Float.floatToRawIntBits(f);
        String binaryString = Integer.toBinaryString(in);
        String buling = "";
        for(int i=0;i<Float.SIZE-binaryString.length();i++){
            buling += "0";
        }
        binaryString = buling + binaryString;
        String flag = binaryString.substring(0, 1); // 第1位 符号位
        String finger = binaryString.substring(1, 9); // 第1-8位 指数位
        String tail = binaryString.substring(9, 32); // 第9-31位 尾数位
        System.out.println("在内存中的表示形式为： " + flag + "-" + finger + "-" + tail);
        System.out.println("原样转换回去： " + Float.intBitsToFloat(in));
        System.out.println("符号数： " + flag);
        System.out.println("指数： " + finger);
        int _finger = Integer.parseInt(finger, 2) - 127;
        System.out.println("EXCESS系统还原之后的指数： " + _finger);
        System.out.println("尾数： " + tail);
        String realTail = "1." + tail;
        System.out.println("实际的尾数： " + realTail);
        // 将二进制的尾数转换为十进制的尾数。
        double _dtail = 1;
        for(int i=2;i < realTail.length();i++){
            char c = realTail.charAt(i);
            if(c=='0'){
                continue;
            }
            double d = Double.parseDouble("" + c) * Math.pow(2, 1-i);
            _dtail += d;
        }
        System.out.println("尾数的二进制数转换为十进制数： " + _dtail);
        double value = _dtail * Math.pow(2,_finger);
        // 值为尾数乘以2的指数次方。
        if("1".equals(flag)){
            value = -value;
        }
        System.out.println("手动转换之后： " + value);
    }
}
```
输入
```
存入的小数： 0.75
在内存中的表示形式为： 0-01111110-10000000000000000000000
原样转换回去： 0.75
符号数： 0
指数： 01111110
EXCESS系统还原之后的指数： -1
尾数： 10000000000000000000000
实际的尾数： 1.10000000000000000000000
尾数的二进制数转换为十进制数： 1.5
手动转换之后： 0.75
```
而当把0.75换成0.1之后输出
```
存入的小数： 0.1
在内存中的表示形式为： 0-01111011-10011001100110011001101
原样转换回去： 0.1
符号数： 0
指数： 01111011
EXCESS系统还原之后的指数： -4
尾数： 10011001100110011001101
实际的尾数： 1.10011001100110011001101
尾数的二进制数转换为十进制数： 1.600000023841858
手动转换之后： 0.10000000149011612
```
可以看出0.1的尾数是一个循环小数，无法表示。  
2的负几次方，分别可以区分0.5,0.25,0.125....所以无法表示0.1  
如果要想精确计算，可以把小数转换为整数，在除就可以了。  
