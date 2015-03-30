package playid.domain.communication.gdloutput;

import org.ggp.base.util.gdl.grammar.GdlTerm;

import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;

public interface IFodotGdlTranslator {
	GdlTerm translate(IFodotTypeEnumerationElement fodot);
}
