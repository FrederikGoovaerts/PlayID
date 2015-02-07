package fodot.gdl_parser.firstphase.data;

import java.util.Collection;
import java.util.List;

import fodot.gdl_parser.firstphase.data.occurrences.GdlArgumentListOccurrence;
import fodot.objects.vocabulary.elements.FodotType;

public interface IGdlArgumentListData {
	FodotType getArgumentType(int index);
	List<FodotType> getArgumentTypes();
	void setArgumentType(int index, FodotType type);
	Collection<? extends GdlArgumentListOccurrence> getOccurences();
}
