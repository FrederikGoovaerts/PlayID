package fodot.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fodot.objects.IFodotElement;

public class CollectionUtil {
	public static List<String> toCode(Collection<? extends IFodotElement> list) {
		List<String> toCodify = new ArrayList<String>();
		for (IFodotElement element : list) {
			toCodify.add(element.toCode());
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
		return printStringList("(", ")", ", ", new ArrayList<String>(list));
	}
	
	public static String toDomain(Collection<String> list) {
		return printStringList("{", "}", ";", new ArrayList<String>(list));
	}

	public static String toDomain(String head, String last) {
		return "{" + head + ".." + last +"}";
	}
	
	public static String toNakedList(Collection<String> list) {
		return printStringList("", "", ", ", new ArrayList<String>(list));
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
