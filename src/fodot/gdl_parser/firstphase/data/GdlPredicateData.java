package fodot.gdl_parser.firstphase.data;

import java.util.ArrayList;
import java.util.List;

import fodot.gdl_parser.firstphase.data.occurrences.GdlPredicateOccurrence;
import fodot.objects.vocabulary.elements.FodotType;

public class GdlPredicateData implements IGdlArgumentListData {

	private List<FodotType> argumentTypes;
	private List<GdlPredicateOccurrence> occurrences = new ArrayList<GdlPredicateOccurrence>();
	private boolean isDynamic = false;
	
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
	
	public void makeDynamic() {
		this.isDynamic = true;
	}
	
	public boolean isDynamic() {
		return this.isDynamic;
	}
}
