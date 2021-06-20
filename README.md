 # eblog后端项目的spring cloud alibaba版本
 
 ## 工程文件夹
```
 iblog
 ├── iblog-attach 附加功能服务
 ├── iblog-auth 认证功能服务
 ├── iblog-blog 博客核心服务
 ├── iblog-common 公共文件夹
 ├── iblog-gateway 网关服务
 ├── iblog-user 用户服务
 ├── iblog-service-api 服务接口模块
 └── sql 服务中用到的sql文件
 ```

## 环境搭建
1.  准备好mysql、redis、nacos、sentinel
2. 创建一个eblog库，如需自定义数据库名称，则需要在iblog-common包中的entity/pojo里面需改表映射
3. 在eblog库中执行sql文件夹下的sql命令
4. 修改每个项目服务中的MYSQL地址、Redis地址、nacos地址、sentinel地址
5. 启动项目即可

## 链接
该项目是eblog的微服务版本，前端版本在[git](https://github.com/adroitwolf/blog-vue);后端单机版本在[这里](https://github.com/adroitwolf/eblog-cloud)