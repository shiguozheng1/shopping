## 域控服务器组织架构同步
 域控服务器组织架构同步基于jpa+maven+mvc+springboot开发，
 具有：批量的创建和修改公司的组织架构以及修改部门的上下级的parentDn，提高工作人员的效率
 
 ### 快速启动
   * 数据库一个，ldap一个
   * IDEA插件一个，lombok插件
   * 修改AD域服务器的最大返回条目数限制（默认1000）
   
       
     具体使用方式如下：
     > 输入 ntdsutil 
     > 输入 help 命令，查看可用的命令；（选择ldap policies）
     > 输入 ldap policies
     > 输入 connections
     > 输入 connect to domain stepelectric.com
     > 输入 quit
     > 输入 set maxpagesize to 4000
     > 输入 show values
     > 输入 commit changes
    
     
     
   * 项目启动
   
    
     修改application.yml配置参数
     运行Application这个类的main方法 

   * 同步公司，部门，人员 
    
      
    http://localhost:8080/ldap/test    

    
   * 修改公司或部门的name和parentDn
    
    
     http://localhost:8080/ldap

   * 特殊情况如删除(是否需要移出部门下对应的人员)

    
     设置配置文件参数值：isMove(true移出  false不移出(默认))
     
     http://localhost:8080/ldap
        

   * 恢复已移出的人员(默认：ParentDN="OU=移出人员,DC=stepelectric,DC=COM")

    
     http://localhost:8080/ldap/recoveryPerson

