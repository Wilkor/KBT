package validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Entity;
import model.Intention;
import model.KnowledgeBase;
import util.StringUtil;

public class IntentionEntityValidator {

	private final static Logger LOGGER = LoggerFactory.getLogger(IntentionEntityValidator.class);

	public void validate(KnowledgeBase kb) {

		try {

			List<Intention> uniqueIntentions = new ArrayList<>();

			List<Intention> ints = new ArrayList<>();

			kb.getContentList().stream().forEach(c -> {
				ints.add(c.getIntention());
			});

			uniqueIntentions = ints.stream().filter(distinctByKey(Intention::getName))
					.sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).collect(Collectors.toList());

			List<Intention> broken = new ArrayList<>();
			for (Intention intention : ints) {
				Predicate<Intention> p = e -> e.getKey().equals(intention.getKey());
				
				if (intention.getEntities().size() == 1) {

					List<Intention> is = searchIntentions(ints, StringUtil.removeSpecialCharacters(intention.getName()));

					if (is.size() == 0) {
						if (broken.stream().noneMatch(p)) {
							broken.add(intention);
						}
					}

				} else {
					List<Entity> entities = intention.getEntities();
					List<Intention> is;
					for (Entity entity : entities) {
						is = searchIntentions(ints, StringUtil.removeSpecialCharacters(intention.getName() + "_" + entity.getKey()));

						if (is.size() == 0) {
							if (broken.stream().noneMatch(p)) {
								broken.add(intention);
							}
						}
						
					}
					
					is = searchIntentions(ints, StringUtil.removeSpecialCharacters(intention.getName()));

					if (is.size() == 0) {
						
						if (broken.stream().noneMatch(p)) {
							broken.add(intention);
						}

					}
				}

			}
			
			broken.forEach(b -> {
				LOGGER.info("Broken itentions and entities " + b.getKey(), b.getKey(), b);
			});

		} catch (Exception e) {
			LOGGER.error("Error on validate intention entity", e.getMessage(), e);
		}

	}

	private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, String> seen = new ConcurrentHashMap<>();
		return t -> seen.put(keyExtractor.apply(t), "") == null;
	}

	private static List<Intention> searchIntentions(List<Intention> ints, String key) throws Exception {
		return ints.stream().filter(b -> b.getKey().equals(key.toLowerCase())).collect(Collectors.toList());
	}

}
