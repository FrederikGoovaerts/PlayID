package playid.domain.fodot.theory.elements.terms.aggregates;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import playid.domain.fodot.general.FodotElement;
import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.fodot.theory.elements.terms.FodotVariable;
import playid.domain.fodot.theory.elements.terms.IFodotTerm;
import playid.domain.fodot.vocabulary.elements.FodotType;

public class FodotAggregate extends FodotElement implements IFodotTerm {
	private static final int BINDING_ORDER = -1;

	private String name;
	private FodotSet set;

	public FodotAggregate(String name, FodotSet set) {
		super();
		this.name = name;
		this.set = set;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FodotSet getSet() {
		return set;
	}
	public void setSet(FodotSet set) {
		this.set = set;
	}



	@Override
	public Set<FodotVariable> getFreeVariables() {
		return set.getFreeVariables();
	}
	@Override
	public int getBindingOrder() {
		return BINDING_ORDER;
	}
	@Override
	public String toCode() {
		return getName() + " " + getSet().toCode();
	}
	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return Arrays.asList(getSet());
	}
	@Override
	public FodotType getType() {
		return FodotType.INTEGER;
	}

	@Override @Deprecated
	public IFodotTypeEnumerationElement toEnumerationElement() {
		throw new RuntimeException("Can't convert an aggregate to enumeration element");
	}


}
