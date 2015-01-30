package fodot.gdl_parser.firstphase.data;

import java.util.ArrayList;
import java.util.List;

import fodot.exceptions.gdl.GdlTypeIdentificationError;
import fodot.gdl_parser.firstphase.data.occurrences.GdlPredicateOccurrence;
import fodot.objects.vocabulary.elements.FodotType;

public class GdlPredicateData implements IGdlArgumentListData {

	private List<FodotType> argumentTypes;
	private List<GdlPredicateOccurrence> occurrences = new ArrayList<GdlPredicateOccurrence>();
	private boolean isDynamic = false;
	private boolean typesLocked = false;
	
	public GdlPredicateData(List<FodotType> argumentTypes, boolean typesLocked) {
		this.argumentTypes = argumentTypes;
		this.typesLocked = typesLocked;
	}

	/**********************************************
	 *  Arguments
	 ***********************************************/
	public FodotType getArgumentType(int index) {
		return this.argumentTypes.get(index);
	}	
	public void setArgumentType(int index, FodotType type) {
		if (typesLocked) {
			throw new GdlTypeIdentificationError("Can't update a locked type!");
		}
		this.argumentTypes.set(index, type);
	}
	public void lockTypes() {
		this.typesLocked = true;
	}
	public boolean isTypeLocked() {
		return this.typesLocked;
	}
	/**********************************************/

	/**********************************************
	 *  Occurrences
	 ***********************************************/
	public List<GdlPredicateOccurrence> getOccurences() {
		return occurrences;
	}
	
	public void addOccurence(GdlPredicateOccurrence occurrence) {
		this.occurrences.add(occurrence);
	}
	/**********************************************/
	
	
	/**********************************************
	 *  Dynamic
	 ***********************************************/
	public void makeDynamic() {
		this.isDynamic = true;
	}
	
	public boolean isDynamic() {
		return this.isDynamic;
	}
	/**********************************************/
}
