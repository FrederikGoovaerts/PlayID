package fodot.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.vocabulary.elements.FodotType;

public class NameUtil {

	private static final String VALID_NAME_REGEX = "^[a-zA-Z$][a-zA-Z_0-9]*$";
	private static final List<String> ALLOWED_SPECIAL_NAMES = Arrays.asList("-");
	private static final String NON_ALPHA_NUMERIC_REGEX = "[^a-zA-Z0-9]";
	private static final String BASIC_VAR_NAME = "var";

	public static String generateVariableName(String nameSuggestion, FodotType type, Collection<? extends String> usedNames) {
		//Generate a new valid name
		String correctedName = convertToValidVariableName(nameSuggestion, type);

		//Put numbers after it if the name is already used
		String newName = correctedName;
		int index = 2;
		while (usedNames.contains(newName)) {
			newName = correctedName + index++;
		}		

		return newName;
	}


	/**
	 * This method will do it's best to convert a string into a string that can actually be used as a variable name in FodotIDP
	 * @param name
	 * @return
	 */
	public static String convertToValidVariableName(String name, FodotType type) {
		if (name != null) {
			if (isValidName(name)) {
				return name;
			}
			String newName = name.replaceAll(NON_ALPHA_NUMERIC_REGEX, "");
			if (isValidName(newName)) {
				return newName;
			}
		}
		return createBaseName(type);

	}

	public static String convertToValidPredicateName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name can't be null");
		}
		
		if (isValidName(name)) {
			return name;
		}
		
		String newName = name.replaceAll("-", "_");
		if (isValidName(newName)) {
			return newName;
		}
		newName = name.replaceAll(NON_ALPHA_NUMERIC_REGEX, "");
		if (isValidName(newName)) {
			return newName;
		}
		return "pr_" + newName;
	}

	/**
	 * Checks if the string is a valid name for a variable name in FodotIDP
	 * @param name
	 * @return
	 */
	public static boolean isValidName(String name) {
		if (name == null || name.length() == 0) {
			return false;
		}
		return (name.matches(VALID_NAME_REGEX)
				|| ALLOWED_SPECIAL_NAMES.contains(name));
	}

	/**
	 * Creates a basic name for the given type. Takes the first letter in lowercasing of the typename.
	 * @param type
	 * @return
	 */
	private static String createBaseName(FodotType type) {
		if (type == null) {
			return BASIC_VAR_NAME;
		}
		return type.getName().trim().substring(0, 1).toLowerCase();
	}
	
	public static void solveNameCollisions(Collection<? extends String> prohibitedNames, Collection<? extends FodotVariable> variables) {
		
		/* The "Claimed Names" set contains the prohibited names as well as all variablenames.
		 * This is used for generating a new name.
		 * DON'T USE THIS TO CHECK IF THE VARIABLE SHOULD CHANGE NAME, IT WILL ALWAYS RESULT IN "TRUE"
		 */
		Set<String> claimedNames = new HashSet<String>(prohibitedNames);
		for (FodotVariable otherVar : variables) {
			claimedNames.add(otherVar.getName());
		}
		
		for (FodotVariable var : variables) {
			if (prohibitedNames.contains(var.getName())) {
				var.setName(generateVariableName(var.getName(), var.getType(), claimedNames));
			}
		}
	}
	
	
	
	
	
}
