package extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.KnowledgeBase;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
   public static void main( String[] args )
   {
       try {
    	   	ExcelExtractor excelExtractor = new ExcelExtractor();
   			KnowledgeBase kb = excelExtractor.extractExcelData("C:\\kb.xlsx");

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
   }
    
}
