package fodot.objects.theory.elements.terms.aggregates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import fodot.exceptions.fodot.FodotException;
import fodot.objects.general.FodotElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.formulas.IFodotFormula;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.util.CollectionPrinter;

public class FodotSet extends FodotElement implements IFodotElement {
	private List<FodotVariable> variables;
	private IFodotFormula formula;
	private IFodotTerm term;

	//Constructors	
	public FodotSet(List<FodotVariable> variables, IFodotFormula formula,
			IFodotTerm term) {
		super();
		setFormula(formula);
		setVariables(variables);
		setTerm(term);
	}

	public FodotSet(List<FodotVariable> variables, IFodotFormula formula) {
		this(variables, formula, null);
	}

	public FodotSet(IFodotFormula formula) {
		this(null, formula, null);
	}


	//Variables
	public List<FodotVariable> getVariables() {
		return variables;
	}
	private void setVariables(List<FodotVariable> variables) {
		if (variables == null) {
			this.variables = new ArrayList<FodotVariable>(formula.getFreeVariables());
		} else {
			this.variables = variables;
		}
		if (this.variables.isEmpty()) {
			throw new FodotException("A set can't contain zero variables");
		}
	}
	public Set<FodotVariable> getFreeVariables() {
		Set<FodotVariable> freeVariables = getFormula().getFreeVariables();
		freeVariables.addAll(getTerm().getFreeVariables());
		freeVariables.removeAll(getVariables());
		return freeVariables;
	}
	
	
	//Formula
	public IFodotFormula getFormula() {
		return formula;
	}
	private void setFormula(IFodotFormula formula) {
		this.formula = formula;
	}
	
	//Term
	public IFodotTerm getTerm() {
		return term;
	}
	private void setTerm(IFodotTerm term) {
		this.term = term;
	}
	public boolean hasTerm() {
		return getTerm() != null;
	}

	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append("{ ");
		builder.append(
				CollectionPrinter.printStringList("", "", " ",
						CollectionPrinter.toCode(getVariables())) );
		builder.append(" : ");
		builder.append(getFormula().toCode());
		if (hasTerm()) {
			builder.append(" : ");
			builder.append(getTerm().toCode());
		}
		builder.append(" }");
		return builder.toString();
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		ArrayList<IFodotElement> result = new ArrayList<IFodotElement>();
		result.addAll(getVariables());
		result.add(getFormula());
		result.add(getTerm());
		return result;
	}


}
