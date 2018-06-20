package creator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class ALMediaChoosePath implements ActionListener{
	
	
	JTextField tf;
	public ALMediaChoosePath(JTextField tf) {
		this.tf = tf;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		JFileChooser filechooser = new JFileChooser();
		filechooser.setDialogTitle("Choose Xml File!");
		filechooser.showOpenDialog(null);
		String filepath = filechooser.getSelectedFile().getPath();
		System.out.println(filepath);
		this.tf.setText(filepath);
	}

}
