package setting;

/**
 * Classe respons�vel por armazenar todos os dados de configura��o da aplica��o.
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
