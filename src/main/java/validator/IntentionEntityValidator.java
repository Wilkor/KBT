package validator;

import java.util.ArrayList;
import java.util.List;
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

	public boolean validate(KnowledgeBase kb) {

		try {

			List<Intention> ints = new ArrayList<>();

			kb.getContentList().stream().forEach(c -> {
				ints.add(c.getIntention());
			});

			List<Intention> broken = new ArrayList<>();
			
			List<Intention> withoutExamples = new ArrayList<>();
			
			for (Intention intention : ints) {
				Predicate<Intention> p = e -> e.getKey().equals(intention.getKey());
				
				if (intention.getExamples().size() == 0) {
					LOGGER.info("Intention (" + intention.getName() + ") without examples ", intention.getName(), intention);
					withoutExamples.add(intention);
				}
				
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
			
			return !(broken.size() > 0 || withoutExamples.size() > 0);

		} catch (Exception e) {
			LOGGER.error("Error on validate intention entity", e.getMessage(), e);
			
			return false;
		}

	}

//	private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
//		Map<Object, String> seen = new ConcurrentHashMap<>();
//		return t -> seen.put(keyExtractor.apply(t), "") == null;
//	}

	private static List<Intention> searchIntentions(List<Intention> ints, String key) throws Exception {
		return ints.stream().filter(b -> b.getKey().equals(key.toLowerCase())).collect(Collectors.toList());
	}

}
