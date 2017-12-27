package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Keila Lacerda
 *
 */
public class KnowledgeBase {

	private Map<String, EntityValue> mapEntityValues;

	private Map<String, Intention> mapIntentions;

	private Map<String, Entity> mapEntities;

	private List<Content> contentList;

	private List<EntityValue> entityValues;

	public KnowledgeBase() {
		this.entityValues = new ArrayList<>();
		this.contentList = new ArrayList<>();
	}

	public List<EntityValue> getEntityValues() {
		return entityValues;
	}

	public void setEntityValues(List<EntityValue> entityValues) {
		this.entityValues = entityValues;
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

	public void add(EntityValue entityValue) {
		this.entityValues.add(entityValue);
	}

	public void add(Content content) {
		this.contentList.add(content);
	}

}
