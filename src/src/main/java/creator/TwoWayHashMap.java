package creator;

import java.util.ArrayList;
import domain.*;
import java.util.HashMap;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * Hilfsklasse zum Mappen von SAObjects auf TreeItems
 * 
 * @author Julian Blumenr�ther
 * @version 1.0
 */
public class TwoWayHashMap {

	private HashMap<TreeItem<String>, SAObject> forward = new HashMap<TreeItem<String>, SAObject>();
	private HashMap<SAObject, TreeItem<String>> backward = new HashMap<SAObject, TreeItem<String>>();
	private ArrayList<Question> Questions = new ArrayList<Question>();
	private ArrayList<Category> Categories = new ArrayList<Category>();
	private ArrayList<Conclusion> Conclusions = new ArrayList<Conclusion>();
	private ArrayList<TreeItem<String>> AllTreeItems = new ArrayList<TreeItem<String>>();

	public TwoWayHashMap() {
		// this.Questions = Questions;
	}

	/**
	 * Maps the two arguments together.
	 * 
	 * @param firstkey The TreeItem which will be linked together with the SAObject.
	 * @param secondkey The SAObject which will be linked together with the TreeItem.
	 */
	protected void put(TreeItem<String> firstkey, SAObject secondkey) {

		forward.put(firstkey, secondkey);
		backward.put(secondkey, firstkey);
		AllTreeItems.add(firstkey);
		if (secondkey.getClass().isInstance(new Category())) {
			Category c = (Category) secondkey;
			Categories.add(c);
		} else if (secondkey.getClass().isInstance(new Question())) {
			Questions.add((Question) secondkey);
		} else if (secondkey.getClass().isInstance(new Answer())) {
			Question q = (Question) forward.get(firstkey.getParent());
			// Arraylist does allow duplicates...
			if (!q.getAnswers().contains(secondkey)) {
				q.getAnswers().add((Answer) secondkey);
			}
		} else if (secondkey.getClass().isInstance(new Conclusion())) {
			Conclusion conc = (Conclusion) secondkey;
			Conclusions.add(conc);
		}

	}

	/**
	 * Returns all Question TreeItems related to a Category.
	 * 
	 * @param category The Category of which the TreeItems are returned.
	 * @return A List containing all Question TreeItems which are related to a Category
	 */
	protected ArrayList<TreeItem<String>> getQuestionTreeItems(Category category) {
		ArrayList<TreeItem<String>> tis = new ArrayList<TreeItem<String>>();
		for (Question q : Questions)
			if (q.getCategory().equals(category)) {
				TreeItem<String> ti = backward.get(q);
				tis.add(ti);
			}
		return tis;
	}
/**
 * Updates the Values of all Question TreeItems.
 */
	protected void updateQuestionTreeItems() {
		for (Category c : Categories) {
			ArrayList<TreeItem<String>> qtis = new ArrayList<TreeItem<String>>();
			for (Question q : Questions) {
				if (q.getCategory().equals(c)) {
					qtis.add(backward.get(q));
				}
			}
			for (int i = 0; i < qtis.size(); i++) {
				qtis.get(i).setValue("Question: " + (i + 1));
			}
		}
	}

	/**
	 * Returns all Question TreeItems.
	 * 
	 * @return A List containing all Question TreeItems
	 */
	protected ArrayList<TreeItem<String>> getQuestionTreeItems() {

		ArrayList<TreeItem<String>> tis = new ArrayList<TreeItem<String>>();
		for (int i = 0; i < AllTreeItems.size(); i++) {
			if (forward.get(AllTreeItems.get(i)).getClass().isInstance(new Question())) {
				tis.add(AllTreeItems.get(i));
			}
		}
		return tis;
	}

	/**
	 * Returns all Questions related to a Category.
	 * 
	 * @param category The Category of which the Questions are returned.	
	 * @return A List containing all Questions which are related to a Category.
	 */
	protected ArrayList<Question> getQuestionsforCategory(Category category) {
		ArrayList<Question> res = new ArrayList<Question>();
		for (int i = 0; i < Questions.size(); i++) {
			if (category.equals(Questions.get(i).getCategory())) {
				res.add(Questions.get(i));
			}
		}
		return res;
	}

	/**
	 * Updates the Question id's.
	 */
	protected void UpdateQuestionIds() {
		for (int i = 0; i < Questions.size(); i++) {
			Question q = Questions.get(i);
			q.setId(i);
		}
	}
	
