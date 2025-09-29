package org.fhq.codegen;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.query.DefaultQuery;
import org.apache.ibatis.annotations.Mapper;
import org.postgresql.ds.PGSimpleDataSource;

import java.util.Map;

public class Main {
    public static void main(String[] args) {

        var ds = new PGSimpleDataSource();
        ds.setServerNames(new String[]{"postgres"});  // 数据库服务器地址
        ds.setPortNumbers(new int[]{5432});  // 数据库服务器端口
        ds.setUser("postgres");  // 数据库用户名
        ds.setPassword("postgrespassword");  // 数据库密码
        ds.setDatabaseName("postgres");  // 数据库名称
        ds.setCurrentSchema("public");

        var dir = "/Users/imac/codegen";
        var packageDir = "/org/fhq/fund/";
        var moduleName = "app";

        var dataSourceConfig = new DataSourceConfig.Builder(ds)
                .databaseQueryClass(DefaultQuery.class);

        FastAutoGenerator.create(dataSourceConfig)

                .globalConfig(builder -> builder.disableOpenDir() // 允许自动打开输出目录
                        .enableSpringdoc()
                        .outputDir(dir) // 设置输出目录
                        .author("fuhaiq@gmail.com") // 设置作者名
                        .enableSwagger() // 开启 Swagger 模式
                        .dateType(DateType.TIME_PACK) // 设置时间类型策略
                        .commentDate("yyyy-MM-dd")) // 设置注释日期格式)

                .packageConfig(builder -> builder.parent("org.fhq.fund") // 设置父包名
//                        .moduleName(moduleName) // 设置父包模块名
                        .entity("entity") // 设置 Entity 包名
                        .service("service") // 设置 Service 包名
                        .serviceImpl("service.impl") // 设置 Service Impl 包名
                        .mapper("mapper") // 设置 Mapper 包名
                        .xml("mapper.xml") // 设置 Mapper XML 包名
                        .controller("controller") // 设置 Controller 包名
                        .pathInfo(Map.of(
                                        OutputFile.entity, dir + packageDir + "/entity",
                                        OutputFile.mapper, dir + packageDir + "/mapper",
                                        OutputFile.service, dir + packageDir + "/service",
                                        OutputFile.serviceImpl, dir + packageDir + "/service/impl",
                                        OutputFile.controller, dir + packageDir + "/controller",
                                        OutputFile.xml, dir + packageDir + "/mapper"
                                )
                        ))

                .strategyConfig(builder -> builder.enableCapitalMode() // 开启大写命名
                        .enableSkipView() // 开启跳过视图
                        .addInclude(moduleName) // 增加表匹配

                        // entity 策略配置
                        .entityBuilder()
                        .idType(IdType.ASSIGN_ID) // 雪花ID
                        .versionColumnName("version") // 乐观锁字段
                        .enableLombok() // 启用 Lombok
                        .enableTableFieldAnnotation() // 启用字段注解
                        .enableActiveRecord() // 启用 ActiveRecord 模式
                        .addTableFills(
                                new Column("create_time", FieldFill.INSERT),
                                new Column("update_time", FieldFill.INSERT_UPDATE),
                                new Column("create_by", FieldFill.INSERT),
                                new Column("update_by", FieldFill.INSERT_UPDATE),
                                new Column("version", FieldFill.INSERT)
                        )

                        .mapperBuilder()
                        .mapperAnnotation(Mapper.class)

                        // controller 策略配置
                        .controllerBuilder()
                        .enableRestStyle())

                .execute();
    }
}
