package fodot.objects.theory.elements.terms.aggregates;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import fodot.objects.general.FodotElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotType;

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


}
