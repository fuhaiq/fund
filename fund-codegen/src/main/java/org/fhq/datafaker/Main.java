package org.fhq.datafaker;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import net.datafaker.transformations.Schema;
import net.datafaker.transformations.sql.SqlDialect;
import net.datafaker.transformations.sql.SqlTransformer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import static net.datafaker.transformations.Field.field;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        Schema<String, Object> schema = getSchema();
        SqlTransformer<String> transformer =
                new SqlTransformer.SqlTransformerBuilder<String>()
                        .batch(1_000)
                        .tableName("app")
                        .dialect(SqlDialect.POSTGRES)
                        .build();
        String output = transformer.generate(schema, 1_000);

        Path filePath = Paths.get("/Users/imac/umbrella-java/fund/fund-codegen/src/main/resources/migration/app.sql");
        Files.writeString(filePath, output);
        log.info("文件已保存至: {}", filePath.toAbsolutePath());
    }

    private static Schema<String, Object> getSchema() {
        var idGenerator = DefaultIdentifierGenerator.getInstance(); // 雪花id

        Faker faker = new Faker();
        return Schema.of(
                field("id", () -> idGenerator.nextId(null)),
                field("name", () -> faker.app().name()),
                field("author", () -> faker.app().author()),
                field("price", () -> faker.number().numberBetween(1, 1000)),
                field("create_by", () -> "fuhaiqing"),
                field("update_by", () -> "fuhaiqing"),
                field("create_time", () -> Instant.now()),
                field("update_time", () -> Instant.now())
        );
    }
}
