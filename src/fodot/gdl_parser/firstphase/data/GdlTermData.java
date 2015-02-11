package fodot.gdl_parser.firstphase.data;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import fodot.gdl_parser.firstphase.data.occurrences.GdlTermOccurrence;
import fodot.objects.vocabulary.elements.FodotType;

public class GdlTermData implements IGdlTermData {
	private FodotType type;
	private Set<GdlTermOccurrence> occurrences = new LinkedHashSet<GdlTermOccurrence>();
	
	public GdlTermData(FodotType type) {
		this.type = type;
	}

	public FodotType getType() {
		return type;
	}
	
	public void setType(FodotType type) {
		this.type = type;
	}
	
	public Collection<GdlTermOccurrence> getOccurences() {
		return occurrences;
	}
	
	public void addOccurence(GdlTermOccurrence occurrence) {
		this.occurrences.add(occurrence);
	}
}
