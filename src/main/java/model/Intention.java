package model;

import java.util.List;

import net.take.iris.messaging.resources.artificialIntelligence.Entity;

public class Intention extends net.take.iris.messaging.resources.artificialIntelligence.Intention {

	private List<Entity> entities;
	
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Intention() {
		super();
	}
	
	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	
	
}
