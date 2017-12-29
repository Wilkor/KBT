package controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.limeprotocol.Command;
import org.limeprotocol.Command.CommandMethod;
import org.limeprotocol.EnvelopeId;
import org.limeprotocol.JsonDocument;
import org.limeprotocol.LimeUri;
import org.limeprotocol.MediaType;
import org.limeprotocol.Node;
import org.limeprotocol.messaging.contents.PlainText;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import enums.MediaTypeEnum;
import model.Content;
import model.Entity;
import model.Intention;
import service.HttpService;
import setting.KBTSettings;

/**
 * Classe responsável por controlar o Load dos dados da base de conhecimento no Blip
 * 
 * @author Keila Lacerda
 *
 */
@Component
public class KBTLoadController {

	private HttpService httpService;
	
	/**
	 * Construtor que recebe um HttpService.
	 * 
	 * @param httpService
	 */
	@Inject
	public KBTLoadController(HttpService httpService) {
		this.httpService = httpService;
	}
	
	/**
	 * Realiza o cadastramento de uma Intenção no Blip
	 * 
	 * @param intention
	 */
	public void loadIntention(Intention intention) {

		Command command = createCommandSet(intention);
		command.setUri(LimeUri.parse(KBTSettings.BLIP_SET_INTENT_URI));
		command.setResource(getJsonDocument(intention, MediaTypeEnum.INTENTION.getMediaTypeLime()));

		this.httpService.post(command);
	}
	
	/**
	 * Realiza o cadastramento de uma Entidade no Blip
	 * 
	 * @param entity
	 */
	public void loadEntity(Entity entity) {
		Command command = createCommandSet(entity);
		command.setUri(LimeUri.parse(KBTSettings.BLIP_SET_ENTITY_URI));
		command.setResource(getJsonDocument(entity, MediaTypeEnum.ENTITY.getMediaTypeLime()));

		this.httpService.post(command);
	}
	
	/**
	 * Realiza o cadastramento de um Recurso/Conteúdo no Blip
	 * 
	 * @param content
	 */
	public void loadContent(Content content) {
		Command command = createCommandSet(content);
		//TODO O que concatenar na URI????
		command.setUri(LimeUri.parse(KBTSettings.BLIP_SET_RESOURCE_URI));
		
		PlainText text = new PlainText(content.getValue());
		command.setResource(text);

		this.httpService.post(command);
	}
	
	/**
	 * Cria um comando set para o Blip preenchendo os atributos comuns.
	 * 
	 * @param object
	 * @return
	 */
	private Command createCommandSet(Object object) {
		Command command = new Command(EnvelopeId.newId());
		command.setId(EnvelopeId.newId());
		command.setTo(Node.parse(KBTSettings.BLIP_ADDRESS_AI));
		command.setMethod(CommandMethod.SET);
		
		return command;
	}
	
	/**
	 * Cria e preenche um JsonDocument a partir de um objeto.
	 * 
	 * @param objeto
	 * @param mediaType
	 * @return
	 */
	private JsonDocument getJsonDocument( Object objeto, MediaType mediaType){
		
		ObjectMapper oMapper = new ObjectMapper();
		TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
		 Map<String, Object> map = oMapper.convertValue(objeto, typeRef);
		 
		JsonDocument doc = new JsonDocument(map, new MediaType(MediaType.DiscreteTypes.Application, MediaType.SubTypes.JSON));
		doc.setMediaType(mediaType);
		
		return doc;
		
	}
}
