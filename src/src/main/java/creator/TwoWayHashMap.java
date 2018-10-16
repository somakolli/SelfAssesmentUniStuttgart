package creator;

import java.util.ArrayList;
import domain.*;
import java.util.HashMap;

import javafx.scene.control.TreeItem;

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
	 * @param TreeItem<String>
	 *            firstkey, SAObject secondkey Links the elements together and saves
	 *            them in their respective Lists
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
	 * Returns an ArrayList containing all treeitems for all Questions
	 * 
	 * @param
	 * 
	 * @return ArrayList<TreeItem<String>>
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

	public ArrayList<TreeItem<String>> getCategoryTreeItems() {
		ArrayList<TreeItem<String>> tis = new ArrayList<TreeItem<String>>();
		for (int i = 0; i < AllTreeItems.size(); i++) {
			if (forward.get(AllTreeItems.get(i)).getClass().isInstance(new Category())) {
				tis.add(AllTreeItems.get(i));
			}
		}
		return tis;
	}

	public void removePair(TreeItem<String> firstkey) {

		if (forward.get(firstkey).getClass().isInstance(new Category())) {
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

	public void removePair(SAObject secondkey) {
		if (secondkey.getClass().isInstance(new Category())) {
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

	public boolean contains(SAObject Object) {
		return forward.containsValue(Object) && backward.containsKey(Object);
	}

	public boolean contains(TreeItem<String> ti) {
		return backward.containsValue(ti) && forward.containsKey(ti);
	}

	public SAObject getSAObject(TreeItem<String> firstkey) {
		return forward.get(firstkey);
	}

	public TreeItem<String> getTreeItem(SAObject secondkey) {
		return backward.get(secondkey);
	}

	public boolean isCategory(SAObject secondkey) {
		try {
			if (secondkey.getClass().isInstance(new Category())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isCategory(TreeItem<String> firstkey) {
		try {
			if (forward.get(firstkey).getClass().isInstance(new Category())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isQuestion(SAObject secondkey) {
		try {
			if (secondkey.getClass().isInstance(new Question())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isQuestion(TreeItem<String> firstkey) {
		try {
			if (forward.get(firstkey).getClass().isInstance(new Question())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isAnswer(SAObject secondkey) {
		try {
			if (secondkey.getClass().isInstance(new Answer())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isAnswer(TreeItem<String> firstkey) {
		try {
			if (forward.get(firstkey).getClass().isInstance(new Answer())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isEmpty() {
		if (forward.isEmpty() && backward.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isConsistent() {

		if (backward.size() == forward.size() && forward.size() == AllTreeItems.size()) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<Question> getQuestionsforCategory(Category c) {
		ArrayList<Question> res = new ArrayList<Question>();
		for (int i = 0; i < Questions.size(); i++) {
			if (c.equals(Questions.get(i).getCategory())) {
				res.add(Questions.get(i));
			}
		}
		return res;
	}

	public ArrayList<Question> getQuestions() {
		return this.Questions;
	}

	public ArrayList<Category> getCategories() {
		return this.Categories;
	}

	public ArrayList<TreeItem<String>> getTreeItems() {
		return this.AllTreeItems;
	}

	public Question getQuestion(Answer a) {

		for (int i = 0; i < Questions.size(); i++) {
			if (Questions.get(i).getAnswers().contains(a)) {
				return Questions.get(i);
			}

		}

		return null;

	}

	public Category getCategory(Answer a) {
		Question q = null;
		for (int i = 0; i < Questions.size(); i++) {
			if (Questions.get(i).getAnswers().contains(a)) {
				q = Questions.get(i);
			}
		}

		return q.getCategory();

	}

	public Category getCategory(Question q) {

		return q.getCategory();

	}

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

	public int getQuestionAmount() {

		return Questions.size();

	}

	public int getAnswerAmount(Question q) {

		return q.getAnswers().size();

	}

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