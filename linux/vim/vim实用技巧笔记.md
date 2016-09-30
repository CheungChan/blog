## 31.重复上次的Ex命令
>* 命令：@:  
>* 举例: :bn[ext]用来在缓冲区列表中正向移动，如果要逐个查看每个缓冲区，可以使用@:来重复上次的:bn命令，运行过@:后可以用@@来重复。如果不小心按过了，当然可以通过:bp[revious]，但是更好的是使用<C-o>，作用是回到跳转列表中的上一条记录。对于操作缓冲区文本的Ex命令可以采用u来撤销，但是对于@:的回退，没有直截了当的方式。  

## 32.自动补全Ex命令
>* 命令：Tab、\<C-d>
>* 举例：：col<C-d>显示补全列表colder colorscheme选择用Tab，反向选择\<S-Tab>。vim的命令行补全非常智能。可以补全命令 文件 目录 标签名等等。补全风格有两种。一是bash shell风格的set wildmode=longest,list。另一种是zsh风格的set wildmenu  set wildmode=full


