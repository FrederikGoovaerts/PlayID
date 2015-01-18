package fodot.objects.comments;

import java.util.HashSet;
import java.util.Set;

import fodot.objects.general.IFodotElement;
import fodot.objects.structure.elements.IFodotStructureElement;
import fodot.objects.theory.elements.IFodotTheoryElement;
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
		for (int i = 0; i < amount-1; i++) {
			builder.append("\n");
		}
		return builder.toString();
	}

	@Override
	public IFodotVocabularyElement getDeclaration() {
		return null;
	}

	@Override
	public int getArity() {
		return 0;
	}
}
