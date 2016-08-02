"设置编码"
set encoding=utf-8
set fileencodings=utf-8,chinese,latin-1
if has("win32")
    set fileencoding=chinese
else
    set fileencoding=utf-8
endif
language messages zh_CN.utf-8
"设置菜单不乱码"
source $VIMRUNTIME/delmenu.vim
source $VIMRUNTIME/menu.vim
"设置字体"
set guifont=Consolas:h14:cDEFAULT
"设置折行"
set wrap
"设置自动缩进"
set autoindent
"设置主题"
colorscheme torte 
"设置tab为4个空格"
set ts=4  
set expandtab  
set shiftwidth=4  
"设置语法高亮"
syntax on
"设置换行符为windows的"
set ffs=dos
"设置自动加载被其他程序修改的文件，相当于执行了:e!"
set autoread
""设置ctrl+A为全选"
"map <C-A> ggVG
"map! <C-A> <Esc>ggVG
""设置键盘ctrl+C为复制"
"vmap <C-c> "+y
"设置标尺"
set ruler
"设置光标当前行浅色高亮"
set cursorline
"设置隐藏图标栏"
set guioptions-=T
"设置自动侦测文件类型"
filetype on
"设置自动缩进"
filetype indent on
set lines=40 columns=155
"使回格键（backspace）正常处理indent, eol, start等"
set backspace=2
" 允许backspace和光标键跨越行边界"
set whichwrap+=<,>,h,l
"共享剪贴板  
set clipboard+=unnamed 
"设置启动时最大化"
if has('win32')    
	au GUIEnter * simalt ~x
else    
	au GUIEnter * call MaximizeWindow()
endif 

function! MaximizeWindow()    
	silent !wmctrl -r :ACTIVE: -b add,maximized_vert,maximized_horz
endfunction
"设置相对行号"
set number
set relativenumber
"搜索结果语法高亮"
set hlsearch
