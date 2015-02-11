package fodot.communication.gdloutput;

import org.ggp.base.util.gdl.grammar.GdlTerm;

import fodot.objects.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;

public interface IFodotGdlTranslator {
	GdlTerm translate(IFodotTypeEnumerationElement fodot);
}
