package model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Keila Lacerda
 *
 */
public class Entity {

	private String name;

	private Set<EntityValue> values;

	@JsonIgnore
	private String key;

	public Entity() {
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Entity(String name) {
		this.name = name;
	}
	public Entity(String name, String key) {
		this.name = name;
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<EntityValue> getValues() {
		return values;
	}

	public void setValues(Set<EntityValue> values) {
		this.values = values;
	}

}
