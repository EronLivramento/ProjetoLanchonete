package lanchonete.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class GenericDao {
    
    public ResultSet executeQuery(PreparedStatement pstm,Object... params) throws SQLException{
        for (int i = 0; i < params.length; i++) {
            pstm.setObject(i+1, params[i]);
        }
        return pstm.executeQuery();
    }
    public int executeCommand(PreparedStatement pstm,Object... params) throws SQLException{
        for (int i = 0; i < params.length; i++) {
           pstm.setObject(i+1, params[i]);
        }
        return pstm.executeUpdate();
    }
}
