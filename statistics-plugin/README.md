# statistics-plugin
statistics-plugin is a jenkins plugin which aims to jenkins job&build statistics.

---------

## 目录
* [概述](#概述)
* [编绎](#编绎)
    * [环境准备](#环境准备)
    * [编绎工程](#编绎工程)
* [开发指南](#开发指南)
    * [工程目录说明](#工程目录说明)
    * [插件开发步骤](#插件开发步骤)

## 概述
---
通过jenkins插件查询与统计需要的构建资源数据并对外暴露类restful的查询与统计接口。

## 编绎&开发指南
---
* 参考[`jenkins-extend说明`](../README.md)

## 插件说明
* 插件类图
![](assets/statistics-plugin.png)

1. 定义插件类StatisticsPlugin，该类继承InvisiblePlugin
2. 定义插件url为statistics，根访问路径为http://{JNEKINS_URL}/statistics

  ```java
  @Override
  @WebMethod(name = "url")
  @Exported(name = "url")
  public String getUrlName() {
    return "sample";
  }
  ```
* 接口说明：参考[`SamplePlugin`](../jenkins插件接口文档.docx)。

* 测试：参考[`SamplePluginTest`](sampleplugin/src/test/java/com/wingarden/cicd/jenkins/plugins/sampleplugin/SamplePluginTest.java),[`Jenkins单元测试`](https://wiki.jenkins.io/display/JENKINS/Unit+Test),[`Jenkins单元测试+Mock`](https://wiki.jenkins.io/display/JENKINS/Mocking+in+Unit+Tests)

* 分布式插件开发参考
  * https://wiki.jenkins.io/display/JENKINS/Making+your+plugin+behave+in+distributed+Jenkins
  * http://www.lilihongblog.com/Blog/jenkins+Remoting+Architecture
  * http://jenkins-ci.361315.n4.nabble.com/Hudson-getInstance-returns-null-td3756423.html#a3763894;
  * https://wiki.jenkins.io/display/JENKINS/Splitting+a+big+job+into+smaller+jobs
  * https://barnashcode.blogspot.jp/2010/07/split-hudson-jobs.html
  * https://wiki.jenkins.io/display/JENKINS/Slave+To+Master+Access+Control/#SlaveToMasterAccessControl-I%27maplugindeveloper.WhatshouldIdo%3F
  * https://wiki.jenkins.io/display/JENKINS/Plugin+tutorial#Plugintutorial-DistributingaPlugin
