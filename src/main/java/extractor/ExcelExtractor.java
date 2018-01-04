package extractor;

import java.util.ArrayList;
import java.util.HashMap;
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
import model.EntityValue;
import model.Intention;
import model.KnowledgeBase;
import net.take.iris.messaging.resources.artificialIntelligence.Entity;
import net.take.iris.messaging.resources.artificialIntelligence.Entity.EntityValues;
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

			extractEntityValues(kb);
//			extractEntity(kb);
//			extractIntention(kb);
//			extractContent(kb);

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

		if (sheet != null) {
			int rows = sheet.getPhysicalNumberOfRows();

			for (int i = 1; i <= rows; i++) {
				Row row = sheet.getRow(i);

				if (row != null) {

					Entity ent = new Entity();

					ent.setName(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY));

					EntityValues ev = ent.new EntityValues();
					ev.setName(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY_VALUE));

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
	public void extractEntity(KnowledgeBase kb) throws ImportExcelException {
		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_SINONIMOS);
		Map<String, Entity> mapEntities = new HashMap<String, Entity>();

		if (sheet != null) {
			int rows = sheet.getPhysicalNumberOfRows();

			// percorre todas as linhas da aba
			for (int i = 1; i <= rows; i++) {
				Row row = sheet.getRow(i);

				if (row != null) {
					String entityName = ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_ENTITY);
					String entityValueName = ExcelUtil.getCellText(workbook, row,
							ExtratorConstants.CELL_INDEX_ENTITY_VALUE);
					Entity entity = null;

					if (!mapEntities.containsKey(entityName)) {
						entity = new Entity();
						entity.setName(entityName);
						// entity.setValues(new HashSet<EntityValue>());
						mapEntities.put(StringUtils.lowerCase(entityName), entity);
					}

					// mapEntities.get(StringUtils.lowerCase(entityName)).getValues()
					// .add(kb.getMapEntityValues().get(StringUtils.lowerCase(entityValueName)));
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

		if (sheet != null) {
			int rows = sheet.getPhysicalNumberOfRows();

			// percorre todas as linhas da aba
			for (int i = 1; i <= rows; i++) {
				Row row = sheet.getRow(i);

				if (row != null) {
					String intentionName = ExcelUtil
							.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_INTENTION_NAME).trim()
							.toLowerCase();
					Intention intention = null;

					// Se a intencao ainda nao estiver no map cria e adiciona
					if (!mapIntentions.containsKey(intentionName)) {
						intention = new Intention();
						intention.setName(intentionName);
						intention.setExamples(new ArrayList<String>());
						mapIntentions.put(StringUtils.lowerCase(intentionName), intention);
					}

					mapIntentions.get(StringUtils.lowerCase(intentionName)).getExamples()
							.add(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_EXAMPLE));
				}
			}
		}

		kb.setMapIntentions(mapIntentions);
	}

	public List<String> extractExamples(Intention intention) throws ImportExcelException {

		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_INTENTION);

		List<String> examples = null;

		if (sheet != null) {
			examples = new ArrayList<>();

			for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
				Row row = sheet.getRow(i);

				if (row != null) {

					String currentIntention = ExcelUtil
							.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_INTENTION_NAME).trim()
							.toLowerCase();

					if (intention.getName().trim().toLowerCase().equals(currentIntention)) {
						examples.add(ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_EXAMPLE));
					}
				}
			}
		}

		return examples;
	}

	/**
	 * @param kb
	 * @throws ImportExcelException
	 */
	public void extractContent(KnowledgeBase kb) throws ImportExcelException {

		Sheet sheet = ExcelUtil.getSheetByName(workbook, ExtratorConstants.SHEET_NAME_CONTENT);

		List<Content> contentList = new ArrayList<>();

		if (sheet != null) {
			int rows = sheet.getPhysicalNumberOfRows();

			// percorre todas as linhas da aba
			for (int i = 1; i <= rows; i++) {
				Row row = sheet.getRow(i);

				if (row != null) {
					Content content = new Content();

					content.setIntention(new Intention(
							ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_CONTENT_INTENTION)));

					// TODO VALIDAR questao pergunta intermediaria
					String intermediateResponse = ExcelUtil.getCellText(workbook, row,
							ExtratorConstants.CELL_INDEX_CONTENT_RESPOSTA_INTERMEDIARIA);

					if (StringUtils.isEmpty(intermediateResponse)
							|| intermediateResponse.equalsIgnoreCase(ExtratorConstants.NAO)) {

						content.setValue(
								ExcelUtil.getCellText(workbook, row, ExtratorConstants.CELL_INDEX_CONTENT_RESPOSTA));

						content.setIntermediate(false);
					} else {
						content.setValue(intermediateResponse);
						content.setIntermediate(true);
					}

					content.setEntityValues(getEntityValuesContent(row, kb.getMapEntityValues()));

					content.getIntention().setEntities(getEntityContent(row, content.getIntention()));

					content.getIntention().setExamples(extractExamples(content.getIntention()));

					if (content.getIntention().getEntities().size() == 0) {
						content.getIntention()
								.setKey(StringUtil.removeSpecialCharacters(content.getIntention().getName()));
					} else {
						// content.getIntention()
						// .setKey(makeKey(content.getIntention().getEntities(),
						// content.getIntention()));
					}

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
					// ret.add(new Entity(entity,
					// StringUtil.removeSpecialCharacters(entity.replace(" ", ""))));
				}

			}
		}
		return ret;
	}

	// private static String makeKey(List<Entity> entities, Intention i) {
	// return i.getName().toLowerCase() + "_"
	// + entities.stream().map(n -> n.getKey()).collect(Collectors.joining("_"));
	// }

}
