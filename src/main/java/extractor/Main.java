package extractor;

import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import model.KnowledgeBase;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
   public static void main( String[] args )
   {
       try {
    	   	ExcelExtractor excelExtractor = new ExcelExtractor();
   			KnowledgeBase kb = excelExtractor.extractExcelData("C:\\kb.xlsx");
   			Gson gson = new Gson();
   			gson.toJson(kb, new FileWriter("C:\\fileKBT.json"));
    	   
//    	   KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");
//    	   
//    	   HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
//    	   KBTLoadController controller = new KBTLoadController(httpService );
//    	   Intention intention = new Intention();
//    	   intention.setName("Teste I3");
//    	   controller.loadIntention(intention);
    	   
    	   
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
   }
    
}
