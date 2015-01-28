package fodot.communication.gdloutput;

import org.ggp.base.util.gdl.grammar.GdlTerm;

import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;

public interface IFodotGdlTranslator {
	GdlTerm translate(IFodotTypeEnumerationElement fodot);
}
