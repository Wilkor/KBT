package model;

import java.util.List;

/**
 * @author Keila Lacerda
 *
 */
public class Entity {
	private String name;
	private List<EntityValue> values;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<EntityValue> getValues() {
		return values;
	}
	public void setValues(List<EntityValue> values) {
		this.values = values;
	}
	
}
