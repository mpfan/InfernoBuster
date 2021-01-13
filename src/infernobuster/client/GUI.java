package infernobuster.client;

import javax.swing.JFrame; 

public class GUI {
	public static void main(String[] args) {
		JFrame frame = new JFrame();  
		
	    frame.setTitle("Infernobuster"); 
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().add(new ControlPane());
	    frame.pack();
	    frame.setResizable(true);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
}
