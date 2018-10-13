package creator;

import javafx.application.Application;

import domain.*;
import parser.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.BooleanStringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TextEditor extends Application {

	public static final TreeItem<String> rootitem = new TreeItem<>();
	public static TreeItem<String> currentSelectedTreeItem = new TreeItem<>();
	public int i = 0;
	public static final TwoWayHashMap twMap = new TwoWayHashMap();
	public static final TableView<SAObject> table = new TableView<SAObject>();

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

		// Tabelle zum ‰ndern der eigenschaften

		table.setPrefHeight(60);
		root.setBottom(table);
		table.setEditable(true);
		table.setFixedCellSize(40.0);

		// Tree
		rootitem.setExpanded(true);
		TreeView<String> tree = new TreeView<>(rootitem);
		tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {

				TreeItem<String> oldSelected = (TreeItem<String>) oldValue;
				TreeItem<String> selectedItem = (TreeItem<String>) newValue;

				currentSelectedTreeItem = selectedItem;

				if (oldSelected != null && twMap.getSAObject(oldSelected) != null) {
					twMap.setContent(twMap.getSAObject(oldSelected), text.getText());
				}
				if (selectedItem != null) {
					text.setText(twMap.getContent(selectedItem));
				}

				table.getColumns().clear();
				table.getItems().clear();

				if (twMap.isCategory(selectedItem)) {
					TableColumn<SAObject, String> nameCol = new TableColumn<SAObject, String>("Name");
					nameCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
					nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
					nameCol.setOnEditCommit((CellEditEvent<SAObject, String> t) -> {
						((Category) t.getTableView().getItems().get(t.getTablePosition().getRow()))
								.setCategoryName(t.getNewValue());
						selectedItem.setValue(t.getNewValue());

					});
					table.getColumns().add(nameCol);
					table.getItems().add(twMap.getSAObject(selectedItem));
					table.setEditable(true);

				} else if (twMap.isQuestion(selectedItem)) {

					TableColumn<SAObject, Integer> pointsCol = new TableColumn<SAObject, Integer>("Points");
					TableColumn<SAObject, Integer> timeCol = new TableColumn<SAObject, Integer>("Time");

					pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
					pointsCol.setCellFactory(
							TextFieldTableCell.<SAObject, Integer>forTableColumn(new IntegerStringConverter()));
					pointsCol.setOnEditCommit((CellEditEvent<SAObject, Integer> t) -> {

						((Question) t.getTableView().getItems().get(t.getTablePosition().getRow()))
								.setPoints(t.getNewValue());

					});

					timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
					timeCol.setCellFactory(
							TextFieldTableCell.<SAObject, Integer>forTableColumn(new IntegerStringConverter()));
					timeCol.setOnEditCommit((CellEditEvent<SAObject, Integer> t) -> {
						((Question) t.getTableView().getItems().get(t.getTablePosition().getRow()))
								.setTime(t.getNewValue());
					});

					table.getColumns().add(pointsCol);
					table.getColumns().add(timeCol);

					table.getItems().add(twMap.getSAObject(selectedItem));
					table.setEditable(true);

				} else if (twMap.isAnswer(selectedItem)) {

					TableColumn<SAObject, Boolean> correctCol = new TableColumn<SAObject, Boolean>("Correct");
					correctCol.setCellValueFactory(new PropertyValueFactory<>("correct"));
					correctCol.setCellFactory(
							TextFieldTableCell.<SAObject, Boolean>forTableColumn(new BooleanStringConverter()));
					correctCol.setOnEditCommit((CellEditEvent<SAObject, Boolean> t) -> {
						((Answer) t.getTableView().getItems().get(t.getTablePosition().getRow()))
								.setCorrect(t.getNewValue());
					});
					table.getColumns().add(correctCol);
					table.getItems().add(twMap.getSAObject(selectedItem));

					table.setEditable(true);

				}

				// do what ever you want

			}

		});

		// Create Tree
		tree.setShowRoot(false);
		root.setLeft(tree);

		// File menut
		Menu fileMenu = new Menu("Datei");

		MenuItem newcMenuItem = new MenuItem("New Category");
		newcMenuItem.setOnAction(actionEvent -> {

			Category c = new Category();
			makeBranch(rootitem, c);

		});

		MenuItem newqMenuItem = new MenuItem("New Question");
		newqMenuItem.setOnAction(actionEvent -> {

			Question q = new Question();

			if (currentSelectedTreeItem != null) {

				if (twMap.isCategory(currentSelectedTreeItem)) {

					makeBranch(currentSelectedTreeItem, q);

				} else if (twMap.isQuestion(currentSelectedTreeItem)) {

					makeBranch(currentSelectedTreeItem.getParent(), q);

				} else if (twMap.isAnswer(currentSelectedTreeItem)) {

					makeBranch(currentSelectedTreeItem.getParent().getParent(), q);

				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Can't create Answer!");

					// Header Text: null
					alert.setHeaderText(null);
					alert.setContentText("You need to select a Category to add a Question!");

					alert.showAndWait();
				}

			}

		});

		MenuItem newaMenuItem = new MenuItem("New Answer");
		newaMenuItem.setOnAction(actionEvent -> {

			Answer a = new Answer();

			if (currentSelectedTreeItem != null) {

				if (twMap.isCategory(currentSelectedTreeItem)) {

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Can't create Answer!");

					// Header Text: null
					alert.setHeaderText(null);
					alert.setContentText("You need to select a Question to add an Answer!");

					alert.showAndWait();

				} else if (twMap.isQuestion(currentSelectedTreeItem)) {

					makeBranch(currentSelectedTreeItem, a);

				} else if (twMap.isAnswer(currentSelectedTreeItem)) {

					makeBranch(currentSelectedTreeItem.getParent(), a);

				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Can't create Answer!");

					// Header Text: null
					alert.setHeaderText(null);
					alert.setContentText("You need to select a Question to add an Answer!");

					alert.showAndWait();
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

				if (twMap.isCategory(currentSelectedTreeItem)) {
					twMap.removePair(currentSelectedTreeItem);
					rootitem.getChildren().remove(currentSelectedTreeItem);
					// No need to update since category names are unique
					// for (int i = 0; i < twMap.getCategoryTreeItems().size(); i++) {
					// twMap.getCategoryTreeItems().get(i).setValue("Category: " + (i + 1));
					// }

				} else if (twMap.isQuestion(currentSelectedTreeItem)) {

					twMap.removePair(currentSelectedTreeItem);

					ObservableList<TreeItem<String>> ol = currentSelectedTreeItem.getParent().getChildren();

					currentSelectedTreeItem.getParent().getChildren().remove(currentSelectedTreeItem);

					for (int i = 0; i < ol.size(); i++) {

						ol.get(i).setValue("Question: " + (i + 1));

					}

				} else if (twMap.isAnswer(currentSelectedTreeItem)) {

					twMap.removePair(currentSelectedTreeItem);

					ObservableList<TreeItem<String>> ol = currentSelectedTreeItem.getParent().getChildren();

					currentSelectedTreeItem.getParent().getChildren().remove(currentSelectedTreeItem);

					for (int i = 0; i < ol.size(); i++) {
						ol.get(i).setValue("Answer: " + (i + 1));
					}

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

		MenuItem saveMenuItem = new MenuItem("Export xml");
		saveMenuItem.setOnAction(actionEvent -> {
			twMap.setContent(twMap.getSAObject(currentSelectedTreeItem), text.getText());
			save(primaryStage, text);
		});

		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setOnAction(actionEvent -> {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Exit Platform?");
			alert.setHeaderText("All unsaved changes will be lost.");
			alert.setContentText("Do you want this?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				Platform.exit();
			} else {

			}

		});

		fileMenu.getItems().addAll(newcMenuItem, newqMenuItem, newaMenuItem, delMenuItem, new SeparatorMenuItem(),
				openMenuItem, saveMenuItem, new SeparatorMenuItem(), exitMenuItem);
		menuBar.getMenus().addAll(fileMenu);

		Menu sMenu = new Menu("Search");
		MenuItem sucheMenuItem = new MenuItem("Search");
		sucheMenuItem.setOnAction(actionEvent -> suche(text));
		MenuItem esucheMenuItem = new MenuItem("Search and Replace");
		esucheMenuItem.setOnAction(actionEvent -> esuche(text));
		sMenu.getItems().addAll(sucheMenuItem, esucheMenuItem);
		menuBar.getMenus().addAll(sMenu);

		root.setCenter(text);
		primaryStage.setScene(scene);
		// primaryStage.setFullScreen(true);
		primaryStage.show();

		// Treeview Context Menu
		ContextMenu cm = new ContextMenu();
		MenuItem mi1 = new MenuItem("New Category");
		mi1.setOnAction(actionEvent -> {

			Category c = new Category();
			makeBranch(rootitem, c);

		});
		MenuItem mi2 = new MenuItem("New Question");
		mi2.setOnAction(actionEvent -> {

			Question q = new Question();

			if (currentSelectedTreeItem != null) {

				if (twMap.isCategory(currentSelectedTreeItem)) {

					makeBranch(currentSelectedTreeItem, q);

				} else if (twMap.isQuestion(currentSelectedTreeItem)) {

					makeBranch(currentSelectedTreeItem.getParent(), q);

				} else if (twMap.isAnswer(currentSelectedTreeItem)) {

					makeBranch(currentSelectedTreeItem.getParent().getParent(), q);

				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Can't create Answer!");

					// Header Text: null
					alert.setHeaderText(null);
					alert.setContentText("You need to select a Category to add a Question!");

					alert.showAndWait();
				}

			}

		});
		MenuItem mi3 = new MenuItem("New Answer");
		mi3.setOnAction(actionEvent -> {

			Answer a = new Answer();

			if (currentSelectedTreeItem != null) {

				if (twMap.isCategory(currentSelectedTreeItem)) {

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Can't create Answer!");

					// Header Text: null
					alert.setHeaderText(null);
					alert.setContentText("You need to select a Question to add an Answer!");

					alert.showAndWait();

				} else if (twMap.isQuestion(currentSelectedTreeItem)) {

					makeBranch(currentSelectedTreeItem, a);

				} else if (twMap.isAnswer(currentSelectedTreeItem)) {

					makeBranch(currentSelectedTreeItem.getParent(), a);

				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Can't create Answer!");

					// Header Text: null
					alert.setHeaderText(null);
					alert.setContentText("You need to select a Question to add an Answer!");

					alert.showAndWait();
				}

			}

		});

		MenuItem mi4 = new MenuItem("Delete");
		mi4.setOnAction(actionEvent -> {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Tree Item?");
			alert.setHeaderText("You are about to delete " + currentSelectedTreeItem.getValue() + "!");
			alert.setContentText("Do you want this?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				// ... user chose OK
				text.setText("");

				if (twMap.isCategory(currentSelectedTreeItem)) {
					twMap.removePair(currentSelectedTreeItem);
					rootitem.getChildren().remove(currentSelectedTreeItem);
					//// No need to update since category names are unique
					// for (int i = 0; i < twMap.getCategoryTreeItems().size(); i++) {
					// twMap.getCategoryTreeItems().get(i).setValue("Category: " + (i + 1));
					// }

				} else if (twMap.isQuestion(currentSelectedTreeItem)) {

					twMap.removePair(currentSelectedTreeItem);

					ObservableList<TreeItem<String>> ol = currentSelectedTreeItem.getParent().getChildren();

					currentSelectedTreeItem.getParent().getChildren().remove(currentSelectedTreeItem);

					for (int i = 0; i < ol.size(); i++) {

						ol.get(i).setValue("Question: " + (i + 1));

					}

				} else if (twMap.isAnswer(currentSelectedTreeItem)) {

					twMap.removePair(currentSelectedTreeItem);

					ObservableList<TreeItem<String>> ol = currentSelectedTreeItem.getParent().getChildren();

					currentSelectedTreeItem.getParent().getChildren().remove(currentSelectedTreeItem);

					for (int i = 0; i < ol.size(); i++) {
						ol.get(i).setValue("Answer: " + (i + 1));
					}

				}

			} else {
				// ... user chose CANCEL or closed the dialog

			}

		});
		cm.getItems().addAll(mi1, mi2, mi3, new SeparatorMenuItem(), mi4);
		tree.setContextMenu(cm);

	}

	private void makeBranch(TreeItem<String> root, SAObject obj) {

		TreeItem<String> item = new TreeItem<>();

		item.setExpanded(true);
		root.getChildren().add(item);

		if (!twMap.contains(obj)) {
			twMap.put(item, obj);
		}

		if (twMap.isCategory(obj)) {

			// for (int i = 0; i < twMap.getCategoryTreeItems().size(); i++) {
			//
			// twMap.getTreeItem(obj).getParent().getChildren().get(i).setValue("Category: "
			// + (i + 1));
			// }

			// TODO Set Cat Name
			TextInputDialog dialog = new TextInputDialog("Category");

			dialog.setTitle("Category Name");
			dialog.setHeaderText("Enter category name:");
			dialog.setContentText("Name:");

			Optional<String> result = dialog.showAndWait();
			Category c = (Category) obj;

			item = new TreeItem<>("Category: " + (twMap.getTreeItem(obj).getParent().getChildren().size() + 1));

			result.ifPresent(name -> {
				twMap.getTreeItem(obj).setValue(name);
				c.setCategoryName(name);

			});

		} else if (twMap.isQuestion(obj)) {
			Question q = (Question) obj;
			q.setCategory((Category) twMap.getSAObject(twMap.getTreeItem(q).getParent()));
			Category c = twMap.getCategory(q);
			for (int i = 0; i < twMap.getQuestionTreeItems(c).size(); i++) {
				twMap.getQuestionTreeItems(c).get(i).setValue("Question: " + (i + 1));
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
		textBox.getChildren().add(new Label("Search for"));
		TextField stext = new TextField("");
		textBox.getChildren().add(stext);
		root.setTop(textBox);
		CheckBox checkBox = new CheckBox("Take Capital letters into account?");
		root.setCenter(checkBox);
		HBox bBox = new HBox(4);
		Button bsuche = new Button("Search");
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
		textBox1.getChildren().add(new Label("Search for"));
		TextField stext = new TextField("");
		textBox1.getChildren().add(stext);
		HBox textBox2 = new HBox(4);
		textBox2.setAlignment(Pos.BOTTOM_CENTER);
		textBox2.getChildren().add(new Label("Replace with"));
		TextField stext2 = new TextField("");
		textBox2.getChildren().add(stext2);
		textboxV.getChildren().add(textBox1);
		textboxV.getChildren().add(textBox2);
		root.setTop(textboxV);
		CheckBox checkBox = new CheckBox("Take Capital letters into account?");
		root.setCenter(checkBox);
		HBox bBox = new HBox(4);
		Button bsuche = new Button("Search");
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

		Button replace = new Button("replace");
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

		Button replaceAll = new Button("replace all");
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

	public void checkindex(CheckBox checkBox, List<Integer> fList, TextArea fullText, TextField stext) {
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