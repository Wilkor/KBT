package model;

import java.util.ArrayList;
import java.util.List;

import net.take.iris.messaging.resources.artificialIntelligence.Entity;

/**
 * @author Keila Lacerda
 *
 */
public class KnowledgeBase {

	private List<Entity> entities;

	private List<Intention> intentions;

	public KnowledgeBase() {

		this.entities = new ArrayList<>();

		this.intentions = new ArrayList<>();
	}

	public List<Intention> getIntentions() {
		return intentions;
	}

	public void setIntentions(List<Intention> intentions) {
		this.intentions = intentions;
	}

	public void add(Intention intention) {
		this.intentions.add(intention);
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
}