	/**
	 * Returns all Conclusions.
	 * @return A List containing all Conclusions.
	 */
	protected ArrayList<Conclusion> getConclusions() {
		return Conclusions;
	}

	/**
	 * Returns all Category TreeItems.
	 * @return A list containing all Category TreeItems.
	 */
	protected ArrayList<TreeItem<String>> getCategoryTreeItems() {
		ArrayList<TreeItem<String>> tis = new ArrayList<TreeItem<String>>();
		for (TreeItem<String> AllTreeItem : AllTreeItems) {
			if (forward.get(AllTreeItem).getClass().isInstance(new Category())) {
				tis.add(AllTreeItem);
			}
		}
		return tis;
	}

	/**
	 * Returns all Conclusion TreeItems.
	 * @return A list containing all Conclusion TreeItems.
	 */
	protected ArrayList<TreeItem<String>> getConclusionTreeItems() {
		ArrayList<TreeItem<String>> tis = new ArrayList<TreeItem<String>>();
		for (int i = 0; i < AllTreeItems.size(); i++) {
			if (forward.get(AllTreeItems.get(i)).getClass().isInstance(new Conclusion())) {
				tis.add(AllTreeItems.get(i));
			}
		}
		return tis;
	}

	/**
	 * Removes all elements in the map for a given TreeItem.
	 * @param firstkey The TreeItem of which all related elements are removed.
	 */
	protected void removePair(TreeItem<String> firstkey) {

		if (forward.get(firstkey).getClass().isInstance(new Category())) {

			ArrayList<Question> remove = new ArrayList<Question>();
			for (Question q : Questions) {
				if (q.getCategory().equals(forward.get(firstkey))) {
					remove.add(q);
				}
			}
			Questions.removeAll(remove);
			Categories.remove(forward.get(firstkey));

		} else if (forward.get(firstkey).getClass().isInstance(new Question())) {
			Questions.remove(forward.get(firstkey));
		} else if (forward.get(firstkey).getClass().isInstance(new Answer())) {
			for (domain.Question Question : Questions) {
				if (Question.getAnswers().contains(forward.get(firstkey))) {
					Question.getAnswers().remove(forward.get(firstkey));
				}
			}
		} else if (forward.get(firstkey).getClass().isInstance(new Conclusion())) {

			Conclusions.remove(forward.get(firstkey));

		}

		forward.remove(firstkey);
		backward.values().remove(firstkey);

		AllTreeItems.remove(firstkey);
	}

	/**
	 * Removes all elements in the map for a given SAObject.
	 * @param secondkey The SAObject of which all related elements are removed.
	 */
	protected void removePair(SAObject secondkey) {
		if (secondkey.getClass().isInstance(new Category())) {

			ArrayList<Question> remove = new ArrayList<Question>();
			for (Question q : Questions) {
				if (q.getCategory().equals(secondkey)) {
					remove.add(q);
				}
			}
			Questions.removeAll(remove);

			Categories.remove(secondkey);
		} else if (secondkey.getClass().isInstance(new Question())) {
			Questions.remove(secondkey);
		} else if (secondkey.getClass().isInstance(new Answer())) {
			for (domain.Question Question : Questions) {
				if (Question.getAnswers().contains(secondkey)) {
					Question.getAnswers().remove(secondkey);
				}
			}
		} else if (secondkey.getClass().isInstance(new Conclusion())) {

			Conclusions.remove(secondkey);

		}
		forward.values().remove(secondkey);
		backward.remove(secondkey);
		AllTreeItems.remove(backward.get(secondkey));
	}

	/**
	 * Resets the given TreeView and the twMap's Contents.
	 * @param t All Elements in this treeView will be deleted.
	 */
	protected void clear(TreeView t) {
		t.getRoot().getChildren().clear();
		Questions.clear();
		Categories.clear();
		Conclusions.clear();
		forward.clear();
		backward.clear();
		AllTreeItems.clear();
	}

	/**
	 * Helpfull method to print the contents of the map.
	 */
	protected void printContents() {
		System.out.println("Amount of Categories: " + Categories.size());
		System.out.println("Amount of Questions: " + Questions.size());
		System.out.println("Amount of Conclusions: " + Conclusions.size());
		System.out.println("Amount of TreeItems: " + AllTreeItems.size());
		System.out.println("forward Pairs: ");
		for (int i = 0; i < AllTreeItems.size(); i++) {
			System.out.print("/  " + AllTreeItems.get(i) + " - " + forward.get(AllTreeItems.get(i)).getClass() + "  /");
		}
		System.out.println();
	}

