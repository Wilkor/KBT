package extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.KnowledgeBase;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExtractor.class);
    
   public static void main( String[] args )
   {
       try {
    	   	ExcelExtractor excelExtractor = new ExcelExtractor();
   			KnowledgeBase kb = excelExtractor.extractExcelData("C:\\ZUP\\Santander\\ChatBots\\20171219_Base de conhecimento - Cartões, Canais, Conta, Segurança.xlsx");

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
   }
    
}
