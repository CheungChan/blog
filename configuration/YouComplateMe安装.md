YouCompleteMe(YCM)是一个功能非常强大的代码补全工具，可说是python开发的最佳搭档。  

在安装YCM前，还需要先安装一些必须的软件：
```bash
sudo apt install python-dev python3-dev cmake
```
接着在~/.vimrc上文的空白处添加YCM配置：
```vim
Plugin 'Valloric/YouCompleteMe'
```
然后运行vim在normal模式下运行<code>:PluginInstall</code>，系统即会将YCM安装到~/.vim/bundle/目录下。  
这里需要特别提醒大家的是YCM的体积比较大，等待的时间会比较长，有时会出错退出安装，这时在运行vim时，在窗口下部会出现红色的<code>YouCompleteMe unavailable: No module named 'future'</code>的提示。这是由于YCM没有下载完毕所造成的，这时可以换用Git来继续安装YCM:  
在Shell下输入
```bash
cd ~/.vim/bundle/YouCompleteMe
```
进入YCM目录，然后输入
```bash
git submodule update --init --recursive
```
命令，如下载过程中又出现中断出错，就继续运行此命令。  
在克隆完成之后，输入：  
```bash
./install.py --clang-completer
```
然后再在文件夹里执行<code>./install.py</code>安装，根据你的机器环境可能会提示你先执行一个Git命令克隆一些必须的库，按照提示运行就可以了。克隆完成之后再执行<code>./install.py</code>。
运行完毕后可在.vimrc文件中添加以下配置来让完成补全之后preview窗口自动消失：
```vim
let g:SimpyIFold_docstring_preview=1
```
复制.ycm_extra_conf.py文件至~/.vim目录下
```bash
cp ~/.vim/bundle/YoucompleteMe/third_parth/ycmd/examples/.ycm_extra_conf.py ~/.vim/
```
在.vimrc中添加YCM配置，打开.vimrc文件，在文件最后加入：
```vim
let g:ycm_server_python_interpreter='/usr/bin/python3'
let g:ycm_global_ycm_extra_conf='~/.vim/.ycm_extra_conf.py'
```
此处要填写你自己机器上的python解释器的版本位置。

以下配置你可以根据自己需求进行配置：
```vim
set completeopt-=preview
```
补全内容不以分割子窗口形式出现，只显示补全列表
```vim
let g:ycm_autoclose_preview_window_after_completion=1
```
完成操作后自动补全窗口不消失

作者：zhengjie
链接：https://www.jianshu.com/p/d8ea4bbff59c
來源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
