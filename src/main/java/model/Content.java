package model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Keila Lacerda
 *
 */
public class Content {
	
	@JsonIgnore
	private Intention intention;
	@JsonIgnore
	private List<EntityValue> entityValues;
	
	private String value;
	@JsonIgnore
	private boolean intermediate;

	
	public Content() {
		this.entityValues = new ArrayList<>();
		this.intention = new Intention();
	}
	
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

	public boolean isIntermediate() {
		return intermediate;
	}

	public void setIntermediate(boolean intermediate) {
		this.intermediate = intermediate;
	}

}
