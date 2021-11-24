package com.thestatemc.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * A simple sql statement builder to make your code more readable
 * @author Ethan Bulmer 2019
 */
public class StatementBuilder implements AutoCloseable {
    private String lastAction = "";
    private StringBuilder builder = new StringBuilder();
    private int index = 1;
    private PreparedStatement preparedStatement = null;

    public StatementBuilder() {}

    public StatementBuilder selectFrom(String table, String... keys) {
        lastAction = "selectFrom";

        if (keys.length == 0) {
            keys = new String[] {"*"};
        }

        builder.append("select ");

        for (int i = 0; i < keys.length; i++) {
            builder.append(keys[i]);
            if (i + 1 < keys.length) {
                builder.append(", ");
            }
        }

        builder.append(" from ");
        builder.append(table);

        builder.append(" ");
        return this;
    }

    public StatementBuilder insertInto(String table, String... keys) {
        lastAction = "insertInto";

        builder.append("insert into ");
        builder.append(table);

        if (keys.length > 0) {
            builder.append("(");
            for (int i = 0; i < keys.length; i++) {
                builder.append(keys[i]);
                if (i + 1 < keys.length) {
                    builder.append(", ");
                }
            }
            builder.append(")");
        }

        builder.append(" ");
        return this;
    }

    public StatementBuilder deleteFrom(String table) {
        lastAction = "deleteFrom";

        builder.append("delete from ");
        builder.append(table);

        builder.append(" ");
        return this;
    }

    public StatementBuilder values(String... values) {
        if (lastAction.equals("values")) {
            builder.append(", ");
        } else {
            builder.append("values ");
        }

        builder.append("(");
        for (int i = 0; i < values.length; i++) {
            builder.append(values[i]);
            if (i + 1 < values.length) {
                builder.append(", ");
            }
        }
        builder.append(")");

        builder.append(" ");
        lastAction = "values";
        return this;
    }

    public StatementBuilder onDuplicateKey() {
        lastAction = "onDuplicateKey";

        builder.append("on duplicate key ");

        return this;
    }

    public StatementBuilder update(String key, String value) {
        if (lastAction.equals("update")) {
            builder.append(", ");
        } else {
            builder.append("update ");
        }

        builder.append(key);
        builder.append(" = ");
        builder.append(value);

        builder.append(" ");
        lastAction = "update";
        return this;
    }

    public StatementBuilder where(String claus) {
        if (lastAction.equals("where")) {
            builder.append("and ");
        } else {
            builder.append("where ");
        }

        builder.append(claus);

        builder.append(" ");
        lastAction = "where";
        return this;
    }

    public StatementBuilder or(String claus) {
        if (lastAction.equals("where")) {
            builder.append("or ");
        } else {
            builder.append("where ");
        }

        builder.append(claus);

        builder.append(" ");
        lastAction = "where";
        return this;
    }

    public StatementBuilder orderAscBy(String key) {
        lastAction = "orderAscBy";

        builder.append("order by ");
        builder.append(key);
        builder.append(" asc ");

        return this;
    }

    public StatementBuilder orderDescBy(String key) {
        lastAction = "orderDescBy";

        builder.append("order by ");
        builder.append(key);
        builder.append(" desc ");

        return this;
    }

    public StatementBuilder groupBy(String claus) {
        builder.append("group by ");

        builder.append(claus);

        builder.append(" ");
        lastAction = "groupBy";
        return this;
    }

    public boolean hasWhere() {
        return builder.indexOf("where") >= 0;
    }

    public void execAsync(Object... objects) {
        Database.asyncHandler.run(() -> {
            try {
                preparedStatement = Database.connect().prepareStatement(builder.toString());

                for (Object obj : objects) {
                    try {
                        if (obj instanceof UUID) obj = obj.toString();
                        preparedStatement.setObject(index++, obj);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                preparedStatement.execute();

                close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean execSync(Object... objects) {
        try {
            preparedStatement = Database.connect().prepareStatement(builder.toString());

            for (Object obj : objects) {
                try {
                    if (obj instanceof UUID) obj = obj.toString();
                    preparedStatement.setObject(index++, obj);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet execQuery(Object... objects) {
        try {
            preparedStatement = Database.connect().prepareStatement(builder.toString());

            for (Object obj : objects) {
                try {
                    if (obj instanceof UUID) obj = obj.toString();
                    preparedStatement.setObject(index++, obj);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int execUpdate(Object... objects) {
        try {
            preparedStatement = Database.connect().prepareStatement(builder.toString());

            for (Object obj : objects) {
                try {
                    if (obj instanceof UUID) obj = obj.toString();
                    preparedStatement.setObject(index++, obj);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public StatementBuilder reset() throws SQLException {
        lastAction = "";
        builder.setLength(0);
        index = 1;
        if (preparedStatement != null) {
            preparedStatement.close();
            preparedStatement = null;
        }
        return this;
    }

    @Override
    public void close() {
        boolean appendToPool = preparedStatement != null;
        try {
            reset();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (appendToPool) {
            Database.appendStatementBuilder(this);
        }
    }
}