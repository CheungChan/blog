# CentOS 6 安装 Python3.5以及配置Django(修订版)

## 安装python3.5
### 1 准备编译环境(环境如果不对的话，可能遇到各种问题，比如wget无法下载https链接的文件)
```
yum groupinstall 'Development Tools'
yum install zlib-devel bzip2-devel  openssl-devel ncurses-devel
```
### 2 下载 Python3.5代码包
```
wget  https://www.python.org/ftp/python/3.5.0/Python-3.5.0.tar.xz
```
### 3.下载的编译版本的python缺少一些东西，必须先安装这些东西在重新编译python再安装，否则以后启动django时报错找不到这些包，这些东西采用yum install xx进行安装。
**安装必须的软件包**
- readline-devel                                   //如果不安装该包，安装的python在交互式模式下就不能使用向上翻和向下翻
- tk-devel 和tcl-devel                          //如果不安装这两个包，安装的python就没有tkinter和ttk这两个模块
- openssl-devel                                   //如果不安装该包，将缺少HTTPSHandler模块
- sqlite-devel                                       //django默认使用的数据库
### 4 编译
```
tar Jxvf  Python-3.5.0.tar.xz
cd Python-3.5.0
./configure --prefix=/usr/local/python3
make && make install
```
### 4 设置环境变量
```
vi /etc/profile
末尾加上两行
export PATH=$PATH:/usr/local/python3/bin
export PATH=$PATH:/usr/local/python3/lib/python3.5/site-packages
```
### 5 将系统默认的python替换为pyhton3
```
rm   /usr/bin/python
ln -sv  /usr/local/python3/bin/python3.5 /usr/bin/python
rm  /usr/bin/python2
ln -sv /usr/bin/python2.6 /usr/bin/python2
```
这样做的目的是在系统任意目录敲入python调用的是python3的命令，而非系统默认2.6.6的
但是这样同时这会导致依赖python2.6的yum不能使用，因此还要修改yum配置。
### 6 更新yum配置。
```
ll /usr/bin | grep python
```
这时/usr/bin目录下面是包含以下几个文件的（ll |grep python），其中有个python2.6，只需要指定yum配置的python指向这里即可
```
vim /usr/bin/yum
通过vim修改yum的配置
#!/usr/bin/python改为#!/usr/bin/python2.6，保存退出。
```
## 完成了python3的安装。安装pip
方法一：
```
rpm -ivh http://dl.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
yum install -y python-pip
```
方法二：
如果输入pip报错,可以采用编译安装
首先我们先下载下载python-pip的tar包：
下载pip
```
wget https://pypi.python.org/packages/source/p/pip/pip-7.1.0.tar.gz --no-check-certificate
 ```
注意问题
这里如果没有安装setuptools是会出现错误（ImportError: No module named setuptools ）的。具体可以参考下面方法解决　：
```
wget https://pypi.python.org/packages/source/s/setuptools/setuptools-18.0.1.tar.gz --no-check-certificate
tar zxvf setuptools-18.0.1.tar.gz
cd setuptools-18.0.1
python setup.py build
python setup.py install
 ```
安装PIP
```
cd ..
chmod +x pip-7.1.0.tar.gz
tar xzvf pip-7.1.0.tar.gz
cd pip-7.1.0
python setup.py install
pip
```
安装成功
## 安装git：
执行并根据提示一路next，安装完成后执行git --help测试是否安装成功。需要说明git的安装不是必须的，除非你希望始终保持最新发布的django代码，否则可以忽略。
 ```
 yum install git
 ```
## 安装django

有了pip，这件事变得非常简单，只需要一个命令即可。需要说明的是和上面的问题一样，你需要在系统classpath目录下建立到django-admin.py的连接。
```
pip install django
ln -s /usr/local/python3/lib/python3.5/site-packages/django/bin/django-admin.py    /usr/bin/django-admin.py
```
这里视你的django安装位置而定——一般是python的site-packages下。
如果pip没法执行，直接去官方下载Django最新版，编译安装。
它会安装在你的python3目录里面，所以需要配置环境变量。
执行后django安装完成。下面建立一个演示的website，参考来自django的指导，通过执行命令
```
cd 要新建工程的目录
django-admin.py startproject mysite
```
建立一个名为mysite的工程其结构如下：
```
mysite/ 
       manage.py   
       mysite/ 
                __init__.py 
                settings.py 
                urls.py
                wsgi.py
```
外层mysite：仅仅是工程的容器，叫什么名字没关系。    
manage.py:一个命令行的工具类用于和你的工程交互。  
内层mysite：实际的python package。     
__init__.py:空文件，它的存在是为了表明这是一个python package。    settings.py: Django的配置文件。   
urls.py：简单的说就是用于url派发的配置。   
wsgi.py: 用于和wsgi server连接的接入点，商用部署时用到的server。  
到现在django的安装和工程已经建立，可以启动django开发的服务器了。django警告这个内置的server只适合开发用而非商用是没有安全保护的。执行以下命令启动django：
```
python manage.py runserver
```
这时候如果你是直接在linux主机上访问的那么已经可以通过localhost:8000来访问了，如果和我一样通过vmvare启动的linux并想在自己的物理机上访问暂时是行不通的。
可以通过命令
```
python manage.py runserver 0.0.0.0:8000
```
使django server监听public ip以便从外面访问