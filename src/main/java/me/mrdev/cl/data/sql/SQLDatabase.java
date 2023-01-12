package me.mrdev.cl.data.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLDatabase {

    private String host; //that's all that's needed for sqlite
    private int port;
    private String dbname;
    private String user;
    private String password;
    private SQLType type;

    private Connection connection;

    public SQLDatabase(String host, int port, String dbname, String user, String password, SQLType type) {
        this.host = host;
        this.port = port;
        this.dbname = dbname;
        this.user = user;
        this.password = password;
        this.type = type;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if(type == SQLType.MYSQL) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname + "?user=" + user + "&password=" + password);
        }else {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + host);
        }
    }

    public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        if(isConnected()) {
            return connection.prepareStatement(sql).executeQuery();
        }
        return null;
    }

    public void executeUpdate(String sql) throws SQLException {
        if(isConnected()) {
            connection.prepareStatement(sql).executeUpdate();
        }
    }

    public boolean TableExists(String name) throws SQLException {
        ResultSet set = connection.getMetaData().getTables(null , null , name , null);
        return set.next();
    }

    public void addRow(String table , String SQLparms) throws SQLException {
        executeUpdate("INSERT INTO " + table + " VALUES" + "(" + SQLparms + ")");
    }

    public void deleteTable(String name) throws SQLException {
        executeUpdate("DROP TABLE " + name);
    }

    public void createTable(String name , String SQLparams) {
        try {
            executeUpdate("CREATE TABLE IF NOT EXISTS " + name + "(" + SQLparams + ")");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


}
