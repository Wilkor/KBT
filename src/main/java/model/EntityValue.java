package model;

import java.util.List;

import org.json.JSONObject;

/**
 * @author Keila Lacerda
 *
 */
public class EntityValue {
	private String name;
	private List<String> synonyms;
	private boolean main;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getSynonyms() {
		return synonyms;
	}
	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}
	public boolean isMain() {
		return main;
	}
	public void setMain(boolean main) {
		this.main = main;
	}
	
	@Override
	public String toString() {
		JSONObject j = new JSONObject();
		
		j.put("name", this.getName());
		j.put("main", this.isMain());
		
		return j.toString();
		
	}
}
