﻿Option Explicit  
 
Dim objshell,path,DigitalID, Result  
Set objshell = CreateObject("WScript.Shell") 
'Set registry key path 
Path = "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\" 
'Registry key value 
DigitalID = objshell.RegRead(Path & "DigitalProductId") 
Dim ProductName,ProductID,ProductKey,ProductData 
'Get ProductName, ProductID, ProductKey 
ProductName = "系统名称：" & objshell.RegRead(Path & "ProductName") 
ProductID = "产品 ID：" & objshell.RegRead(Path & "ProductID") 
ProductKey = "已安装密钥：" & ConvertToKey(DigitalID)  
ProductData = ProductName  & vbNewLine & ProductID  & vbNewLine & ProductKey 
'Show messbox if save to a file  
If vbYes = MsgBox(ProductData  & vblf & vblf & "要将密钥保存到桌面文件吗？", vbYesNo + vbQuestion, "软媒魔方备份 Windows 密钥信息") then 
   Save ProductData  
End If 
 
 
 
'Convert binary to chars 
Function ConvertToKey(Key) 
    Const KeyOffset = 52 
    Dim isWin10, Maps, i, j, Current, KeyOutput, Last, keypart1, insert 
    'Check if OS is Windows 10 
    isWin10 = (Key(66) \ 6) And 1 
    Key(66) = (Key(66) And &HF7) Or ((isWin10 And 2) * 4) 
    i = 24 
    Maps = "BCDFGHJKMPQRTVWXY2346789" 
    Do 
           Current= 0 
        j = 14 
        Do 
           Current = Current* 256 
           Current = Key(j + KeyOffset) + Current 
           Key(j + KeyOffset) = (Current \ 24) 
           Current=Current Mod 24 
            j = j -1 
        Loop While j >= 0 
        i = i -1 
        KeyOutput = Mid(Maps,Current+ 1, 1) & KeyOutput 
        Last = Current 
    Loop While i >= 0  
     
    If (isWin10 = 1) Then 
        keypart1 = Mid(KeyOutput, 2, Last) 
        insert = "N" 
        KeyOutput = Replace(KeyOutput, keypart1, keypart1 & insert, 2, 1, 0) 
        If Last = 0 Then KeyOutput = insert & KeyOutput 
    End If     
     
 
    ConvertToKey = Mid(KeyOutput, 1, 5) & "-" & Mid(KeyOutput, 6, 5) & "-" & Mid(KeyOutput, 11, 5) & "-" & Mid(KeyOutput, 16, 5) & "-" & Mid(KeyOutput, 21, 5) 
    
     
End Function 
'Save data to a file 
Function Save(Data) 
    Dim fso, fName, txt,objshell,UserName 
    Set objshell = CreateObject("wscript.shell") 
    'Get current user name  
    UserName = objshell.ExpandEnvironmentStrings("%UserName%")  
    'Create a text file on desktop  
    fName = "C:\Users\" & UserName & "\Desktop\Windows密钥备份.txt" 
    Set fso = CreateObject("Scripting.FileSystemObject") 
    Set txt = fso.CreateTextFile(fName) 
    txt.Writeline Data 
    txt.Close 
End Function 