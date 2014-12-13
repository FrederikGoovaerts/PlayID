package fodot.helpers;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fodot.objects.Fodot;
import fodot.objects.procedure.FodotProcedure;
import fodot.objects.procedure.FodotProcedures;
import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.formulas.argumented.FodotPredicate;
import fodot.objects.sentence.formulas.connectors.FodotFormulaConnector;
import fodot.objects.sentence.formulas.connectors.FodotTermConnector;
import fodot.objects.sentence.formulas.quantifiers.FodotQuantifier;
import fodot.objects.sentence.formulas.unary.FodotNot;
import fodot.objects.sentence.terms.FodotConstant;
import fodot.objects.sentence.terms.FodotFunction;
import fodot.objects.sentence.terms.FodotVariable;
import fodot.objects.sentence.terms.IFodotTerm;
import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.enumerations.FodotEnumeration;
import fodot.objects.structure.enumerations.FodotFunctionEnumeration;
import fodot.objects.structure.enumerations.FodotPredicateEnumeration;
import fodot.objects.structure.enumerations.FodotTypeEnumeration;
import fodot.objects.theory.FodotSentence;
import fodot.objects.theory.FodotTheory;
import fodot.objects.theory.definitions.FodotInductiveDefinitionBlock;
import fodot.objects.theory.definitions.FodotInductiveDefinitionConnector;
import fodot.objects.theory.definitions.FodotInductiveFunction;
import fodot.objects.vocabulary.FodotLTCVocabulary;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.util.FormulaUtil;
import fodot.util.NameUtil;

public class FodotPartBuilder {
	
	// FORMULA CONNECTORS
	private static final String AND_SYMBOL = "&";
	 
	public static FodotFormulaConnector createAnd(Collection<IFodotFormula> forms) {
		return new FodotFormulaConnector(AND_SYMBOL, forms);
	}
	
	public static FodotFormulaConnector createAnd(IFodotFormula... forms) {
		return createAnd(Arrays.asList(forms));
	}

	private static final String OR_SYMBOL = "|";
	
	public static FodotFormulaConnector createOr(Collection<IFodotFormula> forms) {
		return new FodotFormulaConnector(OR_SYMBOL, forms);
	}
	
	public static FodotFormulaConnector createOr(IFodotFormula... forms) {
		return createOr(Arrays.asList(forms));
	}

	private static final String IMPLIES_SYMBOL = "=>";
	
	public static FodotFormulaConnector createImplies(IFodotFormula form1, IFodotFormula form2) {
		return new FodotFormulaConnector(form1, IMPLIES_SYMBOL, form2);
	}

	private static final String IS_IMPLIED_SYMBOL = "<=";
	
	public static FodotFormulaConnector createIsImpliedBy(IFodotFormula form1, IFodotFormula form2) {
		return new FodotFormulaConnector(form1, IS_IMPLIED_SYMBOL, form2);
	}

	private static final String EQUIVALENT_SYMBOL = "<=>";
	
	public static FodotFormulaConnector createEquivalent(IFodotFormula form1, IFodotFormula form2) {
		return new FodotFormulaConnector(form1, EQUIVALENT_SYMBOL, form2);
	}
	
	//TERM CONNECTORS
	private static final String EQUALS_SYMBOL = "=";
	
	public static FodotTermConnector createEquals(IFodotTerm term1, IFodotTerm term2) {
		return new FodotTermConnector(term1, EQUALS_SYMBOL, term2);
	}

	private static final String DISTINCT_SYMBOL = "~=";
	
	public static FodotTermConnector createDistinct(IFodotTerm term1, IFodotTerm term2) {
		return new FodotTermConnector(term1, DISTINCT_SYMBOL, term2);
	}
	
	//QUANTIFIERS	
	
	//Lil' helper
	private static Set<FodotVariable> wrapVariableInSet(FodotVariable var) {
		HashSet<FodotVariable> variables = new HashSet<FodotVariable>();
		variables.add(var);
		return variables;		
	}
	
	private static final String FORALL_SYMBOL = "!";
	
