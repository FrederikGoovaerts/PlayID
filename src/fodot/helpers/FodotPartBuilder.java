package fodot.helpers;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fodot.objects.Fodot;
import fodot.objects.comments.FodotBlankLines;
import fodot.objects.comments.FodotComment;
import fodot.objects.includes.FodotIncludeHolder;
import fodot.objects.includes.elements.FodotIncludeFile;
import fodot.objects.includes.elements.FodotIncludeLibrary;
import fodot.objects.includes.elements.FodotIncludeStatement;
import fodot.objects.procedure.FodotProcedureStatement;
import fodot.objects.procedure.FodotProcedures;
import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.elements.IFodotStructureElement;
import fodot.objects.structure.elements.functionenum.FodotConstantFunctionEnumeration;
import fodot.objects.structure.elements.functionenum.FodotFunctionEnumeration;
import fodot.objects.structure.elements.functionenum.FodotNumericalTypeRangeEnumeration;
import fodot.objects.structure.elements.functionenum.elements.FodotFunctionEnumerationElement;
import fodot.objects.structure.elements.functionenum.elements.IFodotFunctionEnumerationElement;
import fodot.objects.structure.elements.predicateenum.FodotPredicateEnumeration;
import fodot.objects.structure.elements.predicateenum.elements.FodotPredicateEnumerationElement;
import fodot.objects.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import fodot.objects.structure.elements.typenum.FodotTypeEnumeration;
import fodot.objects.structure.elements.typenum.elements.FodotPredicateTermTypeEnumerationElement;
import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.FodotTheory;
import fodot.objects.theory.elements.FodotSentence;
import fodot.objects.theory.elements.IFodotTheoryElement;
import fodot.objects.theory.elements.formulas.FodotFormulaConnector;
import fodot.objects.theory.elements.formulas.FodotNot;
import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.theory.elements.formulas.FodotQuantifier;
import fodot.objects.theory.elements.formulas.FodotTermConnector;
import fodot.objects.theory.elements.formulas.IFodotFormula;
import fodot.objects.theory.elements.inductivedefinitions.FodotInductiveDefinitionBlock;
import fodot.objects.theory.elements.inductivedefinitions.FodotInductiveDefinitionConnector;
import fodot.objects.theory.elements.inductivedefinitions.FodotInductiveFunction;
import fodot.objects.theory.elements.inductivedefinitions.FodotInductiveQuantifier;
import fodot.objects.theory.elements.inductivedefinitions.FodotInductiveSentence;
import fodot.objects.theory.elements.inductivedefinitions.IFodotInductiveDefinitionElement;
import fodot.objects.theory.elements.terms.FodotArithmeticConnector;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotFunction;
import fodot.objects.theory.elements.terms.FodotPredicateTerm;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.FodotLTCVocabulary;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateTermDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.objects.vocabulary.elements.IFodotDomainElement;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;
import fodot.util.FormulaUtil;
import fodot.util.NameUtil;

public class FodotPartBuilder {

	//COMMENTS
	public static FodotComment createComment(String... comments) {
		return new FodotComment(comments);
	}
	
	public static FodotBlankLines createBlankLines(int amountOfLines) {
		return new FodotBlankLines(amountOfLines);
	}
	
	// FORMULA CONNECTORS
	private static final String AND_SYMBOL = "&";

	public static FodotFormulaConnector createAnd(Collection<? extends IFodotFormula> forms) {
		return new FodotFormulaConnector(AND_SYMBOL, forms);
	}

	public static FodotFormulaConnector createAnd(IFodotFormula... forms) {
		return createAnd(Arrays.asList(forms));
	}

	private static final String OR_SYMBOL = "|";

