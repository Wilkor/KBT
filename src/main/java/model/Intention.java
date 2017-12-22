package model;

import java.util.List;

/**
 * @author Keila Lacerda
 *
 */
public class Intention {
	private String name;
	private List<String> examples;
	
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
