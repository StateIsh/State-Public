package com.thestatemc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class Database {
    public static String HOST;
    public static String PORT;
    public static String DATABASE;
    public static String USERNAME;
    public static String PASSWORD;

    private static Connection connect = null;

    public static IAsyncHandler asyncHandler = Runnable::run;

    private static long lastValidCheck = 0;
    private static final long VALID_CHECK_PERIOD = 30000;

    public static Connection connect() throws SQLException {
        if (connect != null && !connect.isClosed()) {
            if (lastValidCheck < System.currentTimeMillis() - VALID_CHECK_PERIOD) {
                if (connect.isValid(5000)) {
                    lastValidCheck = System.currentTimeMillis();
                } else {
                    return openConnection();
                }
            }

            return connect;
        }

        return openConnection();
    }

    private static Connection openConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = String.format("jdbc:mysql://%s:%s/%s", HOST, PORT, DATABASE);
            connect = DriverManager.getConnection(url, USERNAME, PASSWORD);
            return connect();
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
    }

    public static PreparedStatement preparedStatement(String sql) {
        try {
            return connect().prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final Queue<StatementBuilder> pool = new LinkedList<>();

    public static StatementBuilder builder() {
        synchronized (pool) {
            StatementBuilder builder = pool.poll();
            if (builder != null) return builder;
        }
        return new StatementBuilder();
    }

    public static void appendStatementBuilder(StatementBuilder statementBuilder) {
        synchronized (pool) {
            pool.add(statementBuilder);
        }
    }
}
