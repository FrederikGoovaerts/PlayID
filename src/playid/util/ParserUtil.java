package playid.util;

import java.util.ArrayList;
import java.util.List;

public class ParserUtil {

	public static List<String> trimElements(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			list.set(i, list.get(i).trim());
		}
		return removeEmptyStrings(list);
	}

	/**********************************************
	 *  Empty string remover
	 ***********************************************/

	private static List<String> removeEmptyStrings(List<String> list) {
		List<String> result = new ArrayList<String>();
		for (String s : list) {
			if (!isEmpty(s)) {
				result.add(s);
			}
		}
		return result;
	}
	
	private static boolean isEmpty(String s) {
		return s == null
				|| s.trim().equals("");
	}	

	/**********************************************/

	/**********************************************
	 *  Splitters
	 ***********************************************/

	public static List<String> splitOn(String toSplit, String splitter) {
		List<String> result = new ArrayList<String>();

		//don't use String's .split, it uses regexes!

		String processedToSplit = toSplit;
		while (processedToSplit.contains(splitter)) {
			int firstOccurance = processedToSplit.indexOf(splitter);
			result.add(processedToSplit.substring(0,firstOccurance));
			processedToSplit = processedToSplit.substring(firstOccurance+splitter.length());
		}
		result.add(processedToSplit);

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

	public static List<String> splitOnTrimmed(String toSplit, String splitter) {
		return trimElements(splitOn(toSplit, splitter));
	}

	public static List<String> splitOnTrimmed(List<String> toSplit, String splitter) {
		return trimElements(splitOn(toSplit, splitter));
	}


	/**********************************************/

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

	/**********************************************
	 *  Element counters
	 ***********************************************/

	public static int getAmountOfCharInString(char toCount, String string) {
		int result = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == toCount) {
				result += 1;
			}
		}
		return result;		
	}

	public static int getAmountOfStringInString(String toCount, String string) {
		int result = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.substring(i).startsWith(toCount)) {
				result += 1;
			}
		}
		return result;		
	}

	/**********************************************/
}
