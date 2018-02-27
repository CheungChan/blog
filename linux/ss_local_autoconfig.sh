#!/bin/bash
# 使用全局变量PM,因为shell函数的返回值只能是数字,不能是字符串.
PM=""
Get_Dist_Name()
{
    if grep -Eqi "CentOS" /etc/issue || grep -Eq "CentOS" /etc/*-release; then
        DISTRO='CentOS'
        PM='yum'
    elif grep -Eqi "Red Hat Enterprise Linux Server" /etc/issue || grep -Eq "Red Hat Enterprise Linux Server" /etc/*-release; then
        DISTRO='RHEL'
        PM='yum'
    elif grep -Eqi "Aliyun" /etc/issue || grep -Eq "Aliyun" /etc/*-release; then
        DISTRO='Aliyun'
        PM='yum'
    elif grep -Eqi "Fedora" /etc/issue || grep -Eq "Fedora" /etc/*-release; then
        DISTRO='Fedora'
        PM='yum'
    elif grep -Eqi "Debian" /etc/issue || grep -Eq "Debian" /etc/*-release; then
        DISTRO='Debian'
        PM='apt'
    elif grep -Eqi "Ubuntu" /etc/issue || grep -Eq "Ubuntu" /etc/*-release; then
        DISTRO='Ubuntu'
        PM='apt'
    elif grep -Eqi "Raspbian" /etc/issue || grep -Eq "Raspbian" /etc/*-release; then
        DISTRO='Raspbian'
        PM='apt'
    else
        DISTRO='unknow'
    fi
}
# 执行函数
Get_Dist_Name
# 更新软件源
# 把变量作为命令的写法 先定义变量把命令赋值给变量  然后eval加变量名.
cmd="$PM update -y && $PM upgrade -y"
echo "$cmd"
eval "$cmd"
# 安装 pip 和 privoxy
cmd="$PM install python-pip privoxy -y"
echo "$cmd"
eval "$cmd"
#  安装 shadowsocks
pip install shadowsocks

# 相关配置文件
sscfg="/etc/ss.json"
privoxycfg="/etc/privoxy/config"
proxycmd="/usr/local/bin/proxy"

# 创建 shadowsocks 配置样例
cat >"$sscfg"<<EOF
{
    "server":"vultr.cheungchan.cc",
    "server_port":27343,
    "local_address": "127.0.0.1",
    "local_port":1080,
    "password":"Alang34925//",
    "timeout":300,
    "method":"aes-256-cfb",
    "fast_open": true,
    "workers": 1
}
EOF

# 备份 privoxy 配置
mv $privoxycfg /etc/privoxy/config.bak

# 创建 privoxy 配置
cat >"$privoxycfg"<<EOF
# 转发地址
forward-socks5   /               127.0.0.1:1080 .
# 监听地址
listen-address  localhost:8118
# local network do not use proxy
forward         192.168.*.*/     .
forward            10.*.*.*/     .
forward           127.*.*.*/     .
EOF

# 创建代理脚本
cat >"$proxycmd"<<EOF
#!/bin/bash
http_proxy=http://127.0.0.1:8118 https_proxy=http://127.0.0.1:8118 \$*
EOF

# 增加执行权限
chmod +x $proxycmd

echo "安装完成!"
echo "shadowsocks 配置请修改 /etc/ss.json!"
echo "使用 nohup sslocal -c /etc/ss.json & 后台运行 shadowsocks!"
echo "使用 systemctl start privoxy 启动privoxy!"
echo "使用 proxy xxxx 代理指定应用!"
