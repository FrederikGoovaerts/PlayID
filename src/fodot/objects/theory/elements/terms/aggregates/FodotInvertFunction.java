package fodot.objects.theory.elements.terms.aggregates;

import java.util.Arrays;

import fodot.objects.theory.elements.terms.FodotFunction;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;

public class FodotInvertFunction extends FodotFunction {

	public FodotInvertFunction(FodotFunctionDeclaration decl, IFodotTerm term) {
		super(decl, Arrays.asList(term));
	}
	
	@Override
	public String toCode() {
		return "-" + getArguments().get(0).toCode();
	}

}
