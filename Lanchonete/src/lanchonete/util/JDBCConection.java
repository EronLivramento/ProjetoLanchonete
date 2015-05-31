package lanchonete.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JDBCConection {

    private static ResourceBundle config;
    private static JDBCConection connection;

    private JDBCConection() {
        config = ResourceBundle.getBundle("config");
    }

    public static JDBCConection getIntance() {
        if (connection == null) {
            connection = new JDBCConection();
        }
        return connection;
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(config.getString("BD.connection"),
                    config.getString("BD.user"), config.getString("BD.password"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public static void close(Connection conn, PreparedStatement pstm, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstm != null) {
                pstm.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public static void close(Connection conn, PreparedStatement pstm) {
        try {
            if (pstm != null) {
                pstm.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}
