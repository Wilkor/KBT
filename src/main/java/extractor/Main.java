package extractor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import controller.KBTLoadController;
import model.Content;
import model.Entity;
import model.EntityValue;
import model.Intention;
import service.HttpService;
import setting.KBTSettings;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
   public static void main( String[] args )
   {
       try {
//    	   	ExcelExtractor excelExtractor = new ExcelExtractor();
//   			KnowledgeBase kb = excelExtractor.extractExcelData("C:\\kb.xlsx");
//   			Gson gson = new Gson();
//   			gson.toJson(kb, new FileWriter("C:\\fileKBT.json"));
    	   
    	   
    	   loadEntity();
    	   
    	   
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
   }
   
   private static void loadIntention() {
	   KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");
	   
	   HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
	   KBTLoadController controller = new KBTLoadController(httpService );
	   Intention intention = new Intention();
	   intention.setName("Teste I4");
	   controller.loadIntention(intention);
   }
   
   private static void loadEntity() {
	   KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");
	   
	   HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
	   KBTLoadController controller = new KBTLoadController(httpService );
	   
	   Entity entity = new Entity("Entity 9", "key 9");
	   Set<EntityValue> values = new HashSet<>();
	   EntityValue e = new EntityValue();
	   e.setCategory("test cat");
	   List<String> synonyms = new ArrayList<>();
	   synonyms.add("sin 11");
	   synonyms.add("sin 12");
	   e.setSynonyms(synonyms);
	   e.setName("Val 1");
	   values.add(e);
	   entity.setValues(values);
	   controller.loadEntity(entity);

   }
   
   private static void loadContent() {
	   KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");
	   
	   HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
	   KBTLoadController controller = new KBTLoadController(httpService );
	   
	   Content content = new Content();
	   content.setValue("Teste Resource");
	   
	   controller.loadContent(content);


   }
    
}
