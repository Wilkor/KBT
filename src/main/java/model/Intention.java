package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.limeprotocol.DocumentBase;
import org.limeprotocol.MediaType;

/**
 * @author Keila Lacerda
 *
 */
public class Intention  extends DocumentBase {
	public static String MIME_TYPE = "application/vnd.iris.ai.intention+json";
	
	private String id;
	
	private String name;
	
	private transient List<String> examples;

	private transient List<Entity> entities;
	
	private String key;
	
	public Intention() {
		super(MediaType.parse(MIME_TYPE));
		this.entities = new ArrayList<>();
	}
	
	public Intention(String name) {
		super(MediaType.parse(MIME_TYPE));
		this.name = name;
	}
	
	@Override
    public boolean equals(Object o) {
        if (!(o instanceof Intention)) return false;
        Intention intention = (Intention) o;
        return Objects.equals(id, intention.getId());
    }

    @Override
    public int hashCode() {
        int result = id == null || id.isEmpty() ? 0 : id.hashCode();
        return result;
    }
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
