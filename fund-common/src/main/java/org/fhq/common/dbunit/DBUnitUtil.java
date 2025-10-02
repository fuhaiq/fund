package org.fhq.common.dbunit;

import lombok.SneakyThrows;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkNotNull;

public class DBUnitUtil {

    @SneakyThrows
    public static void printPretty(ITable table) {
        checkNotNull(table, "Table is null");

        ITableMetaData metaData = table.getTableMetaData();
        Column[] columns = metaData.getColumns();
        int rowCount = table.getRowCount();

        // 1. 计算每列的最大宽度
        int[] maxWidths = calculateMaxWidths(table, columns, rowCount);

        // 2. 构造分隔线
        String lineSeparator = buildSeparator(maxWidths);

        // 3. 打印表格
        printTable(table, columns, rowCount, maxWidths, lineSeparator);
    }

    private static int[] calculateMaxWidths(ITable table, Column[] columns, int rowCount) throws DataSetException {
        int[] maxWidths = new int[columns.length];

        // 初始化列名宽度
        for (int i = 0; i < columns.length; i++) {
            maxWidths[i] = columns[i].getColumnName().length();
        }

        // 更新为数据中的最大宽度
        for (int rowNum = 0; rowNum < rowCount; rowNum++) {
            for (int colIdx = 0; colIdx < columns.length; colIdx++) {
                Object value = table.getValue(rowNum, columns[colIdx].getColumnName());
                String strValue = value != null ? value.toString() : "NULL";
                maxWidths[colIdx] = Math.max(maxWidths[colIdx], strValue.length());
            }
        }

        return maxWidths;
    }

    private static String buildSeparator(int[] maxWidths) {
        return Arrays.stream(maxWidths)
                .mapToObj(width -> repeat("-", width + 2))
                .collect(Collectors.joining("+", "+", "+"));
    }

    private static void printTable(ITable table, Column[] columns, int rowCount,
                                   int[] maxWidths, String lineSeparator) throws DataSetException {
        // 打印顶部分隔线
        System.out.println(lineSeparator);

        // 打印表头
        printRow(columns, maxWidths, col -> columns[col].getColumnName());

        // 打印表头下的分隔线
        System.out.println(lineSeparator);

        // 打印数据行
        for (int rowNum = 0; rowNum < rowCount; rowNum++) {
            final int currentRow = rowNum;
            printRow(columns, maxWidths, col -> {
                try {
                    Object value = table.getValue(currentRow, columns[col].getColumnName());
                    return value != null ? value.toString() : "NULL";
                } catch (DataSetException e) {
                    return "ERROR";
                }
            });
        }

        // 打印底部分隔线
        System.out.println(lineSeparator);

        // 打印总行数
        System.out.println("Total rows: " + rowCount);
    }

    private static void printRow(Column[] columns, int[] maxWidths, RowValueProvider valueProvider) {
        String row = IntStream.range(0, columns.length)
                .mapToObj(colIdx -> {
                    String value = valueProvider.getValue(colIdx);
                    return " " + padRight(value, maxWidths[colIdx]) + " ";
                })
                .collect(Collectors.joining("|", "|", "|"));
        System.out.println(row);
    }

    @FunctionalInterface
    private interface RowValueProvider {
        String getValue(int columnIndex);
    }

    /**
     * 将字符串右对齐，并用空格填充至指定宽度
     */
    private static String padRight(String s, int width) {
        if (s == null) s = "";
        return String.format("%-" + width + "s", s);
    }

    /**
     * 字符串重复方法
     */
    private static String repeat(String str, int n) {
        if (str == null || n <= 0) return "";
        return IntStream.range(0, n)
                .mapToObj(i -> str)
                .collect(Collectors.joining());
    }
}