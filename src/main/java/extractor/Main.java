package extractor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.limeprotocol.serialization.JacksonEnvelopeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import constant.ExtratorConstants;
import controller.KBTLoadController;
import enums.StatusFileEnum;
import model.KnowledgeBase;
import service.HttpService;
import setting.KBTSettings;
import validator.Validator;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Path filePath = initProcessKBT();
		
		try {
			//Extrai os dados da planilha
			ExcelExtractor excelExtractor = new ExcelExtractor();
			KnowledgeBase kb = excelExtractor.extractExcelData(filePath.toFile());
			
			//Valida aplicando regras de negócio
			Validator validator = new Validator();
			
			if (!validator.validate(kb)) {
				LOGGER.info("Não foi possível completar a atualização da base.", kb, kb);
				moveFileToRejected(filePath);
			}
			else {
				//Insere os dados no Blip
				KBTSettings kbtSettings = new KBTSettings("Ym90d2g6MVRocWhBa2xvWTdxMHo2d2dCOTQ=");
				HttpService httpService = new HttpService(new RestTemplate(), kbtSettings);
				KBTLoadController controller = new KBTLoadController(httpService, new JacksonEnvelopeSerializer());
				
				controller.loadBase(kb,LOGGER);
				
				moveFileToProcessed(filePath);
				LOGGER.info("Processo de atualização de base concluído!");
			}
			
		} catch (Exception e) {
			moveFileToRejected(filePath);
            LOGGER.error(e.getMessage());
        }

   }
	
	/**
	 * Move arquivo para a pasta Processando e loga início do processo
	 */
	private static Path initProcessKBT() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmm");
		String fileRename = StatusFileEnum.PROCESSING.getPathFile() + "kb_" + df.format(new Date()) + ".xlsx";
		
		Path movefrom = FileSystems.getDefault().getPath(ExtratorConstants.FILE_PATH);
		Path targetProcessing = FileSystems.getDefault().getPath(fileRename);
		
        try {
            Files.move(movefrom, targetProcessing, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Início do processo de atualização da base de conhecimento");
            LOGGER.info("Arquivo movido para a pasta 'Processando.");
        } catch (IOException e) {
            System.err.println(e);
        }
        
        return targetProcessing;
	}
	
	/**
	 * @param pathFile
	 */
	private static void moveFileToRejected(Path pathFile) {
		String fileRename = StatusFileEnum.REJECTED.getPathFile() + pathFile.getFileName().toString();
		Path pathToMove = FileSystems.getDefault().getPath(fileRename );
		
        try {
            Files.move(pathFile, pathToMove, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Falha ao processar arquivo. Arquivo movido para a pasta 'Rejeitados'");
        } catch (IOException e) {
            System.err.println(e);
        }
	}
	
	
	/**
	 * @param pathFile
	 */
	private static void moveFileToProcessed(Path pathFile) {
		String fileRename = StatusFileEnum.PROCESSED.getPathFile() + pathFile.getFileName().toString();
		Path pathToMove = FileSystems.getDefault().getPath(fileRename );
		
        try {
            Files.move(pathFile, pathToMove, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Processo de importação de dados concluído.");
            LOGGER.info("Arquivo movido para a pasta 'Processados'.");
        } catch (IOException e) {
            System.err.println(e);
        }
	}

}
