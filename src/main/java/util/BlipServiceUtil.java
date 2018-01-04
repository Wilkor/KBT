package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.limeprotocol.Command;
import org.limeprotocol.Command.CommandMethod;
import org.limeprotocol.Document;
import org.limeprotocol.DocumentCollection;
import org.limeprotocol.Envelope;
import org.limeprotocol.EnvelopeId;
import org.limeprotocol.JsonDocument;
import org.limeprotocol.MediaType;
import org.limeprotocol.Node;

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
	 * @return
	 */
	public static Command createCommandSet() {
		Command command = createCommand();
		command.setMethod(CommandMethod.SET);
		
		return command;
	}
	
	/**
	 * Cria um comando set para o Blip preenchendo os atributos comuns.
	 * 
	 * @return
	 */
	public static Command createCommandGet() {
		Command command = createCommand();
		command.setMethod(CommandMethod.GET);
		
		return command;
	}
	
	/**
	 * Cria um comando set para o Blip preenchendo os atributos comuns.
	 * 
	 * @return
	 */
	public static Command createCommand() {
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

	/**
	 * @return
	 */
	public static Command createCommandDelete() {
		Command command = createCommand();	
		command.setMethod(CommandMethod.DELETE);
		
		return command;
	}
	
	/**
	 * @param envelope
	 * @return
	 */
	public static List<Document> getItemsDocumentFromEnvelope(Envelope envelope){
		Command commandResp = (Command)envelope;
		DocumentCollection doc = (DocumentCollection) commandResp.getResource();
		
		return Arrays.asList(doc.getItems());
	}
}
