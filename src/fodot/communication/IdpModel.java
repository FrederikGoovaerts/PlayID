package fodot.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.enumerations.FodotConstantFunctionEnumeration;
import fodot.objects.structure.enumerations.FodotFunctionEnumeration;
import fodot.objects.structure.enumerations.FodotPredicateEnumeration;
import fodot.objects.structure.enumerations.FodotTypeEnumeration;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;

public class IdpModel {

	private FodotStructure structure;
	private FodotVocabulary vocabulary;

	public IdpModel(FodotVocabulary vocabulary) {
		super();
		this.vocabulary = vocabulary;
		this.structure = new FodotStructure(vocabulary);
	}

	public void addTypeResult(String name,	List<String> extractSinglevaluedDomain) {
		FodotType type = ((FodotTypeDeclaration) getVocabulary().getElementWithName(name)).getType();
		List<FodotConstant> domain = new ArrayList<FodotConstant>();
		for (String s : extractSinglevaluedDomain) {
			domain.add(new FodotConstant(s, type));
		}
		structure.addElement(new FodotTypeEnumeration(type, domain));
	}

	public void addPredicateResult(String name, List<List<String>> extractMultivaluedDomain) {
		FodotPredicateDeclaration decl = (FodotPredicateDeclaration) getVocabulary().getElementWithName(name);
		List<FodotConstant[]> domain = new ArrayList<FodotConstant[]>();
		for (List<String> s : extractMultivaluedDomain) {
			FodotConstant[] domainEl = new FodotConstant[s.size()];
			for (int i = 0; i < domainEl.length; i++) {
				domainEl[i] = new FodotConstant(s.get(i), decl.getArgumentType(i));
			}
			domain.add(domainEl);
		}
		structure.addElement(new FodotPredicateEnumeration(decl, domain));

	}

	public void addFunctionResult(String name,	Map<List<String>, String> extractMultivaluedResultDomain) {
		FodotFunctionDeclaration decl = (FodotFunctionDeclaration) getVocabulary().getElementWithName(name);
		Map<FodotConstant[], FodotConstant> domain = new HashMap<FodotConstant[], FodotConstant>();
		for (List<String> s : extractMultivaluedResultDomain.keySet()) {
			FodotConstant[] domainEl = new FodotConstant[s.size()];
			FodotConstant returnVal = new FodotConstant(extractMultivaluedResultDomain.get(s), decl.getReturnType());
			domainEl = new FodotConstant[s.size()];
			for (int i = 0; i < s.size(); i++) {
				domainEl[i] = new FodotConstant(s.get(i), decl.getArgumentType(i));
			}
			domain.put(domainEl,returnVal);
		}
		structure.addElement(new FodotFunctionEnumeration(decl, domain));
	}	

	public void addConstantFunctionResult(String name, String value) {
		FodotFunctionDeclaration decl = (FodotFunctionDeclaration) getVocabulary().getElementWithName(name);
		structure.addElement(new FodotConstantFunctionEnumeration(decl, new FodotConstant(value, decl.getReturnType())));
		
	}

	public FodotStructure getStructure() {
		return structure;
	}

	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}

}