	public static FodotQuantifier createForAll(Set<FodotVariable> set, IFodotFormula formula) {
		return new FodotQuantifier(FORALL_SYMBOL, set, formula);
	}
	public static FodotQuantifier createForAll(FodotVariable variable, IFodotFormula formula) {
		return createForAll(wrapVariableInSet(variable), formula);
	}

	private static final String EXISTS_SYMBOL = "?";	
	
	public static FodotQuantifier createExists(Set<FodotVariable> set, IFodotFormula formula) {
		return new FodotQuantifier(EXISTS_SYMBOL, set, formula);
	}
	
	public static FodotQuantifier createExists(FodotVariable variable, IFodotFormula formula) {
		return createExists(wrapVariableInSet(variable), formula);
	}
	
	//Awesome exists
	private static final String EXISTS_EXACTLY_SYMBOL = EXISTS_SYMBOL + "=";
	public static FodotQuantifier createExistsExactly(int amount, Set<FodotVariable> set, IFodotFormula formula) {
		return new FodotQuantifier(EXISTS_EXACTLY_SYMBOL + amount, set, formula);
	}
	
	public static FodotQuantifier createExistsExactly(int amount, FodotVariable var, IFodotFormula formula) {
		return createExistsExactly(amount, wrapVariableInSet(var), formula);		
	}
		
	private static final String EXISTS_LESS_THAN_SYMBOL = EXISTS_SYMBOL + "<";
	public static FodotQuantifier createExistsLessThan(int amount, Set<FodotVariable> set, IFodotFormula formula) {
		return new FodotQuantifier(EXISTS_LESS_THAN_SYMBOL + amount, set, formula);
	}
	
	public static FodotQuantifier createExistsLessThan(int amount, FodotVariable var, IFodotFormula formula) {
		return createExistsLessThan(amount, wrapVariableInSet(var), formula);		
	}

	private static final String EXISTS_AT_MOST_SYMBOL = EXISTS_SYMBOL + "=<";
	public static FodotQuantifier createExistsAtMost(int amount, Set<FodotVariable> set, IFodotFormula formula) {
		return new FodotQuantifier(EXISTS_AT_MOST_SYMBOL + amount, set, formula);
	}
	
	public static FodotQuantifier createExistsAtMost(int amount, FodotVariable var, IFodotFormula formula) {
		return createExistsAtMost(amount, wrapVariableInSet(var), formula);		
	}

	private static final String EXISTS_MORE_THAN_SYMBOL = EXISTS_SYMBOL + ">";
	public static FodotQuantifier createExistsMoreThan(int amount, Set<FodotVariable> set, IFodotFormula formula) {
		return new FodotQuantifier(EXISTS_MORE_THAN_SYMBOL + amount, set, formula);
	}
	
	public static FodotQuantifier createExistsMoreThan(int amount, FodotVariable var, IFodotFormula formula) {
		return createExistsMoreThan(amount, wrapVariableInSet(var), formula);		
	}

	//TERM RELATED	
	public static FodotNot createNot(IFodotFormula form) {
		return new FodotNot(form);
	}
	
	public static FodotConstant createConstant(String value) {
		return new FodotConstant(value);
	}

	public static FodotVariable createVariable(String name, FodotType type) {
		return new FodotVariable(name, type);
	}
	
	public static FodotVariable createVariable(FodotType type) {
		return createVariable(NameUtil.generateVariableName(), type);
	}
	
	public static FodotPredicate createPredicate(FodotPredicateDeclaration declaration, List<IFodotTerm> arguments) {
		return new FodotPredicate(declaration, arguments);
	}
	
	public static FodotPredicate createPredicate(FodotPredicateDeclaration declaration, IFodotTerm... arguments) {
		return new FodotPredicate(declaration, Arrays.asList(arguments));
	}
	
	public static FodotFunction createFunction(FodotFunctionDeclaration declaration, List<IFodotTerm> arguments) {
		return new FodotFunction(declaration, arguments);
	}
	
	public static FodotFunction createFunction(FodotFunctionDeclaration declaration, IFodotTerm... arguments) {
		return new FodotFunction(declaration, Arrays.asList(arguments));
	}

