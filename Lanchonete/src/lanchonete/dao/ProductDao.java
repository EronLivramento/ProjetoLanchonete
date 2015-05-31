package lanchonete.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lanchonete.entity.Product;
import lanchonete.entity.TypeProduct;
import lanchonete.exceptions.PersistenceException;
import lanchonete.util.JDBCConection;

/**
 *
 * @author Livramento
 */
public class ProductDao extends GenericDao {

    private static final String INSERT = "INSERT INTO PRODUCT(NAME,TYPE,PRICE,DESCRIPTION,STOCK)VALUES(?,?,?,?,?)";
    private static final String UPDATE = "UPDATE PRODUCT SET NAME = ?,PRICE = ?,DESCRIPTION =?, STOCK = ? WHERE ID=?";
    private static final String REMOVE = "DELETE FROM PRODUCT WHERE ID=?";
    private static final String FIND_ALL = "SELECT * FROM PRODUCT";
    private static final String FIND_BY_ID = "SELECT * FROM PRODUCT WHERE ID = ?";
    private static final String FIND_BY_NAME = "SELECT * FROM PRODUCT WHERE NAME = ?";

    public void save(Product product) throws PersistenceException {
        if (product.getId() == null) {
            insert(product);
        } else {
            update(product);
        }
    }

    private void insert(Product product) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(INSERT);
            executeCommand(pstm, product.getName(),product.getType().toString(), product.getPrice(), product.getDescription(),product.getStock());
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao inserir!");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }

    private void update(Product product) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(UPDATE);
            executeCommand(pstm, product.getName(), product.getPrice(), product.getDescription(),product.getStock(),product.getId());
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao atualizar!");
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
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao remover!");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }

    public Product findByName(String name) throws PersistenceException {
        Product product = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_BY_NAME);
            rs = executeQuery(pstm, name);
            if (rs.next()) {
                product = populateProductInfo(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar!");
        } finally {
            JDBCConection.close(conn, pstm, rs);
        }
        return product;
    }

    public Product findById(Integer id) throws PersistenceException {
        Product product = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_BY_ID);
            rs = executeQuery(pstm, id);
            if (rs.next()) {
                product = populateProductInfo(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar!");
        } finally {
            JDBCConection.close(conn, pstm, rs);
        }
        return product;
    }

    public List<Product> findAll() throws PersistenceException {
        List<Product> list = new LinkedList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_ALL);
            rs = executeQuery(pstm);
            while (rs.next()) {
                list.add(populateProductInfo(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar!");
        } finally {
            JDBCConection.close(conn, pstm, rs);
        }
        return list;
    }

    public static Product populateProductInfo(ResultSet rs) throws SQLException {
        Product toReturn = new Product();
        toReturn.setId(rs.getInt("ID"));
        toReturn.setName(rs.getString("NAME"));
        toReturn.setPrice(rs.getDouble("PRICE"));
        toReturn.setDescription(rs.getString("DESCRIPTION"));
        toReturn.setType(TypeProduct.valueOf(rs.getString("TYPE")));
        toReturn.setStock(rs.getInt("STOCK"));
        return toReturn;
    }
}
