package setting;

/**
 * Classe responsável por armazenar todos os dados de configuração da aplicação.
 * 
 * @author Keila Lacerda
 *
 */
public class KBTSettings {
	public final static String BLIP_ADDRESS_AI = "postmaster@ai.msging.net";
	public final static String BLIP_COMMAND_ENDPOINT = "https://msging.net/commands";
	public final static String BLIP_SET_INTENT_URI = "/intentions";
	public final static String BLIP_SET_ENTITY_URI = "/entities";
	public final static String BLIP_SET_RESOURCE_URI = "/resources";
	public final static String BLIP_GET_INTENTIONS_TOP10_URI = "/intentions?$skip=0&$take=10";
	public final static String BLIP_GET_INTENTIONS_URI = "/intentions?$skip=0";
	public final static String BLIP_DELETE_INTENTION_URI = "/intentions/";
	
	private String blipAuthorizationToken;

	public KBTSettings(String blipAuthorizationToken) {
		this.blipAuthorizationToken = blipAuthorizationToken;
	}
	
	public String getBlipAuthorizationToken() {
		return blipAuthorizationToken;
	}
	
	public void setBlipAuthorizationToken(String blipAuthorizationToken) {
		this.blipAuthorizationToken = blipAuthorizationToken;
	}
}
