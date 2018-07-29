package creator;

import javafx.application.Application;

import domain.*;
import parser.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javafx.stage.Stage;

import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TextEditor extends Application {

	public static TreeItem<String> rootitem = new TreeItem<>();
	public static TreeItem<String> currentSelectedTreeItem = new TreeItem<>();
	public int i = 0;
	public static TwoWayHashMap twMap = new TwoWayHashMap();

	public static void main(String[] args) {

		Application.launch(args);

	}

	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 800, 600);
		TextArea text = new TextArea();
		primaryStage.setTitle("Self Assessment Test Creator");

		MenuBar menuBar = new MenuBar();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		root.setTop(menuBar);

		// Tree
		rootitem.setExpanded(true);
		TreeView<String> tree = new TreeView<>(rootitem);

		tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {

				TreeItem<String> oldSelected = (TreeItem<String>) oldValue;
				TreeItem<String> selectedItem = (TreeItem<String>) newValue;
				currentSelectedTreeItem = selectedItem;
				System.out.println("Selected Text : " + selectedItem.getValue());

				if (oldSelected != null && twMap.getSAObject(oldSelected) != null) {
					twMap.setContent(twMap.getSAObject(oldSelected), text.getText());
				}
				if (selectedItem != null) {
					text.setText(twMap.getContent(selectedItem));
				}

				// do what ever you want

			}

		});

		// Add Custom Questions to tree
		// for (int i = 0; i < twMap.getQuestionAmount(); i++) {
		// Question q = twMap.getQuestions().get(i);
		// makeBranch(rootitem, q);
		// TreeItem<String> Question = twMap.getTreeItem(q);
		// for (int j = 0; j < q.getAnswers().size(); j++) {
		// Answer a = q.getAnswers().get(j);
		// makeBranch(Question, a);
		// }
		//
		// }

		// Create Tree
		tree.setShowRoot(false);
		root.setLeft(tree);

		// File menut
		Menu fileMenu = new Menu("Datei");

		MenuItem newqMenuItem = new MenuItem("New Question");
		newqMenuItem.setOnAction(actionEvent -> {
			System.out.println("ROOTITEM: " + rootitem);
			Question q = new Question();
			makeBranch(rootitem, q);
			twMap.printContents();
		});

		MenuItem newaMenuItem = new MenuItem("New Answer");
		newaMenuItem.setOnAction(actionEvent -> {

			Answer a = new Answer();

			if (currentSelectedTreeItem != null) {

				if (twMap.isQuestion(currentSelectedTreeItem)) {

					makeBranch(currentSelectedTreeItem, a);

				} else if (twMap.isAnswer(currentSelectedTreeItem)) {

					makeBranch(currentSelectedTreeItem.getParent(), a);

				}

			}

		});

		MenuItem delMenuItem = new MenuItem("Delete Item");
		delMenuItem.setOnAction(actionEvent -> {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Tree Item?");
			alert.setHeaderText("You are about to delete " + currentSelectedTreeItem.getValue() + "!");
			alert.setContentText("Do you want this?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				// ... user chose OK
				text.setText("");

				if (twMap.isQuestion(currentSelectedTreeItem)) {
					twMap.removePair(currentSelectedTreeItem);
					rootitem.getChildren().remove(currentSelectedTreeItem);
					for (int i = 0; i < twMap.getQuestionTreeItems().size(); i++) {
						twMap.getQuestionTreeItems().get(i).setValue("Question: " + (i + 1));
					}

				} else if (twMap.isAnswer(currentSelectedTreeItem)) {

					twMap.removePair(currentSelectedTreeItem);

					for (int i = 0; i < currentSelectedTreeItem.getParent().getChildren().size(); i++) {
						currentSelectedTreeItem.getParent().getChildren().get(i).setValue("Answer: " + (i + 1));
					}

					currentSelectedTreeItem.getParent().getChildren().remove(currentSelectedTreeItem);

					TreeItem<String> ti = currentSelectedTreeItem;

				}

			} else {
				// ... user chose CANCEL or closed the dialog

			}

		});

		MenuItem openMenuItem = new MenuItem("Import xml");
		openMenuItem.setOnAction(actionEvent -> {
			try {
				open(primaryStage, text);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		MenuItem editMenuItem = new MenuItem("Edit");
		editMenuItem.setOnAction(actionEvent -> {
			if (twMap.getSAObject(currentSelectedTreeItem).getClass().isInstance(new Question())) {
				properties((Question) twMap.getSAObject(currentSelectedTreeItem));
			} else {
				Question q = twMap.getQuestion((Answer) twMap.getSAObject(currentSelectedTreeItem));
				properties(q);
			}
		});

		MenuItem saveMenuItem = new MenuItem("Export xml");
		saveMenuItem.setOnAction(actionEvent -> {
			twMap.setContent(twMap.getSAObject(currentSelectedTreeItem), text.getText());
			save(primaryStage, text);
		});
		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setOnAction(actionEvent -> Platform.exit());

		fileMenu.getItems().addAll(newqMenuItem, newaMenuItem, delMenuItem, new SeparatorMenuItem(), editMenuItem,
				openMenuItem, saveMenuItem, new SeparatorMenuItem(), exitMenuItem);
		menuBar.getMenus().addAll(fileMenu);

		Menu sMenu = new Menu("Suche");
		MenuItem sucheMenuItem = new MenuItem("Suche");
		sucheMenuItem.setOnAction(actionEvent -> suche(text));
		MenuItem esucheMenuItem = new MenuItem("Suche und ersetz");
		esucheMenuItem.setOnAction(actionEvent -> esuche(text));
		sMenu.getItems().addAll(sucheMenuItem, esucheMenuItem);
		menuBar.getMenus().addAll(sMenu);

		root.setCenter(text);
		primaryStage.setScene(scene);
		// primaryStage.setFullScreen(true);
		primaryStage.show();
	}

	private void makeBranch(TreeItem<String> root, SAObject obj) {

		TreeItem<String> item = new TreeItem<>();

		item.setExpanded(true);
		System.out.println(root);
		System.out.println(root.getChildren());
		root.getChildren().add(item);

		if (!twMap.contains(obj)) {
			twMap.put(item, obj);
		}

		if (twMap.isQuestion(obj)) {

			for (int i = 0; i < twMap.getQuestionTreeItems().size(); i++) {
				twMap.getQuestionTreeItems().get(i).setValue("Question: " + (i + 1));
			}

			item = new TreeItem<>("Question: " + (twMap.getQuestionTreeItems().size() + 1));

		} else if (twMap.isAnswer(obj)) {

			for (int i = 0; i < twMap.getTreeItem(obj).getParent().getChildren().size(); i++) {
				twMap.getTreeItem(obj).getParent().getChildren().get(i).setValue("Answer: " + (i + 1));
			}

			item = new TreeItem<>("Answer: " + (twMap.getTreeItem(obj).getParent().getChildren().size() + 1));

		}
	}

	public void open(Stage primaryStage, TextArea text) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select xml File");
		fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
		fileChooser.setInitialFileName("file.xml");
		File file = fileChooser.showOpenDialog(primaryStage);
		/**
		 * if (file != null) { InputStream in = new FileInputStream(file);
		 * BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		 * StringBuilder out = new StringBuilder(); String line; while ((line =
		 * reader.readLine()) != null) { out.append(line + "\n"); }
		 * text.setText(out.toString()); reader.close(); in.close(); }
		 */

		// open parser
		parser parser = new parser();
		if (file != null) {
			parser.setFile(file);
			parser.startParser();

			for (Question q : parser.getGeneratedQuestions()) {

				makeBranch(rootitem, q);

				for (Answer a : q.getAnswers()) {
					makeBranch(twMap.getTreeItem(q), a);

				}

			}
		}
	}

	public void save(Stage primaryStage, TextArea text) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save as XML-File");
		fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
		fileChooser.setInitialFileName("file.xml");
		File file = fileChooser.showSaveDialog(primaryStage);
		/**
		 * if (file != null) { try { FileWriter fileWriter = null; fileWriter = new
		 * FileWriter(file); fileWriter.write(text.getText()); fileWriter.close(); }
		 * catch (IOException ex) {
		 * 
		 * }
		 */
		parser parser = new parser();
		SARoot root = new SARoot();
		root.setQuestions(twMap.getQuestions());
		parser.writeObjectsToXML(root, file);

	}

	public void properties(Question q) {
		Stage propStage = new Stage();
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 200, 85);

		HBox textBox = new HBox(4);
		textBox.setAlignment(Pos.BOTTOM_CENTER);
		textBox.getChildren().add(new Label("Points"));
		TextField stext = new TextField("");
		textBox.getChildren().add(stext);
		stext.setText(String.valueOf(q.getPoints()));
		
		HBox textBox2 = new HBox(4);
		textBox2.setAlignment(Pos.BOTTOM_CENTER);
		textBox2.getChildren().add(new Label("Time"));
		TextField stext2 = new TextField("");
		stext2.setText(String.valueOf(q.getTime()));
		textBox2.getChildren().add(stext2);

		root.setTop(textBox);
		root.setCenter(textBox2);

		// CheckBox checkBox = new CheckBox("Groﬂ- / Kleinschreibung beachten");
		// root.setCenter(checkBox);
		HBox bBox = new HBox(3);

		Button bsave = new Button("Save");
		Label eLabel = new Label("");

		bsave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				q.setPoints(Integer.parseInt(stext.getText()));
				q.setTime(Integer.parseInt(stext2.getText()));
				propStage.close();
				
			}
		});

		Button bexit = new Button("Exit");

		bexit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				propStage.close();
				
			}
		});

		bBox.getChildren().add(bsave);
		bBox.getChildren().add(bexit);
		bBox.getChildren().add(eLabel);
		root.setBottom(bBox);
		propStage.setScene(scene);
		// primaryStage.setFullScreen(true);
		propStage.show();
	}

	public void suche(TextArea fullText) {
		Stage sucheStage = new Stage();
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 400, 100);
		HBox textBox = new HBox(4);
		textBox.setAlignment(Pos.BOTTOM_CENTER);
		textBox.getChildren().add(new Label("Suche nach"));
		TextField stext = new TextField("");
		textBox.getChildren().add(stext);
		root.setTop(textBox);
		CheckBox checkBox = new CheckBox("Groﬂ- / Kleinschreibung beachten");
		root.setCenter(checkBox);
		HBox bBox = new HBox(4);
		Button bsuche = new Button("Suche");
		Label eLabel = new Label("");
		List<Integer> fList = new ArrayList<Integer>(1);

		bsuche.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				asuche(checkBox, fList, fullText, stext, eLabel);
			}
		});

		Button bnext = new Button("Next");
		bnext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (i == fList.size() - 1) {
					i = 0;
				} else {
					i++;
				}
				fullText.selectRange(fList.get(i), fList.get(i) + stext.getText().length());

			}
		});

		Button bPrevious = new Button("Previous");
		bPrevious.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (i == 0) {
					i = fList.size() - 1;
				} else {
					i--;
				}
				fullText.selectRange(fList.get(i), fList.get(i) + stext.getText().length());

			}
		});

		bBox.getChildren().add(bsuche);
		bBox.getChildren().add(bPrevious);
		bBox.getChildren().add(bnext);
		bBox.getChildren().add(eLabel);
		root.setBottom(bBox);
		sucheStage.setScene(scene);
		// primaryStage.setFullScreen(true);
		sucheStage.show();
	};

	public void esuche(TextArea fullText) {
		Stage sucheStage = new Stage();
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 400, 150);
		VBox textboxV = new VBox(2);
		HBox textBox1 = new HBox(4);
		textBox1.setAlignment(Pos.BOTTOM_CENTER);
		textBox1.getChildren().add(new Label("Suche nach"));
		TextField stext = new TextField("");
		textBox1.getChildren().add(stext);
		HBox textBox2 = new HBox(4);
		textBox2.setAlignment(Pos.BOTTOM_CENTER);
		textBox2.getChildren().add(new Label("Ersetz Mit"));
		TextField stext2 = new TextField("");
		textBox2.getChildren().add(stext2);
		textboxV.getChildren().add(textBox1);
		textboxV.getChildren().add(textBox2);
		root.setTop(textboxV);
		CheckBox checkBox = new CheckBox("Groﬂ- / Kleinschreibung beachten");
		root.setCenter(checkBox);
		HBox bBox = new HBox(4);
		Button bsuche = new Button("Suche");
		Label eLabel = new Label("");
		List<Integer> fList = new ArrayList<Integer>(1);

		bsuche.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				asuche(checkBox, fList, fullText, stext, eLabel);
			}
		});

		Button bnext = new Button("Next");
		bnext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (fList.size() != 0) {
					if (i == fList.size() - 1) {
						i = 0;
					} else {
						i++;
					}
					fullText.selectRange(fList.get(i), fList.get(i) + stext.getText().length());

				}
			}
		});

		Button bPrevious = new Button("Previous");
		bPrevious.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (fList.size() != 0) {

					if (i == 0) {
						i = fList.size() - 1;
					} else {
						i--;
					}
					fullText.selectRange(fList.get(i), fList.get(i) + stext.getText().length());

				}
			}
		});

		Button replace = new Button("ersetzen");
		replace.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println(fList.size());
				System.out.println(i);
				for (int i : fList) {
					System.out.println(i);
				}
				if (fList.size() != 0) {
					fullText.replaceText(fList.get(i), fList.get(i) + stext.getText().length(), stext2.getText());
					fList.remove(i);
					if (fList.size() != 0) {
						checkindex(checkBox, fList, fullText, stext);
					}
				}
				if (fList.size() != 0) {
					if (i >= fList.size()) {
						i = fList.size() - 1;
					}
					fullText.selectRange(fList.get(i), fList.get(i) + stext.getText().length());
				} else {
					fullText.selectRange(0, 0);
				}

			}
		});

		Button replaceAll = new Button("alles ersetzen");
		replaceAll.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				asuche(checkBox, fList, fullText, stext, eLabel);
				if (fList.size() != 0) {
					if (checkBox.isSelected()) {
						fullText.setText(fullText.getText().replaceAll("(?i)" + stext.getText(), stext2.getText()));
					} else {
						fullText.setText(fullText.getText().replaceAll(stext.getText(), stext2.getText()));
					}
					fList.clear();
					fullText.selectRange(0, 0);
				}

			}
		});

		bBox.getChildren().add(bsuche);
		bBox.getChildren().add(bPrevious);
		bBox.getChildren().add(bnext);
		bBox.getChildren().add(replace);
		bBox.getChildren().add(replaceAll);
		VBox bBoxV = new VBox(2);
		bBoxV.getChildren().add(eLabel);
		bBoxV.getChildren().add(bBox);
		root.setBottom(bBoxV);
		sucheStage.setScene(scene);
		// primaryStage.setFullScreen(true);
		sucheStage.show();
	};

	public void checkindex(CheckBox checkBox, List fList, TextArea fullText, TextField stext) {
		if (checkBox.isSelected()) {
			fList.clear();
			int x = fullText.getText().toLowerCase().indexOf(stext.getText().toLowerCase());
			fList.add(x);
			while (x >= 0) {
				x = fullText.getText().toLowerCase().indexOf(stext.getText().toLowerCase(), x + 1);
				if (x != -1) {
					fList.add(x);
				}
			}
		} else {
			fList.clear();
			int x = fullText.getText().indexOf(stext.getText());
			fList.add(x);
			while (x >= 0) {
				x = fullText.getText().indexOf(stext.getText(), x + 1);
				if (x != -1) {
					fList.add(x);
				}
			}
		}

	}

	public void asuche(CheckBox checkBox, List<Integer> fList, TextArea fullText, TextField stext, Label eLabel) {
		fList.clear();
		i = 0;
		if (checkBox.isSelected()) {
			if (stext.getText().toLowerCase() != null && !stext.getText().isEmpty()) {
				int index = fullText.getText().toLowerCase().indexOf(stext.getText().toLowerCase());
				if (index == -1) {
					eLabel.setText("Search key Not in the text");
				} else {
					int x = fullText.getText().toLowerCase().indexOf(stext.getText().toLowerCase());
					fList.add(x);
					while (x >= 0) {
						x = fullText.getText().toLowerCase().indexOf(stext.getText().toLowerCase(), x + 1);
						if (x != -1) {
							fList.add(x);
						}
					}
					eLabel.setText("Found");
					i = 0;
					fullText.selectRange(fList.get(i), fList.get(i) + stext.getText().toLowerCase().length());
				}
			} else {
				eLabel.setText("Missing search key");
				// errorText.setFill(Color.RED);

			}
		} else {
			if (stext.getText() != null && !stext.getText().isEmpty()) {
				int index = fullText.getText().indexOf(stext.getText());
				if (index == -1) {
					eLabel.setText("Search key Not in the text");
				} else {
					int x = fullText.getText().indexOf(stext.getText());
					fList.add(x);
					while (x >= 0) {
						x = fullText.getText().indexOf(stext.getText(), x + 1);
						if (x != -1) {
							fList.add(x);
						}
					}
					eLabel.setText("Found");
					i = 0;
					fullText.selectRange(fList.get(i), fList.get(i) + stext.getText().length());
				}
			} else {
				eLabel.setText("Missing search key");
				// errorText.setFill(Color.RED);

			}
		}

	}
}