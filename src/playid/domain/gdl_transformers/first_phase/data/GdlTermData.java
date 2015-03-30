package playid.domain.gdl_transformers.first_phase.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import playid.domain.fodot.vocabulary.elements.FodotType;
import playid.domain.gdl_transformers.first_phase.data.occurrences.GdlTermOccurrence;

public class GdlTermData {
	private List<FodotType> types = new ArrayList<FodotType>();
	private boolean canHaveMultipleTypes = false;
	private Set<GdlTermOccurrence> occurrences = new LinkedHashSet<GdlTermOccurrence>();

	public GdlTermData(FodotType defaultType) {
		this.types.add(defaultType);
	}
	public List<FodotType> getTypes() {
		return types;
	}

	public void addType(FodotType argType) {
		if (this.types.size() > 0 && !canHaveMultipleTypes()) {
			this.types = new ArrayList<FodotType>(Arrays.asList(argType));
		} else {
			this.types.add(argType);
		}
	}

	public void removeType(FodotType argType) {
		this.types.remove(argType);
	}
	
	public boolean hasOnlyType(FodotType argType) {
		return types.size() == 1 && hasType(argType);
	}
	
	public boolean hasType(FodotType argType) {
		return types.contains(argType);
	}
	
	public boolean hasMultipleTypes() {
		return types.size() > 1;
	}

	public boolean canHaveMultipleTypes() {
		return canHaveMultipleTypes;
	}

	public void allowMultipleTypes() {
		this.canHaveMultipleTypes = true;
	}

	/**********************************************
	 *  Occurrences
	 ***********************************************/
	public Collection<GdlTermOccurrence> getOccurences() {
		return occurrences;
	}

	public void addOccurence(GdlTermOccurrence occurrence) {
		this.occurrences.add(occurrence);
	}
	/**********************************************/


}
