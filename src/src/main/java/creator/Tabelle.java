package creator;

import java.awt.Color;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import domain.Answer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import domain.Question;

public class Tabelle {

	public JTable table;

	// Saves Table contents in Questions as Java Objects
	public ArrayList<Question> Questions = new ArrayList<Question>();
	public ArrayList<Answer> Answers = new ArrayList<Answer>();

	public Tabelle() {

		// create JFrame and JTable
		JFrame frame = new JFrame("Questions");
		frame.setSize(900, 450);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.DARK_GRAY);
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
		Font font = new Font("", 1, 12);
		table.setFont(font);
		table.setRowHeight(20);

		// Labels
		JLabel QuestionLabel = new JLabel();
		QuestionLabel.setText("Question:");
		QuestionLabel.setBounds(20, 200, 200, 80);
		QuestionLabel.setForeground(Color.WHITE);
		frame.add(QuestionLabel);

		// JLabel TimeLabel = new JLabel();
		// TimeLabel.setText("Time:");
		// TimeLabel.setBounds(220,250,200,80);
		// frame.add(TimeLabel);

		JLabel AnswerLabel = new JLabel();
		AnswerLabel.setText("Answer:");
		AnswerLabel.setBounds(320, 200, 200, 80);
		AnswerLabel.setForeground(Color.WHITE);
		frame.add(AnswerLabel);

		JLabel PointsLabel = new JLabel();
		PointsLabel.setText("Points&Time:");
		PointsLabel.setBounds(20, 250, 200, 80);
		PointsLabel.setForeground(Color.WHITE);
		frame.add(PointsLabel);

		JLabel MediaPathLabel = new JLabel();
		MediaPathLabel.setText("MediaPath:");
		MediaPathLabel.setBounds(320, 250, 200, 80);
		MediaPathLabel.setForeground(Color.WHITE);
		frame.add(MediaPathLabel);

		// Fields
		JTextField QuestionField = new JTextField("Is this a Question?");
		QuestionField.setBounds(100, 220, 200, 40);
		frame.add(QuestionField);

		JTextField TimeField = new JTextField("10");
		TimeField.setBounds(210, 270, 90, 40);
		frame.add(TimeField);

		// JTextField AnswerField = new JTextField("This is an Answer!");
		// AnswerField.setBounds(400, 220, 200, 40);
		// frame.add(AnswerField);

		JTextField PointsField = new JTextField("10");
		PointsField.setBounds(100, 270, 90, 40);
		frame.add(PointsField);

		// JTextField MediaPathField = new JTextField("C:/Users/...");
		// MediaPathField.setBounds(400, 270, 200, 40);
		// MediaPathField.addActionListener(new ALMediaChoosePath(MediaPathField));
		// frame.add(MediaPathField);

		// create JButtons
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(750, 220, 100, 25);
		btnAdd.setBackground(Color.GRAY);
		btnAdd.setForeground(Color.WHITE);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(750, 265, 100, 25);
		btnDelete.setBackground(Color.GRAY);
		btnDelete.setForeground(Color.WHITE);
		
		JButton btnImportXML = new JButton("Import XML");
		btnImportXML.setBounds(750, 310, 100, 25);
		btnImportXML.setBackground(Color.GRAY);
		btnImportXML.setForeground(Color.WHITE);
		
		JButton btnExportXML = new JButton("Export XML");
		btnExportXML.setBounds(750, 355, 100, 25);
		btnExportXML.setBackground(Color.GRAY);
		btnExportXML.setForeground(Color.WHITE);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(750, 400, 100, 25);
		btnUpdate.setBackground(Color.GRAY);
		btnUpdate.setForeground(Color.WHITE);
		
		JButton btnAddAnswers = new JButton("Add Answers");
		btnAddAnswers.setBounds(400, 220, 200, 40);
		btnAddAnswers.setBackground(Color.GRAY);
		btnAddAnswers.setForeground(Color.WHITE);
		
