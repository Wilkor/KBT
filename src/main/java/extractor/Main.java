package extractor;

import org.limeprotocol.serialization.JacksonEnvelopeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import controller.KBTLoadController;
import model.KnowledgeBase;
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
			}
			
		} catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

   }

}
