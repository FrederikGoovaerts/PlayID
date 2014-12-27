package fodot.objects.file;

import java.util.List;

import fodot.objects.general.IFodotElement;

public interface IFodotFile extends IFodotElement {
	List<? extends IFodotFileElement> getElementOf(Class<?> claz);
}
