package org.fhq.fund.app;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.ITable;
import org.fhq.fund.AsbtractDBTestCase;
import org.fhq.fund.service.IAppService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AppDBTestCase extends AsbtractDBTestCase {
    @Autowired
    private IAppService appService;

    public AppDBTestCase() {
        super("App table DB unit test", "dataset/20250929");
    }

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
}
