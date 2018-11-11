package creator;

import javafx.application.Application;

import domain.*;
import generator.VGenerator;
import javafx.stage.DirectoryChooser;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.BooleanStringConverter;

import java.util.*;

import javax.swing.plaf.basic.BasicBorders.SplitPaneBorder;

/**
 *
 * 
 * Benutzeroberfläche zum Erstellen eines Self-Assesment-Tests.
 * 
 * @author Julian Blumenröther
 * 
 * @version 1.0
 * 
 * 
 */
public class TextEditor extends Application {

	private static final TreeItem<String> rootitem = new TreeItem<>();
	private static TreeItem<String> currentSelectedTreeItem = rootitem;
	private static final TwoWayHashMap twMap = new TwoWayHashMap();
	private static final TableView<SAObject> table = new TableView<SAObject>();
	private static final TreeView<String> tree = new TreeView<>(rootitem);;
	private VGenerator vg = new VGenerator();


	@Override
	public void start(Stage primaryStage) {

		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1000, 800);
		primaryStage.setMaximized(true);

		primaryStage.setTitle("Self Assessment Test Creator");

		// Confirmation on Close
		primaryStage.setOnCloseRequest(actionEvent -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Exit Platform?");
			alert.setHeaderText("All unsaved changes will be lost.");
			alert.setContentText("Do you want this?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				Platform.exit();
			} else {
				actionEvent.consume();
			}
		});

		MenuBar menuBar = new MenuBar();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		root.setTop(menuBar);

		// Webview & Engine
		WebView mywebview = new WebView();
		WebEngine engine = mywebview.getEngine();

		TextArea text = new TextArea();
		text.setEditable(false);
		text.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {

				changedEvent(engine, newValue);

			}
		});

		SplitPane s = new SplitPane();
		s.getItems().add(text);
		s.getItems().add(mywebview);
		root.setCenter(s);

		// Tabelle zum ändern der Eigenschaften
		table.setPrefHeight(60);
		root.setBottom(table);
		table.setEditable(true);
		table.setFixedCellSize(40.0);

		// TreeView
		rootitem.setExpanded(true);
		// Create Tree
		tree.setShowRoot(false);
		root.setLeft(tree);
		tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {

				TreeItem<String> oldSelected = (TreeItem<String>) oldValue;
				TreeItem<String> selectedItem = (TreeItem<String>) newValue;
				text.setEditable(true);

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
					text.setEditable(false);
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
					TableColumn<SAObject, Boolean> singleChoiceCol = new TableColumn<SAObject, Boolean>(
							"Single Choice");

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

					singleChoiceCol.setCellValueFactory(new PropertyValueFactory<>("singleChoice"));
					singleChoiceCol.setCellFactory(
							TextFieldTableCell.<SAObject, Boolean>forTableColumn(new BooleanStringConverter()));
					singleChoiceCol.setOnEditCommit((CellEditEvent<SAObject, Boolean> t) -> {
						((Question) t.getTableView().getItems().get(t.getTablePosition().getRow()))
								.setSingleChoice(t.getNewValue());
					});

					table.getColumns().add(pointsCol);
					table.getColumns().add(timeCol);
					table.getColumns().add(singleChoiceCol);

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

				} else if (twMap.isConclusion(selectedItem)) {

					TableColumn<SAObject, Integer> rangeCol = new TableColumn<SAObject, Integer>("Range");

					rangeCol.setCellValueFactory(new PropertyValueFactory<>("range"));
					rangeCol.setCellFactory(
							TextFieldTableCell.<SAObject, Integer>forTableColumn(new IntegerStringConverter()));
					rangeCol.setOnEditCommit((CellEditEvent<SAObject, Integer> t) -> {

						((Conclusion) t.getTableView().getItems().get(t.getTablePosition().getRow()))
								.setRange(t.getNewValue());

					});

					table.getColumns().add(rangeCol);
					table.getItems().add(twMap.getSAObject(selectedItem));
					table.setEditable(true);

				}

			}

		});

		// File menu
		Menu fileMenu = new Menu("  File  ");
		MenuItem newcMenuItem = new MenuItem("New Category");
		newcMenuItem.setOnAction(actionEvent -> {

			createCategory();

		});

		MenuItem newqMenuItem = new MenuItem("New Question");
		newqMenuItem.setOnAction(actionEvent -> {

			createQuestion();

		});

		MenuItem newaMenuItem = new MenuItem("New Answer");
		newaMenuItem.setOnAction(actionEvent -> {

			createAnswer();

		});

		MenuItem newconcMenuItem = new MenuItem("New Conclusion");
		newconcMenuItem.setOnAction(actionEvent -> {

			createConclusion();

		});

		MenuItem delMenuItem = new MenuItem("Delete Item");
		delMenuItem.setOnAction(actionEvent -> {

			delete(text);

		});

		MenuItem openMenuItem = new MenuItem("Import xml");
		openMenuItem.setOnAction(actionEvent -> {
			if (twMap.getQuestions().isEmpty() && twMap.getCategories().isEmpty() && twMap.getConclusions().isEmpty()){
				try {
					open(primaryStage, text, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Select an Option.");
				alert.setHeaderText("Merge with current Progress?");
				alert.setContentText(
						"Choosing no will delete your current progress. \nChoosing yes will merge the current progress with the chosen file.");
				ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
				ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
				ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
				alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
				alert.showAndWait().ifPresent(type -> {
					if (type == okButton) {
						try {
							open(primaryStage, text, true);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (type == noButton) {
						try {
							open(primaryStage, text, false);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {

					}
				});
			}
		});

		MenuItem saveMenuItem = new MenuItem("Export xml");
		saveMenuItem.setOnAction(actionEvent -> {

			try {
				save(primaryStage);
				if (!currentSelectedTreeItem.equals(null)) {
					twMap.setContent(twMap.getSAObject(currentSelectedTreeItem), text.getText());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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

		MenuItem generator = new MenuItem("Generate website");
		generator.setOnAction(actionEvent -> {

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Generate");
			fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip"));
			fileChooser.setInitialFileName("website.zip");
			File file = fileChooser.showSaveDialog(primaryStage);

			SARoot saroot = new SARoot();

			saroot.setQuestions(twMap.getQuestions());
			saroot.setConclusions(twMap.getConclusions());
			Collections.sort(saroot.getConclusions());

			try {

				vg.createZipArchive(saroot, file.getPath());

			} catch (Exception e) {

				e.printStackTrace();

			}

		});

		fileMenu.getItems().addAll(newcMenuItem, newqMenuItem, newaMenuItem, newconcMenuItem, delMenuItem,
				new SeparatorMenuItem(), generator, new SeparatorMenuItem(), openMenuItem, saveMenuItem,
				new SeparatorMenuItem(), exitMenuItem);

		// Treeview Context Menu
		ContextMenu treecm = new ContextMenu();
		MenuItem newCContMenuItem = new MenuItem("New Category");
		newCContMenuItem.setOnAction(actionEvent -> {

			createCategory();

		});
		MenuItem newQContMenuItem = new MenuItem("New Question");
		newQContMenuItem.setOnAction(actionEvent -> {

			createQuestion();

		});

		MenuItem newAContMenuItem = new MenuItem("New Answer");
		newAContMenuItem.setOnAction(actionEvent -> {

			createAnswer();

		});

		MenuItem newConcContMenuItem = new MenuItem("New Conclusion");
		newConcContMenuItem.setOnAction(actionEvent -> {

			createConclusion();

		});

		MenuItem DelContMenuItem = new MenuItem("Delete Item");
		DelContMenuItem.setOnAction(actionEvent -> {

			delete(text);

		});

		treecm.getItems().addAll(newCContMenuItem, newQContMenuItem, newAContMenuItem, newConcContMenuItem,
				new SeparatorMenuItem(), DelContMenuItem);
		tree.setContextMenu(treecm);

		Menu insertMenu = new Menu("Insert Media");
		MenuItem imageMenuItem = new MenuItem("Image");
		imageMenuItem.setOnAction(actionEvent -> {
			String currenttext = text.getText();
			String newtext1 = currenttext.substring(0, text.getCaretPosition());
			String newtext2 = currenttext.substring(text.getCaretPosition());
			String finalstring = newtext1 + "\n<img src=\"\" width=\"50%\">\n" + newtext2;
			text.setText(finalstring);
		});

		MenuItem videoMenuItem = new MenuItem("Video");
		videoMenuItem.setOnAction(actionEvent -> {
			String currenttext = text.getText();
			String newtext1 = currenttext.substring(0, text.getCaretPosition());
			String newtext2 = currenttext.substring(text.getCaretPosition());
			String finalstring = newtext1 + "\n<video width=\"50%\" controls><source src=\"\"></video>\n" + newtext2;
			text.setText(finalstring);
		});

		MenuItem setMediaPathMenuItem = new MenuItem("Set Media Folder");
		setMediaPathMenuItem.setOnAction(actionEvent -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("Select Media Folder");
			File file = directoryChooser.showDialog(primaryStage);
			vg.setMediaPath(file.getAbsolutePath() + "/");
		});

		insertMenu.getItems().addAll(imageMenuItem, videoMenuItem, new SeparatorMenuItem(), setMediaPathMenuItem);
		menuBar.getMenus().addAll(fileMenu, insertMenu);

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * Updates the content of the WebEngine.
	 * 
	 * @param engine The WebEngine on which the Content will be shown.
	 * @param newValue The Content which will be shown in the WebEngine.
	 */
	private void changedEvent(WebEngine engine, String newValue) {

		SAObject sao = twMap.getSAObject(currentSelectedTreeItem);

		if (twMap.isCategory(currentSelectedTreeItem)) {
			twMap.setContent(sao, newValue);
			engine.loadContent(vg.getCategoryHtml((Category) twMap.getSAObject(currentSelectedTreeItem)), "text/html");

		} else if (twMap.isQuestion(currentSelectedTreeItem)) {
			twMap.setContent(sao, newValue);
			engine.loadContent(vg.getQuestionHtml((Question) twMap.getSAObject(currentSelectedTreeItem)), "text/html");

		} else if (twMap.isAnswer(currentSelectedTreeItem)) {
			twMap.setContent(sao, newValue);
			engine.loadContent(vg.getQuestionHtml((Question) twMap.getSAObject(currentSelectedTreeItem.getParent())),
					"text/html");

		} else if (twMap.isConclusion(currentSelectedTreeItem)) {
			twMap.setContent(sao, newValue);
			engine.loadContent(vg.getConclusionHtml((Conclusion) twMap.getSAObject(currentSelectedTreeItem)),
					"text/html");

		} else {

		}

	}

	/**
	 * Creates a new category.
	 */
	private void createCategory() {
		Category c = new Category();
		TextInputDialog dialog = new TextInputDialog("Category");
		dialog.setTitle("Category Name");
		dialog.setHeaderText("Enter category name. \nDo not give two Categories the same name.");
		dialog.setContentText("Name:");

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> {

			ArrayList<String> names = new ArrayList<String>();
			for (Category category : twMap.getCategories()) {
				names.add(category.getCategoryName());
			}
			
			if (names.contains(name)) {
			} else {
				c.setCategoryName(name);
				makeBranch(rootitem, c);
			}
		});

	}

	/**
	 * Creates a new question.
	 */
	private void createQuestion() {

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

	}

	/**
	 * Creates a new answer.
	 */
	private void createAnswer() {
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
	}

	/**
	 * Creates a new conclusion.
	 */
	private void createConclusion() {

		Conclusion c = new Conclusion();

		int maxRange = 0;
		for (Conclusion conclusion : twMap.getConclusions()) {
			if (conclusion.getRange() > maxRange)
				maxRange = conclusion.getRange();
		}
		c.setRange(maxRange + 10);

		makeBranch(rootitem, c);

	}

	/**
	 * Removes the currentSelectedTreeItem from the twMap and the TreeView.
	 * 
	 * @param text The main TextArea.
	 */
	private void delete(TextArea text) {

		try {
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

				} else if (twMap.isConclusion(currentSelectedTreeItem)) {
					twMap.removePair(currentSelectedTreeItem);

					ArrayList<TreeItem<String>> al = twMap.getConclusionTreeItems();

					rootitem.getChildren().remove(currentSelectedTreeItem);

					for (int i = 0; i < al.size(); i++) {

						al.get(i).setValue("Conclusion: " + (i + 1));

					}

				}

				if (twMap.getAllTreeItems().size() == 0) {
					table.getColumns().clear();
					table.getItems().clear();
				}

				if (rootitem.getChildren().isEmpty()) {
					text.setEditable(false);
				} else {
					text.setEditable(true);
				}

				twMap.UpdateQuestionIds();

			} else {
				// ... user chose CANCEL or closed the dialog

			}
		} catch (Exception e) {

		}

	}

	/**
	 * Creates a Branch between the given rootitem and the given SAObject and links
	 * them together in the Two-Way-Hash-Map.
	 * 
	 * @param root The TreeItem on which the newly created TreeItem will be attached to.
	 * @param obj The new created SAObject.
	 */
	private void makeBranch(TreeItem<String> root, SAObject obj) {

		TreeItem<String> item = new TreeItem<>();

		item.setExpanded(true);

		root.getChildren().add(item);

		if (!twMap.contains(obj)) {

			twMap.put(item, obj);

		}

		if (twMap.isCategory(obj)) {

			Category c = (Category) obj;
			if (c.getCategoryName().equals("")) {

				item.setValue("Category");

			} else {

				item.setValue(c.getCategoryName());

			}

		} else if (twMap.isQuestion(obj)) {
			
			Question q = (Question) obj;
			Category c = (Category) twMap.getSAObject(twMap.getTreeItem(q).getParent());
			q.setCategory(c);
			twMap.updateQuestionTreeItems();
//			for (int i = 0; i < twMap.getQuestionTreeItems(c).size(); i++) {
//				twMap.getQuestionTreeItems(c).get(i).setValue("Question: " + (i + 1));
//			}
			

		} else if (twMap.isAnswer(obj)) {

			for (int i = 0; i < twMap.getTreeItem(obj).getParent().getChildren().size(); i++) {
				twMap.getTreeItem(obj).getParent().getChildren().get(i).setValue("Answer: " + (i + 1));
			}

			item.setValue("Answer: " + (twMap.getTreeItem(obj).getParent().getChildren().size()));

		} else if (twMap.isConclusion(obj)) {

			for (int i = 0; i < twMap.getConclusionTreeItems().size(); i++) {
				twMap.getConclusionTreeItems().get(i).setValue("Conclusion: " + (i + 1));
			}

			item.setValue(("Conclusion: " + (twMap.getConclusions().size())));

		}

		// Conclusions always on the Bottom
		for (TreeItem<String> ti : twMap.getConclusionTreeItems()) {
			rootitem.getChildren().remove(ti);
			rootitem.getChildren().add(ti);
		}

		twMap.UpdateQuestionIds();

	}

	/**
	 * Reads the contents of a selected XML file, converts them into java objects
	 * and creates the corresponding TreeItems in the treeview.
	 * 
	 * @param primaryStage The main stage.
	 * @param text The TextArea.
	 * @param keep True, if you want to keep your progress, false otherwise.
	 * @throws IOException Will be thrown, if there is no selected file.
	 */
	private void open(Stage primaryStage, TextArea text, boolean keep) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select xml File");
		fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
		fileChooser.setInitialFileName("file.xml");
		File file = fileChooser.showOpenDialog(primaryStage);

		Parser parser = new Parser();
		if (file != null) {
			parser.setFile(file);
			parser.startParser();

			if (!keep) {
				twMap.clear(tree);
			}

			currentSelectedTreeItem = tree.getRoot();

			SARoot saRoot = parser.getRootelement();

			HashMap<Category, ArrayList<Question>> categoryQuestionHashMap = saRoot.getCategoryQuestionMap();

			for (Category category : categoryQuestionHashMap.keySet()) {

				makeBranch(rootitem, category);

				for (Question question : categoryQuestionHashMap.get(category)) {
					makeBranch(twMap.getTreeItem(category), question);
					for (Answer a : question.getAnswers()) {
						makeBranch(twMap.getTreeItem(question), a);
					}
				}

			}

			for (Conclusion conclusion : saRoot.getConclusions()) {
				makeBranch(rootitem, conclusion);
			}

		}
	}

	/**
	 * Saves the current state of Test to a XML-file.
	 * @param primaryStage The main stage.
	 */
	private void save(Stage primaryStage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save as XML-File");
		fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
		fileChooser.setInitialFileName("file.xml");
		File file = fileChooser.showSaveDialog(primaryStage);

		Parser parser = new Parser();
		SARoot root = new SARoot();
		root.setQuestions(twMap.getQuestions());
		root.setConclusions(twMap.getConclusions());
		parser.writeObjectsToXML(root, file);

	}

}
