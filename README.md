# plm

> 使用的是springboot2.2.6的版本，因此最好使用JDK8，为了方便部署，最后输出的是war包，可以直接放入tomcat的webapp目录，注意这里只测是了tomcat8的版本，对于老的或是新的版本请自行测试。

```
 # 需要引入项目中的jar包到本地的maven仓库
 mvn install:install-file  -DgroupId=com.digiwin -DartifactId=nist -Dversion=1.0  -Dpackaging=jar  -Dfile=NIST.jar
```

开发环境直接启动PlmLicenseApplication主类即可