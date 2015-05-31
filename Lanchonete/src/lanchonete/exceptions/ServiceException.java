package lanchonete.exceptions;

/**
 *
 * @author Livramento
 */
public class ServiceException extends Exception{
    
    public ServiceException(){
    }
            
    public ServiceException(String message){
        super(message);
    }
            
}
