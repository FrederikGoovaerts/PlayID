package fodot.objects.termdefinition;

import java.util.Arrays;

import fodot.objects.file.FodotFileElement;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.FodotVocabulary;

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
