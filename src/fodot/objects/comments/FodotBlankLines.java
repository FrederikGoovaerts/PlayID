package fodot.objects.comments;

import java.util.HashSet;
import java.util.Set;

import fodot.objects.general.IFodotElement;
import fodot.objects.structure.IFodotStructureElement;
import fodot.objects.theory.IFodotTheoryElement;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;

public class FodotBlankLines implements IFodotElement, IFodotTheoryElement, IFodotStructureElement, IFodotVocabularyElement {
	private int amount;
	
	public FodotBlankLines(int amount) {
		super();
		this.amount = amount;
	}

	@Override
	public Set<FodotType> getPrerequiredTypes() {
		return new HashSet<FodotType>();
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < amount; i++) {
			builder.append("\n");
		}
		return builder.toString();
	}
}
