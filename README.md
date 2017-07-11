# Introduction
    这是一个使用Spring Boot示例项目, 可以直接使它做生产环境的骨架，也可以当作一个学习示例，它集成了一个Web框架常用的东东。

    你需要有的开发环境: JDK8+  JetBrainsIDEA  Mysql  Mongo  Redis  ActiveMQ  Solr


# Features
    1) Spring Boot + MyBatis = Web API

    2) 统一异常处理

    3) 日志处理, 根据不同的active profile激活不同的日志级别

    4) 多环境配置: 正式(prod)/开发(dev)/测试(test)/本地(local)

    5) 对返回的JSON进行统一的封装

    6) 使用YAML而不是properties

    7) 使用配置类代替spring.xml

    8) 单元测试(完善中...)
    
    9) 启用spring缓存管理
    
    10) 集成mysql,mongod,redis,ActiveMQ,Solr
    
    11) mongo日志存储,访问日志，异常日志,超时响应日志
    
    12) mongo序列服务
    
    13) 缓存配置服务

    14) token+signature接口安全机制
    
    15) mysql读写分离
    
    16) ActiveMQ示例
    
    17) Solr使用示例(完善中...)

# Quick Start
     1) 初始化数据库, 在你自己的MySQL中执行/resource/user.sql

     2) 运行Application.java
