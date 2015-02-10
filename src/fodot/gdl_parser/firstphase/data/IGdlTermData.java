package fodot.gdl_parser.firstphase.data;

import java.util.Collection;

import fodot.gdl_parser.firstphase.data.occurrences.IGdlTermOccurrence;
import fodot.objects.vocabulary.elements.FodotType;

public interface IGdlTermData {
	FodotType getType();
	void setType(FodotType type);
	Collection<? extends IGdlTermOccurrence> getOccurences();
}
