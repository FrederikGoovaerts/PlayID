package fodot.gdl_parser.firstphase.data.occurrences;

import java.util.ArrayList;
import java.util.List;

import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlTerm;

public abstract class GdlArgumentListOccurrence {
	private GdlRule rule;
	private List<GdlTerm> arguments;
	
	public GdlArgumentListOccurrence(GdlRule rule, List<GdlTerm> arguments) {
		super();
		setRule(rule);
		setArguments(arguments);
	}

	/**********************************************
	 *  Getters&Setters
	 ***********************************************/

	public GdlRule getRule() {
		return rule;
	}

	private void setRule(GdlRule rule) {
		this.rule = rule;
	}

	public List<GdlTerm> getArguments() {
		return new ArrayList<GdlTerm>(arguments);
	}
	
	public GdlTerm getArgument(int index) {
		return arguments.get(index);
	}

	private void setArguments(List<GdlTerm> arguments) {
		this.arguments = arguments;
	}

	/**********************************************/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GdlArgumentListOccurrence other = (GdlArgumentListOccurrence) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		return true;
	}
	
	
	
}