		JButton btnAddMediaPaths = new JButton("Add Media Paths");
		btnAddMediaPaths.setBounds(400, 270, 200, 40);
		btnAddMediaPaths.setBackground(Color.GRAY);
		btnAddMediaPaths.setForeground(Color.WHITE);

		// create JScrollPane
		JScrollPane pane = new JScrollPane(table);
		pane.setBounds(5, 5, 870, 200);

		frame.add(pane);

		// add JButtons to the jframe
		frame.add(btnAdd);
		frame.add(btnDelete);
		frame.add(btnExportXML);
		frame.add(btnImportXML);
		frame.add(btnAddAnswers);
		frame.add(btnAddMediaPaths);

		// create an array of objects to set the row data
		Object[] row = new Object[6];

		// button add row
		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// add Question to the List
				Question newq = new Question();
				newq.setAnswers(Answers);
				newq.setQuestion(QuestionField.getText());
				newq.setPoints(Integer.parseInt(PointsField.getText()));
				newq.setTime(Integer.parseInt(TimeField.getText()));
				

				Questions.add(newq);

				// write into rows
				row[0] = Questions.size();
				row[1] = QuestionField.getText();
				row[2] = Answers.size();
				row[3] = PointsField.getText();
				row[4] = TimeField.getText();
				// row[5] = MediaPathField.getText();

				// add row to the model
				Answers.removeAll(Answers);
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

