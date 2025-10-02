package org.fhq.fund;

import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

@Slf4j
public abstract class AsbtractDBTestCase extends DBTestCase {

    private final String dir;

    private final Map<String, ITable> expectTables = new HashMap<>();

    protected static final String[] defaultIgnoreCols = {
            "id", "create_time", "update_time", "create_by", "update_by", "version"
    };

    @Autowired
    private DataSource dataSource;

    @SneakyThrows
    public AsbtractDBTestCase(String name, String dir) {
        super(name);
        this.dir = dir;
        IDataSet dataset = loadDataSet("expect.xls");
        for (String tableName : dataset.getTableNames()) {
            expectTables.put(tableName, dataset.getTable(tableName));
        }
    }

    @Override
    protected void setUpDatabaseConfig(DatabaseConfig config) {
        config.setProperty(DatabaseConfig.PROPERTY_TABLE_TYPE, new String[]{"BASE TABLE", "TABLE"});
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return loadDataSet("actual.xls");
    }

    @Override
    protected IDatabaseTester getDatabaseTester() {
        return new DataSourceDatabaseTester(dataSource);
    }

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected DatabaseOperation getTearDownOperation() {
        return DatabaseOperation.DELETE_ALL;
    }

    @AfterEach
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SneakyThrows
    private IDataSet loadDataSet(String fileName) {
        String filePath = dir + "/" + fileName;
        File dataFile = new ClassPathResource(filePath).getFile();
        return new XlsDataSet(dataFile);
    }

    @SneakyThrows
    protected ITable getActualTable(String name) {
        checkNotNull(name, "Table name is null or empty");
        return getDatabaseTester().getConnection().createDataSet().getTable(name);
    }

    @SneakyThrows
    protected ITable getActualTable(String name, @NotNull String sql) {
        checkNotNull(name, "Table name is null or empty");
        checkState(Strings.isNotBlank(sql), "Sql is null or empty");
        return getDatabaseTester().getConnection().createQueryTable(name, sql);
    }

    protected ITable getExpectTable(String name) {
        return expectTables.get(name);
    }

    @SneakyThrows
    protected void assertTables(ITable actual, ITable expect) {
        final ITable expectedFiltered = DefaultColumnFilter
                .excludedColumnsTable(expect, defaultIgnoreCols);
        final ITable actualFiltered = DefaultColumnFilter
                .excludedColumnsTable(actual, defaultIgnoreCols);
        Assertion.assertEquals(
                new SortedTable(expectedFiltered),
                new SortedTable(actualFiltered, expectedFiltered.getTableMetaData())
        );
    }
}
