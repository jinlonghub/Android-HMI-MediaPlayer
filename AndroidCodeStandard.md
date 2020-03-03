#Android 代码规范

##命名规则
> 1. Pakage命名：全部小写，连续的单词只是简单地连接起来，不使用下划线  
>>   如：'com.exampl.playerdemo'  

----------
> 2. 类/ 接口命名  
>> + 使用大驼峰命名法，用名词或者名词词组命名，每个单词的首字母大写   
     如： 'public class MyActivity extends AppCompatActivity() { }'
>> + 接口命名规则与类一样采用大驼峰命名法，多以able 或 ible 结尾，如interface Runnable、interface Accessible 。

     |  类                  | 描述                          | 举例                       |  
     |  ----                |  ----                        | ----                       |  
     |  Activity 类         | 首字母大写，以 Activity 为后缀 | 登录页面类 LoginActivity    |  
     |  其他类同Activity     |                              |                            |  

----------
> 3. 变量命名  
>> + 成员变量 / 局部变量
    >> + 使用小驼峰命名。(除第一个单词以外，每一个单词的第一个字母大写)  
         例如：'protected void onCreate(Bundle savedInstanceState) { }'

----------
> 4. 常量命名   
>> 单词每个字母均大写  
>> 单词之间用下划线连接，力求语义表达完整清楚，不要嫌名字长。 
>> 如：'public static final int ONLY_IF_NO_MATCH_FOUND = 0x00000004;'    

----------
> 5. Function命名   
>> 使用小驼峰命名。   
>> 如： 'public static void verifyStoragePermissions(Activity activity) { }'  

----------
> 6. 资源文件命名  
>> 全部小写，采用下划线命名法。  
>> + 布局文件命名（xml 文件）  
>>>    例如： 'activity_player.xml'
>> + drawable 文件命名  

----------

