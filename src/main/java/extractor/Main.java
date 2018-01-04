package extractor;

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

//	private static Validator validator;
//
//	@Inject
//	public Main(Validator validator) {
//		Main.validator = validator;
//	}

	public static void main(String[] args) {
		try {
			
			ExcelExtractor excelExtractor = new ExcelExtractor();
			KnowledgeBase kb = excelExtractor.extractExcelData("C:\\kb.xlsx");
			
			Validator validator = new Validator();
			
			if (!validator.validate(kb)) {
				LOGGER.info("Can not complete KB import", kb, kb);
			}
//			getAll();
			
//			KnowledgeBase kb = excelExtractor.extractExcelData("C:\\ZUP\\Santander\\ChatBots\\kb.xlsx");
//			Gson gson = new Gson();
//			gson.toJson(kb, new FileWriter("C:\\ZUP\\Santander\\ChatBots\\fileKBT.json"));
			
		} catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
			// controller.loadIntention(intention);

   }

	protected static void loadIntention() {
		KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");

		HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
		KBTLoadController controller = new KBTLoadController(httpService);
		Intention intention = new Intention();
		intention.setName("Teste I4");
		controller.loadIntention(intention);
	}

	protected static void loadEntity() {
		KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");

		HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
		KBTLoadController controller = new KBTLoadController(httpService);

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

	protected static void loadContent() {
		KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");

		HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
		KBTLoadController controller = new KBTLoadController(httpService);

		Content content = new Content();
		content.setValue("Teste Resource");

		controller.loadContent(content);

	}

	protected static void getAll() {
		KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");

		HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
		KBTLoadController controller = new KBTLoadController(httpService);

//		controller.getIntentions();
		controller.getEntities();		
	}
	

	protected static void deleteOne() {
		KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");

		HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
		KBTLoadController controller = new KBTLoadController(httpService);

//		controller.deleteIntention("order_pizza2");
		controller.deleteEntity("entity_1");
		
	}
}
