package util;

import org.apache.commons.lang.StringEscapeUtils;

public class StringUtil {

	public static String removeSpecialCharacters(String str) {
//		return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase().trim();
		return StringEscapeUtils.escapeHtml(str.toLowerCase().trim());
	}

}
