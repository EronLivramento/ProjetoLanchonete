package lanchonete.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lanchonete.entity.Product;
import lanchonete.entity.Sell;
import lanchonete.entity.SellItem;
import lanchonete.entity.TypeProduct;
import lanchonete.exceptions.PersistenceException;
import lanchonete.util.JDBCConection;

/*
    SELECT s.*, si.*, p.name FROM sell_item si
    left join sell s on s.id = si.id_sell
    left join product p on p.id = si.id_product
    */

public class SellItemDao extends GenericDao {

    private static final String INSERT_SELL_ITEM = "INSERT INTO SELL_ITEM(ID_PRODUCT,ID_SELL,QNT)VALUES(?,?,?)";
    private static final String UPDATE_SELL_ITEM = "UPDATE SELL_ITEM SET ID_PRODUCT= ?, ID_SELL= ?, QNT= ? WHERE ID=?";
    private static final String DELETE_SELL_ITEM = "DELETE FROM SELL_ITEM WHERE ID = ?";
    private static final String FIND_SELL_ITEM_BY_ID_SELL ="SELECT S.*,SI.*,P.NAME,P.PRICE,P.TYPE FROM SELL_ITEM SI"
                                                    + " LEFT JOIN SELL S ON S.ID = SI.ID_SELL"
                                                    + " LEFT JOIN PRODUCT P ON P.ID = SI.ID_PRODUCT";
    //private static final String FIND_SELL_ITEM_ALL = "SELECT * FROM SELL_ITEM";

    public void insertSellItem(SellItem sellItem) throws PersistenceException {
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(INSERT_SELL_ITEM);
            executeCommand(pstm, sellItem.getProduct().getId(), sellItem.getSell().getId(), sellItem.getQnt());
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao salvar esse item");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }

    public void updateSellItem(SellItem sellItem) throws PersistenceException {
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(UPDATE_SELL_ITEM);
            executeCommand(pstm, sellItem.getProduct().getId(), sellItem.getSell().getId(), sellItem.getQnt(), sellItem.getId());
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao atualizar esse item");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }

    public void removeSellItem(Integer id) throws PersistenceException {
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(DELETE_SELL_ITEM);
            executeCommand(pstm, id);
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao remover esse item");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }

    public List<SellItem> findSellItens(Sell sell) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<SellItem> toReturn = new LinkedList<>();
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_SELL_ITEM_BY_ID_SELL);
            rs = executeQuery(pstm, sell.getId());
            while (rs.next()) {
                toReturn.add(populateSellItemInfo(rs, sell));
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao procurar esse item");
        } finally {
            JDBCConection.close(conn, pstm, rs);
        }
        return toReturn;
    }

    public SellItem populateSellItemInfo(ResultSet rs, Sell sell) throws SQLException, PersistenceException {
        SellItem toReturn = new SellItem();
        Product product = new Product();
        product.setName(rs.getString("NAME"));
        product.setPrice(rs.getDouble("PRICE"));
        product.setType(TypeProduct.valueOf(rs.getString("TYPE")));
        toReturn.setId(rs.getInt("ID"));
        toReturn.setQnt(rs.getInt("QNT"));
        toReturn.setProduct(product);
       // toReturn.setSell(sellDao.sellFindById(rs.getInt("ID_SELL")));
        return toReturn;
    }
    /*public SellItem sellItemFindById(Integer id) throws PersistenceException{
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
     ex.printStackTrace();
     throw new PersistenceException("Erro ao procurar esse item" );
     }finally{
     JDBCConection.close(conn, pstm,rs);
     }
     return sellItem;
     }*/
}
