package fodot.gdl_parser.firstphase.data;

import java.util.ArrayList;
import java.util.List;

import fodot.gdl_parser.firstphase.data.occurrences.GdlConstantOccurrence;
import fodot.objects.vocabulary.elements.FodotType;

public class GdlConstantData implements IGdlTermData {
	private FodotType type;
	private List<GdlConstantOccurrence> occurrences = new ArrayList<GdlConstantOccurrence>();
	
	public GdlConstantData(FodotType type) {
		this.type = type;
	}

	public FodotType getType() {
		return type;
	}
	
	public void setType(FodotType type) {
		this.type = type;
	}
	
	public List<GdlConstantOccurrence> getOccurences() {
		return occurrences;
	}
	
	public void addOccurences(GdlConstantOccurrence occurrence) {
		this.occurrences.add(occurrence);
	}
	
}
