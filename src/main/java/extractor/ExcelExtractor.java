package extractor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import constant.ExtratorConstants;
import exception.ImportExcelException;
import model.Entity;
import model.EntityValue;
import util.ExcelUtil;

/**
 * @author Keila Lacerda
 *
 */
public class ExcelExtractor {

	private Workbook workbook;
	private Map<String, EntityValue> mapEntityValues;
	
	
	/**
	 * @throws ImportExcelException
	 */
	public void convertEntityValues() throws ImportExcelException {
		
		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_SINONIMOS);
		
		if(sheet != null) {
			int rows = sheet.getPhysicalNumberOfRows();
			mapEntityValues = new HashMap<String, EntityValue>();
			
			//percorre todas as linhas da aba
			for(int i = 1; i<= rows;  i++) {
				Row row = sheet.getRow(i);
				
				if(row != null) {
					EntityValue entityValue = new EntityValue();
					entityValue.setName(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY_VALUE));
					entityValue.setSynonyms(ExcelUtil.getValuesTextBetweenColumns(workbook, row, ExtratorConstants.SINONIMOS_CELL_BEGIN, ExtratorConstants.SINONIMOS_CELL_END));
				
					mapEntityValues.put(entityValue.getName(), entityValue);
				}
			}
		}
		
	}
	
	/**
	 * @return
	 * @throws ImportExcelException
	 */
	public Map<String, Entity> convertEntity() throws ImportExcelException {
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
						mapEntities.put(entityName, entity);
					}
					
					mapEntities.get(entityName).getValues().add(mapEntityValues.get(entityValueName));
				}
			}
		}
		
		return mapEntities;
	}
	
}
