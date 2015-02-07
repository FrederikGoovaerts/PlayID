package fodot.gdl_parser.firstphase.data;

import java.util.ArrayList;
import java.util.List;

import fodot.gdl_parser.firstphase.data.occurrences.GdlFunctionOccurrence;
import fodot.objects.vocabulary.elements.FodotType;

public class GdlFunctionData implements IGdlTermData, IGdlArgumentListData {
	private FodotType type;
	private List<FodotType> argumentTypes;
	private List<GdlFunctionOccurrence> occurrences = new ArrayList<GdlFunctionOccurrence>();
	
	public GdlFunctionData(FodotType returnType, List<FodotType> argumentTypes ) {
		setType(returnType);
		this.argumentTypes = argumentTypes;
	}
	
	public FodotType getType() {
		return type;
	}
	
	public void setType(FodotType type) {
		this.type = type;
	}
	
	public FodotType getArgumentType(int index) {
		return this.argumentTypes.get(index);
	}
	
	public List<FodotType> getArgumentTypes() {
		return new ArrayList<FodotType>(argumentTypes);
	}
	
	public int getAmountOfArguments() {
		return argumentTypes.size();
	}
	
	public void setArgumentType(int index, FodotType type) {
		this.argumentTypes.set(index, type);
	}
	
	public List<GdlFunctionOccurrence> getOccurences() {
		return occurrences;
	}
	
	public void addOccurence(GdlFunctionOccurrence occurrence) {
		this.occurrences.add(occurrence);
	}
}
