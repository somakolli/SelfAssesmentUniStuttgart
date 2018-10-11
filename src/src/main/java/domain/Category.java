package domain;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class Category implements SAObject{
	private String name;
	
	
	public String getName() {
		return this.name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public HashMap<String, String> getStringProperties() {
		// TODO Auto-generated method stub
		return null;
	}
}
