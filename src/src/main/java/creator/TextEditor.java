package creator;

import javafx.application.Application;

import domain.*;
import generator.VGenerator;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.swing.plaf.basic.BasicBorders.SplitPaneBorder;

/**
 * 
 * 
 * *TODO Context Menu Button for inserting a link, Window on the right to
 * convert to.
 *
 * Benutzeroberfläche zum Erstellen eines Self-Assesment-Tests.
 * 
 * @author Julian Blumenröther
 * @version 1.0
 * 
 * 
 */
public class TextEditor extends Application {

	public static final TreeItem<String> rootitem = new TreeItem<>();
	public static TreeItem<String> currentSelectedTreeItem = rootitem;
	public int i = 0;
	public static final TwoWayHashMap twMap = new TwoWayHashMap();
	public static final TableView<SAObject> table = new TableView<SAObject>();
	public static final TreeView<String> tree = new TreeView<>(rootitem);;
    private VGenerator vg = new VGenerator();

	public static void main(String[] args) {

		Application.launch(args);

	}

	@Override
	public void start(Stage primaryStage) {

		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1600, 800);

		
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

		// primaryStage.setResizable(false);

		MenuBar menuBar = new MenuBar();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		root.setTop(menuBar);

		// Webview & Engine
		WebView mywebview = new WebView();
		WebEngine engine = mywebview.getEngine();
		//root.setRight(mywebview);
		

