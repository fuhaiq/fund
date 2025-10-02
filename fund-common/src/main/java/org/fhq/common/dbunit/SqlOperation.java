package org.fhq.common.dbunit;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.IBatchStatement;
import org.dbunit.database.statement.IStatementFactory;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.AbstractOperation;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class SqlOperation extends AbstractOperation {
    private final String sqlPath;

    @Override
    public void execute(IDatabaseConnection connection, IDataSet dataSet) throws DatabaseUnitException, SQLException {
        log.debug("[Schema Init]execute(connection={}, dataSet={}) - start", connection, dataSet);

        DatabaseConfig databaseConfig = connection.getConfig();
        IStatementFactory statementFactory = (IStatementFactory) databaseConfig.getProperty(DatabaseConfig.PROPERTY_STATEMENT_FACTORY);
        IBatchStatement statement = statementFactory.createBatchStatement(connection);
        try {
            String sql = Files.readString(Path.of(new ClassPathResource(sqlPath).getURI()));
            statement.addBatch(sql);
            statement.executeBatch();
        } catch (IOException e) {
            throw new DatabaseUnitException(e);
        } finally {
            statement.close();
        }
    }
}
