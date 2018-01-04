package validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

			if (entities.size() > 0) {

				for (Entity en : entities) {
					for (EntityValues ev : Arrays.asList(en.getValues())) {
//						for (String s : Arrays.asList(ev.getSynonymous())) {
							if (ev.getName().equals(en.getName())) {
								entityInCat.add(en);
							}
//						}
					}
				}
				
//				entities.forEach(en -> {
//					Arrays.stream(en.getValues()).forEach( ev -> {
//						if (Arrays.asList(ev).contains(en.getName())) {
//							entityInCat.add(en);
//						}
//					});
//				});
				
//				entityValues.stream().filter(f -> f.getEntity().getName().equals(f.getCategory()))
//						.forEach(entityInCat::add);
				
				entityInCat.forEach(e -> {
					LOGGER.info("Entity (" + e.getName() + ") in category", e.getName(), e);
				});
				
				

			} else {
				LOGGER.info("Entities equals 0");
			}
			
			return !(entityInCat.size() > 0);
			
		} catch (Exception e) {
			LOGGER.error("Error on validate", e.getMessage(), e);
			
			return false;
		}
	}

}
