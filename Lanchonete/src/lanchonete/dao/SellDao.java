package lanchonete.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lanchonete.entity.Adress;
import lanchonete.entity.Client;
import lanchonete.entity.Product;
import lanchonete.entity.Sell;
import lanchonete.entity.SellItem;
import lanchonete.entity.User;
import lanchonete.exceptions.PersistenceException;
import lanchonete.util.JDBCConection;

/**
 *
 * @author Livramento
 */
public class SellDao extends GenericDao {

    private static final String INSERT_SELL = "INSERT INTO SELL(ID_VENDER,ID_CLIENT,TOTAL,DATE_OF_SALE)VALUES(?,?,?,?)";
    private static final String UPDATE_SELL = "UPDATE SELL SET ID_VENDER= ?,ID_CLIENT = ?, TOTAL = ? WHERE ID_SELL = ?";
    private static final String DELETE_SELL = "DELETE FROM SELL WHERE ID_SELL = ?";
    private static final String FIND_SELL_BY_ID = "SELECT * FROM SELL WHERE ID=?";
    private static final String FIND_SELL_ALL = "SELECT S.*,C.*,A.*,V.NAME as name_vender  " +
                                                "FROM SELL S, SELL_ITEM SI, USER V, ADRESS A, CLIENT C,PRODUCT P " +
                                                "WHERE S.ID_VENDER = V.ID " +
                                                "AND S.ID_CLIENT = C.ID " +
                                                "AND C.ID_ADRESS = A.ID " +
                                                "GROUP BY S.ID_SELL";
    private static final SellItemDao sellItemDao = new SellItemDao();

    public void save(Sell sell) throws PersistenceException {
            insertSell(sell);
    }

    public void insertSell(Sell sell) throws PersistenceException {
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            double totalOfSale = 0;
            Integer toReturn = null;
            for (SellItem item : sell.getItens()) {
                totalOfSale += item.getProduct().getPrice() * item.getQnt();
            }
            pstm = conn.prepareStatement(INSERT_SELL, PreparedStatement.RETURN_GENERATED_KEYS);
            executeCommand(pstm, sell.getVender().getId(),sell.getClient().getId(), totalOfSale, sell.getDateOfSale());
            rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                toReturn = rs.getInt(1);
            }
            sell.setId(toReturn);
            for (SellItem item : sell.getItens()) {
                sellItemDao.insertSellItem(item);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao salvar essa venda");
        } finally {
            JDBCConection.close(conn, pstm, rs);
        }
    }
/*
    public void updateSell(Sell sell) throws PersistenceException {
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try {
            double totalOfSale = 0;
            for (SellItem item : sell.getItens()) {
                totalOfSale += item.getProduct().getPrice() * item.getQnt();
            }
            pstm = conn.prepareStatement(UPDATE_SELL);
            executeCommand(pstm, sell.getVender().getId(), totalOfSale, sell.getId());
            for (SellItem item : sell.getItens()) {
                sellItemDao.updateSellItem(item);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao atualizar essa venda");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }*/

    public void removeSell(Integer idSell) throws PersistenceException {
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(DELETE_SELL);
            sellItemDao.removeSellItem(idSell);
            executeCommand(pstm, idSell);
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao remover essa venda");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }

    public Sell sellFindById(Integer id) throws PersistenceException {
        Sell sell = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_SELL_BY_ID);
            rs = executeQuery(pstm, id);
            if (rs.next()) {
                sell = populateSellInfo(rs);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao procurar essa venda");
        } finally {
            JDBCConection.close(conn, pstm, rs);
        }
        return sell;
    }

    public List<Sell> findAll() throws PersistenceException {
        List<Sell> list = new LinkedList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_SELL_ALL);
            rs = executeQuery(pstm);
            while (rs.next()) {
                list.add(populateSellInfo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException("Erro ao listar as venda");
        } finally {
            JDBCConection.close(conn, pstm, rs);
        }
        return list;
    }

    public static Sell populateSellInfo(ResultSet rs) throws SQLException, PersistenceException {
        Sell toReturn = new Sell();
        User vender = new User();
        vender.setName(rs.getString("NAME_VENDER"));
        toReturn.setClient(ClientDao.populateClientInfo(rs));
        toReturn.setId(rs.getInt("ID_SELL"));
        toReturn.setVender(vender);
        toReturn.setDateOfSale(rs.getDate("DATE_OF_SALE"));
        toReturn.setTotal(rs.getDouble("TOTAL"));
        toReturn.setItens(sellItemDao.findSellItens(toReturn));
        return toReturn;
    }    
}
