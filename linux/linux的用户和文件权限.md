#### /etc/passwd 查看用户
root:x:0:0:root:/root:/bin/bash  
bin:x:1:1:bin:/bin:/sbin/nologin  
daemon:x:2:2:daemon:/sbin:/sbin/nologin  
adm:x:3:4:adm:/var/adm:/sbin/nologin  
内容结构为  
用户名：密码：UID:GID:用户全名：home目录：shell
#### /etc/group 查看组
root:x:0:  
bin:x:1:bin,daemon  
daemon:x:2:bin,daemon  
内容结构为  
组名：用户组密码：GID:用户组内的用户名  
其中用户组内的用户名是兼职到该组的，也就是支持用户组（有效用户组），而不是初始用户组  
#### 对用户进行增删改 useradd usermod userdel  
#### 对用户组进行增删改 groupadd groupmod groupdel