				// Update Index for Table
				for (int j = 0; j < Questions.size(); j++) {
					model.setValueAt(j + 1, j, 0);
				}

			}
		});

		// button xml Import
		btnImportXML.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser F = new JFileChooser();
				F.showOpenDialog(null);
				File file = F.getSelectedFile();

			}
		});

		// button xml Export
		btnExportXML.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser F = new JFileChooser();
				F.showOpenDialog(null);
				File file = F.getSelectedFile();

			}
		});

		// button Add Answers
		btnAddAnswers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// create JFrame and JTable
				JFrame frame = new JFrame("Questions");
				frame.setSize(800, 500);
				frame.setLocationRelativeTo(null);
				frame.setLayout(null);
				frame.setResizable(false);
				frame.getContentPane().setBackground(Color.DARK_GRAY);

				JLabel check = new JLabel("Check the Boxes to mark correct Answers!");
				check.setBounds(0, 0, 400, 20);
				check.setForeground(Color.WHITE);
				frame.add(check);

				JTextField Answer1Field = new JTextField("First Answer");
				Answer1Field.setBounds(20, 20, 400, 40);
				frame.add(Answer1Field);
				JCheckBox Answer1CB = new JCheckBox();
				Answer1CB.setBounds(440, 20, 40, 40);
				Answer1CB.setBackground(Color.DARK_GRAY);
				frame.add(Answer1CB);

				JTextField Answer2Field = new JTextField("Second Answer");
				Answer2Field.setBounds(20, 70, 400, 40);
				frame.add(Answer2Field);
				JCheckBox Answer2CB = new JCheckBox();
				Answer2CB.setBounds(440, 70, 40, 40);
				Answer2CB.setBackground(Color.DARK_GRAY);
				frame.add(Answer2CB);

				JTextField Answer3Field = new JTextField("Third Answer");
				Answer3Field.setBounds(20, 120, 400, 40);
				frame.add(Answer3Field);
				JCheckBox Answer3CB = new JCheckBox();
				Answer3CB.setBounds(440, 120, 40, 40);
				Answer3CB.setBackground(Color.DARK_GRAY);
				frame.add(Answer3CB);

				JTextField Answer4Field = new JTextField("Fourth Answer");
				Answer4Field.setBounds(20, 170, 400, 40);
				frame.add(Answer4Field);
				JCheckBox Answer4CB = new JCheckBox();
				Answer4CB.setBounds(440, 170, 40, 40);
				Answer4CB.setBackground(Color.DARK_GRAY);
				frame.add(Answer4CB);

				JTextField Answer5Field = new JTextField("Fifth Answer");
				Answer5Field.setBounds(20, 220, 400, 40);
				frame.add(Answer5Field);
				JCheckBox Answer5CB = new JCheckBox();
				Answer5CB.setBounds(440, 220, 40, 40);
				Answer5CB.setBackground(Color.DARK_GRAY);
				frame.add(Answer5CB);

				JTextField Answer6Field = new JTextField("Sixth Answer");
				Answer6Field.setBounds(20, 270, 400, 40);
				frame.add(Answer6Field);
				JCheckBox Answer6CB = new JCheckBox();
				Answer6CB.setBounds(440, 270, 40, 40);
				Answer6CB.setBackground(Color.DARK_GRAY);
				frame.add(Answer6CB);

				JTextField Answer7Field = new JTextField("Seventh Answer");
				Answer7Field.setBounds(20, 320, 400, 40);
				frame.add(Answer7Field);
				JCheckBox Answer7CB = new JCheckBox();
				Answer7CB.setBounds(440, 320, 40, 40);
				Answer7CB.setBackground(Color.DARK_GRAY);
				frame.add(Answer7CB);

				JTextField Answer8Field = new JTextField("Eigth Answer");
				Answer8Field.setBounds(20, 370, 400, 40);
				frame.add(Answer8Field);
				JCheckBox Answer8CB = new JCheckBox();
				Answer8CB.setBounds(440, 370, 40, 40);
				Answer8CB.setBackground(Color.DARK_GRAY);
				frame.add(Answer8CB);

				JButton btnSave = new JButton("Save Answers");
				btnSave.setBounds(500, 20, 200, 40);
				btnSave.setBackground(Color.GRAY);
				btnSave.setForeground(Color.WHITE);
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						if (!Answer1Field.getText().equals("") && !Answer1Field.getText().equals("First Answer")) {
							Answer Answer1 = new Answer();
							Answer1.setContent(Answer1Field.getText());
							Answer1.setCorrect(Answer1CB.isSelected());
							Answers.add(Answer1);
						}
						if (!Answer2Field.getText().equals("") && !Answer2Field.getText().equals("Second Answer")) {
							Answer Answer2 = new Answer();
							Answer2.setContent(Answer2Field.getText());
							Answer2.setCorrect(Answer2CB.isSelected());
							Answers.add(Answer2);
						}
						if (!Answer3Field.getText().equals("") && !Answer3Field.getText().equals("Third Answer")) {
							Answer Answer3 = new Answer();
							Answer3.setContent(Answer3Field.getText());
							Answer3.setCorrect(Answer3CB.isSelected());
							Answers.add(Answer3);
						}
						if (!Answer4Field.getText().equals("") && !Answer4Field.getText().equals("Fourth Answer")) {
							Answer Answer4 = new Answer();
							Answer4.setContent(Answer4Field.getText());
							Answer4.setCorrect(Answer4CB.isSelected());
							Answers.add(Answer4);
						}
						if (!Answer5Field.getText().equals("") && !Answer5Field.getText().equals("First Answer")) {
							Answer Answer5 = new Answer();
							Answer5.setContent(Answer5Field.getText());
							Answer5.setCorrect(Answer5CB.isSelected());
							Answers.add(Answer5);
						}
						if (!Answer6Field.getText().equals("") && !Answer6Field.getText().equals("Sixth Answer")) {
							Answer Answer6 = new Answer();
							Answer6.setContent(Answer6Field.getText());
							Answer6.setCorrect(Answer6CB.isSelected());
							Answers.add(Answer6);
						}
						if (!Answer7Field.getText().equals("") && !Answer7Field.getText().equals("Seventh Answer")) {
							Answer Answer7 = new Answer();
							Answer7.setContent(Answer7Field.getText());
							Answer7.setCorrect(Answer7CB.isSelected());
							Answers.add(Answer7);
						}
						if (!Answer8Field.getText().equals("") && !Answer1Field.getText().equals("First Answer")) {
							Answer Answer8 = new Answer();
							Answer8.setContent(Answer8Field.getText());
							Answer8.setCorrect(Answer8CB.isSelected());
							Answers.add(Answer8);
						}
						frame.dispose();
					}
				});
				
				frame.add(btnSave);
				frame.setVisible(true);
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