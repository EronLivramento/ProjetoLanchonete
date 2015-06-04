/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchonete.entity;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Livramento
 */
public class Request {
    
    private Integer id;
    private List<Product> product;
    private TypeRequest type;
    private Double total;
    private Date dateOfRequest;
}
