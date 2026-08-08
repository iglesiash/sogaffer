package es.unican.hgi834.sogaffer.utils;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public final class TestDataSourceUtils {

    private TestDataSourceUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isClosed(DataSource dataSource) {
        return dataSource instanceof HikariDataSource hikari && hikari.isClosed();
    }
}