package fodot.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserUtil {


	public static List<String> trimElements(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			list.set(i, list.get(i).trim());
		}
		return list;
	}


	public static List<String> splitOn(String toSplit, String splitter) {
		List<String> result;
		if (toSplit.contains(splitter)) {
			result = Arrays.asList(toSplit.split(splitter));
		} else {
			result = Arrays.asList(toSplit);
		}

		result = concatElementsWithBrackets(result);

		return result;
	}
	
	public static List<String> splitOn(List<String> toSplit, String splitter) {
		ArrayList<String> result = new ArrayList<String>();
		for (String s : toSplit) {
			result.addAll(splitOn(s, splitter));
		}
		return result;
	}

	/**
	 * This method concats elements if they contain open brackets so it's not split over multiple elements
	 * @param result
	 * @return
	 */
	public static List<String> concatElementsWithBrackets(List<String> input) {
		List<String> result = new ArrayList<String>();
		int openBracketCounter = 0;
		String currentElement = null;
		for (String el : input) {
			openBracketCounter = openBracketCounter + getAmountOfCharInString('(', el) - getAmountOfCharInString(')', el);

			if (currentElement == null) {
				currentElement = el;
			} else {
				currentElement += ", " + el;
			}
			if (openBracketCounter == 0) {
				result.add(currentElement);
				currentElement = null;
			}
		}

		return result;
	}

	public static int getAmountOfCharInString(char splitter, String string) {
		int result = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == splitter) {
				result += 1;
			}
		}
		return result;		
	}
}
