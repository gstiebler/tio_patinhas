/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testejavadesktop;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class CPainel extends JPanel {
    public Image img=null;
  
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (img!=null)
        {
            g.drawImage(img, 0, 0, this);
        }
    }
    
}
