package playid.domain.fodot.theory.elements.inductivedefinitions;

import java.util.List;

import playid.domain.fodot.general.FodotElementList;
import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.theory.elements.IFodotTheoryElement;
import playid.util.CollectionPrinter;


public class FodotInductiveDefinitionBlock extends FodotElementList<FodotInductiveSentence> implements IFodotElement, IFodotTheoryElement {
	
	public FodotInductiveDefinitionBlock(
			List<FodotInductiveSentence> elements) {
		super(elements);
	}
	
	/* Fodot element */

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append(CollectionPrinter.toNewLinesWithTabsAsCode(getElements(),2));
		builder.append("\t}");
		return builder.toString();
	}

	@Override
	public boolean isValidElement(FodotInductiveSentence argElement) {
		return argElement != null;
	}
}
