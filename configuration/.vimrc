set encoding=utf-8
set fileencodings=utf-8,chinese,latin-1
language messages zh_CN.utf-8
set guifont=consolas:h16:b
set wrap
"设置自动缩进"
set autoindent
"设置主题"
"colorscheme peachpuff
colorscheme ron
set ts=4  
set expandtab  
set shiftwidth=4  
"设置语法高亮"
syntax on
set autoread
""设置ctrl+A为全选"
map <C-A> ggVG
map! <C-A> <Esc>ggVG
""设置键盘ctrl+C为复制"
vmap <C-c> "+y
"设置标尺"
set ruler
""设置光标当前行浅色高亮"
set cursorline
"高亮当前列
set cursorcolumn
"设置自动侦测文件类型"
filetype on
""根据不同的类型加载不同的插件
filetype plugin on
"设置自动缩进"
filetype indent on
""使回格键（backspace）正常处理indent, eol, start等"
set backspace=2
" 允许backspace和光标键跨越行边界"
set whichwrap+=<,>,h,l
" "共享剪贴板  
set clipboard+=unnamed
set number
set relativenumber
"搜索结果语法高亮"
set hlsearch
""设置前缀键
let mapleader="kj"
"定义快捷键到行首行尾
nmap LB 0
nmap LE $
""设置将选中的文本复制到系统剪贴板
vnoremap <Leader>y "+y
"设置快捷键将系统剪贴板内容粘贴到vim
nmap <Leader>p "+p
""定义快捷键关闭当前分割窗口
nmap <Leader>q :q<CR>
"定义快捷键保存当前窗口内容
nmap <Leader>w :w<R>
""定义快捷键保存所有窗口的内容并退出
nmap <Leader>WQ :wa<CR>:q<CR>
"不做任何保存直接退出vim
nmap <Leader>Q :qa!<CR>
""依次遍历子窗口
nnoremap nw <C-W><C-W>
"跳转到右方的窗口
nnoremap <Leader>lw <C-W>l
""跳转到左方的子窗口
nnoremap <Leader>hw <C-W>h
"跳转到上方的子窗口
nnoremap <Leader>kw <C-W>k
""跳转到上方的子窗口
nnoremap <Leader>jw <C-W>j
"定义快捷键在结对符之间跳转
nmap <Leader>M %
""让配置变更立即生效
autocmd BufWritePost $MYVIMRC source $MYVIMRC
"开启实时搜索功能
set incsearch
""搜索时大小写不敏感
set ignorecase
"关闭兼容模式
set nocompatible
""vim 自身命令行式补全
set wildmenu

