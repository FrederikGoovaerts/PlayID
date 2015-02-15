package fodot.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.exceptions.fodot.FodotException;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.vocabulary.elements.FodotType;

public class NameUtil {

	private static final String VALID_NAME_REGEX = "^[a-zA-Z][a-zA-Z_0-9]*$";
	private static final List<String> ALLOWED_SPECIAL_NAMES = Arrays.asList("-");
	private static final String NON_ALPHA_NUMERIC_REGEX = "[^a-zA-Z0-9]";
	private static final String BASIC_VAR_NAME = "var";
	
	/**
	 * Found at: https://bitbucket.org/krr/idp/src/9935dfa0b98923c605a4706a78e697b67d2b5ba9/src/parser/lex.ll?at=master
	 */
	private static final List<String> IDP_RESERVED_NAMES = Arrays.asList(
			"type", "partial", "isa", "contains", "extern", "vocabulary", "constructed",
			"card", "sum", "prod", "min", "max", "LFD", "GFD", "true", "false", "define",
			"in", "sat", "procedure", "generate"
			);

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
			if (isReserverByIdp(name)) {
				name = name + "1";
			}
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

	private static String tryConvertingToValidName(String name) {
		if (isValidName(name)) {
			return name;
		}

		String newName = name.replaceAll("-", "_").replace("+", "plus");
		if (isValidName(newName)) {
			return newName;
		}
		newName = name.replaceAll(NON_ALPHA_NUMERIC_REGEX, "");
		return newName;
	}
	
	public static String convertToValidPredicateName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name can't be null");
		}

		String newName = tryConvertingToValidName(name);
		if (isValidName(newName)) {
			return newName;
		}
		return "pr_" + newName;
	}

	public static final String NUMBER_REGEX = "^[-]?[0-9]+$";
	public static final String POSITIVE_NUMBER_REGEX = "^[0-9]+$";
	public static final String NEGATIVE_NUMBER_REGEX = "^[-][0-9]+$";

	public static String convertToValidConstantName(String name, FodotType type) {
		if (!type.isASubtypeOf(FodotType.INTEGER)) {
			if (name.matches(POSITIVE_NUMBER_REGEX)) {
				return "i_" + name;
			} else if (name.matches(NEGATIVE_NUMBER_REGEX)) {
				return "i_m" + name.replace("-", "");
			}
			if (isReserverByIdp(name)) {
				name = name + "1";
			}
		}
		
		String newName = tryConvertingToValidName(name);
		if (isValidName(newName)) {
			return newName;
		}
		return newName;
	}
	
	public static int convertConstantNameToInteger(String name) {
		if (name.matches(NUMBER_REGEX)) {
			return Integer.parseInt(name);
		}
		
		if (!name.startsWith("i_")) {
			throw new FodotException("Given constant was never a number");
		}
		
		return Integer.parseInt( name.replaceAll("i_", "").replaceAll("m", "-") );
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
	
	public static boolean isReserverByIdp(String name) {
		return IDP_RESERVED_NAMES.contains(name);
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
