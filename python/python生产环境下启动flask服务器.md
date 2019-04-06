## python在生产环境下 用gunicorn 启动 flask 程序
```bash
nohup gunicorn -w 4 -b 127.0.0.1:5000 app:app &
```
启动4个进程用gunicorn来运行

亦可以编写gunicorn.conf配置文件,内容为
```editorconfig
workers = 4
bind = '0.0.0.0:8000'
proc_name = 'project_name'
pidfile = '/tmp/project_name.pid'
timeout = 3600
```

然后使用 gunicorn -c gunicorn.conf app:app -D 来启动