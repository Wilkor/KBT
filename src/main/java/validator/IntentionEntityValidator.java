package validator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Intention;
import model.KnowledgeBase;
import net.take.iris.messaging.resources.artificialIntelligence.Entity;
import util.StringUtil;

public class IntentionEntityValidator {

	private final static Logger LOGGER = LoggerFactory.getLogger(IntentionEntityValidator.class);

	public boolean validate(KnowledgeBase kb) {

		try {

			List<Intention> ints = kb.getIntentions();

			List<Intention> broken = new ArrayList<>();

			List<String> notFindKey = new ArrayList<>();

			List<Intention> withoutExamples = new ArrayList<>();

			for (Intention intention : ints) {

				List<Intention> is;

				Predicate<Intention> p = e -> e.getKey().equals(intention.getKey());

				Predicate<Intention> pName = e -> e.getKey().equals(intention.getName());
				
				if (intention.getQuestions().length == 0) {

					if (withoutExamples.stream().noneMatch(pName)) {
						withoutExamples.add(intention);
					}
						
				}

				if (intention.getEntities().size() == 1) {

					is = searchIntentions(ints, StringUtil.removeSpecialCharacters(intention.getName()));

					if (is.size() == 0) {
						if (broken.stream().noneMatch(p)) {
							broken.add(intention);
						}

						notFindKey.add(StringUtil.removeSpecialCharacters(intention.getName()));
					}

				} else {
					List<Entity> entities = intention.getEntities();

					for (Entity entity : entities) {

						String key = StringUtil.removeSpecialCharacters(intention.getName()) + "_"
								+ StringUtil.removeSpecialCharacters(entity.getName());

						is = searchIntentions(ints, StringUtil.removeSpecialCharacters(key));

						if (is.size() == 0) {
							if (broken.stream().noneMatch(p)) {
								broken.add(intention);
							}

							notFindKey.add(key);
						}

					}

					is = searchIntentions(ints, StringUtil.removeSpecialCharacters(intention.getName()));

					if (is.size() == 0) {

						if (broken.stream().noneMatch(p)) {
							broken.add(intention);
						}

						notFindKey.add(StringUtil.removeSpecialCharacters(intention.getName()));

					}
				}

			}

			List<Intention> i = withoutExamples.stream().distinct().collect(Collectors.toList());

			i.stream().forEach(e -> {
				LOGGER.info("Intention (" + e.getName() + ") without examples ", e.getName(), e);
			});

			notFindKey.stream().distinct().forEach(e -> {
				LOGGER.info("Itentions and entities not found " + e);
			});

			// broken.forEach(b -> {
			// LOGGER.info("Broken itentions and entities " + b.getKey(), b.getKey(), b);
			// });

			return !(broken.size() > 0 || withoutExamples.size() > 0);

		} catch (Exception e) {
			LOGGER.error("Error on validate intention entity", e.getMessage(), e);

			return false;
		}

	}

	// private static <T> Predicate<T> distinctByKey(Function<? super T, Object>
	// keyExtractor) {
	// Map<Object, String> seen = new ConcurrentHashMap<>();
	// return t -> seen.put(keyExtractor.apply(t), "") == null;
	// }

	private static List<Intention> searchIntentions(List<Intention> ints, String key) throws Exception {
		return ints.stream().filter(b -> b.getKey().equals(key.toLowerCase())).collect(Collectors.toList());
	}

}
