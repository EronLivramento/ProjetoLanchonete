package lanchonete.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lanchonete.entity.Adress;
import lanchonete.exceptions.PersistenceException;
import lanchonete.util.JDBCConection;

public class AdressDao extends GenericDao {

    private static final String INSERT = "INSERT INTO ADRESS(STREET,NUMBER,CITY,DISTRICT)VALUES(?,?,?,?)";
    private static final String UPDATE = "UPDATE ADRESS SET STREET= ?, NUMBER= ?, CITY= ?, DISTRICT= ? WHERE ID = ?";
    private static final String REMOVE = "DELETE FROM ADRESS WHERE ID = ?";
    private static final String FIND_BY_ID = "SELECT * FROM ADRESS WHERE ID = ?";

    public Integer insert(Adress adress) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        Integer toReturn = null;
        ResultSet rs = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            executeCommand(pstm, adress.getStreet(), adress.getNumber(), adress.getCity(), adress.getDistrict());
            rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                toReturn = rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao inserir o endereço");
        } finally {
            JDBCConection.close(conn, pstm, rs);
        }
        return toReturn;
    }

    public void update(Adress adress) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(UPDATE);
            executeCommand(pstm, adress.getStreet(), adress.getNumber(), adress.getCity(),
                    adress.getDistrict(), adress.getId());
           
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao atualizar o endereço");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }

    public void remove(Integer id) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(REMOVE);
            executeCommand(pstm, id);
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao remover o endereço");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }

    public Adress findById(Integer id) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        Adress adress = null;
        ResultSet rs = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_BY_ID);
            rs = executeQuery(pstm, id);
            if(rs.next()){
                adress = populateAdressInfo(rs);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao buscar o endereço");
        } finally {
            JDBCConection.close(conn, pstm,rs);
        }
        return adress;
    }

    public static Adress populateAdressInfo(ResultSet rs) throws SQLException {
        Adress toReturn = new Adress();
        toReturn.setId(rs.getInt("ID"));
        toReturn.setStreet(rs.getString("STREET"));
        toReturn.setNumber(rs.getInt("NUMBER"));
        toReturn.setCity(rs.getString("CITY"));
        toReturn.setDistrict(rs.getString("DISTRICT"));
        return toReturn;
    }
}
