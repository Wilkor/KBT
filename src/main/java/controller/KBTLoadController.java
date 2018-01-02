package controller;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.limeprotocol.Command;
import org.limeprotocol.LimeUri;
import org.limeprotocol.messaging.contents.PlainText;
import org.limeprotocol.serialization.JacksonEnvelopeSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import enums.MediaTypeEnum;
import model.Content;
import model.Entity;
import model.Intention;
import service.HttpService;
import setting.KBTSettings;
import util.BlipServiceUtil;

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

		Command command = BlipServiceUtil.createCommandSet();
		command.setUri(LimeUri.parse(KBTSettings.BLIP_SET_INTENT_URI));
		command.setResource(BlipServiceUtil.getJsonDocument(intention, MediaTypeEnum.INTENTION.getMediaTypeLime()));

		this.httpService.post(command);
	}
	
	/**
	 * Realiza o cadastramento de uma Entidade no Blip
	 * 
	 * @param entity
	 */
	public void loadEntity(Entity entity) {
		Command command = BlipServiceUtil.createCommandSet();
		command.setUri(LimeUri.parse(KBTSettings.BLIP_SET_ENTITY_URI));
		command.setResource(BlipServiceUtil.getJsonDocument(entity, MediaTypeEnum.ENTITY.getMediaTypeLime()));

		this.httpService.post(command);
	}
	
	/**
	 * Realiza o cadastramento de um Recurso/Conteúdo no Blip
	 * 
	 * @param content
	 */
	public void loadContent(Content content) {
		Command command = BlipServiceUtil.createCommandSet();
		//TODO O que concatenar na URI????
		command.setUri(LimeUri.parse(KBTSettings.BLIP_SET_RESOURCE_URI));
		
		PlainText text = new PlainText(content.getValue());
		command.setResource(text);

		this.httpService.post(command);
	}
	
	@SuppressWarnings("unchecked")
	public void getIntentions() {
		//TODO DESCONSIDERA A BAGUNÇA ESTAVA TESTANDO TUDO NO MODO DEBUG, PODE DELETAR METADE DO METODO
		Command command = BlipServiceUtil.createCommandGet();
		command.setUri(LimeUri.parse(KBTSettings.BLIP_GET_INTENTIONS_URI));
		
		ResponseEntity<Object> response = this.httpService.post(command);
		Map intentionsList = (Map) response.getBody();
		Object intentions = ((Map)intentionsList.get("resource")).get("items");
		
		System.out.println(response);
		ObjectMapper oMapper = new ObjectMapper();
		oMapper.enable(MapperFeature.AUTO_DETECT_CREATORS); 
//		oMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); 
		oMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); 
		
		try {
//			new JacksonEnvelopeSerializer().deserialize(intentions.toString());
			oMapper.readValue(intentions.toString(), new TypeReference<Set<Intention>>() {});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(intentions.get(0).getName());
	}
	
	
}
