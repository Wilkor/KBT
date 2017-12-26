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
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
   }
    
}
