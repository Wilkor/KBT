package controller;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.limeprotocol.Command;
import org.limeprotocol.LimeUri;
import org.limeprotocol.messaging.contents.PlainText;
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
 * Classe respons�vel por controlar o Load dos dados da base de conhecimento no
 * Blip
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
	 * Realiza o cadastramento de uma Inten��o no Blip
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
	 * Realiza o cadastramento de um Recurso/Conte�do no Blip
	 * 
	 * @param content
	 */
	public void loadContent(Content content) {
		Command command = BlipServiceUtil.createCommandSet();
		// TODO O que concatenar na URI????
		command.setUri(LimeUri.parse(KBTSettings.BLIP_SET_RESOURCE_URI));

		PlainText text = new PlainText(content.getValue());
		command.setResource(text);

		this.httpService.post(command);
	}

	@SuppressWarnings("unchecked")
	public void getIntentions() {
		try {
			Command command = BlipServiceUtil.createCommandGet();
			command.setUri(LimeUri.parse(KBTSettings.BLIP_GET_INTENTIONS_URI));

			ResponseEntity<String> response = (ResponseEntity<String>) this.httpService.post(command);

			if (response.getStatusCode() == HttpStatus.OK) {

				System.out.println(response);

				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

				objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
				objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

				objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
				objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
				
				model.Command cmd = objectMapper.readValue(response.getBody().toString(), model.Command.class);
				
//				Envelope envelope = new JacksonEnvelopeSerializer().deserialize(response.getBody().toString());
//				Command commandResp = (Command)envelope;
//				Document doc = commandResp.getResource();
				
				System.out.println(cmd.getId());
				
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(intentions.get(0).getName());
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
	
}
