package util;

import java.util.HashMap;
import java.util.Map;

import org.limeprotocol.Command;
import org.limeprotocol.EnvelopeId;
import org.limeprotocol.JsonDocument;
import org.limeprotocol.MediaType;
import org.limeprotocol.Node;
import org.limeprotocol.Command.CommandMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import setting.KBTSettings;

/**
 * @author Keila Lacerda
 *
 */
public class BlipServiceUtil {

	/**
	 * Cria um comando set para o Blip preenchendo os atributos comuns.
	 * 
	 * @param object
	 * @return
	 */
	public static Command createCommandSet(Object object) {
		Command command = createCommand(object);
		command.setMethod(CommandMethod.SET);
		
		return command;
	}
	
	/**
	 * Cria um comando set para o Blip preenchendo os atributos comuns.
	 * 
	 * @param object
	 * @return
	 */
	public static Command createCommandGet(Object object) {
		Command command = createCommand(object);
		command.setMethod(CommandMethod.GET);
		
		return command;
	}
	
	/**
	 * Cria um comando set para o Blip preenchendo os atributos comuns.
	 * 
	 * @param object
	 * @return
	 */
	public static Command createCommand(Object object) {
		Command command = new Command(EnvelopeId.newId());
		command.setId(EnvelopeId.newId());
		command.setTo(Node.parse(KBTSettings.BLIP_ADDRESS_AI));
		
		return command;
	}
	
	/**
	 * Cria e preenche um JsonDocument a partir de um objeto.
	 * 
	 * @param objeto
	 * @param mediaType
	 * @return
	 */
	public static JsonDocument getJsonDocument( Object objeto, MediaType mediaType){
		
		ObjectMapper oMapper = new ObjectMapper();
		TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
		 Map<String, Object> map = oMapper.convertValue(objeto, typeRef);
		 
		JsonDocument doc = new JsonDocument(map, new MediaType(MediaType.DiscreteTypes.Application, MediaType.SubTypes.JSON));
		doc.setMediaType(mediaType);
		
		return doc;
		
	}
}
