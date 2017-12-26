package extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constant.ExtratorConstants;
import exception.ImportExcelException;
import model.Content;
import model.Entity;
import model.EntityValue;
import model.Intention;
import model.KnowledgeBase;
import util.ExcelUtil;
import validator.EntityValidator;

/**
 * @author Keila Lacerda
 *
 */
public class ExcelExtractor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExtractor.class);
	
	private Workbook workbook;
		
	/**
	 * @param path
	 */
	public KnowledgeBase extractExcelData(String path) {
		try {
			workbook = ExcelUtil.getWorkbookByUrl(path);
			
			KnowledgeBase kb = new KnowledgeBase();
			
			extractEntityValues(kb);
			extractEntity(kb);
			extractIntention(kb);
			extractContent(kb);
			
			return kb;
			
		} catch (ImportExcelException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * @param kB 
	 * @throws ImportExcelException
	 */
	public void extractEntityValues(KnowledgeBase kB) throws ImportExcelException {
		
		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_SINONIMOS);
		
		if(sheet != null) {
			int rows = sheet.getPhysicalNumberOfRows();
			Map<String, EntityValue> mapEntityValues = new HashMap<String, EntityValue>();
			
			//percorre todas as linhas da aba
			for(int i = 1; i<= rows;  i++) {
				Row row = sheet.getRow(i);
				
				if(row != null) {
					EntityValue entityValue = new EntityValue();
					
					entityValue.setName(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY_VALUE));
					
					entityValue.setSynonyms(ExcelUtil.getValuesTextBetweenColumns(workbook, row, ExtratorConstants.SINONIMOS_CELL_BEGIN, ExtratorConstants.SINONIMOS_CELL_END));
					
					entityValue.setEntity(new Entity(entityValue.getName()));
					
					entityValue.setCategory(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_CATEGORY_VALUE));
					
					mapEntityValues.put(entityValue.getName(), entityValue);
					
					kB.add(entityValue);
				
					mapEntityValues.put(StringUtils.lowerCase(entityValue.getName()), entityValue);
				}
			}
			
			kB.setMapEntityValues(mapEntityValues);
			EntityValidator evalid = new EntityValidator();
			evalid.validate(kB);
		}
		
	}
	
	/**
	 * @param kb 
	 * @throws ImportExcelException
	 */
	public void extractEntity(KnowledgeBase kb) throws ImportExcelException {
		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_SINONIMOS);
		Map<String, Entity> mapEntities = new HashMap<String, Entity>();
		
		if(sheet != null) {
			int rows = sheet.getPhysicalNumberOfRows();
			
			//percorre todas as linhas da aba
			for(int i = 1; i<= rows;  i++) {
				Row row = sheet.getRow(i);
				
				if(row != null) {
					String entityName = ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY);
					String entityValueName = ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY_VALUE);
					Entity entity = null;
					
					if(!mapEntities.containsKey(entityName)) {
						entity = new Entity();
						entity.setName(entityName);
						entity.setValues(new HashSet<EntityValue>());
						mapEntities.put(StringUtils.lowerCase(entityName), entity);
					}
					
					mapEntities.get(StringUtils.lowerCase(entityName)).getValues().add(kb.getMapEntityValues().get(StringUtils.lowerCase(entityValueName)));
				}
			}
		}
		
		kb.setMapEntities(mapEntities);
	}
	
	
	/**
	 * @param kb 
	 * @throws ImportExcelException
	 */
	public void extractIntention(KnowledgeBase kb) throws ImportExcelException {
		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_INTENTION);
		Map<String, Intention> mapIntentions = new HashMap<String, Intention>();
		
		if(sheet != null) {
			int rows = sheet.getPhysicalNumberOfRows();
			
			//percorre todas as linhas da aba
			for(int i = 1; i<= rows;  i++) {
				Row row = sheet.getRow(i);
				
				if(row != null) {
					String intentionName = ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_INTENTION_NAME);
					Intention intention = null;
					
					//Se a inten��o ainda n�o estiver no map cria e adiciona
					if(!mapIntentions.containsKey(intentionName)) {
						intention = new Intention();
						intention.setName(intentionName);
						intention.setExamples(new ArrayList<String>());
						mapIntentions.put(StringUtils.lowerCase(intentionName), intention);
					}
					
					mapIntentions.get(StringUtils.lowerCase(intentionName)).getExamples().add(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_EXAMPLE));
				}
			}
		}
		
		kb.setMapIntentions(mapIntentions);
	}
	
	/**
	 * @param kb 
	 * @throws ImportExcelException
	 */
	public void extractContent(KnowledgeBase kb) throws ImportExcelException {
		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_CONTENT);
		List<Content> contentList = new ArrayList<Content>();
		
		if(sheet != null) {
			int rows = sheet.getPhysicalNumberOfRows();
			
			//percorre todas as linhas da aba
			for(int i = 1; i<= rows;  i++) {
				Row row = sheet.getRow(i);
				
				if(row != null) {
					Content content = new Content();
					content.setIntention(kb.getMapIntentions().get(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_CONTENT_INTENTION)));
					
					//TODO VALIDAR quest�o da pergunta intermedi�ria
					String intermediateResponse = ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_CONTENT_RESPOSTA_INTERMEDIARIA);
					
					if(StringUtils.isEmpty(intermediateResponse) || intermediateResponse.equalsIgnoreCase(ExtratorConstants.NAO)) {
						content.setValue(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_CONTENT_RESPOSTA));
						content.setIntermediate(false);
					}
					else {
						content.setValue(intermediateResponse);
						content.setIntermediate(true);
					}
					content.setEntityValues(getEntityValuesContent(row, kb.getMapEntityValues()));
					
					contentList.add(content);
				}
			}
		}
		
		kb.setContentList(contentList);
	}
	
	/**
	 * @param row
	 * @param mapEntityValues 
	 * @return
	 */
	private List<EntityValue> getEntityValuesContent(Row row, Map<String, EntityValue> mapEntityValues) {
		List<EntityValue> listReturn = new ArrayList<EntityValue>();
		  
		  if(workbook != null && row != null) {
			  for (int i = ExtratorConstants.ENTITIES_VALUE_CONTENT_CELL_BEGIN; i <= ExtratorConstants.ENTITIES_VALUE_CONTENT_CELL_END; i++) {
				  listReturn.add(mapEntityValues.get(StringUtils.lowerCase(ExcelUtil.getCellText(workbook, row, i))));				
			}
		  }
		return listReturn;
	}
	
}
