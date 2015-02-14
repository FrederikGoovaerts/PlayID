package fodot.objects.general;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class FodotElement implements IFodotElement{
	@Override
	public Collection<? extends IFodotElement> getDirectElementsOfClass(Class<? extends IFodotElement> clazz) {
		Set<IFodotElement> result = new LinkedHashSet<IFodotElement>();
		if (clazz == null) {
			return result;
		}
		if (clazz.isInstance(this)) {
			result.add(this);
		}
		for (IFodotElement el : getDirectFodotElements()) {
			if (clazz.isInstance(el)) {
				result.add(el);
			}
		}
		return result;
	}
	@Override	
	public Collection<? extends IFodotElement> getAllInnerElementsOfClass(Class<? extends IFodotElement> clazz) {
		Set<IFodotElement> result = new LinkedHashSet<IFodotElement>();
		result.addAll(getDirectElementsOfClass(clazz));

		Set<IFodotElement> innerElements = new LinkedHashSet<IFodotElement>(getDirectFodotElements());
		for (IFodotElement el : innerElements) {
			result.addAll(el.getAllInnerElementsOfClass(clazz));
		}
		return result;
	}
	
	@Override
	public boolean hasEmptyCode() {
		return toCode().trim().equals("");
	}
}
