package fodot.gdl_parser.first_phase.data;

import java.util.Collection;

import fodot.gdl_parser.first_phase.data.occurrences.GdlTermOccurrence;
import fodot.objects.vocabulary.elements.FodotType;

public interface IGdlTermData {
	FodotType getType();
	void setType(FodotType type);
	Collection<? extends GdlTermOccurrence> getOccurences();
}
