package fodot.objects.util;

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
	
	public static String toCoupleAsCode(Collection<? extends IFodotElement> list) {
		return toCouple(toCode(list));
	}
	
	public static String toCouple(Collection<String> list) {
		return printStringList("(", ")", ", ", new ArrayList<String>(list));
	}
	
	public static String toDomain(Collection<String> list) {
		return printStringList("{", "}", ";", new ArrayList<String>(list));
	}
	
	public static String printStringList(String openingbracket, String closingBracket, String divider, List<String> list) {
		StringBuilder builder = new StringBuilder();
		builder.append(openingbracket);
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				builder.append(divider);
			}
			builder.append(list.get(i));
		}
		builder.append(closingBracket);
		return builder.toString();
		
	}
}
