package lanchonete.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lanchonete.entity.Product;
import lanchonete.entity.Sell;
import lanchonete.entity.SellItem;
import lanchonete.entity.TypeProduct;
import lanchonete.entity.User;
import lanchonete.exceptions.PersistenceException;
import lanchonete.util.JDBCConection;

/**
 *
 * @author Livramento
 */
public class SellDao extends GenericDao {

    private static final String INSERT_SELL = "INSERT INTO SELL(ID_VENDER,TOTAL,DATE_OF_SALE,DELIVERY_FEE)VALUES(?,?,?,?)";
    private static final String UPDATE_SELL = "UPDATE SELL SET ID_VENDER= ?, TOTAL = ?, DELIVERY_FEE WHERE ID_SELL = ?";
    private static final String DELETE_SELL = "DELETE FROM SELL WHERE ID_SELL = ?";
    private static final String FIND_SELL_BY_ID = "SELECT * FROM SELL WHERE ID=?";
    private static final String FIND_SELL_ALL = "SELECT S.*,SI.*,V.NAME,P.NAME as NAME_PRODUCT, P.PRICE FROM SELL S,SELL_ITEM SI, USER V, PRODUCT P WHERE S.ID_SELL = SI.ID_SELL AND S.ID_VENDER = V.ID AND SI.ID_PRODUCT = P.ID";
    private final SellItemDao sellItemDao = new SellItemDao();

    public void save(Sell sell) throws PersistenceException {
        if (sell.getId() == null) {
            insertSell(sell);
        } else {
            updateSell(sell);
        }
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
            executeCommand(pstm, sell.getVender().getId(), totalOfSale, sell.getDateOfSale(), sell.getDeliveryFee());
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

    public void updateSell(Sell sell) throws PersistenceException {
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try {
            double totalOfSale = 0;
            for (SellItem item : sell.getItens()) {
                totalOfSale += item.getProduct().getPrice() * item.getQnt();
            }
            pstm = conn.prepareStatement(UPDATE_SELL);
            executeCommand(pstm, sell.getVender().getId(), totalOfSale, sell.getDeliveryFee(), sell.getId());
            for (SellItem item : sell.getItens()) {
                sellItemDao.updateSellItem(item);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao atualizar essa venda");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }

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

    private Sell populateSellInfo(ResultSet rs) throws SQLException, PersistenceException {
        Sell toReturn = new Sell();
        User vender = new User();
        List<SellItem> sellItem = new LinkedList<>();
        vender.setName(rs.getString("NAME"));
        toReturn.setId(rs.getInt("ID_SELL"));
        toReturn.setVender(vender);
        toReturn.setDateOfSale(rs.getDate("DATE_OF_SALE"));
        toReturn.setTotal(rs.getDouble("TOTAL"));
        toReturn.setDeliveryFee(rs.getDouble("DELIVERY_FEE"));
        while (rs.next()) {
            sellItem.add(populateSellItemInfo(rs, toReturn));
        }
        toReturn.setItens(sellItem);
        return toReturn;
    }

    public SellItem populateSellItemInfo(ResultSet rs, Sell sell) throws SQLException, PersistenceException {
        SellItem toReturn = new SellItem();
        Product product = new Product();
        product.setName(rs.getString("NAME_PRODUCT"));
        product.setPrice(rs.getDouble("PRICE"));
        toReturn.setId(rs.getInt("ID_SELL_ITEM"));
        toReturn.setQnt(rs.getInt("QNT"));
        toReturn.setProduct(product);
        toReturn.setSell(sell);
        return toReturn;
    }
}
