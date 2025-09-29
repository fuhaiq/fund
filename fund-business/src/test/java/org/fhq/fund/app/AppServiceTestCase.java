package org.fhq.fund.app;

import lombok.extern.slf4j.Slf4j;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.ITable;
import org.fhq.fund.AbstractDbUnitTest;
import org.fhq.fund.service.IAppService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class AppServiceTestCase extends AbstractDbUnitTest {

    @Autowired
    private IAppService appService;

    @Test
    public void testAdjustPricesBelowBy30Percent() throws DatabaseUnitException {
        boolean success = appService.adjustPricesBelowBy30Percent(100);
        Assertions.assertTrue(success, "上调价格失败");
        ITable actual = getActualTable("app");
        ITable expect = getExpectTable("app");
        assertTables(actual, expect);
    }

    @Test
    public void testAdjustPricesBelowBy30PercentFailed() throws DatabaseUnitException {
        boolean success = appService.adjustPricesBelowBy30Percent(200);
        Assertions.assertTrue(success, "上调价格失败");
        ITable actual = getActualTable("app");
        ITable expect = getExpectTable("app");
        assertTables(actual, expect);
    }

    @Override
    protected String baseDir() {
        return "dataset/20250929";
    }
}
