package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import domain.Question;

public class Tabelle extends JFrame {

	public static JTable table;
	public static ArrayList<Question> Questions = new ArrayList<Question>();

	public Tabelle() {
		
		//JFrame setters
		setTitle("Fragen");
		setSize(1000, 600);
		setResizable(false);		
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setBackground(Color.DARK_GRAY);
		
		//Tabledata
		
		DefaultTableModel model = new DefaultTableModel();
		String[] columnNames = { "Question Number", "Question", "Answers", "Points", "Time", "Media" };
		model.setColumnIdentifiers(columnNames);
		Object[][] data = new Object[Questions.size()][6];
	
		
		for (int i = 0; i < Questions.size(); i++) {
			Question q = Questions.get(i);
			data[i][0] = i;
			data[i][1] = q.getQuestion();
			data[i][2] = q.getAnswers().toString();
			data[i][3] = q.getPoints();
			data[i][4] = q.getTime();
			data[i][5] = q.getMediaPaths();
			model.addRow(data[i]);
		}
		
		
		
		
		table.setModel(model);
		table.setBackground(Color.DARK_GRAY);
		table.setForeground(Color.WHITE);
		
		
		
		
		table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(980, 500));
		table.setFillsViewportHeight(true);
		JScrollPane scrollpane = new JScrollPane(table);
		add(scrollpane);
		
		//Buttons
		JButton AddButton = new JButton("Add new Question");
		AddButton.addActionListener(new ALAddQuestion());
		add(AddButton);
		
		JButton ImportXmlFile = new JButton("Import Xml File");
		ImportXmlFile.addActionListener(new ALImportXml());
		add(ImportXmlFile);
		
		JButton CreateXmlFile = new JButton("Create Xml File");
		add(CreateXmlFile);
		
	}
	
	public static void paintTable() {
		//TableData
				String[] columnNames = { "Question Number", "Question", "Answers", "Points", "Time", "Media" };
				Object[][] data = new Object[Questions.size()][6];
				
				for (int i = 0; i < Questions.size(); i++) {
					Question q = Questions.get(i);
					data[i][0] = i;
					data[i][1] = q.getQuestion();
					data[i][2] = q.getAnswers().toString();
					data[i][3] = q.getPoints();
					data[i][4] = q.getTime();
					data[i][5] = q.getMediaPaths();
				}
				
				table = new JTable(data, columnNames);
				table.setPreferredScrollableViewportSize(new Dimension(980, 500));
				table.setFillsViewportHeight(true);
				JScrollPane scrollpane = new JScrollPane(table);
				
	}

	public static void main(String[] args) {
		
		Question q = new Question();
		q.setQuestion("Question1");
		Questions.add(q);
		Tabelle gui = new Tabelle();
		gui.setVisible(true);
		
		for (int i = 0; i < Questions.size(); i++) {
			System.out.println("here2");
			System.out.println(Questions.get(i).getQuestion());
		}
	}
	
}