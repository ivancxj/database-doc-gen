# database-doc-gen
数据库文档生成器

- 运行程序后按照下面提示输入对应数据库参数：

```bash
input host :
input port :
input username :
input password :
input database name :

```
- 输入完成后回车，即可生成数据库文档目录${dbname}-doc,目录中文档以markdown文件为载体：

![doc](doc.jpg)

- 确保安装了gitbook后，进入上述文件目录的命令行窗口运行：gitbook serve
- 访问 http://localhost:4000，即可在线查看数据库文档

![summary](summary.jpg)

![table](table.jpg)