package fodot.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.IFodotSentenceElement;

public class CollectionPrinter {
	public static List<String> toCode(Collection<? extends IFodotElement> list) {
		if (list == null) {
			throw new IllegalArgumentException("Not a valid list to convert to code: " + list);
		}
		List<String> toCodify = new ArrayList<String>();
		for (IFodotElement element : list) {
			toCodify.add(element.toCode());
		}
		return toCodify;
	}
	
	/**
	 * This method codifies sentence elements and adds necessary brackets
	 * @param list
	 * @param bindingorder	The bindingorder of the caller
	 * @return	List of codified elements
	 */
	public static List<String> toCode(Collection<? extends IFodotSentenceElement> list, int bindingorder) {
		if (list == null) {
			throw new IllegalArgumentException("Not a valid list to convert to code: " + list);
		}
		List<String> toCodify = new ArrayList<String>();
		for (IFodotSentenceElement element : list) {
			String stringified = element.toCode();
			if (bindingorder >= 0
					&& element.getBindingOrder() >= bindingorder) {
				stringified = "(" + stringified + ")";
			}
			toCodify.add(stringified);
		}
		return toCodify;
	}
	
	public static List<String> toString(Collection<? extends IFodotElement> list) {
		List<String> toStringify = new ArrayList<String>();
		for (IFodotElement element : list) {
			toStringify.add(element.toString());
		}
		return toStringify;
	}
	
	public static String toCoupleAsCode(Collection<? extends IFodotElement> list) {
		return toCouple(toCode(list));
	}
	
	public static String toCouple(Collection<String> list) {
		return printStringList("(", ")", ",", new ArrayList<String>(list));
	}
	
	public static String toDomain(Collection<String> list) {
		return printStringList("{", "}", ";", new ArrayList<String>(list));
	}

	public static String toDeclarationDomain(Collection<String> list) {
		return printStringList("{", "}", ",", new ArrayList<String>(list));
	}

	public static String toDomain(String head, String last) {
		return "{" + head + ".." + last +"}";
	}
	
	public static String toNakedList(Collection<String> list) {
		return printStringList("", "", ",", new ArrayList<String>(list));
	}
	
	public static String toNewLinesWithTabsAsCode(Collection<? extends IFodotElement> list, int tabs) {
		StringBuilder prefixB = new StringBuilder();
		for (int i = 0; i < tabs; i++) {
			prefixB.append("\t");
		}
		String prefix = prefixB.toString();
		return prefix + printStringList("","","\n"+prefix, toCode(list)) + "\n";
	}
	
	public static String printStringList(String openingbracket, String closingBracket, String divider, List<?> list) {
		StringBuilder builder = new StringBuilder();
		builder.append(openingbracket);
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				builder.append(divider);
			}
			builder.append(list.get(i).toString());
		}
		builder.append(closingBracket);
		return builder.toString();
		
	}
}
