## git简介
Git的诞生确实是一个有趣的故事，我们知道，当年Linus创建了开源的Linux，从此，Linux系统不断发展，现在已经成为最大的服务器系统软件了。(请不要傻傻分不清Linus和Linux)

但是随着Linux的不断壮大，就需要各种版本控制了，起初Linus带着他的小弟们使用的是BitKeeper(商业版本控制系统),之后呢由于某种原因BitKeeper的公司不让他们使用了，于是Linus自己花了两周时间写出了Git并且开源了(BitKeeper已哭晕在厕所)，阿弥陀佛，幸亏BitKeeper不让Linus他们用了，要不然我们现在也不会有这么好用的Git了，博主更不会在这写这篇博文了。

之后的岁月里，渐渐有了github,coding等一些可以使用git存储的网站，Git的江湖地位变得无可替代了，如果你是个开发者却还不会使用Git那就太out了。  

这里先引用一张图解释Git  
工作原理：
![image](image/fea7461e85aee80bbe96.png)
Git原理  
* Workspace:工作区，执行git add *命令就把改动提交到了暂存区，执行git pull命令将远程仓库的数据拉到当前分支并合并，执行git checkout [branch-name]切换分支  
* Index:暂存区，执行git commit -m '说明' 命令就把改动提交到了仓库区（当前分支）  
* Repository:仓库区（或本地仓库），执行git push origin master提交到远程仓库，执行git clone 地址将克隆远程仓库到本地
* Remote:远程仓库，就是类似github，coding等网站所提供的仓库

## 安装
1. 安装git

Linux和OSX系统：
```
yum/brew install git
```
windows请到官网下载相应版本

2. 设置username和email
```
git config --global user.name "你的名字"
git config --global user.email "邮箱"
```
本地单用户工作流  
流程图： 
![image](image/index_files_0.63(12-05-21-43-18).png)
1. 初始化git仓库
```
git init
```
执行后，在当前目录将生成一个.git隐藏文件夹
创建文件后，添加到暂存区
```
git add 文件
```
提交暂存区文件到git仓库
```
git commit -m '提交备注'
```
远程单用户工作流
![image](image/index_files_0.62(12-05-21-43-18).png)

克隆远端git或关联本地版本库
```
git clone 项目的git检出地址
git remote add origin 项目git检出地址
```
当克隆时出现要求输入密码，则表示尚未添加自己的公钥到git服务器端：
```
$ git clone git@git.ntalker.com:fengxu/my2.git
Initialized empty Git repository in /home/vagrant/my2/.git/
git@git.ntalker.com's password:
```
创建文件后，添加到暂存区
```
git add 文件
```
提交暂存区文件到git仓库
```
git commit -m '提交备注'
```
推送到远端git仓库
```
git push [-u跟踪] <远程主机名> <本地分支名>:<远程分支名>
```
若推送过程中提醒版本已经是过时的：
```
   mypro git:(develop) git push
To git@git.jixiang.com/mypro.git
 ! [rejected]        develop -> develop (fetch first)
error: failed to push some refs to 'git@git.jixiang.com/mypro.git'
hint: Updates were rejected because the remote contains work that you do
hint: not have locally. This is usually caused by another repository pushing
hint: to the same ref. You may want to first integrate the remote changes
hint: (e.g., 'git pull ...') before pushing again.
hint: See the 'Note about fast-forwards' in 'git push --help' for details.
```
需先拉取代码
```
git pull <远程主机名> <远程分支名>:<本地分支名>
```
git分支
![image](image/index_files_0.28(12-05-21-43-18).png)

本地创建分支
```
git checkout -b 新分支名称 [目标分支]
```
该命令等同于：
```
git branch 新分支 [目标分支] # 创建分支
git checkout 新分支名称      # 切换到新建的分支
```
编辑文件并提交
```
git add / git commit
```
合并到开发主分支
```
git checkout develop
git merge --no-ff 分支名
```
使用``` --no-ff ```会执行正常合并，在Master分支上生成一个新节点。为了保证版本演进的清晰，希望采用这种做法。   
4. 提交对develop开发分支的修改
```
git push -u origin develop:develop
```
``` -u```参数用于本地分支跟踪远程对应分支，以便下次使用git push直接提交  
 5. develop分支的某模块功能完成稳定后，需在Gitlab页面中提交Merge合并请求，合并到master分支  
git解决冲突
更新代码
```
git pull
```
编辑冲突
将修改完的冲突文件添加到暂存区
```
git add 冲突的文件
```
提交并推送到远端
```
git commit -m '提交备注'
git push
```
其他常用命令
查看分支
```
git branch [-a查看全部  -r查看远端分支]
```
查看文件状态
```
git status
```
查看文件修改
```
git diff 文件名
```
