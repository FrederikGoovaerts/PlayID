package fodot.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.vocabulary.elements.FodotType;

public class NameUtil {
	/* STATIC HELPERS */

	private static final String VALID_NAME_REGEX = "^[a-zA-Z$][a-zA-Z_0-9]*$";
	private static final List<String> ALLOWED_SPECIAL_NAMES = Arrays.asList("-");
	private static final String NON_ALPHA_NUMERIC_REGEX = "[^a-zA-Z0-9]";
	private static final String BASIC_VAR_NAME = "var";

	private static Map<String, Integer> prefixCounter = new HashMap<String, Integer>();

	public static String generateVariableName(String nameSuggestion, FodotType type, Collection<? extends String> usedNames) {
		//Generate a new valid name
		String correctedName = convertToValidName(nameSuggestion, type);

		//Put numbers after it if the name is already used
		String newName = correctedName;
		int index = 2;
		while (usedNames.contains(newName)) {
			newName = correctedName + index++;
		}		
		
		return newName;
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
	 * This method will do it's best to convert a string into a string that can actually be used as a variable name in FodotIDP
	 * @param name
	 * @return
	 */
	public static String convertToValidName(String name, FodotType type) {
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

	private static synchronized String generateVariableName(String prefix) {
		if (!prefixCounter.containsKey(prefix)) {
			prefixCounter.put(prefix, 0);
		}
		prefixCounter.put(prefix, prefixCounter.get(prefix)+1);
		return prefix + Integer.toString(prefixCounter.get(prefix));
	}

	private static final String GENERATED_VARIABLE_NAME_REGEX = "^[v][a][r][0-9]*$";

	
	/**
	 * Some complex stuff to generate better variable names for a given collection of variables
	 * @param argVariables
	 */
	public static void improveGeneratedVariableNames(Collection<? extends FodotVariable> argVariables) {
		
		Set<String> usedNames = new HashSet<String>();
		
		//Sort all variables with generated names per type
		Map<FodotType, Set<FodotVariable>> variablesPerType = new HashMap<FodotType, Set<FodotVariable>>();
		for(FodotVariable var : argVariables) {
			
			if (var.getName().matches(GENERATED_VARIABLE_NAME_REGEX)) {
				//Init set if empty
				if (variablesPerType.get(var.getType()) == null) {
					variablesPerType.put(var.getType(), new HashSet<FodotVariable>());
				}
				
				//Add to set
				variablesPerType.get(var.getType()).add(var);
			} else {
				//Add names to "names not to use"
				usedNames.add(var.getName());
			}
		}
		
		for (FodotType type : variablesPerType.keySet()) {
			Set<FodotVariable> variables = variablesPerType.get(type);
			String basicName = type.getName().substring(0, 1).toLowerCase();
			
			
			if (variables.size() == 1) {
				String newName = basicName;
				
				int index = 0;
				while (usedNames.contains(newName)) {
					newName = basicName + index;
					index++;
				}
				variables.iterator().next().setName(newName);
				usedNames.add(newName);
			}
			else {//More variables of this type
				int index = 1;
				for (FodotVariable var : variables) {
					String newName = basicName + index;
					while (usedNames.contains(newName)) {
						newName = basicName + index;
						index++;
					}
					var.setName(newName);
					usedNames.add(newName);
				}
			}
		}

	}
}
