package playid.domain.fodot.termdefinition;

import java.util.Arrays;

import playid.domain.fodot.file.FodotFileElement;
import playid.domain.fodot.file.IFodotFileElement;
import playid.domain.fodot.theory.elements.terms.IFodotTerm;
import playid.domain.fodot.vocabulary.FodotVocabulary;

public class FodotTermDefinition extends FodotFileElement<IFodotTerm> implements IFodotFileElement {
	
	public FodotTermDefinition(String name, IFodotTerm term, FodotVocabulary voc) {
		super(name, Arrays.asList(term), Arrays.asList(voc), 1, 1);
	}

	@Override
	public String getFileElementName() {
		return "term";
	}

	@Override
	public String getDefaultName() {
		return "t";
	}

	@Override
	public boolean isValidElement(IFodotTerm argElement) {
		return true;
	}

}
