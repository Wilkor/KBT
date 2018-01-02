package model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Resource {
	
	@JsonProperty("total")
	private int total;

	@JsonProperty("itemType")
	private String itemType;
	
	@JsonProperty("items")
	private List<Item> items;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	
}
