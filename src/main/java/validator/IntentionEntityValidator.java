package validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Intention;
import model.KnowledgeBase;

public class IntentionEntityValidator {

	private final static Logger LOGGER = LoggerFactory.getLogger(IntentionEntityValidator.class);

	public void validate(KnowledgeBase kb) {

		try {

			List<Intention> uniqueIntentions = new ArrayList<>();

			List<Intention> ints = new ArrayList<>();
			
			kb.getContentList().stream().forEach(c -> {
				ints.add(c.getIntention());
			});

			uniqueIntentions = ints.stream().filter(distinctByKey(Intention::getName)).sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).collect(Collectors.toList());
			
			for (Intention intention : uniqueIntentions) {
				List<Intention> is = ints.stream().filter(b -> b.getName().equals(intention.getName())).collect(Collectors.toList());
				
//				is.stream().
				
				break;
			}

		} catch (Exception e) {
			LOGGER.error("Error on validate intention entity", e.getMessage(), e);
		}

	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T,Object> keyExtractor) {
	    Map<Object,String> seen = new ConcurrentHashMap<>();
	    return t -> seen.put(keyExtractor.apply(t), "") == null;
	}

}
