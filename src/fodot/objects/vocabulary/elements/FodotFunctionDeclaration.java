package fodot.objects.vocabulary.elements;

import java.util.List;

import fodot.objects.util.CollectionUtil;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotFunctionDeclaration extends FodotArgumentListDeclaration {

	private final FodotType returnType;
	
	public FodotFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType) {
		super(name, argumentTypes);
		this.returnType = returnType;
	}

	public FodotType getReturnType() {
		return returnType;
	}

	@Override
	public String toCode() {
		return getName() + CollectionUtil.toCoupleAsCode(getArgumentTypes()) + " : " + getReturnType().getTypeName();
	}
    
}
