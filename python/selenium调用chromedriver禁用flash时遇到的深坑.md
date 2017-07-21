今天中午到晚上，一直有一个问题让我头疼。就是使用selenium打开赶集网的页面，由于业务需求需要禁用掉flash。代码是这样的：
``` python
CHROME_NO_FLASH_ARG = [
    '--disable-component-update',
    '--allow-outdated-plugins',
    '--disable-bundled-ppapi-flash',
    'lang=zh_CN.UTF-8',
    '--start-maximized',
]
option = webdriver.ChromeOptions()
        arg = config.CHROME_NO_FLASH_ARG
        for iarg in arg:
            option.add_argument(iarg)
        driver = webdriver.Chrome(config.CHROME_DRIVER_PATH, chrome_options=option)
```
但是查看浏览器中chrome:version发现flash就是未禁用。整了一整天，最终发现是因为我是用的flash官网的安装包安装的，是ppflash，该flash被安装到了system32目录下而'--disable-bundled-ppapi-flash'只能禁用用户目录下的google文件夹下的flash，只有卸载了ppflash，而使用用户目录下的flash，才能被成功禁用。 坑死我了。