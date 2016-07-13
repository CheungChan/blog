## git常用分支操作
git不要在下代码的主分支上修改代码，要checkout一个开发分支，在上面开发，开发完成后再切换回主分支，
进行衍合或合并操作。最后再在主分支上向远程提交代码。类似的修bug也要在主分支上创建一个分支进行操作，
**永远确保主分支是稳定版。**
## git修改密码
打开git bash
输入 cd ~/.ssh
ls 确定有 id_rsa 和 id_rsa.pub文件
ssh-keygen -p -f id_rsa
第一次输入旧密码
新密码
确认新密码
## git压缩多次提交为一次提交
比如压缩最后4次提交为一次提交   
```
git rebase -i HEAD~4
```
该命令执行后，会弹出vim的编辑窗口，4次提交的信息会倒序排列，
最上面的是第四次提交，最下面的是最近一次提交。  
![image](image/1.png)
我们需要修改第2-4行的第一个单词pick为squash，
这个意义为将最后三次的提交压缩到倒数第四次的提交，
效果就是我们在pick所在的提交就已经做了4次动作，但是看起来就是一次而已：  
![image](image/2.png)
然后我们保存退出，git会一个一个压缩提交历史，如果有冲突，需要修改，修改的时候要注意，
保留最新的历史，不然我们的修改就丢弃了。修改以后要记得敲下面的命令：
```
git add .
git rebase --continue
```
如果你想放弃这次压缩的话，执行以下命令：
```
git rebase --abort
```
如果所有冲突都已经解决了，会出现如下的编辑窗口：  
![image](image/3.png)
这个时候我们需要修改一下合并后的commit的描述信息，我们将其描述为helloworld吧：  
![image](image/4.png)
如果想压缩第一三四次的提交，不压缩第二次的提交，可以移动一下提交顺序。
## 修改最后一次提交
如果你已经完成提交，又因为之前提交时忘记添加一个新创建的文件，想通过添加或修改文件来更改提交的快照，
也可以通过类似的操作来完成。 通过修改文件然后运行 git add 或 git rm 一个已追踪的文件，
随后运行 git commit --amend 拿走当前的暂存区域并使其做为新提交的快照。
使用这个技巧的时候需要小心，因为修正会改变提交的 SHA-1 校验和。 
它类似于一个小的衍合 - 如果已经推送了最后一次提交就不要修正它。
## Stash未提交的更改
你正在修改某个bug或者某个特性，又突然被要求展示你的工作。而你现在所做的工作还不足以提交，这个阶段你还无法进行展示（不能回到更改之前）。在这种情况下， git stash可以帮助你。
stash在本质上会取走所有的变更并存储它们为以备将来使用。stash你的变更，你只需简单地运行下面的命令-  
```
git stash
```
希望检查stash列表，你可以运行下面的命令：  
```
git stash list
```
![image](image/5.png)
如果你想要解除stash并且恢复未提交的变更，你可以进行apply stash:  
```
git stash apply
```
在屏幕截图中，你可以看到每个stash都有一个标识符，一个唯一的号码（尽管在这种情况下我们只有一个stash）。
如果你只想留有余地进行apply stash，你应该给apply添加特定的标识符：
```
git stash apply stash@{2}
```
丢弃stash区的内容  
```
git stash drop
```
如果想应用stash同时丢弃
```
git stash pop
```
就相当于先执行git stash apply 再执行 git stash drop  
## Cherry Pick
我把最优雅的Git命令留到了最后。cherry-pick命令是我目前为止最喜欢的git命令，
既是因为它的字面意思，也因为它的功能。

简而言之，cherry-pick就是从不同的分支中捡出一个单独的commit，
并把它和你当前的分支合并。如果你以并行方式在处理两个或以上分支，
你可能会发现一个在全部分支中都有的bug。如果你在一个分支中解决了它，
你可以使用cherry-pick命令把它commit到其它分支上去，而不会弄乱其他的文件或commit。

让我们来设想一个用得着它的场景。我现在有两个分支，并且我想cherry-pick b20fd14:
Cleaned junk 这个commit到另一个上面去。  
![image](image/6.png)
我切换到想被cherry-pick应用到的这个分支上去，然后运行了如下命令：
```
git cherry-pick [commit_hash]
```
![image](image/7.png)