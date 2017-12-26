package model;

import java.util.Set;

/**
 * @author Keila Lacerda
 *
 */
public class Entity {
	private String name;
	private Set<EntityValue> values;

	public Entity() {
	}

	public Entity(String name) {
		this.name = name;
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
