package fodot.objects.vocabulary.elements;

import java.util.Set;

import fodot.objects.IFodotElement;

public interface IFodotDomainElement extends IFodotElement {
	/**
	 * This method should return all types that should be defined before the type it belongs to
	 * @return
	 */
	Set<FodotType> getRequiredTypes();
}
