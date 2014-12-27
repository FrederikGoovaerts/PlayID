package fodot.objects.theory.elements.inductivedefinitions;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.IFodotTheoryElement;
import fodot.util.CollectionPrinter;


public class FodotInductiveDefinitionBlock implements IFodotElement, IFodotTheoryElement {

	private List<FodotInductiveSentence> elements;
	
	public FodotInductiveDefinitionBlock(
			List<FodotInductiveSentence> elements) {
		super();
		this.elements = elements;
	}
	
	/* Elements control */
	
	public List<FodotInductiveSentence> getElements() {
		return new ArrayList<FodotInductiveSentence>(elements);
	}
	
	public void addElement(FodotInductiveSentence element) {
		elements.add(element);
	}
	
	public void removeElement(FodotInductiveSentence element) {
		elements.remove(element);
	}
	
	public boolean containsElement(FodotInductiveSentence element) {
		return elements.contains(element);
	}

	/* Fodot element */

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append(CollectionPrinter.toNewLinesWithTabsAsCode(elements,2));
		builder.append("\t}");
		return builder.toString();
	}

}
