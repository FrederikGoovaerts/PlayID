package fodot.gdl_parser.firstphase.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fodot.exceptions.gdl.GdlTypeIdentificationError;
import fodot.gdl_parser.firstphase.data.declarations.IGdlTermDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
public class GdlArgumentListData implements IGdlArgumentListData {
	private List<FodotType> argumentTypes;
	private Map<Integer, Set<IGdlTermDeclaration>> argumentOccurrences = new HashMap<Integer, Set<IGdlTermDeclaration>>();
	private boolean typeLocked;
	
	public GdlArgumentListData(List<FodotType> argumentTypes) {
		super();
		this.argumentTypes = argumentTypes;
		for (int i = 0; i < argumentTypes.size(); i++) {
			argumentOccurrences.put(i, new HashSet<IGdlTermDeclaration>());
		}
	}
	public FodotType getArgumentType(int index) {
		return this.argumentTypes.get(index);
	}	
	public List<FodotType> getArgumentTypes() {
		return new ArrayList<FodotType>(argumentTypes);
	}
	public void setArgumentType(int index, FodotType type) {
		if (isTypeLocked()) {
			throw new GdlTypeIdentificationError("Can't update a locked type!");
		}
		this.argumentTypes.set(index, type);
	}
	public void lockTypes() {
		this.typeLocked = true;
	}
	public boolean isTypeLocked() {
		return this.typeLocked;
	}
	public void addArgumentType(FodotType argType) {
		this.argumentTypes.add(argType);
	}
	public int getAmountOfArguments() {
		return argumentTypes.size();
	}
	
	/**********************************************
	 *  Occurrences
	 ***********************************************/
	@Override
	public void addArgumentOccurrence(int i, IGdlTermDeclaration term) {
		argumentOccurrences.get(i).add(term);		
	}
	@Override
	public Collection<IGdlTermDeclaration>  getArgumentOccurrences(int index) {
		return argumentOccurrences.get(index);
	}
	/**********************************************/
}
