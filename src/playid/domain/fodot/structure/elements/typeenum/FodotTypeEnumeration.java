package playid.domain.fodot.structure.elements.typeenum;

import java.util.Collection;

import playid.domain.fodot.structure.elements.FodotEnumeration;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.fodot.vocabulary.elements.FodotType;
import playid.domain.fodot.vocabulary.elements.FodotTypeDeclaration;

public class FodotTypeEnumeration extends FodotEnumeration<IFodotTypeEnumerationElement> {
	
	public FodotTypeEnumeration(FodotTypeDeclaration typeDeclaration, Collection<? extends IFodotTypeEnumerationElement> values) {
		super(typeDeclaration, values);
	}

	public FodotTypeEnumeration(FodotType type, Collection<? extends IFodotTypeEnumerationElement> values) {
		this(type == null? null : type.getDeclaration(), values);
	}
	
	public FodotTypeEnumeration(FodotTypeDeclaration typeDeclaration) {
		this(typeDeclaration, null);
	}

}