	//ENUMERATIONS
	
	public static FodotFunctionEnumeration createFunctionEnumeration(
			FodotFunctionDeclaration declaration, Map<FodotConstant[], FodotConstant> values) {
		return new FodotFunctionEnumeration(declaration, values);
	}
	
	public static FodotFunctionEnumeration createFunctionEnumeration(FodotFunctionDeclaration declaration) {
		return new FodotFunctionEnumeration(declaration);
	}
	
	public static FodotPredicateEnumeration createPredicateEnumeration(
			FodotPredicateDeclaration declaration, List<FodotConstant[]> values) {
		return new FodotPredicateEnumeration(declaration, values);
	}
	
	public static FodotPredicateEnumeration createPredicateEnumeration(FodotPredicateDeclaration declaration) {
		return new FodotPredicateEnumeration(declaration);
	}
	
	public static FodotTypeEnumeration createTypeEnumeration(FodotType type, List<FodotConstant> values) {
		return new FodotTypeEnumeration(type, values);
	}
	
	public static FodotTypeEnumeration createTypeEnumeration(FodotTypeDeclaration type, List<FodotConstant> values) {
		return new FodotTypeEnumeration(type.getType(), values);
	}
	
	
	//INDUCTIVE DEFINITIONS
	
	public static FodotInductiveDefinitionBlock createInductiveDefinition(List<FodotSentence> sentences) {
		return new FodotInductiveDefinitionBlock(sentences);
	}
	
	public static FodotInductiveDefinitionConnector createInductiveDefinitionConnector(FodotPredicate head, IFodotFormula body) {
		return new FodotInductiveDefinitionConnector(head, body);
	}
	
	public static FodotInductiveDefinitionConnector createInductiveDefinitionConnector(FodotInductiveFunction head, IFodotFormula body) {
		return new FodotInductiveDefinitionConnector(head, body);
	}

	public static FodotInductiveFunction createInductiveFunctionHead(FodotFunction function, IFodotTerm functionResult) {
		return new FodotInductiveFunction(function, functionResult);
	}
	
