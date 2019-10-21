# SpringBootApiTest
spring boot+angular的接口测试平台
angular前端代码在frontend分支下

---
### 更新历史：

#### 2019-10-21:
    1.升级版本到2.2.0.RELEASE
    2.替换已过时的方法
    3.修复登录/退出操作的一个bug

#### 2019-10-10:
    1.修复Rest请求时的错误
    2.升级版本到2.2.0.RC1
    3.增加socket.io的测试（netty.socket.io)

#### 2019-9-9:
    1.修复中文编码bug
    2.修复重置密码的数据库语句错误bug
    3.删除某个多余类

#### 2019-4-25:
    1.修复定时任务停止后再启动会重复上次任务的大bug

#### 2019-4-17:
    1.修复caseServiceImpl里的bug
    2.修改错误提示

#### 2019-4-12:
    1.修复api的mapper里method、url、envId三者的查询bug

#### 2019-4-4:
    1.升级springboot版本为2.1.4
    2.修复task的service和mapper的bug

#### 2019-3-14:
    1.修改task里quartz的bug
    2.修改Logs的字段
    3.修改错误提示内容

#### 2019-3-13:
    1.完成断言判断操作
    2.修改实体类的时间为时间戳

#### 2019-3-12:
    1.修改错误

#### 2019-3-8:
    1.去掉不必要的maven repository
    2.修改写入日志操作为通过redis的消息队列来异步操作

#### 2019-3-7:
    1.修改mapper的update语句的bug

#### 2019-3-6:
    1.修改Task的关联Api操作bug
    2.修改EnvComponent的注入问题

#### 2019-3-5:
    1.修改spring data jpa为mybatis
    2.更新springboot的版本为2.1.3
    
#### 2019-2-28:
    1.修复bug
    
#### 2019-1-28:
    1.完善任务调度功能
    
#### 2019-1-16:
    1.修改cookie的初始化代码
    2.修复bug
    
#### 2019-1-14:
    1.完善任务调度功能
    2.更新springboot的版本为2.1.2
    
#### 2019-1-10:
    1.修改定时任务的框架为Quartz
