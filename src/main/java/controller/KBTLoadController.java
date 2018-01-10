package controller;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.limeprotocol.Command;
import org.limeprotocol.Command.CommandStatus;
import org.limeprotocol.Document;
import org.limeprotocol.DocumentCollection;
import org.limeprotocol.LimeUri;
import org.limeprotocol.PlainDocument;
import org.limeprotocol.messaging.Registrator;
import org.limeprotocol.serialization.EnvelopeSerializer;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import constant.LoaderConstants;
import enums.MediaTypeEnum;
import exception.LoaderException;
import model.KnowledgeBase;
import net.take.iris.messaging.resources.artificialIntelligence.Entity;
import net.take.iris.messaging.resources.artificialIntelligence.Intention;
import net.take.iris.messaging.resources.artificialIntelligence.Question;
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
	 * @param kb
	 * @param logger 
	 * @throws LoaderException 
	 */
	public void loadBase(KnowledgeBase kb, Logger logger) throws LoaderException {
		if(kb != null) {
			
			/*
			 * Processo Load Intention
			 */
			logger.info("Deletando Intenções...");
			deleteIntentions(getIntentions());
			logger.info("Inserindo Intenções...");
			loadIntentions(kb.getIntentions());
			logger.info("Intenções inseridas com sucesso.");
			
			/*
			 * Processo Load Entity
			 */
			logger.info("Deletando Entidades...");
			deleteEntities(getEntities());
			logger.info("Inserindo Entidades...");
			loadEntities(kb.getEntities());
			logger.info("Entidades inseridas com sucesso.");
			
			/*
			 * Processo Load Conteudo/Resource
			 */
			logger.info("Deletando Conteúdo...");
			deleteResources(getResources());
			logger.info("Inserindo Conteúdo...");
			loadResources(kb.getIntentions());
			logger.info("Conteúdo inserido com sucesso.");
		}
	}
	
	/**
	 * @param resources
	 */
	private void deleteResources(List<PlainDocument> resources) {
		
		if(resources != null && !resources.isEmpty()) {
			resources.forEach(resource -> deleteResource(resource));
		}
	}

	private void deleteResource(PlainDocument resource) {
		if(resource != null) {
			Command command = BlipServiceUtil.createCommandDelete();
			command.setTo(null);
			command.setUri(LimeUri.parse(KBTSettings.BLIP_DELETE_RESOURCE_URI + resource.getValue()));
			
			this.httpService.post(command);
		}
	}

	/**
	 * @param intentionsToDelete
	 */
	private void deleteIntentions(List<Intention> intentionsToDelete) {
		
		if(intentionsToDelete != null && !intentionsToDelete.isEmpty()) {
			intentionsToDelete.forEach(intention -> deleteIntention(intention));
		}
	}
	
	/**
	 * Percorre toda a lista de Intentions inserindo no Blip e atualizando o id
	 * 
	 * @param intentionsList
	 */
	public void loadIntentions(List<model.Intention> intentionsList) {
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

		ResponseEntity<String> response = this.httpService.post(command);
		Command commandResp = (Command)serializer.deserialize(response.getBody().toString());
		net.take.iris.messaging.resources.artificialIntelligence.Intention intentionResp = (net.take.iris.messaging.resources.artificialIntelligence.Intention) commandResp.getResource();
		
		intention.setId(intentionResp.getId());
		
		loadQuestions(intention);
		
		return intentionResp.getId();
	}

	/**
	 * @param intention
	 */
	private void loadQuestions(Intention intention) {
		Command command = BlipServiceUtil.createCommandSet();
		command.setUri(LimeUri.parse(KBTSettings.BLIP_SET_QUESTIONS_URI.replace("{idIntention}", intention.getId())));
		
		DocumentCollection doc = new DocumentCollection();
		doc.setItemType(MediaTypeEnum.QUESTIONS.getMediaTypeLime());
		doc.setItems(new Document[intention.getQuestions().length]);
		for (int i = 0; i< intention.getQuestions().length; i++) {
			doc.getItems()[i] = BlipServiceUtil.getJsonDocument(intention.getQuestions()[i], MediaTypeEnum.QUESTIONS.getMediaTypeLime());
		}
		
		command.setResource(doc);
		this.httpService.post(command);
	}

	/**
	 * Remove Entidade
	 * 
	 * @param entities
	 */
	private void deleteEntities(List<Entity> entities) {
		if (entities != null && !entities.isEmpty()) {
			entities.forEach( entity -> deleteEntity(entity.getId()));			
		}
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
		
		ResponseEntity<String> response = this.httpService.post(command);
		Command commandResp = (Command)serializer.deserialize(response.getBody().toString());
		Entity entityResp = (Entity) commandResp.getResource();
		
		return entityResp.getId();
	}

	/**
	 * Realiza o cadastramento de um Recurso/Conteudo no Blip
	 * 
	 * @param content
	 */
	public void loadResources(List<model.Intention> intentions) {
		
		if(intentions != null && !intentions.isEmpty()) {
			intentions.forEach(intention -> loadResource(intention));
		}
	}

	/**
	 * @param resource
	 */
	private void loadResource(model.Intention intention) {
		Command command = BlipServiceUtil.createCommandSet();
		
		command.setUri(LimeUri.parse(KBTSettings.BLIP_SET_RESOURCE_URI + LoaderConstants.PREFIX_AI + intention.getKey()));
				
//		PlainText text = new PlainText(content.getValue());
		command.setResource(intention.getResource());
		command.setTo(null);
		
		this.httpService.post(command);
	}

