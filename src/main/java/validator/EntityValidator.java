package validator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.EntityValue;
import model.KnowledgeBase;

public class EntityValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(EntityValidator.class);

	public boolean validate(KnowledgeBase kb) {

		try {

			List<EntityValue> entityValues = kb.getEntityValues();
			
			List<EntityValue> entityInCat = new ArrayList<>();

			if (entityValues.size() > 0) {

				entityValues.stream().filter(f -> f.getEntity().getName().equals(f.getCategory()))
						.forEach(entityInCat::add);
				
				entityInCat.forEach(e -> {
					LOGGER.info("Entity (" + e.getName() + ") in category", e.getName(), e);
				});
				
				

			} else {
				LOGGER.info("Entities equals 0");
			}
			
			return entityInCat.size() > 0;
			
		} catch (Exception e) {
			LOGGER.error("Error on validate", e.getMessage(), e);
			
			return false;
		}
	}

}
