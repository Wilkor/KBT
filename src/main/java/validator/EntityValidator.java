package validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.limeprotocol.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.KnowledgeBase;
import net.take.iris.messaging.resources.artificialIntelligence.Entity;
import net.take.iris.messaging.resources.artificialIntelligence.Entity.EntityValues;

public class EntityValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(EntityValidator.class);

	public boolean validate(KnowledgeBase kb) {

		try {

			List<Entity> entities = kb.getEntities();

			List<Entity> entityInCat = new ArrayList<>();

			List<Entity> entityWithoutValues = new ArrayList<>();
			
			if (entities.size() > 0) {

				for (Entity en : entities) {
					for (EntityValues ev : Arrays.asList(en.getValues())) {
						if (ev.getName().equals(en.getName())) {
							entityInCat.add(en);
						}

						if (StringUtils.isNullOrEmpty(ev.getName())) {
							LOGGER.info("Entity (" + en.getName() + ") value is empty  ", en);
							entityWithoutValues.add(en);
						}
					}

				}

				entityInCat.forEach(e -> {
					LOGGER.info("Entity (" + e.getName() + ") in category", e.getName(), e);
				});

			} else {
				LOGGER.info("Entities equals 0");
			}

			return !(entityInCat.size() > 0 && entityWithoutValues.size() > 0);

		} catch (Exception e) {
			LOGGER.error("Error on validate", e.getMessage(), e);

			return false;
		}
	}

}
