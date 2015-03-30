package playid.domain.fodot.general;

import java.util.Collection;

public interface IFodotElement {
	String toCode();
	boolean hasEmptyCode();
	/**
	 * @return	All elements that really are part of the FodotElement, will mostly contain all elements when printing this element
	 */
	Collection<? extends IFodotElement> getDirectFodotElements();
	/**
	 * @param clazz
	 * @return Will return all inner elements of a given class
	 */
	Collection<? extends IFodotElement> getDirectElementsOfClass(Class<? extends IFodotElement> clazz);
	/**
	 * @param clazz
	 * @return Will return all inner elements and recursively all their inner elements of a given class
	 */
	Collection<? extends IFodotElement> getAllInnerElementsOfClass(Class<? extends IFodotElement> clazz);
}
