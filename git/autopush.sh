#!bin/sh
set -o nounset
set -o errexit
# if the script self name not in ./.info/exclude,just write append to the file
excludeAddr="./.info/exclude"
excludeInfo=$(cat $excludeAddr|grep $0)
if [ -z "$excludeInfo" ]; then
    echo $0>>$excludeAddr
fi
# get the origin branch
# e.g
# "master"
oribr=$(git branch | sed -n '/A/s/\s//gp')
#
#
# get the current branch
# e.g
# "dev" "dev~1 dev~0 " "dev~2 dev~1 dev~0 "
curbr=$(git branch | sed -n '\* /s///p')
s=""
num=1
if [ $# -eq 0 ];then
    s="$curbr"
elif [ $# -eq 1 ];then
    ((num=$1))
    ((i=$1-1))
    while [ $i -ge 0 ];do
        s="$s${curbr}~${i} "
        ((i=$i-1))
    done
else
    echo "最多输入一个参数"
    exit 1
fi
##
# #begin git command
##
echo "(1/7)初始化脚本成功"
git checkout $oribr
echo "(2/7)切换到${oribr}成功"
git pull --rebase --progress "origin"
echo "(3/7)衍合拉取成功"
git cherry-pick $s
echo "(4/7)优选${num}个提交成功"
git push --recurse-submodules=check --progress "origin" refs/heads/$oribr:refs/heads/$oribr
echo "(5/7)推送成功"
git checkout $curbr 
echo "(6/7)切换到${curbr}分支成功"
git rebase $oribr
echo "(7/7)衍合成功"
echo "脚本执行成功"

