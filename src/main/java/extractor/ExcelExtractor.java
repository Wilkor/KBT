package extractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import constant.ExtratorConstants;
import exception.ImportExcelException;
import model.EntityValue;
import util.ExcelUtil;

/**
 * @author Keila Lacerda
 *
 */
public class ExcelExtractor {

	private Workbook workbook;
	private Map<String, List<String>> mapEntityValues;
	
	
	/**
	 * @throws ImportExcelException
	 */
	public void convertEntityValues() throws ImportExcelException {
		
		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_SINONIMOS);
		
		if(sheet != null) {
			int rows = sheet.getPhysicalNumberOfRows();
			mapEntityValues = new HashMap<String, List<String>>();
			
			//percorre todas as linhas da aba
			for(int i = 1; i<= rows;  i++) {
				Row row = sheet.getRow(i);
				
				if(row != null) {
					EntityValue entityValue = new EntityValue();
					entityValue.setName(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY));
					entityValue.setSynonyms(ExcelUtil.getValuesTextBetweenColumns(workbook, row, ExtratorConstants.SINONIMOS_CELL_BEGIN, ExtratorConstants.SINONIMOS_CELL_END));
				
					mapEntityValues.put(entityValue.getName(), entityValue.getSynonyms());
				}
			}
		}
		
	}
	
}
