package extractor;

import org.limeprotocol.serialization.JacksonEnvelopeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import controller.KBTLoadController;
import model.Content;
import model.Intention;
import model.KnowledgeBase;
import service.HttpService;
import setting.KBTSettings;
import validator.Validator;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		try {
			
			ExcelExtractor excelExtractor = new ExcelExtractor();
			KnowledgeBase kb = excelExtractor.extractExcelData("C:\\kb.xlsx");
			
			Validator validator = new Validator();
			
			if (!validator.validate(kb)) {
				LOGGER.info("Can not complete KB import", kb, kb);
			}
			
			//TODO TESTE LOAD
//			KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");
//
//			HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
//			KBTLoadController controller = new KBTLoadController(httpService, new JacksonEnvelopeSerializer());
//			getAll(controller);
			
			//TODO TESTE EXTRATOR
//			KnowledgeBase kb = excelExtractor.extractExcelData("C:\\ZUP\\Santander\\ChatBots\\kb.xlsx");
//			Gson gson = new Gson();
//			gson.toJson(kb, new FileWriter("C:\\ZUP\\Santander\\ChatBots\\fileKBT.json"));
			
		} catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
			// controller.loadIntention(intention);

   }

	//TODO REMOVER APÓS TESTES
	protected static void loadIntention(KBTLoadController controller) {
		
		Intention intention = new Intention();
		intention.setName("Teste I4");
		controller.loadIntention(intention);
	}

	//TODO REMOVER APÓS TESTES
	protected static void loadEntity(KBTLoadController controller) {
		
//		Entity entity = new Entity("Entity 9", "key 9");
//		Set<EntityValue> values = new HashSet<>();
//		EntityValue e = new EntityValue();
//		e.setCategory("test cat");
//		List<String> synonyms = new ArrayList<>();
//		synonyms.add("sin 11");
//		synonyms.add("sin 12");
//		e.setSynonyms(synonyms);
//		e.setName("Val 1");
//		values.add(e);
//		entity.setValues(values);
//		controller.loadEntity(entity);

	}

	//TODO REMOVER APÓS TESTES
	protected static void loadContent(KBTLoadController controller) {
		
		Content content = new Content();
		content.setValue("Teste Resource");

		controller.loadContent(content);

	}

	//TODO REMOVER APÓS TESTES
	protected static void getAll(KBTLoadController controller) {
		
		controller.getIntentions();
//		controller.getEntities();		
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
