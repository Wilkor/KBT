package model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Keila Lacerda
 *
 */
public class Intention {
	
	private String name;
	
	private transient List<String> examples;

	private transient List<Entity> entities;
	
	public Intention() {
		this.entities = new ArrayList<>();
	}
	
	public Intention(String name) {
		this.name = name;
	}
	
	public void add(Entity entity) {
		this.entities.add(entity);
	}
	
	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getExamples() {
		return examples;
	}

	public void setExamples(List<String> examples) {
		this.examples = examples;
	}

}
