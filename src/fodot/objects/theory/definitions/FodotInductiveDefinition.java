package fodot.objects.theory.definitions;

import java.util.ArrayList;
import java.util.List;

import fodot.objects.IFodotElement;
import fodot.objects.sentence.formulas.FodotFormula;
import fodot.objects.theory.FodotSentence;


public class FodotInductiveDefinition implements IFodotElement {

	private List<FodotSentence> elements;
	
	public FodotInductiveDefinition(
			List<FodotSentence> elements) {
		super();
		this.elements = elements;
	}
	
	/* Elements control */
	
	public List<FodotSentence> getElements() {
		return new ArrayList<FodotSentence>(elements);
	}
	
	public void addElement(FodotSentence element) {
		elements.add(element);
	}
	
	public void removeElement(FodotSentence element) {
		elements.remove(element);
	}
	
	public boolean containsElement(FodotSentence element) {
		return elements.contains(element);
	}

	/* Fodot element */

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append("{ \n");
		for (FodotSentence element : elements) {
			builder.append("\t"+element.toCode()+"\n");
		}		
		builder.append("}");
		return builder.toString();
	}

}
