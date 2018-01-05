package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.take.iris.messaging.resources.artificialIntelligence.Entity;

/**
 * @author Keila Lacerda
 *
 */
public class KnowledgeBase {

	private Map<String, EntityValue> mapEntityValues;

	private Map<String, Intention> mapIntentions;

	private Map<String, Entity> mapEntities;

	private List<Content> contentList;

	private List<Entity> entities;

	private List<Intention> intentions;

	public KnowledgeBase() {

		this.contentList = new ArrayList<>();

		this.mapEntities = new HashMap<String, Entity>();

		this.mapEntityValues = new HashMap<String, EntityValue>();

		this.mapIntentions = new HashMap<String, Intention>();

		this.entities = new ArrayList<>();

		this.intentions = new ArrayList<>();
	}

	public List<Intention> getIntentions() {
		return intentions;
	}

	public void setIntentions(List<Intention> intentions) {
		this.intentions = intentions;
	}

	public void add(Content content) {
		this.contentList.add(content);
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

	public Map<String, EntityValue> getMapEntityValues() {
		return mapEntityValues;
	}

	public void setMapEntityValues(Map<String, EntityValue> mapEntityValues) {
		this.mapEntityValues = mapEntityValues;
	}

	public Map<String, Intention> getMapIntentions() {
		return mapIntentions;
	}

	public void setMapIntentions(Map<String, Intention> mapIntentions) {
		this.mapIntentions = mapIntentions;
	}

	public Map<String, Entity> getMapEntities() {
		return mapEntities;
	}

	public void setMapEntities(Map<String, Entity> mapEntities) {
		this.mapEntities = mapEntities;
	}

	public List<Content> getContentList() {
		return contentList;
	}

	public void setContentList(List<Content> contentList) {
		this.contentList = contentList;
	}
}
