package lanchonete.util;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Livramento
 */
public class Message {
    
    public static void addMessage(Component componet,String text){
        JOptionPane.showMessageDialog(componet, text);
    }

    public static void addMessageError(Component componet,String text, String title, int message) {
        JOptionPane.showMessageDialog(componet, text, title, message);
    }

    public static int showConfirm(Component componet,String text, String title, int option, int message) {
       return JOptionPane.showConfirmDialog(componet, text, title, option, message);
    }
    
}
