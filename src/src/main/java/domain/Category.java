package domain;

import java.util.HashMap;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
//@XmlType(propOrder = { "categoryName"})
public class Category extends ContentObject {
	
	private String categoryName = "";
	
	public Category() {

	}

	public Category(Category other) {
		super(other.getContent());
		this.categoryName = other.categoryName;
	}

	/**
	 * 
	 * @param categoryName
	 */
    @XmlElement
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * 
	 * @return categoryName
	 */
	public String getCategoryName() {
		return this.categoryName;
	}

	public HashMap<String, String> getStringProperties() {
		HashMap<String, String> stringVariables = new HashMap<>();
		stringVariables.put("category", this.categoryName);
		return stringVariables;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Category category = (Category) o;
		return Objects.equals(categoryName, category.categoryName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoryName);
	}
}
