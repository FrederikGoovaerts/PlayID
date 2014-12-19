package fodot.objects.vocabulary.elements;

import java.util.List;

import fodot.util.CollectionUtil;

public class FodotPredicateTermDeclaration extends
		FodotArgumentListDeclaration implements IFodotDomainElement {

	private FodotType type;
	
	public FodotPredicateTermDeclaration(String name, List<FodotType> argumentTypes, FodotType elementOfType) {
		super(name, argumentTypes);
		setType(elementOfType);
	}

	
	public FodotType getType() {
		return type;
	}



	private void setType(FodotType type) {
//		if (this.type != null) {
//			this.type.removeDomainElement(this);
//		}
		this.type = type;
		this.type.addDomainElement(this);
	}



	@Override
	public String toCode() {
		return getName() + CollectionUtil.toCoupleAsCode(getArgumentTypes());
	}

}
