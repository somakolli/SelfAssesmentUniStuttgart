package creator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import domain.Question;

public class Tabelle {

	public JTable table;
	
	//Saves Table contents in Questions as Java Objects
	public ArrayList<Question> Questions = new ArrayList<Question>();

	public Tabelle() {

		// create JFrame and JTable
		JFrame frame = new JFrame("Questions");
		frame.setSize(900, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		JTable table = new JTable();

		// create a table model and set a Column Identifiers to this model
		Object[] columns = { "Number", "Question", "Answers", "Points", "Time", "Mediapath" };
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);

		// set the model to the table
		table.setModel(model);

		// Change A JTable Background Color, Font Size, Font Color, Row Height
		table.setBackground(Color.LIGHT_GRAY);
		table.setForeground(Color.black);
		Font font = new Font("", 1, 22);
		table.setFont(font);
		table.setRowHeight(30);

		// Labels
		JLabel QuestionLabel = new JLabel();
		QuestionLabel.setText("Question:");
		QuestionLabel.setBounds(20, 200, 200, 80);
		frame.add(QuestionLabel);

		// JLabel TimeLabel = new JLabel();
		// TimeLabel.setText("Time:");
		// TimeLabel.setBounds(220,250,200,80);
		// frame.add(TimeLabel);

		JLabel AnswerLabel = new JLabel();
		AnswerLabel.setText("Answer:");
		AnswerLabel.setBounds(320, 200, 200, 80);
		frame.add(AnswerLabel);

		JLabel PointsLabel = new JLabel();
		PointsLabel.setText("Points&Time:");
		PointsLabel.setBounds(20, 250, 200, 80);
		frame.add(PointsLabel);

		JLabel MediaPathLabel = new JLabel();
		MediaPathLabel.setText("MediaPath:");
		MediaPathLabel.setBounds(320, 250, 200, 80);
		frame.add(MediaPathLabel);

		// Fields
		JTextField QuestionField = new JTextField("Is this a Question?");
		QuestionField.setBounds(100, 220, 200, 40);
		frame.add(QuestionField);

		JTextField TimeField = new JTextField("10");
		TimeField.setBounds(210, 270, 90, 40);
		frame.add(TimeField);

		JTextField AnswerField = new JTextField("This is an Answer!");
		AnswerField.setBounds(400, 220, 200, 40);
		frame.add(AnswerField);

		JTextField PointsField = new JTextField("10");
		PointsField.setBounds(100, 270, 90, 40);
		frame.add(PointsField);

		JTextField MediaPathField = new JTextField("C:/Users/...");
		MediaPathField.setBounds(400, 270, 200, 40);
		MediaPathField.addActionListener(new ALMediaChoosePath(MediaPathField));
		frame.add(MediaPathField);

		// create JButtons
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(750, 220, 100, 25);
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(750, 265, 100, 25);
		JButton btnImportXML = new JButton("Import XML");
		btnImportXML.setBounds(750, 310, 100, 25);
		JButton btnExportXML = new JButton("Export XML");
		btnExportXML.setBounds(750, 355, 100, 25);
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(750, 400, 100, 25);

		// create JScrollPane
		JScrollPane pane = new JScrollPane(table);
		pane.setBounds(0, 0, 880, 200);

		frame.add(pane);

		// add JButtons to the jframe
		frame.add(btnAdd);
		frame.add(btnDelete);
		frame.add(btnExportXML);
		frame.add(btnImportXML);
//		frame.add(btnUpdate);

		// create an array of objects to set the row data
		Object[] row = new Object[6];

		// button add row
		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// add Question to the List
				Question newq = new Question();
				newq.setQuestion(QuestionField.getText());
				newq.setPoints(Integer.parseInt(PointsField.getText()));
				newq.setTime(Integer.parseInt(TimeField.getText()));

				Questions.add(newq);

				// write into rows
				row[0] = Questions.size();
				row[1] = QuestionField.getText();
				row[2] = AnswerField.getText();
				row[3] = PointsField.getText();
				row[4] = TimeField.getText();
				row[5] = MediaPathField.getText();

				// add row to the model
				model.addRow(row);
			}
		});

		// button remove row & delete question
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// i = the index of the selected row
				int i = table.getSelectedRow();

				Questions.remove(i);

				if (i >= 0) {
					// remove a row from jtable
					model.removeRow(i);
				} else {
					System.out.println("Delete Error");
				}
				
				//Update Index for Table
				for (int j = 0; j < Questions.size(); j++) {
					model.setValueAt(j + 1, j, 0);
				}
				
			}
		});

		// // get selected row data From table to textfields
		// table.addMouseListener(new MouseAdapter(){
		//
		// @Override
		// public void mouseClicked(MouseEvent e){
		//
		// // i = the index of the selected row
		// int i = table.getSelectedRow();
		//
		// textId.setText(model.getValueAt(i, 0).toString());
		// textFname.setText(model.getValueAt(i, 1).toString());
		// textLname.setText(model.getValueAt(i, 2).toString());
		// textAge.setText(model.getValueAt(i, 3).toString());
		// }
		// });

		// button update row
		// btnUpdate.addActionListener(new ActionListener(){
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		//
		// // i = the index of the selected row
		// int i = table.getSelectedRow();
		//
		// if(i >= 0)
		// {
		// model.setValueAt(textId.getText(), i, 0);
		// model.setValueAt(textFname.getText(), i, 1);
		// model.setValueAt(textLname.getText(), i, 2);
		// model.setValueAt(textAge.getText(), i, 3);
		// }
		// else{
		// System.out.println("Update Error");
		// }
		// }
		// });

		frame.setVisible(true);

	}

	public static void main(String[] args) {

		Tabelle gui = new Tabelle();
			System.out.println(gui.Questions);
	}

}