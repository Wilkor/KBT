package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import exception.ImportExcelException;

public class ExcelUtil {

	/**
	 * Abre um arquivo de acordo com a url passada.
	 * 
	 * @param urlFile
	 *            caminho completo onde o arquivo se encontra
	 * @throws ImportExcelException
	 *             Exceção específica de importação
	 */
	public static Workbook getWorkbookByUrl(String urlFile) throws ImportExcelException {
		try {
			File arquivo = new File(urlFile);
			return getFileByInputStream(new FileInputStream(arquivo));
		} catch (FileNotFoundException exp) {
			throw new ImportExcelException("Não foi possível encontrar o arquivo: " + urlFile);
		} catch (ImportExcelException exp) {
			throw new ImportExcelException("Falha ao abrir arquivo: " + urlFile);
		}
	}

	/**
	 * @param file
	 * @return
	 * @throws ImportExcelException
	 */
	public static Workbook getWorkbookByFile(File file) throws ImportExcelException {
		try {
			return getFileByInputStream(new FileInputStream(file));
		} catch (FileNotFoundException exp) {
			throw new ImportExcelException("Não foi possível encontrar o arquivo: " + file.getAbsolutePath());
		} catch (ImportExcelException exp) {
			throw new ImportExcelException("Falha ao abrir arquivo: " + file.getAbsolutePath());
		}
	}
	
	/**
	 * Abre arquivo a ser importado.
	 * 
	 * @param inputStream
	 *            stream de entrada
	 * @throws ImportExcelException
	 *             exceção específica
	 */
	public static Workbook getFileByInputStream(InputStream inputStream) throws ImportExcelException {
		try {
			OPCPackage pkg = OPCPackage.open(inputStream);
			return new XSSFWorkbook(pkg);
		} catch (InvalidFormatException exp) {
			throw new ImportExcelException("Formato do arquivo inválido.");
		} catch (IOException exp) {
			throw new ImportExcelException("Falha ao processar arquivo.");
		}
	}

	/**
	 * Recupera aba pelo nome.
	 * 
	 * @param sheetName
	 *            nome da aba
	 * @return aba recuperada
	 * @throws ImportExcelException
	 *             exceção específica
	 */
	public static Sheet getSheetByName(Workbook workbook, String sheetName) throws ImportExcelException {

		Sheet aba = null;
		if (sheetName != null && !sheetName.isEmpty()) {
			aba = workbook.getSheet(sheetName.trim());

			if (aba == null) {
				throw new ImportExcelException("Não foi encontrada a aba " + sheetName);
			}
		}

		return aba;
	}

	public static String getCellText(Workbook workbook, Row row, int cellIndex) {
		return getCellValueGeneric(workbook, row, cellIndex).toString();
	}

	public static String getEscapedCellText(Workbook workbook, Row row, int cellIndex) {
		return StringEscapeUtils.escapeHtml(getCellValueGeneric(workbook, row, cellIndex).toString());
	}
	
	/**
	 * Obtém o valor da célula independente do tipo de célula.
	 * 
	 * @param row
	 *            linha atual
	 * @param cellIndex
	 *            coluna atual
	 * @return valor da célula
	 */
	public static Object getCellValueGeneric(Workbook workbook, Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);

		if (cell == null) {
			return StringUtils.EMPTY;
		}

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			return StringUtils.EMPTY;

		case Cell.CELL_TYPE_BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());

		case Cell.CELL_TYPE_ERROR:
			return "error";

		case Cell.CELL_TYPE_FORMULA:
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			CellValue value = null;

			try {
				value = evaluator.evaluate(cell);
			} catch (RuntimeException exp) {
				return null;
			}

			if (value != null) {
				if (value.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					return numberFormatter(cell.getNumericCellValue());
				}
			}

			if (value.getCellType() == Cell.CELL_TYPE_STRING) {
				return value.getStringValue();
			}

			return "";

		case Cell.CELL_TYPE_NUMERIC:
			return numberFormatter(cell.getNumericCellValue());

		case Cell.CELL_TYPE_STRING:
			return cell.getRichStringCellValue().getString();

		default:
			return null;
		}
	}

	/**
	 * Formata double com 2 casas decimais, mantendo o separador com ponto(.)
	 * 
	 * @param number
	 *            número a ser formatado
	 * @return número formatado
	 */
	private static Double numberFormatter(Double number) {
		DecimalFormat df = new DecimalFormat("#.##");
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(dfs);

		return Double.valueOf(df.format(number));
	}

	/**
	 * Retorna uma lista de objetos recuperados em uma linha entre as colunas
	 * informadas.
	 * 
	 * @param workbook
	 * @param row
	 * @param cellIndexBegin
	 * @param cellIndexEnd
	 * @return
	 */
	public static List<Object> getValuesBetweenColumns(Workbook workbook, Row row, int cellIndexBegin,
			int cellIndexEnd) {
		List<Object> listReturn = new ArrayList<Object>();

		if (workbook != null && row != null) {
			for (int i = cellIndexBegin; i <= cellIndexEnd; i++) {
				listReturn.add(getCellValueGeneric(workbook, row, i));
			}
		}
		return listReturn;
	}

	/**
	 * Retorna uma lista de Strings recuperados em uma linha entre as colunas
	 * informadas.
	 * 
	 * @param workbook
	 * @param row
	 * @param cellIndexBegin
	 * @param cellIndexEnd
	 * @return
	 */
	public static List<String> getValuesTextBetweenColumns(Workbook workbook, Row row, int cellIndexBegin,
			int cellIndexEnd) {
		List<String> listReturn = new ArrayList<String>();

		if (workbook != null && row != null) {
			for (int i = cellIndexBegin; i <= cellIndexEnd; i++) {
				String cellValue = getCellValueGeneric(workbook, row, i).toString();
				if(!org.limeprotocol.util.StringUtils.isNullOrEmpty(cellValue)) {
					listReturn.add(getCellValueGeneric(workbook, row, i).toString());
				}
			}
		}
		return listReturn;
	}

}
