package fodot.gdl_parser.firstphase.data;

import java.util.ArrayList;
import java.util.List;

import fodot.gdl_parser.firstphase.data.occurrences.GdlFunctionOccurrence;
import fodot.objects.vocabulary.elements.FodotType;

public class GdlFunctionData extends GdlArgumentListData implements IGdlTermData, IGdlArgumentListData {
	private FodotType type;
	private List<GdlFunctionOccurrence> occurrences = new ArrayList<GdlFunctionOccurrence>();
	
	public GdlFunctionData(FodotType returnType, List<FodotType> argumentTypes ) {
		super(argumentTypes);
		setType(returnType);
	}
	
	public FodotType getType() {
		return type;
	}
	
	public void setType(FodotType type) {
		this.type = type;
	}
	
	public List<GdlFunctionOccurrence> getOccurences() {
		return occurrences;
	}
	
	public void addOccurence(GdlFunctionOccurrence occurrence) {
		this.occurrences.add(occurrence);
	}
}
