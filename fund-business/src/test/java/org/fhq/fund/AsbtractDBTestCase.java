package org.fhq.fund;

import lombok.extern.slf4j.Slf4j;
import org.dbunit.*;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = FundApplication.class)
@Slf4j
public abstract class AsbtractDBTestCase extends DBTestCase {

    private final String dir;

    private final Map<String, ITable> expectTables = new HashMap<>();

    protected static final String[] defaultIgnoreCols = {
            "id", "create_time", "update_time", "create_by", "update_by", "version"
    };

    @Autowired
    private DataSource dataSource;

    public AsbtractDBTestCase(String name, String dir) {
        super(name);
        this.dir = dir;

        try {
            IDataSet dataset = loadDataSet("expect.xls");
            for (String tableName : dataset.getTableNames()) {
                expectTables.put(tableName, dataset.getTable(tableName));
            }
        } catch (IOException | DataSetException ex) {
            throw new RuntimeException("DBUnit init expect tables error", ex);
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

    private IDataSet loadDataSet(String fileName) throws IOException, DataSetException {
        String filePath = dir + "/" + fileName;
        File dataFile = new ClassPathResource(filePath).getFile();
        return new XlsDataSet(dataFile);
    }

    protected ITable getActualTable(String name) {
        try {
            return getDatabaseTester().getConnection().createDataSet().getTable(name);
        } catch (Exception ex) {
            throw new RuntimeException("DBUnit getActualTable error", ex);
        }
    }

    protected ITable getExpectTable(String name) {
        return expectTables.get(name);
    }

    protected void assertTables(ITable actual, ITable expect) throws DatabaseUnitException {
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
