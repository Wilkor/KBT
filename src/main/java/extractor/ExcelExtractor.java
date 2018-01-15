package extractor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.limeprotocol.PlainDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constant.ExtratorConstants;
import enums.MediaTypeEnum;
import exception.ImportExcelException;
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
	 * @param path
	 */
	public KnowledgeBase extractExcelData(File file) {
		try {
			workbook = ExcelUtil.getWorkbookByFile(file);

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
					EntityValues ev = ent.new EntityValues();
					List<Entity.EntityValues> entityValues = new ArrayList<>();

					List<Entity> entitiesList = kB.getEntities().stream().filter(b -> b.getName().equalsIgnoreCase(StringUtil.formatString(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY)))).collect(Collectors.toList());
					
					if(entitiesList != null && entitiesList.size() > 0) {
						ent = entitiesList.get(0);
						entityValues = Arrays.stream(ent.getValues()).collect(Collectors.toList());
					}
					else {
						ent.setName(StringUtil.formatString(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY)));
					}
					
					
					ev.setName(StringUtil.formatString(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY_VALUE)));

					ev.setSynonymous(
							ExcelUtil.getValuesTextBetweenColumns(workbook, row, ExtratorConstants.SINONIMOS_CELL_BEGIN,
									ExtratorConstants.SINONIMOS_CELL_END).stream().toArray(String[]::new));

					entityValues.add(ev);
					
					ent.setValues(entityValues.toArray(new Entity.EntityValues[entityValues.size()]));

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

				if (row != null && !ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_CONTENT_INTENTION).equals("")) {
					
					Intention in = new Intention();

					in.setName(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_CONTENT_INTENTION));

					in.setEntities(getEntityContent(row, in));

					in.setQuestions(extractExamples(in).stream().toArray(Question[]::new));

					in.setKey(makeKey(in, in.getEntities()));
					
					PlainDocument resource = new PlainDocument(ExcelUtil.getEscapedCellText(workbook, row,ExtratorConstants.CELL_INDEX_CONTENT_RESPOSTA),MediaTypeEnum.RESOURCE.getMediaTypeLime());
					in.setResource(resource );

					kb.add(in);
				}
			}
		}
	}

	/**
	 * @param intention
	 * @return
	 * @throws ImportExcelException
	 */
	private List<Question> extractExamples(Intention intention) throws ImportExcelException {

		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_INTENTION);

		List<Question> questions = null;

		if (sheet != null) {
			questions = new ArrayList<>();

			for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
				Row row = sheet.getRow(i);

				if (row != null) {

					String currentIntention = ExcelUtil
							.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_INTENTION_NAME).trim()
							.toLowerCase().trim();

					if (intention.getName().trim().toLowerCase().equals(currentIntention)) {

						Question q = new Question();
						q.setText(StringEscapeUtils.escapeHtml(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_EXAMPLE)));

						questions.add(q);
					}
				}
			}
		}

		return questions;
	}

	/**
	 * @param row
	 * @param intention
	 * @return
	 */
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

	/**
	 * @param in
	 * @param entities
	 * @return
	 */
	private static String makeKey(Intention in, List<Entity> entities) {

		String key;

		if (in.getEntities().size() == 0) {
			key = StringUtil.removeSpecialCharacters(in.getName()).replace(" ", "_");
		} else {
			key = StringUtil.removeSpecialCharacters(in.getName().toLowerCase().replace(" ", "_")) + ExtratorConstants.SEPARATION_CHAR + entities.stream()
					.map(n -> StringUtil.removeSpecialCharacters(n.getName().replace(" ", "_"))).collect(Collectors.joining(ExtratorConstants.SEPARATION_CHAR));
			;
		}

		return key;
	}

}