//	/**
//	 * Retorna uma Key formatada da seguinte maneira:
//	 * {idIntention}-{idsEntidades}
//	 * @param content
//	 * @return
//	 */
//	private String getResourceKey(Content content) {
//		if(content != null) {
//			StringBuilder key = new StringBuilder("/");
//			if(content.getIntention() != null) {
//				key.append(content.getIntention().getId());
//			}
//			
//			ArrayList<String> idEntitiesList = new ArrayList<>();
//			content.getEntityValues().forEach( entityValue -> idEntitiesList.add(entityValue.getEntity().getId()));
//			
//			Collections.sort(idEntitiesList);
//			
//			idEntitiesList.forEach(id -> key.append("::").append(id));
//			
//			return key.toString();
//		}
//		
//		return null;
//	}

	/**
	 * @return
	 * @throws LoaderException 
	 */
	@SuppressWarnings("unchecked")
	public List<Intention> getIntentions() throws LoaderException {
		Command command = BlipServiceUtil.createCommandGet();
		command.setUri(LimeUri.parse(KBTSettings.BLIP_GET_INTENTIONS_URI));

		ResponseEntity<String> response = (ResponseEntity<String>) this.httpService.post(command);
		Command commandResp = (Command)serializer.deserialize(response.getBody().toString());
		
		if(CommandStatus.SUCCESS.equals(commandResp.getStatus())) {
			DocumentCollection doc = (DocumentCollection) commandResp.getResource();
			
			return getQuestions((List<Intention>)(List<?>) Arrays.asList(doc.getItems()));
		}
		else if(commandResp.getReason().getCode() == LoaderConstants.RESOURCE_NOT_FOUND){
			return null;
		}
		else {
			throw new LoaderException(commandResp.getReason().getDescription());
		}
	}
	
	/**
	 * @param intentions
	 * @return
	 */
	private List<Intention> getQuestions(List<Intention> intentions){
		if(intentions != null && !intentions.isEmpty()) {
			intentions.forEach(intention -> {
				try {
					intention.setQuestions(getQuestionsByIntention(intention));
				} catch (LoaderException e) {
					new RuntimeException(e.getMessage());
				}
			});
		}
		return intentions;
	}

	/**
	 * @param intention
	 * @return
	 * @throws LoaderException
	 */
	private Question[] getQuestionsByIntention(Intention intention) throws LoaderException {
		Command command = BlipServiceUtil.createCommandGet();
		command.setUri(LimeUri.parse(KBTSettings.BLIP_GET_QUESTIONS_URI.replace(LoaderConstants.ID_INTENTION, intention.getId())));

		ResponseEntity<String> response = (ResponseEntity<String>) this.httpService.post(command);
		Command commandResp = (Command)serializer.deserialize(response.getBody().toString());
		
		if(CommandStatus.SUCCESS.equals(commandResp.getStatus())) {
			DocumentCollection doc = (DocumentCollection) commandResp.getResource();
			
			return Arrays.copyOf(doc.getItems(), doc.getItems().length, Question[].class);
		}
		else if(commandResp.getReason().getCode() == LoaderConstants.RESOURCE_NOT_FOUND){
			return null;
		}
		else {
			throw new LoaderException("Fail to get 'Questions' - " + commandResp.getReason().getDescription());
		}
	}

	/**
	 * @param idIntention
	 */
	public void deleteIntention(Intention intention) {
		if(intention != null) {
			Command command = BlipServiceUtil.createCommandDelete();
			command.setUri(LimeUri.parse(KBTSettings.BLIP_DELETE_INTENTION_URI + intention.getId()));
			
			//remover todas as questões da intention
			deleteQuestions(intention);
			
			this.httpService.post(command);
		}
	}
	
	/**
	 * @param intention
	 */
	private void deleteQuestions(Intention intention) {
		if(intention != null && intention.getQuestions() != null && intention.getQuestions().length > 0) {
			for (Question question : intention.getQuestions()) {
				Command command = BlipServiceUtil.createCommandDelete();
				command.setUri(LimeUri.parse(KBTSettings.BLIP_DELETE_QUESTIONS_URI.replace(LoaderConstants.ID_INTENTION, intention.getId()) + question.getId()));
								
				this.httpService.post(command);
			}
		}
		
	}

	/**
	 * @return
	 * @throws LoaderException 
	 */
	@SuppressWarnings("unchecked")
	public List<Entity> getEntities() throws LoaderException {
		Command command = BlipServiceUtil.createCommandGet();
		command.setUri(LimeUri.parse(KBTSettings.BLIP_GET_ENTITIES_URI));

		ResponseEntity<String> response = (ResponseEntity<String>) this.httpService.post(command);
		Command commandResp = (Command)serializer.deserialize(response.getBody().toString());
		
		if(CommandStatus.SUCCESS.equals(commandResp.getStatus())) {
			DocumentCollection doc = (DocumentCollection) commandResp.getResource();
			
			return (List<Entity>)(List<?>) Arrays.asList(doc.getItems());
		}
		else if(commandResp.getReason().getCode() == LoaderConstants.RESOURCE_NOT_FOUND){
			return null;
		}
		else {
			throw new LoaderException(commandResp.getReason().getDescription());
		}
	}
	
	/**
	 * @return
	 * @throws LoaderException
	 */
	@SuppressWarnings("unchecked")
	public List<PlainDocument> getResources() throws LoaderException {
	
		Command command = BlipServiceUtil.createCommandGet();
		command.setUri(LimeUri.parse(KBTSettings.BLIP_GET_RESOURCE_URI));
		command.setTo(null);

		ResponseEntity<String> response = this.httpService.post(command);
		Command commandRet = (Command)serializer.deserialize(response.getBody().toString());
		
		if(CommandStatus.SUCCESS.equals(commandRet.getStatus())) {
			DocumentCollection doc = (DocumentCollection) commandRet.getResource();
			
			return (List<PlainDocument>)(List<?>) Arrays.asList(doc.getItems());
		}
		else if(commandRet.getReason().getCode() == LoaderConstants.RESOURCE_NOT_FOUND){
			return null;
		}
		else {
			throw new LoaderException(commandRet.getReason().getDescription());
		}
	}
	/**
	 * @param idEntity
	 */
	public void deleteEntity(String idEntity) {
		if(!StringUtils.isEmpty(idEntity)) {
			Command command = BlipServiceUtil.createCommandDelete();
			command.setUri(LimeUri.parse(KBTSettings.BLIP_DELETE_ENTITY_URI + idEntity));
			
			this.httpService.post(command);
		}
	}
	
}
