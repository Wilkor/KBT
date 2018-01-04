package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.limeprotocol.Command;
import org.limeprotocol.DocumentBase;
import org.limeprotocol.DocumentCollection;
import org.limeprotocol.Envelope;
import org.limeprotocol.LimeUri;
import org.limeprotocol.messaging.Registrator;
import org.limeprotocol.messaging.contents.PlainText;
import org.limeprotocol.serialization.EnvelopeSerializer;
import org.limeprotocol.serialization.JacksonEnvelopeSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import enums.MediaTypeEnum;
import model.Content;
import model.Intention;
import net.take.iris.messaging.resources.artificialIntelligence.Entity;
import service.HttpService;
import setting.KBTSettings;
import util.BlipServiceUtil;

/**
 * Classe responsavel por controlar o Load dos dados da base de conhecimento no Blip
 * 
 * @author Keila Lacerda
 *
 */
@Component
public class KBTLoadController {

	private HttpService httpService;

	private EnvelopeSerializer serializer;
	
	/**
	 * Construtor que recebe um HttpService.
	 * 
	 * @param httpService
	 */
	@Inject
	public KBTLoadController(HttpService httpService) {
		this.httpService = httpService;
		init();
	}

	@Inject
	public KBTLoadController(HttpService httpService, EnvelopeSerializer serializer) {
		this.httpService = httpService;
		this.serializer = serializer;
		init();
	}
	
	/**
	 * 
	 */
	public void init() {
		Registrator.registerDocuments();
		net.take.iris.messaging.Registrator.registerDocuments();
	}
	
	/**
	 * Percorre toda a lista de Intentions inserindo no Blip e atualizando o id
	 * 
	 * @param intentionsList
	 */
	public void loadIntentions(List<Intention> intentionsList) {
		if (intentionsList != null && !intentionsList.isEmpty()) {
			intentionsList.forEach( intention -> intention.setId(loadIntention(intention)) );			
		}
	}
	
	/**
	 * Realiza o cadastramento de uma Intention no Blip.
	 * 
	 * @param intention
	 */
	public String loadIntention(Intention intention) {

		Command command = BlipServiceUtil.createCommandSet();
		command.setUri(LimeUri.parse(KBTSettings.BLIP_SET_INTENT_URI));
		command.setResource(BlipServiceUtil.getJsonDocument(intention, MediaTypeEnum.INTENTION.getMediaTypeLime()));

		this.httpService.post(command);
		
		return null;//TODO MUDAR PARA RETORNAR O ID
	}

	/**
	 * Percorre toda a lista de Entities inserindo no Blip e atualizando o id.
	 * 
	 * @param entitiesList
	 */
	public void loadEntities(List<Entity> entitiesList) {
		if (entitiesList != null && !entitiesList.isEmpty()) {
			entitiesList.forEach(entity -> entity.setId(loadEntity(entity)));
		}
	}
	
	/**
	 * Realiza o cadastramento de uma Entidade no Blip
	 * 
	 * @param entity
	 */
	public String loadEntity(Entity entity) {
		Command command = BlipServiceUtil.createCommandSet();
		command.setUri(LimeUri.parse(KBTSettings.BLIP_SET_ENTITY_URI));
		command.setResource(BlipServiceUtil.getJsonDocument(entity, MediaTypeEnum.ENTITY.getMediaTypeLime()));

		this.httpService.post(command);
		
		return null;// TODO MUDAR PARA RETORNAR O ID DA ENTITY CADASTRADA
	}

	/**
	 * Realiza o cadastramento de um Recurso/Conteudo no Blip
	 * 
	 * @param content
	 */
	public void loadContent(Content content) {
		Command command = BlipServiceUtil.createCommandSet();
		// TODO O que concatenar na URI????
		command.setUri(LimeUri.parse(KBTSettings.BLIP_SET_RESOURCE_URI + getResourceKey(content)));

		PlainText text = new PlainText(content.getValue());
		command.setResource(text);

		this.httpService.post(command);
	}

	/**
	 * Retorna uma Key formatada da seguinte maneira:
	 * {idIntention}-{idsEntidades}
	 * @param content
	 * @return
	 */
	private String getResourceKey(Content content) {
		if(content != null) {
			StringBuilder key = new StringBuilder();
			if(content.getIntention() != null) {
				key.append(content.getIntention().getId());
			}
			
			ArrayList<String> idEntitiesList = new ArrayList<>();
			content.getEntityValues().forEach( entityValue -> idEntitiesList.add(entityValue.getEntity().getId()));
			
			Collections.sort(idEntitiesList);
			
			idEntitiesList.forEach(id -> key.append("#").append(id));
		}
		
		return null;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Intention> getIntentions() {
		Command command = BlipServiceUtil.createCommandGet();
		command.setUri(LimeUri.parse(KBTSettings.BLIP_GET_ENTITIES_URI));

		ResponseEntity<String> response = (ResponseEntity<String>) this.httpService.post(command);
		Envelope envelope = serializer.deserialize(response.getBody().toString());
		
		return (List<Intention>)(List<?>) BlipServiceUtil.getItemsDocumentFromEnvelope(envelope);	
		
		//TODO REMOVER
//		try {
//			Command command = BlipServiceUtil.createCommandGet();
//			command.setUri(LimeUri.parse(KBTSettings.BLIP_GET_INTENTIONS_URI));
//
//			ResponseEntity<String> response = (ResponseEntity<String>) this.httpService.post(command);
//
//			if (response.getStatusCode() == HttpStatus.OK) {
//
//				System.out.println(response);
//
//				ObjectMapper objectMapper = new ObjectMapper();
//				objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//				objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
//				objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//				objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
//				objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//				
//				model.Command cmd = objectMapper.readValue(response.getBody().toString(), model.Command.class);
//
//				System.out.println(cmd.getId());
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
			
	}
	
	/**
	 * @param idIntention
	 */
	public void deleteIntention(String idIntention) {
		if(!StringUtils.isEmpty(idIntention)) {
			Command command = BlipServiceUtil.createCommandDelete();
			command.setUri(LimeUri.parse(KBTSettings.BLIP_DELETE_INTENTION_URI + idIntention));
			
			this.httpService.post(command);
		}
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Entity> getEntities() {
		Command command = BlipServiceUtil.createCommandGet();
		command.setUri(LimeUri.parse(KBTSettings.BLIP_GET_ENTITIES_URI));

		ResponseEntity<String> response = (ResponseEntity<String>) this.httpService.post(command);
		Envelope envelope = serializer.deserialize(response.getBody().toString());
		Command commandResp = (Command)envelope;
		DocumentCollection doc = (DocumentCollection) commandResp.getResource();
		
		return (List<Entity>)(List<?>) Arrays.asList(doc.getItems());		
	}
	
	public void deleteEntity(String idEntity) {
		if(!StringUtils.isEmpty(idEntity)) {
			Command command = BlipServiceUtil.createCommandDelete();
			command.setUri(LimeUri.parse(KBTSettings.BLIP_DELETE_ENTITY_URI + idEntity));
			
			this.httpService.post(command);
		}
	}
	
}
