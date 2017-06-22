kill -9 $(ps -ef | grep $1 | grep -v grep | awk '{print $2}')
