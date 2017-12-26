package extractor;

import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import model.KnowledgeBase;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExtractor.class);
    
   public static void main( String[] args )
   {
       try {
    	   	ExcelExtractor excelExtractor = new ExcelExtractor();
   			KnowledgeBase kb = excelExtractor.extractExcelData("C:\\ZUP\\Santander\\ChatBots\\kb.xlsx");
   			Gson gson = new Gson();
   			gson.toJson(kb, new FileWriter("C:\\ZUP\\Santander\\ChatBots\\fileKBT.json"));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
   }
    
}
