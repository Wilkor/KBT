package util;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;

public class StringUtil {

	public static String removeSpecialCharacters(String str) {
		return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase().trim();
//		return StringEscapeUtils.escapeHtml(str.toLowerCase().trim());
	}
	
	public static String formatString(String string) {
		try {
			return new String(string.getBytes("UTF-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}

}
