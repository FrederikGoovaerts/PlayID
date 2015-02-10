package fodot.gdl_parser.firstphase.data;

import java.util.ArrayList;
import java.util.List;

import fodot.exceptions.gdl.GdlTypeIdentificationError;
import fodot.gdl_parser.firstphase.data.occurrences.GdlPredicateOccurrence;
import fodot.objects.vocabulary.elements.FodotType;

public class GdlPredicateData extends GdlArgumentListData {

	private List<GdlPredicateOccurrence> occurrences = new ArrayList<GdlPredicateOccurrence>();
	private boolean typesLocked = false;
	
	public GdlPredicateData(List<FodotType> argArgumentTypes, boolean argTypesLocked) {
		super(new ArrayList<FodotType>(argArgumentTypes));
		this.typesLocked = argTypesLocked;
	}

	/**********************************************
	 *  Arguments
	 ***********************************************/
	public void setArgumentType(int index, FodotType type) {
		if (typesLocked) {
			throw new GdlTypeIdentificationError("Can't update a locked type!");
		}
		super.setArgumentType(index, type);
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
}
