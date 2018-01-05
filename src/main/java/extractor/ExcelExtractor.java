package extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constant.ExtratorConstants;
import exception.ImportExcelException;
import model.Content;
import model.EntityValue;
import model.Intention;
import model.KnowledgeBase;
import net.take.iris.messaging.resources.artificialIntelligence.Entity;
import net.take.iris.messaging.resources.artificialIntelligence.Entity.EntityValues;
import net.take.iris.messaging.resources.artificialIntelligence.Question;
import util.ExcelUtil;
import util.StringUtil;

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

			extractEntity(kb);
			// extractIntention(kb);
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
	public void extractEntity(KnowledgeBase kB) throws ImportExcelException {

		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_ENTIDADES);

		if (sheet != null) {
			int rows = sheet.getPhysicalNumberOfRows();

			for (int i = 1; i <= rows; i++) {
				Row row = sheet.getRow(i);

				if (row != null) {

					Entity ent = new Entity();

					ent.setName(StringUtil.removeSpecialCharacters(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY)));

					EntityValues ev = ent.new EntityValues();
					ev.setName(StringUtil.removeSpecialCharacters(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY_VALUE)));

					ev.setSynonymous(
							ExcelUtil.getValuesTextBetweenColumns(workbook, row, ExtratorConstants.SINONIMOS_CELL_BEGIN,
									ExtratorConstants.SINONIMOS_CELL_END).stream().toArray(String[]::new));

					EntityValues[] evs = { ev };
					ent.setValues(evs);

					kB.add(ent);

				}
			}

		}

	}

	/**
	 * @param kb
	 * @throws ImportExcelException
	 */
	public void extractContent(KnowledgeBase kb) throws ImportExcelException {

		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_CONTENT);

		if (sheet != null) {
			int rows = sheet.getPhysicalNumberOfRows();

			for (int i = 1; i <= rows; i++) {
				Row row = sheet.getRow(i);

				if (row != null) {

					Intention in = new Intention();

					in.setName(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_CONTENT_INTENTION));

					in.setEntities(getEntityContent(row, in));

					in.setQuestions(extractExamples(in).stream().toArray(Question[]::new));

					in.setKey(makeKey(in, in.getEntities()));

					// String intermediateResponse = ExcelUtil.getCellText(workbook, row,
					// ExtratorConstants.CELL_INDEX_CONTENT_RESPOSTA_INTERMEDIARIA);
					//
					// if (StringUtils.isEmpty(intermediateResponse)
					// || intermediateResponse.equalsIgnoreCase(ExtratorConstants.NAO)) {
					//
					// content.setValue(
					// ExcelUtil.getCellText(workbook, row,
					// ExtratorConstants.CELL_INDEX_CONTENT_RESPOSTA));
					//
					// content.setIntermediate(false);
					// } else {
					// content.setValue(intermediateResponse);
					// content.setIntermediate(true);
					// }

					kb.add(in);
				}
			}
		}
	}

	private List<Question> extractExamples(Intention intention) throws ImportExcelException {

		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_INTENTION);

		List<Question> questions = null;

		if (sheet != null) {
			questions = new ArrayList<>();

			for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
				Row row = sheet.getRow(i);

				if (row != null) {

					String currentIntention = StringUtil.removeSpecialCharacters(ExcelUtil
							.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_INTENTION_NAME).trim()
							.toLowerCase());

					if (intention.getName().trim().toLowerCase().equals(currentIntention)) {

						Question q = new Question();
						q.setText(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_EXAMPLE));

						questions.add(q);
					}
				}
			}
		}

		return questions;
	}

	/**
	 * @param row
	 * @param mapEntityValues
	 * @return
	 */
	private List<EntityValue> getEntityValuesContent(Row row, Map<String, EntityValue> mapEntityValues) {
		List<EntityValue> listReturn = new ArrayList<EntityValue>();

		if (workbook != null && row != null) {
			for (int i = ExtratorConstants.ENTITIES_VALUE_CONTENT_CELL_BEGIN; i <= ExtratorConstants.ENTITIES_VALUE_CONTENT_CELL_END; i++) {
				listReturn.add(mapEntityValues.get(StringUtils.lowerCase(ExcelUtil.getCellText(workbook, row, i))));
			}
		}
		return listReturn;
	}

	private List<Entity> getEntityContent(Row row, Intention intention) {
		List<Entity> ret = new ArrayList<Entity>();

		if (workbook != null && row != null) {
			for (int i = ExtratorConstants.ENTITIES_VALUE_CONTENT_CELL_BEGIN; i <= ExtratorConstants.ENTITIES_VALUE_CONTENT_CELL_END; i++) {

				String entity = StringUtils.lowerCase(ExcelUtil.getCellText(workbook, row, i)).trim();

				if (entity != null && !entity.equals("")) {
					Entity e = new Entity();
					e.setName(entity);
					
					ret.add(e);
				}

			}
		}
		return ret;
	}

	private static String makeKey(Intention in, List<Entity> entities) {

		String key;

		if (in.getEntities().size() == 0) {
			key = StringUtil.removeSpecialCharacters(in.getName());
		} else {
			key = in.getName().toLowerCase() + "_" + entities.stream()
					.map(n -> StringUtil.removeSpecialCharacters(n.getName())).collect(Collectors.joining("_"));
			;
		}

		return key;
	}

}