		TextArea text = new TextArea();
		text.setEditable(false);
		text.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				// TODO: Implement functionality
		
				
				changedEvent(engine,newValue);

			}
		});

		// Tabelle zum ändern der eigenschaften

		table.setPrefHeight(60);
		root.setBottom(table);
		table.setEditable(true);
		table.setFixedCellSize(40.0);

		// Tree
		rootitem.setExpanded(true);
		
		SplitPane s = new SplitPane();
		s.getItems().add(text);
		s.getItems().add(mywebview);
		root.setCenter(s);
		
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
					TableColumn<SAObject, Boolean> singleChoiceCol = new TableColumn<SAObject, Boolean>("Single Choice");

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

				// do what ever you want

			}

		});

		// Create Tree
		tree.setShowRoot(false);
		root.setLeft(tree);

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
			try {
				open(primaryStage, text);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		MenuItem saveMenuItem = new MenuItem("Export xml");
		saveMenuItem.setOnAction(actionEvent -> {

			try {
				if (!currentSelectedTreeItem.equals(null)) {
					twMap.setContent(twMap.getSAObject(currentSelectedTreeItem), text.getText());
				}
			} catch (Exception e) {

			} finally {

				save(primaryStage, text);
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


			try {

				vg.createZipArchive(saroot, file.getPath());

			} catch (Exception e) {

				e.printStackTrace();

			}

		});


		fileMenu.getItems().addAll(newcMenuItem, newqMenuItem, newaMenuItem, newconcMenuItem, delMenuItem,
				new SeparatorMenuItem(), generator, new SeparatorMenuItem(), openMenuItem, saveMenuItem,
				new SeparatorMenuItem(), exitMenuItem);
		menuBar.getMenus().addAll(fileMenu);

		Menu sMenu = new Menu("Search");

		MenuItem sucheMenuItem = new MenuItem("Search");
		sucheMenuItem.setOnAction(actionEvent -> suche(text));
		MenuItem esucheMenuItem = new MenuItem("Search and Replace");
		esucheMenuItem.setOnAction(actionEvent -> esuche(text));
		sMenu.getItems().addAll(sucheMenuItem, esucheMenuItem);
		menuBar.getMenus().addAll(sMenu);

		//root.setCenter(text);
		primaryStage.setScene(scene);
		// primaryStage.setFullScreen(true);
		primaryStage.show();

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

		// TODO Adding Insert Media to the Textarea ContextMenu

		Menu insertMenu = new Menu("Insert");
		MenuItem imageMenuItem = new MenuItem("Image");
		imageMenuItem.setOnAction(actionEvent -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select .jpg or .png File");
			fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
			fileChooser.setInitialFileName("");
			File file = fileChooser.showOpenDialog(primaryStage);

			String currenttext = text.getText();
			String newtext1 = currenttext.substring(0, text.getCaretPosition());
			String newtext2 = currenttext.substring(text.getCaretPosition());
			String insert = file.getAbsolutePath();
			String finalstring = newtext1 + "\n<img src=\"file:" + insert + "\" width=\"100px\">\n" + newtext2;
			text.setText(finalstring);

		});
		MenuItem videoMenuItem = new MenuItem("Video");
		videoMenuItem.setOnAction(actionEvent -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select .mp4");
			fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Video Files", "*.mp4"));
			fileChooser.setInitialFileName("");
			File file = fileChooser.showOpenDialog(primaryStage);

			String currenttext = text.getText();
			String newtext1 = currenttext.substring(0, text.getCaretPosition());
			String newtext2 = currenttext.substring(text.getCaretPosition());
			String insert = file.getAbsolutePath();
			String finalstring = newtext1 + "\n<video width=\"100px\" controls><source src=\"file:" + insert + "\"></video>\n" + newtext2;
			text.setText(finalstring);

		});

		// MenuItem tableMenuItem = new MenuItem("Table");
		// tableMenuItem.setOnAction(actionEvent -> {
		//
		// });
		//
		// MenuItem deletecm = new MenuItem("Delete");
		// deletecm.setOnAction(actionEvent -> {
		//
		// });
		//
		// MenuItem insertcm = new MenuItem("Delete");
		// insertcm.setOnAction(actionEvent -> {
		//
		// });
		//
		// MenuItem undocm = new MenuItem("Delete");
		// undocm.setOnAction(actionEvent -> {
		//
		// });
		//
		// MenuItem redocm = new MenuItem("Delete");
		// redocm.setOnAction(actionEvent -> {
		//
		// });

		insertMenu.getItems().addAll(imageMenuItem);
		insertMenu.getItems().addAll(videoMenuItem);
		menuBar.getMenus().add(insertMenu);

	}
	
	public void changedEvent(WebEngine engine, String newValue) {
		
		SAObject sao = twMap.getSAObject(currentSelectedTreeItem);
		VGenerator v = new VGenerator();

		if (twMap.isCategory(currentSelectedTreeItem)) {
			twMap.setContent(sao, newValue);
			engine.loadContent(v.getCategoryHtml((Category) twMap.getSAObject(currentSelectedTreeItem)),
					"text/html");

		} else if (twMap.isQuestion(currentSelectedTreeItem)) {
			twMap.setContent(sao, newValue);
			engine.loadContent(v.getQuestionHtml((Question) twMap.getSAObject(currentSelectedTreeItem)),
					"text/html");

		} else if (twMap.isAnswer(currentSelectedTreeItem)) {
			twMap.setContent(sao, newValue);
			engine.loadContent(v.getQuestionHtml((Question) twMap.getSAObject(currentSelectedTreeItem.getParent())),
					"text/html");

		} else if (twMap.isConclusion(currentSelectedTreeItem)) {
			twMap.setContent(sao, newValue);
			engine.loadContent(v.getConclusionHtml((Conclusion) twMap.getSAObject(currentSelectedTreeItem)),
					"text/html");

		} else {

		}
		
	}

	public void createCategory() {

		Category c = new Category();
		makeBranch(rootitem, c);

	}

	public void createQuestion() {

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

	public void createAnswer() {
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

	public void createConclusion() {

		Conclusion c = new Conclusion();
		makeBranch(rootitem, c);

	}

	public void delete(TextArea text) {

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

			} else if (twMap.isConclusion(currentSelectedTreeItem)) {
				twMap.removePair(currentSelectedTreeItem);

				ArrayList<TreeItem<String>> al = twMap.getConclusionTreeItems();

				rootitem.getChildren().remove(currentSelectedTreeItem);

				for (int i = 0; i < al.size(); i++) {

					al.get(i).setValue("Conclusion: " + (i + 1));

				}

			}

			if (twMap.AllTreeItems.size() == 0) {
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

	}

	/**
	 * Creates a Branch between the given Rootobject and the given SAObject and
	 * links them together in the Two-Way-Hash-Map.
	 * 
	 * @param root
	 * @param obj
	 */
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
			Category c = (Category) obj;
			if (c.getCategoryName().equals("")) {
				TextInputDialog dialog = new TextInputDialog("Category");

				dialog.setTitle("Category Name");
				dialog.setHeaderText("Enter category name:");
				dialog.setContentText("Name:");

				Optional<String> result = dialog.showAndWait();

				item = new TreeItem<>("Category: " + (twMap.getTreeItem(obj).getParent().getChildren().size() + 1));

				result.ifPresent(name -> {
					twMap.getTreeItem(obj).setValue(name);
					c.setCategoryName(name);

				});
			} else {

				twMap.getTreeItem(obj).setValue(c.getCategoryName());

			}

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

		} else if (twMap.isConclusion(obj)) {

			for (int i = 0; i < twMap.getConclusionTreeItems().size(); i++) {
				twMap.getConclusionTreeItems().get(i).setValue("Conclusion: " + (i + 1));
			}

			item = new TreeItem<>("Conclusion: " + (twMap.getConclusions().size() + 1));

		}
		
		//Conclusions always on the Bottom
		for(TreeItem<String> ti: twMap.getConclusionTreeItems()) {
			root.getChildren().remove(ti);
			root.getChildren().add(ti);
		}
		
		twMap.UpdateQuestionIds();

	}

	/**
	 * Reads the contents of a selected XML file, converts them into java objects
	 * and creates the corresponding TreeItems in the treeview.
	 * 
	 * @param primaryStage
	 * @param text
	 * @throws IOException
	 */
	public void open(Stage primaryStage, TextArea text) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select xml File");
		fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
		fileChooser.setInitialFileName("file.xml");
		File file = fileChooser.showOpenDialog(primaryStage);

		/*
		 * if (file != null) { InputStream in = new FileInputStream(file);
		 * BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		 * StringBuilder out = new StringBuilder(); String line; while ((line =
		 * reader.readLine()) != null) { out.append(line + "\n"); }
		 * text.setText(out.toString()); reader.close(); in.close(); }
		 */

		Parser parser = new Parser();
		if (file != null) {
			parser.setFile(file);
			parser.startParser();

			twMap.clear(tree);
			currentSelectedTreeItem = tree.getRoot();

			SARoot saRoot = parser.getRootelement();
			saRoot.setQuestions(parser.getGeneratedQuestions());
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
		}
	}

	/**
	 * Saves the current state of Test to a XML-file.
	 * 
	 * @param primaryStage
	 * @param text
	 */
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
		Parser parser = new Parser();
		SARoot root = new SARoot();
		root.setQuestions(twMap.getQuestions());
		root.setConclusions(twMap.getConclusions());
		parser.writeObjectsToXML(root, file);

	}

	// public int[] properties() {
	// Stage propStage = new Stage();
	// BorderPane root = new BorderPane();
	// Scene scene = new Scene(root, 300, 100);
	//
	// HBox textBox = new HBox();
	// textBox.setAlignment(Pos.BOTTOM_CENTER);
	// textBox.getChildren().add(new Label("Rows"));
	// TextField stext = new TextField("");
	// textBox.getChildren().add(stext);
	//
	// HBox textBox2 = new HBox();
	// textBox2.setAlignment(Pos.BOTTOM_CENTER);
	// textBox2.getChildren().add(new Label("Cols"));
	// TextField stext2 = new TextField("");
	// textBox2.getChildren().add(stext2);
	//
	// root.setTop(textBox);
	// root.setCenter(textBox2);
	//
	// HBox bBox = new HBox(3);
	//
	// Button bsave = new Button("Insert");
	// Label eLabel = new Label("");
	// int[] i = new int[1];
	//
	// bsave.setOnAction(new EventHandler<ActionEvent>() {
	// @Override
	// public void handle(ActionEvent e) {
	//
	// propStage.close();
	//
	// i[0] = Integer.parseInt(stext.getText());
	// i[1] = Integer.parseInt(stext2.getText());
	//
	// }
	// });
	//
	// Button bexit = new Button("Exit");
	// bexit.setOnAction(new EventHandler<ActionEvent>() {
	// @Override
	// public void handle(ActionEvent e) {
	//
	// propStage.close();
	//
	// }
	// });
	//
	// bBox.getChildren().add(bsave);
	// bBox.getChildren().add(bexit);
	// bBox.getChildren().add(eLabel);
	// root.setBottom(bBox);
	// propStage.setScene(scene);
	// // primaryStage.setFullScreen(true);
	// propStage.show();
	// }

	/**
	 * Searches a user input in the text contained by the Textarea.
	 * 
	 * @param fullText
	 */
	public void suche(TextArea fullText) {
		Stage searchStage = new Stage();
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 400, 100);
		HBox textBox = new HBox(4);
		textBox.setAlignment(Pos.BOTTOM_CENTER);
		textBox.getChildren().add(new Label("Search for"));
		TextField stext = new TextField("");
		textBox.getChildren().add(stext);
		root.setTop(textBox);
		CheckBox checkBox = new CheckBox("Take capital letters into account?");
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
		searchStage.setScene(scene);
		// primaryStage.setFullScreen(true);
		searchStage.show();
	};

	/**
	 * Searches a user input in the text contained by the Textarea and replaces the
	 * results with another User Imput.
	 * 
	 * @param fullText
	 */
	public void esuche(TextArea fullText) {
		Stage searchStage = new Stage();
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
		CheckBox checkBox = new CheckBox("Don't take capital letters into account?");
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
		searchStage.setScene(scene);
		// primaryStage.setFullScreen(true);
		searchStage.show();
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