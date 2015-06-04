/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchonete.entity;

/**
 *
 * @author Livramento
 */
public enum TypeRequest {
    
    DELIVERY("Entrega"),TABLE("Mesa");
    
    private final String description;
    
    TypeRequest(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
}
