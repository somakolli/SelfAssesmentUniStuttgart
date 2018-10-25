package domain;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "categoryName", "content" })
public class Category implements SAObject {
	
	private String categoryName = "";
	private String content = "";
	
	public Category() {

	}

	public Category(Category other) {
		this.categoryName = other.categoryName;
		this.content = other.content;
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


	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}


	/**
	 * @param content the content to set
	 */
	@XmlElement
	public void setContent(String content) {
		this.content = content;
	}

}
