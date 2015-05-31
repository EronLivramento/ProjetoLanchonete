package lanchonete.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lanchonete.entity.Sell;
import lanchonete.entity.SellItem;
import lanchonete.exceptions.PersistenceException;
import lanchonete.util.JDBCConection;

/**
 *
 * @author Livramento
 */
public class SellDao extends GenericDao{
    
    private static final String INSERT_SELL_ITEM = "INSERT INTO SELL_ITEM(ID_PRODUCT,ID_SELL,QNT)VALUES()";
    private static final String INSERT_SELL = "INSERT INTO SELL(ID_VENDER,TOTAL,DATE_OF_SALE,DELIVERY_FEE)VALUES(?,?,?)";
    private static final String UPDATE_SELL = "UPDATE SELL SET ID_VENDER= ?, TOTAL = ?, DELIVERY_FEE WHERE ID = ?";
    private static final String UPDATE_SELL_ITEM = "UPDATE SELL_ITEM SET ID_PRODUCT= ?, ID_SELL= ?, QNT= ? WHERE ID=?";
    private static final String DELETE_SELL = "DELETE FROM SELL WHERE ID = ?";
    private static final String DELETE_SELL_ITEM = "DELETE FROM SELL_ITEM WHERE ID = ?";
    private static final String FIND_SELL_BY_ID = "SELECT * FROM SELL WHERE ID=?";
    private static final String FIND_SELL_ITEM_BY_ID = "SELECT * FROM SELL_ITEM WHERE ID=?";
    private static final String FIND_SELL_ALL = "SELECT * FROM SELL";
    private static final String FIND_SELL_ITEM_ALL = "SELECT * FROM SELL_ITEM";
     
    public void insertSell(Sell sell) throws PersistenceException{
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try {
            double totalOfSale = 0;
            for (SellItem item : sell.getItens()) {
                totalOfSale += item.getProduct().getPrice() * item.getQnt();
            }
            pstm = conn.prepareStatement(INSERT_SELL);
            executeCommand(pstm,sell.getVender().getId(),totalOfSale,sell.getDateOfSale(),sell.getDeliveryFee());
            for (SellItem item : sell.getItens()) {
                insertSellItem(item);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao salvar essa venda");
        }finally{
            JDBCConection.close(conn, pstm);
        }
    }
    public void insertSellItem(SellItem sellItem) throws PersistenceException{
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(INSERT_SELL_ITEM);
            executeCommand(pstm,sellItem.getProduct().getId(),sellItem.getSell().getId(),sellItem.getQnt());
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao salvar esse item");
        }finally{
            JDBCConection.close(conn, pstm);
        }
    }
    public void updateSell(Sell sell) throws PersistenceException{
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try {
            double totalOfSale = 0;
            for (SellItem item : sell.getItens()) {
                totalOfSale += item.getProduct().getPrice() * item.getQnt();
            }
            pstm = conn.prepareStatement(UPDATE_SELL);
            executeCommand(pstm,sell.getVender().getId(),totalOfSale,sell.getDeliveryFee());
            for (SellItem item : sell.getItens()) {
                updateSellItem(item);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao atualizar essa venda");
        }finally{
            JDBCConection.close(conn, pstm);
        }
    }
    public void updateSellItem(SellItem sellItem) throws PersistenceException{
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(UPDATE_SELL_ITEM);
            executeCommand(pstm,sellItem.getProduct().getId(),sellItem.getSell().getId(),sellItem.getQnt());
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao atualizar esse item");
        }finally{
            JDBCConection.close(conn, pstm);
        }
    }
    public void removeSell(Integer idSell) throws PersistenceException{
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(DELETE_SELL);
            removeSellItem(idSell);
            executeCommand(pstm, idSell);
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao remover essa venda");
        }finally{
            JDBCConection.close(conn, pstm);
        }
    }
    public void removeSellItem(Integer id) throws PersistenceException{
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(DELETE_SELL_ITEM);
            executeCommand(pstm, id);
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao remover esse item");
        }finally{
            JDBCConection.close(conn, pstm);
        }
    }
    public Sell sellFindById(Integer id) throws PersistenceException{
        Sell sell = null;
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement(FIND_SELL_BY_ID);
            rs = executeQuery(pstm, id);
            if(rs.next()){
               sell = populateSellInfo(rs);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao procurar essa venda");
        }finally{
            JDBCConection.close(conn, pstm,rs);
        }
        return sell;
    }
    
    private Sell populateSellInfo(ResultSet rs) throws SQLException, PersistenceException{
        Sell toReturn = new Sell();
        UserDao userDao = new UserDao();
        toReturn.setId(rs.getInt("ID"));
        toReturn.setVender(userDao.findbyid(rs.getInt("ID")));
        toReturn.setDateOfSale(rs.getDate("DATE_OF_SALE"));
        toReturn.setTotal(rs.getDouble("TOTAL"));
        toReturn.setDeliveryFee(rs.getDouble("DELIVERY_FEE"));
        toReturn.setItens(findSellItens(toReturn));
        return toReturn;
    }
    public SellItem sellItemFindById(Integer id) throws PersistenceException{
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        SellItem sellItem = null;
        try {
            pstm = conn.prepareStatement(FIND_SELL_ITEM_BY_ID);
            rs = executeQuery(pstm, id);
            if(rs.next()){
                sellItem = populateSellItemInfo(rs);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao procurar esse item" );
        }finally{
            JDBCConection.close(conn, pstm,rs);
        }
        return sellItem;
    }

    public List<SellItem> findSellItens(Sell sell) throws PersistenceException {
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<SellItem> toReturn = new LinkedList<>();
        try {
            pstm = conn.prepareStatement(FIND_SELL_BY_ID);
            rs = executeQuery(pstm, sell.getId());
            while(rs.next()){
                toReturn.add(populateSellItemInfo(rs,sell));
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao procurar esse item");
        }finally{
            JDBCConection.close(conn, pstm,rs);
        }
        return toReturn;
    }

    public SellItem populateSellItemInfo(ResultSet rs, Sell... sell) throws SQLException, PersistenceException {
        SellItem toReturn = new SellItem();
        ProductDao productDao = new ProductDao();
        toReturn.setId(rs.getInt("ID"));
        toReturn.setQnt(rs.getInt("QNT"));
        toReturn.setProduct(productDao.findById(rs.getInt("ID")));
        toReturn.setSell(sellFindById(rs.getInt("ID_SELL")));
        return toReturn;
    }
}
