package fodot.gdl_parser.firstphase.data;

import java.util.ArrayList;
import java.util.List;

import fodot.gdl_parser.firstphase.data.occurrences.GdlVariableOccurrence;
import fodot.objects.vocabulary.elements.FodotType;

public class GdlVariableData {
	private FodotType type;
	private List<GdlVariableOccurrence> occurrences = new ArrayList<GdlVariableOccurrence>();
	
	public GdlVariableData(FodotType type) {
		this.type = type;
	}

	public FodotType getType() {
		return type;
	}
	
	public void setType(FodotType type) {
		this.type = type;
	}
	
	public List<GdlVariableOccurrence> getOccurences() {
		return occurrences;
	}
	
	public void addOccurences(GdlVariableOccurrence occurrence) {
		this.occurrences.add(occurrence);
	}
}
