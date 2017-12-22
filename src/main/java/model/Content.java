package model;

import java.util.List;

/**
 * @author Keila Lacerda
 *
 */
public class Content {
	private Intention intention;
	private List<EntityValue> entityValues;
	private String value;
	
	public Intention getIntention() {
		return intention;
	}
	public void setIntention(Intention intention) {
		this.intention = intention;
	}
	public List<EntityValue> getEntityValues() {
		return entityValues;
	}
	public void setEntityValues(List<EntityValue> entityValues) {
		this.entityValues = entityValues;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
		
}
