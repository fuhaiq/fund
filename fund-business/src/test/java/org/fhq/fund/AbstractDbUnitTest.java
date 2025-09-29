package org.fhq.fund;

import lombok.extern.slf4j.Slf4j;
import org.dbunit.Assertion;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
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
public abstract class AbstractDbUnitTest {

    protected static final String[] defaultIgnoreCols = {
            "id", "create_time", "update_time", "create_by", "update_by", "version"
    };

    @Autowired
    private DataSource dataSource;

    private IDatabaseTester databaseTester;

    private final Map<String, ITable> expectTables = new HashMap<>();

    public AbstractDbUnitTest() {
        try {
            IDataSet dataset = loadDataSet("expect.xls");
            for (String tableName : dataset.getTableNames()) {
                expectTables.put(tableName, dataset.getTable(tableName));
            }
        } catch (IOException | DataSetException ex) {
            throw new RuntimeException("DBUnit init expect tables error", ex);
        }
    }

    @BeforeEach
    public void setUp() {
        try {
            databaseTester = new DataSourceDatabaseTester(dataSource);
            IDataSet dataset = loadDataSet("data.xls");
            databaseTester.setDataSet(dataset);
            databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
            databaseTester.onSetup();
        } catch (Exception ex) {
            throw new RuntimeException("DBUnit setUp error", ex);
        }
    }

    @AfterEach
    public void tearDown() {
        if (databaseTester != null) {
            try {
                databaseTester.onTearDown();
            } catch (Exception ex) {
                throw new RuntimeException("DBUnit tearDown error", ex);
            }
        }
    }

    private IDataSet loadDataSet(String fileName) throws IOException, DataSetException {
        String filePath = baseDir() + "/" + fileName;
        File dataFile = new ClassPathResource(filePath).getFile();
        return new XlsDataSet(dataFile);
    }

    protected ITable getActualTable(String name) {
        try {
            return databaseTester.getConnection().createDataSet().getTable(name);
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

    protected abstract String baseDir();
}