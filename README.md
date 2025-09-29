# 简介

基于`spring-boot`+`mybatis-plus`+`postgres`+`keycloak`搭建的Web后端项目

本项目使用[DBUnit](https://www.dbunit.org/)进行UT测试

一共三个模块

1. fund-common：公共类，如安全策略、通用数据验证、插件
2. fund-codegen：代码、数据生成
   - codegen：基于mybatis-plus的CRUD代码生成
   - datafaker：生成测试数据，后面结合DBUnit使用
3. fund-business：业务模块，传统的MVC三层结构