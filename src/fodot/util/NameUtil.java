package fodot.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fodot.objects.vocabulary.elements.FodotType;

public class NameUtil {
	/* STATIC HELPERS */

	// TODO: this is crudely fixed to allow for "-"
	private static String validNameRegex = "^[a-zA-Z$][a-zA-Z_$0-9]*$";
	private static List<String> allowedSpecialNames = Arrays.asList("-");
	private static String alphanumericRegex = "[^a-zA-Z0-9]";

	private static Map<String, Integer> prefixCounter = new HashMap<String, Integer>();

	/**
	 * Checks if the string is a valid name for a variable name in FodotIDP
	 * @param name
	 * @return
	 */
	public static boolean isValidName(String name) {
		if (name == null || name.length() == 0) {
			return false;
		}
		return (name.matches(validNameRegex)||allowedSpecialNames.contains(name));
	}

	/**
	 * This method will do it's best to convert a string into a string that can actually be used as a variable name in FodotIDP
	 * @param name
	 * @return
	 */
	public static String convertToValidName(String name, FodotType type) {
		if (name != null) {
			if (isValidName(name)) {
				return name;
			}
			String newName = name.replaceAll(alphanumericRegex, "");
			if (isValidName(newName)) {
				return newName;
			}
		}
		return generateVariableName(type);
	}
	
	public static String convertToValidName(String name) {
		return convertToValidName(name, null);
	}
	
	private static synchronized String generateVariableName(String prefix) {
		if (!prefixCounter.containsKey(prefix)) {
			prefixCounter.put(prefix, 0);
		}
		prefixCounter.put(prefix, prefixCounter.get(prefix)+1);
		return prefix + Integer.toString(prefixCounter.get(prefix));
	}
	
	public static String generateVariableName(FodotType type) {
		if (type == null) {
			return generateVariableName();
		}
		return generateVariableName(type.getName().toLowerCase());
	}
	
	public static String generateVariableName() {
		return generateVariableName("var");
	}
}
