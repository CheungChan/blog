#!bin/sh

#get the origin branch
#e.g
#"master"
oribr=$(git branch | sed -n '/A/s/\s//gp')
#
#
#get the current branch
#e.g
#"dev" "dev~1 dev~0 " "dev~2 dev~1 dev~0 "
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
##begin git command
##
echo "**********初始化脚本成功**********"
git checkout $oribr && echo "**********切换到${oribr}成功**********" && git cherry-pick $s && echo "**********优选${num}个提交成功**********" && git pull --rebase --progress "origin" && echo "**********衍合拉取成功**********" && git push --recurse-submodules=check --progress "origin" refs/heads/$oribr:refs/heads/$oribr && echo "**********推送成功**********" && git checkout $curbr && echo "**********切换到${curbr}分支成功**********" && git rebase $oribr && echo "**********衍合成功**********" && echo "**********脚本执行成功O(∩_∩)O哈哈~**********"