	public static FodotFormulaConnector createOr(Collection<? extends IFodotFormula> forms) {
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

	private static final String LESS_THAN = "<";

	public static FodotTermConnector createLessThan(IFodotTerm term1, IFodotTerm term2) {
		return new FodotTermConnector(term1, LESS_THAN, term2);
	}

	public static IFodotFormula createLessThanOrEqualTo(IFodotTerm term1, IFodotTerm term2) {
		return createOr(createLessThan(term1, term2), createEquals(term1, term2));
	} 

	private static final String GREATER_THAN = ">";

	public static FodotTermConnector createGreaterThan(IFodotTerm term1, IFodotTerm term2) {
		return new FodotTermConnector(term1, GREATER_THAN, term2);
	}

	public static IFodotFormula createGreaterThanOrEqualTo(IFodotTerm term1, IFodotTerm term2) {
		return createOr(createGreaterThan(term1, term2), createEquals(term1, term2));
	} 

	//ARITHMETIC CONNECTORS
	private static final String ADDITION_SYMBOL = "+";

	public static FodotArithmeticConnector createAddition(IFodotTerm term1, IFodotTerm term2) {
		return new FodotArithmeticConnector(term1, ADDITION_SYMBOL, term2);
	}

	private static final String SUBTRACTION_SYMBOL = "-";

	public static FodotArithmeticConnector createSubtraction(IFodotTerm term1, IFodotTerm term2) {
		return new FodotArithmeticConnector(term1, SUBTRACTION_SYMBOL, term2);
	}

	private static final String MULTIPLICATION_SYMBOL = "*";

	public static FodotArithmeticConnector createMultiplication(IFodotTerm term1, IFodotTerm term2) {
		return new FodotArithmeticConnector(term1, MULTIPLICATION_SYMBOL, term2);
	}

	private static final String DIVISION_SYMBOL = "/";

	public static FodotArithmeticConnector createDivision(IFodotTerm term1, IFodotTerm term2) {
		return new FodotArithmeticConnector(term1, DIVISION_SYMBOL, term2);
	}

	private static final String MODULO_SYMBOL = "%";

	public static FodotArithmeticConnector createModulo(IFodotTerm term1, IFodotTerm term2) {
		return new FodotArithmeticConnector(term1, MODULO_SYMBOL, term2);
	}

	//Other arithmetics (IDP manual p8)
	private static final Map<FodotType, FodotFunctionDeclaration> SUCCESSOR_DECLARATIONS = new HashMap<FodotType, FodotFunctionDeclaration>(); 

	public static FodotFunction createSuccessor(IFodotTerm term) {
		FodotType type = term.getType();
		FodotFunctionDeclaration decl;
		if (SUCCESSOR_DECLARATIONS.containsKey(type)) {
			decl = SUCCESSOR_DECLARATIONS.get(type);
		} else {
			decl = createCompleteFunctionDeclaration("SUCC", Arrays.asList(type), type);
			SUCCESSOR_DECLARATIONS.put(type, decl);
		}
		return createFunction(decl, term);
	}

	private static final Map<FodotType, FodotFunctionDeclaration> PREDECESSOR_DECLARATIONS = new HashMap<FodotType, FodotFunctionDeclaration>(); 

	public static FodotFunction createPredecessor(IFodotTerm term) {
		FodotType type = term.getType();
		FodotFunctionDeclaration decl;
		if (PREDECESSOR_DECLARATIONS.containsKey(type)) {
			decl = PREDECESSOR_DECLARATIONS.get(type);
		} else {
			decl = createCompleteFunctionDeclaration("PRED", Arrays.asList(type), type);
			PREDECESSOR_DECLARATIONS.put(type, decl);
		}
		return createFunction(decl, term);
	}

	private static final Map<FodotType, FodotFunctionDeclaration> MINIMUM_DECLARATIONS = new HashMap<FodotType, FodotFunctionDeclaration>(); 

	public static FodotFunction createMinium(FodotType type) {
		FodotFunctionDeclaration decl;
		if (MINIMUM_DECLARATIONS.containsKey(type)) {
			decl = MINIMUM_DECLARATIONS.get(type);
		} else {
			decl = createCompleteFunctionDeclaration("MIN", Arrays.asList(type), type);
			MINIMUM_DECLARATIONS.put(type, decl);
		}
		return createFunction(decl);
	}

	private static final Map<FodotType, FodotFunctionDeclaration> MAXIMUM_DECLARATIONS = new HashMap<FodotType, FodotFunctionDeclaration>(); 

	public static FodotFunction createMaximum(FodotType type) {
		FodotFunctionDeclaration decl;
		if (MAXIMUM_DECLARATIONS.containsKey(type)) {
			decl = MAXIMUM_DECLARATIONS.get(type);
		} else {
			decl = createCompleteFunctionDeclaration("MAX", Arrays.asList(type), type);
			MAXIMUM_DECLARATIONS.put(type, decl);
		}
		return createFunction(decl);
	}

	private static final FodotFunctionDeclaration ABSOLUTE_VALUE_DECLARATION =
			createCompleteFunctionDeclaration("abs", Arrays.asList(getIntegerType()), getIntegerType());

	public static FodotFunction createAbsoluteValue(IFodotTerm term) {
		return createFunction(ABSOLUTE_VALUE_DECLARATION, term);
	}

	private static final FodotFunctionDeclaration NEGATE_INTEGER_DECLARATION =
			createCompleteFunctionDeclaration("-", Arrays.asList(getIntegerType()), getIntegerType());

	public static FodotFunction createNegateInteger(IFodotTerm term) {
		return createFunction(NEGATE_INTEGER_DECLARATION, term);
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

	public static FodotConstant createConstant(String value, FodotType type) {
		return new FodotConstant(value, type);
	}

	public static FodotConstant createNaturalNumber(int value) {
		return createConstant(Integer.toString(value), getNaturalNumberType());
	}

	public static FodotConstant createInteger(int value) {
		return createConstant(Integer.toString(value), getIntegerType());
	}

	public static FodotVariable createVariable(String name, FodotType type) {
		return new FodotVariable(NameUtil.convertToValidName(name, type), type);
	}

	public static FodotVariable createVariable(FodotType type) {
		return createVariable(NameUtil.generateVariableName(), type);
	}

	public static FodotPredicate createPredicate(FodotPredicateDeclaration declaration, List<IFodotTerm> arguments) {
		return new FodotPredicate(declaration, arguments);
	}

	public static FodotPredicate createPredicate(FodotPredicateDeclaration declaration, IFodotTerm... arguments) {
		return createPredicate(declaration, Arrays.asList(arguments));
	}

	public static FodotPredicateTerm createPredicateTerm(FodotPredicateTermDeclaration declaration, List<IFodotTerm> arguments) {
		return new FodotPredicateTerm(declaration, arguments);
	}
	
	public static FodotPredicateTerm createPredicateTerm(FodotPredicateTermDeclaration declaration, IFodotTerm... arguments) {
		return createPredicateTerm(declaration, Arrays.asList(arguments));
	}

	public static FodotFunction createFunction(FodotFunctionDeclaration declaration, List<IFodotTerm> arguments) {
		return new FodotFunction(declaration, arguments);
	}

	public static FodotFunction createFunction(FodotFunctionDeclaration declaration, IFodotTerm... arguments) {
		return new FodotFunction(declaration, Arrays.asList(arguments));
	}

	//ENUMERATIONS
	public static FodotFunctionEnumeration createFunctionEnumeration(FodotFunctionDeclaration declaration, 
			Collection<? extends IFodotFunctionEnumerationElement> elements) {
		return new FodotFunctionEnumeration(declaration, elements);
	}
	
	public static FodotFunctionEnumeration createFunctionEnumeration(FodotFunctionDeclaration declaration) {
		return createFunctionEnumeration(declaration, new HashSet<FodotFunctionEnumerationElement>()); //TODO: zet null hier
	}
	
	public static FodotFunctionEnumerationElement createFunctionEnumerationElement(
			Collection<? extends IFodotTypeEnumerationElement> elements, IFodotTypeEnumerationElement returnValue) {
		return new FodotFunctionEnumerationElement(elements, returnValue);
	}
	
	public static FodotConstantFunctionEnumeration createConstantFunctionEnumeration(
			FodotFunctionDeclaration declaration, IFodotTypeEnumerationElement value) {
		return new FodotConstantFunctionEnumeration(declaration,value);
	}

	public static FodotPredicateEnumeration createPredicateEnumeration(
			FodotPredicateDeclaration declaration, List<IFodotPredicateEnumerationElement> elements) {
		return new FodotPredicateEnumeration(declaration, elements);
	}
	
	public static FodotPredicateEnumeration createPredicateEnumeration(FodotPredicateDeclaration declaration) {
		return createPredicateEnumeration(declaration, null);
	}
	
	public static FodotPredicateEnumerationElement createPredicateEnumerationElement(
			FodotPredicateDeclaration declaration, List<? extends IFodotTypeEnumerationElement> elements) {
		return new FodotPredicateEnumerationElement(elements);
	}

	public static FodotTypeEnumeration createTypeEnumeration(FodotType type, Collection<? extends IFodotTypeEnumerationElement> values) {
		return new FodotTypeEnumeration(type, values);
	}

	public static FodotTypeEnumeration createTypeEnumeration(FodotTypeDeclaration type, Collection<? extends IFodotTypeEnumerationElement> values) {
		return new FodotTypeEnumeration(type.getType(), values);
	}
	
	public static FodotPredicateTermTypeEnumerationElement createPredicateTermTypeEnumerationElement(FodotPredicateTermDeclaration declaration,
			List<? extends IFodotTypeEnumerationElement> elements) {
		return new FodotPredicateTermTypeEnumerationElement(declaration,elements);
	}

	public static FodotNumericalTypeRangeEnumeration createNumericalTypeRangeEnumeration(
			FodotType type, FodotConstant head, FodotConstant last) {
		return new FodotNumericalTypeRangeEnumeration(type, head, last);
	}

	public static FodotNumericalTypeRangeEnumeration createNumericalTypeRangeEnumeration(
			FodotTypeDeclaration type, FodotConstant head, FodotConstant last) {
		return new FodotNumericalTypeRangeEnumeration(type.getType(), head, last);
	}


	//INDUCTIVE DEFINITIONS

	//TODO: rename this to "inductivedefinitionblock"?
	public static FodotInductiveDefinitionBlock createInductiveDefinition(List<FodotInductiveSentence> sentences) {
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
	
	public static FodotInductiveSentence createInductiveSentence(IFodotInductiveDefinitionElement form) {
		form = FormulaUtil.makeVariableFreeInductive(form);
		return new FodotInductiveSentence(form);
	}
	
	public static FodotInductiveQuantifier createInductiveQuantifier(FodotQuantifier quantifier) {
		if (! (quantifier.getFormula() instanceof IFodotInductiveDefinitionElement) ) {
			throw new IllegalArgumentException(
					"This is not an inductive definition element: " + quantifier.getFormula());
		}
		return new FodotInductiveQuantifier(
				quantifier.getSymbol(), quantifier.getVariable(),
				(IFodotInductiveDefinitionElement) quantifier.getFormula());
	}

	public static FodotInductiveQuantifier createInductiveForAll(Set<FodotVariable> var, IFodotInductiveDefinitionElement form) {
		return new FodotInductiveQuantifier(FORALL_SYMBOL, var, form);
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

	public static FodotFunctionDeclaration createCompleteFunctionDeclaration(String name, FodotType returnType) {
		return createCompleteFunctionDeclaration(name, null, returnType);
	}

	//If "PartialFunction" isn't specified, assume that it is Complete
	public static FodotFunctionDeclaration createFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType) {
		return createFunctionDeclaration(name, argumentTypes, returnType, false);
	}

	public static FodotFunctionDeclaration createFunctionDeclaration(String name, FodotType returnType) {
		return createCompleteFunctionDeclaration(name, null, returnType);
	}

	public static FodotPredicateDeclaration createPredicateDeclaration(String name, List<FodotType> argumentTypes) {
		return new FodotPredicateDeclaration(name, argumentTypes);
	}

	public static FodotPredicateTermDeclaration createPredicateTermDeclaration(String name, List<FodotType> argumentTypes, FodotType type) {
		return new FodotPredicateTermDeclaration(name, argumentTypes, type);
	} 

	//Type declaration
	@Deprecated
	public static FodotTypeDeclaration createTypeDeclaration(FodotType type, Set<IFodotDomainElement> domain, Set<FodotType> supertypes, Set<FodotType> subtypes) {
		type.addAllSubtypes(subtypes);
		type.addAllSupertypes(supertypes);
		type.addAllDomainElements(domain);
		return new FodotTypeDeclaration(type);
	}

	@Deprecated
	public static FodotTypeDeclaration createTypeDeclaration(FodotType type, Set<IFodotDomainElement> domain, Set<FodotType> supertypes) {
		return createTypeDeclaration(type, domain, supertypes, null);
	}

	@Deprecated
	public static FodotTypeDeclaration createTypeDeclaration(FodotType type, FodotType supertype) {
		Set<FodotType> supertypes = new HashSet<FodotType>();
		supertypes.add(supertype);
		return createTypeDeclaration(type, null, supertypes, null);
	}

	@Deprecated
	public static FodotTypeDeclaration createTypeDeclaration(FodotType type, Set<IFodotDomainElement> domain) {
		return createTypeDeclaration(type, domain, null, null);
	}

	public static FodotTypeDeclaration createTypeDeclaration(FodotType type) {
		if (type == null)
			throw new IllegalArgumentException(type + " is not a valid type!");
		return type.getDeclaration();
	}

	//Types
	public static FodotType createType(String name, Set<? extends IFodotDomainElement> domain, Set<FodotType> supertypes, Set<FodotType> subtypes) {
		return new FodotType(name, domain, supertypes, subtypes);
	}

	public static FodotType createType(String name, Set<? extends IFodotDomainElement> domain, Set<FodotType> supertypes) {
		return createType(name, domain, supertypes, null);
	}

	public static FodotType createType(String name, FodotType supertype) {
		Set<FodotType> supertypes = new HashSet<FodotType>();
		supertypes.add(supertype);
		return createType(name, null, supertypes, null);
	}

	public static FodotType createType(String name, Set<IFodotDomainElement> domain) {
		return createType(name, domain, null, null);
	}

	public static FodotType createType(String name) {
		return new FodotType(name);
	}

	public static FodotType getNaturalNumberType() {
		return FodotType.NATURAL_NUMBER;
	}

	public static FodotType getIntegerType() {
		return FodotType.INTEGER;
	}

	public static FodotType createPlaceHolderType() {
		return FodotType.createPlaceHolderType();
	}

	// == FODOT ESSENTIALS ==

	//FODOT INCLUDES

	public static FodotIncludeLibrary createIncludeLibrary(String library) {
		return new FodotIncludeLibrary(library);
	}

	public static FodotIncludeLibrary createIncludeLTC() {
		return FodotIncludeLibrary.LTC;
	}

	public static FodotIncludeFile createIncludeFile(String path) {
		return new FodotIncludeFile(path);
	}

	public static FodotIncludeHolder createIncludeHolder(Collection<? extends FodotIncludeStatement> includes) {
		return new FodotIncludeHolder(includes);
	}

	public static FodotIncludeHolder createIncludeHolder(FodotIncludeStatement initElement) {
		Set<FodotIncludeStatement> toAdd = new HashSet<FodotIncludeStatement>();
		toAdd.add(initElement);
		return createIncludeHolder(toAdd);
	}

	public static FodotIncludeHolder createIncludeHolder() {
		return createIncludeHolder(new HashSet<FodotIncludeStatement>());
	}

	//FODOT THEORY
	public static FodotTheory createTheory(String name, FodotVocabulary vocabulary, Collection<? extends IFodotTheoryElement> elements) {
		return new FodotTheory(name, vocabulary, elements);
	}

	public static FodotTheory createTheory(String name, FodotVocabulary voc) {
		return createTheory(name, voc, null);
	}

	public static FodotTheory createTheory(FodotVocabulary voc, Collection<? extends IFodotTheoryElement> elements) {
		return createTheory(null, voc, elements);
	}

	public static FodotTheory createTheory(FodotVocabulary voc) {
		return createTheory(null, voc, null);
	}


	//STRUCTURE	
	public static FodotStructure createStructure(String name, FodotVocabulary vocabulary, Collection<? extends IFodotStructureElement> elements) {
		return new FodotStructure(name, vocabulary, elements);
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
	public static FodotProcedures createProcedures(String name, List<String> arguments, List<FodotProcedureStatement> procedures) {
		return new FodotProcedures(name, arguments, procedures);
	}

	public static FodotProcedures createProcedures(String name, List<FodotProcedureStatement> procedures) {
		return createProcedures(name, null, procedures);
	}

	public static FodotProcedures createProcedures(List<FodotProcedureStatement> procedures) {
		return createProcedures(null, null, procedures);
	}

	public static FodotProcedures createProcedures(String name) {
		return createProcedures(name, null, null);
	}

	public static FodotProcedures createProcedures() {
		return createProcedures(null, null, null);
	}

	public static FodotProcedureStatement createProcedure(String procedure) {
		return new FodotProcedureStatement(procedure);
	}

	//VOCABULARY
	public static FodotVocabulary createVocabulary(String name, Collection<? extends IFodotVocabularyElement> elements) {
		return new FodotVocabulary(name, elements);
	}
	
	public static FodotVocabulary createVocabulary(String name) {
		return createVocabulary(name, null);
	}

	public static FodotVocabulary createVocabulary() {
		return createVocabulary(null, null);
	}
	
	public static FodotLTCVocabulary createLTCVocabulary(String name, Collection<? extends IFodotVocabularyElement> elements) {
		return new FodotLTCVocabulary(name, elements);
	}
	
	public static FodotLTCVocabulary createLTCVocabulary(String name) {
		return createLTCVocabulary(name, null);
	}

	public static FodotLTCVocabulary createLTCVocabulary() {
		return createLTCVocabulary(null, null);
	}

	//FODOT ITSELF
	public static Fodot createFodot(FodotVocabulary voc, FodotTheory theory, FodotStructure struc, FodotProcedures procedures, FodotIncludeHolder imports) {
		return new Fodot(voc, theory, struc, procedures, imports);
	}

	public static Fodot createFodot(FodotVocabulary voc, FodotTheory theory, FodotStructure struc, FodotProcedures procedures) {
		return createFodot(voc, theory, struc, procedures, null);
	}

	public static Fodot createFodot() {
		FodotVocabulary voc = createVocabulary();
		return createFodot(voc, null, null, null, null);
	}

	public static Fodot createFodot(FodotVocabulary voc) {
		return createFodot(voc, createTheory(voc), createStructure(voc), createProcedures(), null);
	}

}