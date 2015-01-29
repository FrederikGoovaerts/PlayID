package fodot.gdl_parser.firstphase.objects;

import java.util.ArrayList;
import java.util.List;

import fodot.gdl_parser.firstphase.objects.occurrences.GdlPredicateOccurrence;
import fodot.objects.vocabulary.elements.FodotType;

public class GdlPredicateData {

	private List<FodotType> argumentTypes;
	private List<GdlPredicateOccurrence> occurrences = new ArrayList<GdlPredicateOccurrence>();
	
	public GdlPredicateData(List<FodotType> argumentTypes) {
		this.argumentTypes = argumentTypes;
	}

	public FodotType getArgumentType(int index) {
		return this.argumentTypes.get(index);
	}
	
	public void setArgumentType(int index, FodotType type) {
		this.argumentTypes.set(index, type);
	}
	
	public List<GdlPredicateOccurrence> getOccurences() {
		return occurrences;
	}
	
	public void addOccurence(GdlPredicateOccurrence occurrence) {
		this.occurrences.add(occurrence);
	}
}
