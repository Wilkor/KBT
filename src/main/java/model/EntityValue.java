package model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Keila Lacerda
 *
 */
public class EntityValue implements Comparable<EntityValue> {
	
	private String name;
	
	@JsonProperty("synonymous")
	private List<String> synonyms;
	
	@JsonIgnore
	private boolean main;
	
	@JsonIgnore
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private Entity entity;

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

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

	public int compareTo(EntityValue entityValue) {
		return this.name.compareTo(entityValue.getName());
	}
}
