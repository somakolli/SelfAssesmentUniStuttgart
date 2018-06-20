package creator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

public class ALImportXml implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		
		JFileChooser filechooser = new JFileChooser();
		filechooser.setDialogTitle("Choose Xml File!");
		filechooser.showOpenDialog(null);
		String filepath = filechooser.getSelectedFile().getPath();
		System.out.println(filepath);
		
	}

}
