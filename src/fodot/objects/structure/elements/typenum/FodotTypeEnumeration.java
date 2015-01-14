package fodot.objects.structure.elements.typenum;

import java.util.Collection;

import fodot.objects.structure.elements.FodotEnumeration;
import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;

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
