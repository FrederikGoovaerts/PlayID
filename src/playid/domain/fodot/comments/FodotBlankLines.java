package playid.domain.fodot.comments;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import playid.domain.fodot.general.FodotElement;
import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.structure.elements.IFodotStructureElement;
import playid.domain.fodot.theory.elements.IFodotTheoryElement;
import playid.domain.fodot.vocabulary.elements.FodotType;
import playid.domain.fodot.vocabulary.elements.IFodotVocabularyElement;

public class FodotBlankLines extends FodotElement implements IFodotElement, IFodotTheoryElement, IFodotStructureElement, IFodotVocabularyElement {
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

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return new HashSet<IFodotElement>();
	}
}
