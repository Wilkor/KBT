package enums;

import org.limeprotocol.MediaType;

/**
 * Enum os possíveis 'MediaType' para o que vai ser cadastrado na base de conhecimento
 * 
 * @author Keila Lacerda
 *
 */
public enum MediaTypeEnum {

	INTENTION("application/vnd.iris.ai.intention+json"),
	ENTITY("application/vnd.iris.ai.entity+json"),
	RESOURCE("text/plain");
	
	private String type;
	
	MediaTypeEnum(String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public MediaType getMediaTypeLime() {
		return MediaType.parse(this.type);
	}

}
