package extractor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.limeprotocol.serialization.JacksonEnvelopeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import controller.KBTLoadController;
import exception.LoaderException;
import model.Content;
import model.EntityValue;
import model.Intention;
import model.KnowledgeBase;
import net.take.iris.messaging.resources.artificialIntelligence.Entity;
import net.take.iris.messaging.resources.artificialIntelligence.Entity.EntityValues;
import service.HttpService;
import setting.KBTSettings;
import validator.Validator;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		try {
			
			//Extrai os dados da planilha
			ExcelExtractor excelExtractor = new ExcelExtractor();
			KnowledgeBase kb = excelExtractor.extractExcelData("C:\\KB\\kb-Atualizado.xlsx");
			
			//Valida aplicando regras de negócio
			Validator validator = new Validator();
			
			if (!validator.validate(kb)) {
				LOGGER.info("Can not complete KB import", kb, kb);
			}
			else {
				//Insere os dados no Blip
				KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");
				HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
				KBTLoadController controller = new KBTLoadController(httpService, new JacksonEnvelopeSerializer());
				
				controller.loadBase(kb);
//				loadContent(controller);
			}

			//TODO TESTE EXTRATOR
//			KnowledgeBase kb = excelExtractor.extractExcelData("C:\\ZUP\\Santander\\ChatBots\\kb.xlsx");
//			Gson gson = new Gson();
//			gson.toJson(kb, new FileWriter("C:\\ZUP\\Santander\\ChatBots\\fileKBT.json"));
			
		} catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

   }

	//TODO REMOVER APÓS TESTES
	protected static void loadIntention(KBTLoadController controller) {
		
		Intention intention = new Intention();
		intention.setName("Teste I4");
		controller.loadIntention(intention);
	}

	//TODO REMOVER APÓS TESTES
	protected static void loadEntity(KBTLoadController controller) {
		
		Entity entity = new Entity();
		entity.setName("Entity 11");
		Set<EntityValues> values = new HashSet<>();
		EntityValues e = entity.new EntityValues();
		e.setName("EV 11");
		
		List<String> synonyms = new ArrayList<>();
		synonyms.add("sin 11.1");
		synonyms.add("sin 11.2");
		String [] array = new String [synonyms.size()];
		e.setSynonymous(synonyms.toArray(array));
		e.setName("Val 1");
		values.add(e);
		
		EntityValues [] ev = new EntityValues [values.size()];
		entity.setValues(values.toArray(ev));
		controller.loadEntity(entity);

	}

	//TODO REMOVER APÓS TESTES
	protected static void loadContent(KBTLoadController controller) {
		
		Content content = new Content(); 
		content.setValue(StringEscapeUtils.escapeHtml("Teste Resource bão de mais pra menos"));
		Intention intention = new Intention();
		intention.setId("Teste_Content6");
		intention.setName("Teste Content6");
		content.setIntention(intention );
		List<EntityValue> entityValues = new ArrayList<>();
		EntityValue e = new EntityValue();
		e.setName("Value Content6");
		Entity entity = new Entity();
		entity.setId("Entity_Content6");
		e.setEntity(entity );
		entityValues.add(e);
		content.setEntityValues(entityValues );
		controller.loadContent(content);

	}

	//TODO REMOVER APÓS TESTES
	protected static void getAll(KBTLoadController controller) throws LoaderException {
		
//		controller.getIntentions();
		controller.getEntities();		
	}
	

	//TODO REMOVER APÓS TESTES
	protected static void deleteOne() {
		KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");

		HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
		KBTLoadController controller = new KBTLoadController(httpService);

//		controller.deleteIntention("order_pizza2");
		controller.deleteEntity("entity_1");
		
	}
}
