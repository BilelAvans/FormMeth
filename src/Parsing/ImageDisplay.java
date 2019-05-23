package Parsing;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class ImageDisplay {
	
	public ImageDisplay(String name) {
	    SwingUtilities.invokeLater(new Runnable()
	    {
		  public void run()
	      {
		        JFrame editorFrame = new JFrame(name);
		        editorFrame.setVisible(true);
		        editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		        
		        BufferedImage image = null;
		        try
		        {
		          image = ImageIO.read(new File(name));
		        }
		        catch (IOException e)
		        {
		          e.printStackTrace();
		        }
		        
		        ImageIcon imageIcon = new ImageIcon(image);
		        JLabel jLabel = new JLabel();
		        jLabel.setIcon(imageIcon);
		        editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);
		
		        editorFrame.pack();
		        editorFrame.setLocationRelativeTo(null);
		        editorFrame.setVisible(true);
	      }
	    });
	}
}
