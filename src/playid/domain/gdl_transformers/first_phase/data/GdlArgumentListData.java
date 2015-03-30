package playid.domain.gdl_transformers.first_phase.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import playid.domain.exceptions.gdl.GdlTypeIdentificationError;
import playid.domain.fodot.vocabulary.elements.FodotType;
import playid.domain.gdl_transformers.first_phase.data.declarations.IGdlTermDeclaration;
public class GdlArgumentListData {
	private List<FodotType> argumentTypes;
	private Map<Integer, Set<IGdlTermDeclaration>> argumentOccurrences = new HashMap<Integer, Set<IGdlTermDeclaration>>();
	private boolean typeLocked;
	
	public GdlArgumentListData(List<FodotType> argumentTypes) {
		super();
		this.argumentTypes = argumentTypes;
		for (int i = 0; i < argumentTypes.size(); i++) {
			argumentOccurrences.put(i, new LinkedHashSet<IGdlTermDeclaration>());
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
	public void addArgumentType(int i, FodotType argType) {
		this.argumentTypes.add(i, argType);
	}
	public int getAmountOfArguments() {
		return argumentTypes.size();
	}
	
	/**********************************************
	 *  Occurrences
	 ***********************************************/
	public void addArgumentOccurrence(int i, IGdlTermDeclaration term) {
		argumentOccurrences.get(i).add(term);		
	}
	public Collection<IGdlTermDeclaration>  getArgumentOccurrences(int index) {
		return argumentOccurrences.get(index);
	}
	/**********************************************/
}
