package creator;

import java.util.ArrayList;
import domain.*;
import java.util.HashMap;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * Hilfsklasse zum Mappen von SAObjects auf TreeItems
 * 
 * @author Julian Blumenröther
 * @version 1.0
 */
public class TwoWayHashMap {

	private HashMap<TreeItem<String>, SAObject> forward = new HashMap<TreeItem<String>, SAObject>();
	private HashMap<SAObject, TreeItem<String>> backward = new HashMap<SAObject, TreeItem<String>>();
	private ArrayList<Question> Questions = new ArrayList<Question>();
	private ArrayList<Category> Categories = new ArrayList<Category>();
	public ArrayList<TreeItem<String>> AllTreeItems = new ArrayList<TreeItem<String>>();

	public TwoWayHashMap() {
		// this.Questions = Questions;
	}

	/**
	 * Maps the two arguments together.
	 * 
	 * @param firstkey
	 * @param secondkey
	 */
	public void put(TreeItem<String> firstkey, SAObject secondkey) {

		forward.put(firstkey, secondkey);
		backward.put(secondkey, firstkey);
		AllTreeItems.add(firstkey);
		if (secondkey.getClass().isInstance(new Category())) {
			Category c = (Category) forward.get(firstkey.getParent());
			Categories.add(c);
		} else if (secondkey.getClass().isInstance(new Question())) {
			Questions.add((Question) secondkey);
		} else if (secondkey.getClass().isInstance(new Answer())) {
			Question q = (Question) forward.get(firstkey.getParent());
			// Arraylist does allow duplicates...
			if (!q.getAnswers().contains(secondkey)) {
				q.getAnswers().add((Answer) secondkey);
			}
		}

	}

	/**
	 * Returns all Question TreeItems related to a Category.
	 * 
	 * @param c
	 * @return
	 */
	public ArrayList<TreeItem<String>> getQuestionTreeItems(Category c) {
		ArrayList<TreeItem<String>> tis = new ArrayList<TreeItem<String>>();
		for (int i = 0; i < AllTreeItems.size(); i++) {

			if (isQuestion(AllTreeItems.get(i))) {
				Question q = (Question) forward.get(AllTreeItems.get(i));
				if (q.getCategory().equals(c)) {
					tis.add(AllTreeItems.get(i));
				}
			}
		}
		return tis;
	}
	
	/**
	 * Returns all Question TreeItems.
	 * 
	 * @return
	 */
	public ArrayList<TreeItem<String>> getQuestionTreeItems() {

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
	 * @param c
	 * @return
	 */
	public ArrayList<Question> getQuestionsforCategory(Category c) {
		ArrayList<Question> res = new ArrayList<Question>();
		for (int i = 0; i < Questions.size(); i++) {
			if (c.equals(Questions.get(i).getCategory())) {
				res.add(Questions.get(i));
			}
		}
		return res;
	}

	public void UpdateQuestionIds() {
		for (int i = 0; i < Questions.size(); i++) {
			Question q = Questions.get(i);
			q.setId(i);
		}
	}

	/**
	 * Returns all Category TreeItems.
	 * 
	 * @return
	 */
	public ArrayList<TreeItem<String>> getCategoryTreeItems() {
		ArrayList<TreeItem<String>> tis = new ArrayList<TreeItem<String>>();
		for (int i = 0; i < AllTreeItems.size(); i++) {
			if (forward.get(AllTreeItems.get(i)).getClass().isInstance(new Category())) {
				tis.add(AllTreeItems.get(i));
			}
		}
		return tis;
	}

	/**
	 * Removes all elements in the map for a given TreeItem.
	 * 
	 * @param firstkey
	 */
	public void removePair(TreeItem<String> firstkey) {

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
			for (int i = 0; i < Questions.size(); i++) {
				if (Questions.get(i).getAnswers().contains(forward.get(firstkey))) {
					Questions.get(i).getAnswers().remove(forward.get(firstkey));
				}
			}
		}
		forward.remove(firstkey);
		backward.values().remove(firstkey);

		AllTreeItems.remove(firstkey);
	}