	/**
	 * Checks if the twMap contains the SAObject
	 * 
	 * @param Object The object which is checked.
	 * @return True, if the Map contains the given SAObject. False, otherwise.
	 */
	protected boolean contains(SAObject Object) {
		return forward.containsValue(Object) && backward.containsKey(Object);
	}

	/**
	 * Checks if the twMap contains the TreeItem.
	 * @param treeitem The TreeItem which is checked.
	 * @return True, if the Map contains the given TreeItem. False, otherwise.
	 */
	protected boolean contains(TreeItem<String> treeitem) {
		return backward.containsValue(treeitem) && forward.containsKey(treeitem);
	}

	/**
	 * Returns the corresponding SAObject for a given TreeItem.
	 * @param firstkey The TreeItem.
	 * @return The corresponding SAObject for a given TreeItem.
	 */ 
	protected SAObject getSAObject(TreeItem<String> firstkey) {
		return forward.get(firstkey);
	}

	/**
	 * Returns the corresponding TreeItem for a given SAObject.
	 * @param secondkey The SAObject
	 * @return The corresponding TreeItem for a given SAObject.
	 */
	protected TreeItem<String> getTreeItem(SAObject secondkey) {
		return backward.get(secondkey);
	}

	/**
	 * Returns true, if the given SAObject is a Category.
	 * @param object The SAObject which is Checked.
	 * @return True, if the given SAObject is a Category. False, otherwise.
	 */
	protected boolean isCategory(SAObject object) {
		try {
			return object.getClass().isInstance(new Category());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true, if the given TreeItem is related to a Category.
	 * @param treeitem The TreeItem which is checked.
	 * @return True, if the given TreeItem is a Category. False, otherwise.
	 */
	protected boolean isCategory(TreeItem<String> treeitem) {
		try {
			return forward.get(treeitem).getClass().isInstance(new Category());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true, if the given SAObject is a Question.
	 * @param object The SAObject which is Checked.
	 * @return True, if the given SAObject is a Question. False, otherwise.
	 */
	protected boolean isQuestion(SAObject object) {
		try {
			return object.getClass().isInstance(new Question());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true, if the given TreeItem is related to a Question.
	 * @param treeitem The TreeItem which is Checked.
	 * @return True, if the given TreeItem is a Question. False, otherwise.
	 */
	protected boolean isQuestion(TreeItem<String> treeitem) {
		try {
			return forward.get(treeitem).getClass().isInstance(new Question());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true, if the given SAObject is an Answer.
	 * @param object The SAObject which is Checked.
	 * @return True, if the given SAObject is a Answer. False, otherwise.
	 */
	protected boolean isAnswer(SAObject object) {
		try {
			return object.getClass().isInstance(new Answer());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true, if the given TreeItem is related to an Answer.
	 * @param treeitem The TreeItem which is Checked.
	 * @return True, if the given TreeItem is a Answer. False, otherwise.
	 */
	protected boolean isAnswer(TreeItem<String> treeitem) {
		try {
			return forward.get(treeitem).getClass().isInstance(new Answer());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true, if the given SAObject is an Conclusion.
	 * @param object The SAObject which is Checked.
	 * @return True, if the given SAObject is a Conclusion. False, otherwise.
	 */
	protected boolean isConclusion(SAObject object) {
		try {
			return object.getClass().isInstance(new Conclusion());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns true, if the given TreeItem is related to a Conclusion.
	 * @param treeitem The TreeItem which is Checked.
	 * @return True, if the given TreeItem is a Conclusion. False, otherwise.
	 */
	protected boolean isConclusion(TreeItem<String> treeitem) {
		try {
			return forward.get(treeitem).getClass().isInstance(new Conclusion());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks if the Map is Empty.
	 * @return True, if there are no Items in the Map. False, otherwise.
	 */
	protected boolean isEmpty() {
		if (forward.isEmpty() && backward.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks, if both maps contain the same amount of elements.
	 * @return True, if both maps contain the same amount of elements. False, otherwise.
	 */
	protected boolean isConsistent() {

		return backward.size() == forward.size() && forward.size() == AllTreeItems.size();
	}

	/**
	 * Returns all Questions.
	 * @return A List containing all Questions.
	 */
	protected ArrayList<Question> getQuestions() {
		return this.Questions;
	}

	/**
	 * Returns all Categories.
	 * @return A List containing all Categories.
	 */
	protected ArrayList<Category> getCategories() {
		return this.Categories;
	}

	/**
	 * Returns all TreeItems.
	 * @return A List containing all TreeItems
	 */
	protected ArrayList<TreeItem<String>> getTreeItems() {
		return this.AllTreeItems;
	}

	/**
	 * Returns the Question that contains the given Answer.
	 * @param answer The Answer which is contained by the Question.
	 * @return The Question that contains the given Answer.
	 */
	protected Question getQuestion(Answer answer) {

		for (domain.Question Question : Questions) {
			if (Question.getAnswers().contains(answer)) {
				return Question;
			}

		}

		return null;

	}

	/**
	 * Returns the Category that contains the Question that contains the given
	 * @param answer The Answer which is contained by a Question which is contained by the Category.
	 * @return The Category that contains a Question that contains the given Answer.
	 */
	protected Category getCategory(Answer answer) {
		Question q = null;
		for (domain.Question Question : Questions) {
			if (Question.getAnswers().contains(answer)) {
				q = Question;
			}
		}

		return q.getCategory();

	}

	/**
	 * Returns the Category that contains the Question q.
	 * @param question The Question which is contained by the Category.
	 * @return The Category which contains the given Question.
	 */
	protected Category getCategory(Question question) {

		return question.getCategory();

	}

	/**
	 * Sets the Content of a given SAObject.
	 * @param Object The content of this Object will be set.
	 * @param content The content which will be set.
	 */
	protected void setContent(SAObject Object, String content) {
		if (Object.getClass().isInstance(new Category())) {
			Category category = (Category) Object;
			category.setContent(content);
		} else if (Object.getClass().isInstance(new Question())) {
			Question question = (Question) Object;
			question.setContent(content);
		} else if (Object.getClass().isInstance(new Answer())) {
			Answer answer = (Answer) Object;
			answer.setContent(content);
		} else if (Object.getClass().isInstance(new Conclusion())) {
			Conclusion conclusion = (Conclusion) Object;
			conclusion.setContent(content);
		}
	}

	/**
	 * Sets the Content of a SAObject corresponting to the given TreeItem.
	 * @param Object The content of this TreeItem will be set.
	 * @param content The content which will be set.
	 */
	protected void setContent(TreeItem<String> Object, String content) {
		if (Object.getClass().isInstance(new Category())) {
			Category category = (Category) forward.get(Object);
			category.setContent(content);
		} else if (Object.getClass().isInstance(new Question())) {
			Question question = (Question) forward.get(Object);
			question.setContent(content);
		} else if (Object.getClass().isInstance(new Answer())) {
			Answer answer = (Answer) forward.get(Object);
			answer.setContent(content);
		} else if (Object.getClass().isInstance(new Conclusion())) {
			Conclusion conclusion = (Conclusion) forward.get(Object);
			conclusion.setContent(content);
		}
	}

	/**
	 * Returns the Content of a given SAObject.
	 * @param Object The content of this Object will be returned.
	 * @return The content of the given SAObject
	 */
	protected String getContent(SAObject Object) {
		if (Object.getClass().isInstance(new Category())) {
			Category category = (Category) Object;
			return category.getContent();
		} else if (Object.getClass().isInstance(new Question())) {
			Question q = (Question) Object;
			return q.getContent();
		} else if (Object.getClass().isInstance(new Answer())) {
			Answer a = (Answer) Object;
			return a.getContent();
		} else if (Object.getClass().isInstance(new Conclusion())) {
			Conclusion c = (Conclusion) Object;
			return c.getContent();
		}
		return null;

	}

	/**
	 * Returns the Content of a SAObject corresponding to the given TreeItem.
	 * @param Object The content of this TreeItem will be returned.
	 * @return The content of the given Object
	 */
	protected String getContent(TreeItem<String> Object) {
		if (forward.get(Object).getClass().isInstance(new Category())) {
			Category c = (Category) forward.get(Object);
			return c.getContent();
		} else if (forward.get(Object).getClass().isInstance(new Question())) {
			Question q = (Question) forward.get(Object);
			return q.getContent();
		} else if (forward.get(Object).getClass().isInstance(new Answer())) {
			Answer a = (Answer) forward.get(Object);
			return a.getContent();
		} else if (forward.get(Object).getClass().isInstance(new Conclusion())) {
			Conclusion c = (Conclusion) forward.get(Object);
			return c.getContent();
		}
		return null;

	}

	/**
	 * Returns a List containing all TreeItems.
	 * @return A List containing all TreeItems.
	 */
	protected ArrayList<TreeItem<String>> getAllTreeItems() {
		return this.AllTreeItems;
	}

}