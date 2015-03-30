package playid.domain.fodot.theory.elements.terms.aggregates;

import java.util.Arrays;

import playid.domain.fodot.theory.elements.terms.FodotFunction;
import playid.domain.fodot.theory.elements.terms.IFodotTerm;
import playid.domain.fodot.vocabulary.elements.FodotFunctionDeclaration;

public class FodotInvertFunction extends FodotFunction {

	public FodotInvertFunction(FodotFunctionDeclaration decl, IFodotTerm term) {
		super(decl, Arrays.asList(term));
	}
	
	@Override
	public String toCode() {
		return "-" + getArguments().get(0).toCode();
	}

}