	//SENTENCE	
	public static FodotSentence createSentence(IFodotFormula formula) {
		formula = FormulaUtil.makeVariableFree(formula);
		return new FodotSentence(formula);
	}
	
	
	//DECLARATIONS
	public static FodotFunctionDeclaration createFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType, boolean isPartial) {
		return new FodotFunctionDeclaration(name, argumentTypes, returnType, isPartial);
	}

	public static FodotFunctionDeclaration createPartialFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType) {
		return createFunctionDeclaration(name, argumentTypes, returnType, true);
	}

	public static FodotFunctionDeclaration createCompleteFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType) {
		return createFunctionDeclaration(name, argumentTypes, returnType, false);
	}
	
	public static FodotPredicateDeclaration createPredicateDeclaration(String name, List<FodotType> argumentTypes) {
		return new FodotPredicateDeclaration(name, argumentTypes);
	}
	
	public static FodotTypeDeclaration createTypeDeclaration(FodotType type, Set<FodotConstant> domain, Set<FodotType> supertypes, Set<FodotType> subtypes) {
		return new FodotTypeDeclaration(type, domain, supertypes, subtypes);
	}
	
	public static FodotTypeDeclaration createTypeDeclaration(FodotType type, Set<FodotConstant> domain, Set<FodotType> supertypes) {
		return createTypeDeclaration(type, domain, supertypes, null);
	}
	
	public static FodotTypeDeclaration createTypeDeclaration(FodotType type, Set<FodotConstant> domain) {
		return createTypeDeclaration(type, domain, null, null);
	}
	
	
	public static FodotTypeDeclaration createTypeDeclaration(FodotType type) {
		return createTypeDeclaration(type, null, null, null);
	}
	
	public static FodotType createType(String name) {
		return new FodotType(name);
	}
	
	// == FODOT ESSENTIALS ==
	
	//FODOT THEORY
	public static FodotTheory createTheory(String name, FodotVocabulary vocabulary, Set<FodotSentence> sentences) {
		return new FodotTheory(name, vocabulary, sentences);
	}
	
	public static FodotTheory createTheory(String name, FodotVocabulary voc) {
		return createTheory(name, voc, null);
	}
	
	public static FodotTheory createTheory(FodotVocabulary voc, Set<FodotSentence> sentences) {
		return createTheory(null, voc, sentences);
	}

	public static FodotTheory createTheory(FodotVocabulary voc) {
		return createTheory(null, voc, null);
	}
	
	
	//STRUCTURE	
	public static FodotStructure createStructure(String name, FodotVocabulary vocabulary, List<FodotEnumeration> enumerations) {
		return new FodotStructure(name, vocabulary, enumerations);
	}
	
	public static FodotStructure createStructure(String name, FodotVocabulary voc) {
		return createStructure(name, voc, null);
	}
	
	public static FodotStructure createStructure(FodotVocabulary voc) {
		return createStructure(null, voc, null);
	}
		
	public static FodotStructure createStructure(String name) {
		return createStructure(name, null, null);
	}
	
	public static FodotStructure createStructure() {
		return createStructure(null, null, null);
	}
	
	//PROCEDURES
	public static FodotProcedures createProcedures(String name, List<String> arguments, List<FodotProcedure> procedures) {
		return new FodotProcedures(name, arguments, procedures);
	}
	
	public static FodotProcedures createProcedures(String name, List<FodotProcedure> procedures) {
		return createProcedures(name, null, procedures);
	}
	
	public static FodotProcedures createProcedures(List<FodotProcedure> procedures) {
		return createProcedures(null, null, procedures);
	}

	public static FodotProcedures createProcedures(String name) {
		return createProcedures(name, null, null);
	}

	public static FodotProcedures createProcedures() {
		return createProcedures(null, null, null);
	}
	
	public static FodotProcedure createProcedure(String procedure) {
		return new FodotProcedure(procedure);
	}
	
	//VOCABULARY
	public static FodotVocabulary createVocabulary(String name, Set<FodotTypeDeclaration> types, Set<FodotPredicateDeclaration> predicates,
			Set<FodotFunctionDeclaration> functions) {
		return new FodotVocabulary(name, types, predicates, functions);
	}

	public static FodotVocabulary createVocabulary(Set<FodotTypeDeclaration> types, Set<FodotPredicateDeclaration> predicates,
			Set<FodotFunctionDeclaration> functions) {
		return createVocabulary(null, types, predicates, functions);
	}
	
	public static FodotVocabulary createVocabulary(String name) {
		return createVocabulary(name, null, null, null);
	}
	
	public static FodotVocabulary createVocabulary() {
		return createVocabulary(null, null, null, null);
	}
	
	public static FodotLTCVocabulary createLTCVocabulary(String name, Set<FodotTypeDeclaration> types, Set<FodotPredicateDeclaration> predicates,
			Set<FodotFunctionDeclaration> functions) {
		return new FodotLTCVocabulary(name, types, predicates, functions);
	}
	
	public static FodotLTCVocabulary createLTCVocabulary(String name) {
		return createLTCVocabulary(name, null, null, null);
	}
	
	public static FodotLTCVocabulary creatLTCeVocabulary(Set<FodotTypeDeclaration> types, Set<FodotPredicateDeclaration> predicates,
			Set<FodotFunctionDeclaration> functions) {
		return createLTCVocabulary(null, types, predicates, functions);
	}
	
	public static FodotLTCVocabulary createLTCVocabulary() {
		return createLTCVocabulary(null, null, null, null);
	}
	
	//FODOT ITSELF
	public static Fodot createFodot(FodotVocabulary voc, FodotTheory theory, FodotStructure struc, FodotProcedures procedures) {
		return new Fodot(voc, theory, struc, procedures);
	}
	
	public static Fodot createFodot() {
		FodotVocabulary voc = createVocabulary();
		return createFodot(voc);
	}
	
	public static Fodot createFodot(FodotVocabulary voc) {
		return createFodot(voc, createTheory(voc), createStructure(voc), createProcedures());
	}
	
}