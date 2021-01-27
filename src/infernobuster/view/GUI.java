package infernobuster.view;

import javax.swing.JFrame;

import infernobuster.controller.Controller;
import infernobuster.model.Model; 

public class GUI {
	public static void main(String[] args) {
		Model model = new Model();
		ControlPane view = new ControlPane(model);
		Controller controller = new Controller(view, model);
		
		view.setController(controller);
		
		JFrame frame = new JFrame();  
		
	    frame.setTitle("Infernobuster"); 
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().add(view);
	    frame.pack();
	    frame.setResizable(true);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
}