	public void clear(TreeView t) {
		t.getRoot().getChildren().clear();
		Questions.clear();
		Categories.clear();
		forward.clear();
		backward.clear();
		AllTreeItems.clear();
	}

	/**
	 * Removes all elements in the map for a given SAObject.
	 * 
	 * @param secondkey
	 */
	public void removePair(SAObject secondkey) {
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
			for (int i = 0; i < Questions.size(); i++) {
				if (Questions.get(i).getAnswers().contains(secondkey)) {
					Questions.get(i).getAnswers().remove(secondkey);
				}
			}
		}
		forward.values().remove(secondkey);
		backward.remove(secondkey);
		AllTreeItems.remove(backward.get(secondkey));
	}

	/**
	 * Helpfull method to print the contents of the map.
	 */
	public void printContents() {
		System.out.println("Amount of Categories: " + Categories.size());
		System.out.println("Amount of Questions: " + Questions.size());
		System.out.println("Amount of TreeItems: " + AllTreeItems.size());
		System.out.println("forward Pairs: ");
		for (int i = 0; i < AllTreeItems.size(); i++) {
			System.out.print("/  " + AllTreeItems.get(i) + " - " + forward.get(AllTreeItems.get(i)).getClass() + "  /");
		}
		System.out.println();
	}

	/**
	 * Returns true if the map contains the SAObject.
	 * 
	 * @param Object
	 * @return
	 */
	public boolean contains(SAObject Object) {
		return forward.containsValue(Object) && backward.containsKey(Object);
	}

	/**
	 * Returns true if the map contains the TreeItem.
	 * 
	 * @param treeitem
	 * @return
	 */
	public boolean contains(TreeItem<String> treeitem) {
		return backward.containsValue(treeitem) && forward.containsKey(treeitem);
	}

	/**
	 * Returns the corresponding SAObject for a given TreeItem.
	 * 
	 * @param firstkey
	 * @return
	 */
	public SAObject getSAObject(TreeItem<String> firstkey) {
		return forward.get(firstkey);
	}

	/**
	 * Returns the corresponding TreeItem for a given SAObject.
	 * 
	 * @param secondkey
	 * @return
	 */
	public TreeItem<String> getTreeItem(SAObject secondkey) {
		return backward.get(secondkey);
	}

	/**
	 * Returns true, if the given SAObject is a Category.
	 * 
	 * @param object
	 * @return
	 */
	public boolean isCategory(SAObject object) {
		try {
			if (object.getClass().isInstance(new Category())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true, if the given TreeItem is related to a Category.
	 * 
	 * @param object
	 * @return
	 */
	public boolean isCategory(TreeItem<String> object) {
		try {
			if (forward.get(object).getClass().isInstance(new Category())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true, if the given SAObject is a Question.
	 * 
	 * @param object
	 * @return
	 */
	public boolean isQuestion(SAObject object) {
		try {
			if (object.getClass().isInstance(new Question())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true, if the given TreeItem is related to a Question.
	 * 
	 * @param object
	 * @return
	 */
	public boolean isQuestion(TreeItem<String> object) {
		try {
			if (forward.get(object).getClass().isInstance(new Question())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true, if the given SAObject is an Answer.
	 * 
	 * @param object
	 * @return
	 */
	public boolean isAnswer(SAObject object) {
		try {
			if (object.getClass().isInstance(new Answer())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true, if the given TreeItem is related to an Answer.
	 * 
	 * @param object
	 * @return
	 */
	public boolean isAnswer(TreeItem<String> object) {
		try {
			if (forward.get(object).getClass().isInstance(new Answer())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true if the map is empty.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (forward.isEmpty() && backward.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true, if both maps contain the same amount of elements.
	 * 
	 * @return
	 */
	public boolean isConsistent() {

		if (backward.size() == forward.size() && forward.size() == AllTreeItems.size()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns all Questions.
	 * 
	 * @return
	 */
	public ArrayList<Question> getQuestions() {
		return this.Questions;
	}

	/**
	 * Returns all Categories.
	 * 
	 * @return
	 */
	public ArrayList<Category> getCategories() {
		return this.Categories;
	}

	/**
	 * Returns all TreeItems.
	 * 
	 * @return
	 */
	public ArrayList<TreeItem<String>> getTreeItems() {
		return this.AllTreeItems;
	}

	/**
	 * Returns the Question that contains the given Answer.
	 * 
	 * @param a
	 * @return
	 */
	public Question getQuestion(Answer a) {

		for (int i = 0; i < Questions.size(); i++) {
			if (Questions.get(i).getAnswers().contains(a)) {
				return Questions.get(i);
			}

		}

		return null;

	}

	/**
	 * Returns the Category that contains the Question that contains the given
	 * Answer.
	 * 
	 * @param a
	 * @return
	 */
	public Category getCategory(Answer a) {
		Question q = null;
		for (int i = 0; i < Questions.size(); i++) {
			if (Questions.get(i).getAnswers().contains(a)) {
				q = Questions.get(i);
			}
		}

		return q.getCategory();

	}

	/**
	 * Returns the Category that contains the Question q.
	 * 
	 * @param q
	 * @return
	 */
	public Category getCategory(Question q) {

		return q.getCategory();

	}

	/**
	 * Sets the Content of a given SAObject.
	 * 
	 * @param Object
	 * @param content
	 */
	public void setContent(SAObject Object, String content) {
		if (Object.getClass().isInstance(new Category())) {
			Category category = (Category) Object;
			category.setContent(content);
		} else if (Object.getClass().isInstance(new Question())) {
			Question question = (Question) Object;
			question.setQuestion(content);
		} else if (Object.getClass().isInstance(new Answer())) {
			Answer answer = (Answer) Object;
			answer.setContent(content);
		}
	}

	/**
	 * Sets the Content of a SAObject corresponting to the given TreeItem.
	 * 
	 * @param Object
	 * @param content
	 */
	public void setContent(TreeItem<String> Object, String content) {
		if (Object.getClass().isInstance(new Category())) {
			Category category = (Category) forward.get(Object);
			category.setContent(content);
		} else if (Object.getClass().isInstance(new Question())) {
			Question question = (Question) forward.get(Object);
			question.setQuestion(content);
		} else if (Object.getClass().isInstance(new Answer())) {
			Answer answer = (Answer) forward.get(Object);
			answer.setContent(content);
		}
	}

	/**
	 * Returns the Content of a given SAObject.
	 * 
	 * @param Object
	 * @return
	 */
	public String getContent(SAObject Object) {
		if (Object.getClass().isInstance(new Category())) {
			Category category = (Category) Object;
			return category.getContent();
		} else if (Object.getClass().isInstance(new Question())) {
			Question q = (Question) Object;
			return q.getQuestion();
		} else if (Object.getClass().isInstance(new Answer())) {
			Answer a = (Answer) Object;
			return a.getContent();
		}
		return null;

	}

	/**
	 * Returns the Content of a SAObject corresponding to the given TreeItem.
	 * 
	 * @param Object
	 * @return
	 */
	public String getContent(TreeItem<String> Object) {
		if (forward.get(Object).getClass().isInstance(new Category())) {
			Category c = (Category) forward.get(Object);
			return c.getContent();
		} else if (forward.get(Object).getClass().isInstance(new Question())) {
			Question q = (Question) forward.get(Object);
			return q.getQuestion();
		} else if (forward.get(Object).getClass().isInstance(new Answer())) {
			Answer a = (Answer) forward.get(Object);
			return a.getContent();
		}
		return null;

	}

	/**
	 * Returns the amount of Questions.
	 * 
	 * @return
	 */
	public int getQuestionAmount() {

		return Questions.size();

	}

	/**
	 * Returns the amount of Answers for a given Question q.
	 * 
	 * @param q
	 * @return
	 */
	public int getAnswerAmount(Question q) {

		return q.getAnswers().size();

	}

	/**
	 * Returns the amount of Categories.
	 * 
	 * @return
	 */
	public int getCategoryAmount() {

		return Categories.size();

	}

	// public void updateQuestionItemValues() {
	//
	// }
	//
	// public void updateAnswerItemValues(Question q) {
	//
	// }

}