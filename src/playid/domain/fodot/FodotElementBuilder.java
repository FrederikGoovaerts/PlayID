package playid.domain.fodot;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import playid.domain.exceptions.fodot.FodotException;
import playid.domain.fodot.comments.FodotBlankLines;
import playid.domain.fodot.comments.FodotComment;
import playid.domain.fodot.file.FodotFile;
import playid.domain.fodot.file.IFodotFile;
import playid.domain.fodot.file.IFodotFileElement;
import playid.domain.fodot.includes.FodotIncludeHolder;
import playid.domain.fodot.includes.elements.FodotIncludeFile;
import playid.domain.fodot.includes.elements.FodotIncludeLibrary;
import playid.domain.fodot.includes.elements.FodotIncludeStatement;
import playid.domain.fodot.procedure.FodotProcedureStatement;
import playid.domain.fodot.procedure.FodotProcedures;
import playid.domain.fodot.structure.FodotStructure;
import playid.domain.fodot.structure.elements.IFodotStructureElement;
import playid.domain.fodot.structure.elements.functionenum.FodotConstantFunctionEnumeration;
import playid.domain.fodot.structure.elements.functionenum.FodotFunctionEnumeration;
import playid.domain.fodot.structure.elements.functionenum.elements.FodotFunctionEnumerationElement;
import playid.domain.fodot.structure.elements.predicateenum.FodotPredicateEnumeration;
import playid.domain.fodot.structure.elements.predicateenum.elements.FodotPredicateEnumerationElement;
import playid.domain.fodot.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import playid.domain.fodot.structure.elements.typeenum.FodotNumericalTypeRangeEnumeration;
import playid.domain.fodot.structure.elements.typeenum.FodotTypeEnumeration;
import playid.domain.fodot.structure.elements.typeenum.elements.FodotTypeFunctionEnumerationElement;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.fodot.termdefinition.FodotTermDefinition;
import playid.domain.fodot.theory.FodotTheory;
import playid.domain.fodot.theory.elements.FodotSentence;
import playid.domain.fodot.theory.elements.IFodotTheoryElement;
import playid.domain.fodot.theory.elements.formulas.FodotFormulaConnector;
import playid.domain.fodot.theory.elements.formulas.FodotNot;
import playid.domain.fodot.theory.elements.formulas.FodotPredicate;
import playid.domain.fodot.theory.elements.formulas.FodotQuantifier;
import playid.domain.fodot.theory.elements.formulas.FodotTermComparator;
import playid.domain.fodot.theory.elements.formulas.IFodotFormula;
import playid.domain.fodot.theory.elements.inductivedefinitions.FodotInductiveDefinitionBlock;
import playid.domain.fodot.theory.elements.inductivedefinitions.FodotInductiveDefinitionConnector;
import playid.domain.fodot.theory.elements.inductivedefinitions.FodotInductiveFunction;
import playid.domain.fodot.theory.elements.inductivedefinitions.FodotInductivePredicate;
import playid.domain.fodot.theory.elements.inductivedefinitions.FodotInductiveQuantifier;
import playid.domain.fodot.theory.elements.inductivedefinitions.FodotInductiveSentence;
import playid.domain.fodot.theory.elements.inductivedefinitions.IFodotInductiveDefinitionElement;
import playid.domain.fodot.theory.elements.terms.FodotArithmeticConnector;
import playid.domain.fodot.theory.elements.terms.FodotConstant;
import playid.domain.fodot.theory.elements.terms.FodotFunction;
import playid.domain.fodot.theory.elements.terms.FodotVariable;
import playid.domain.fodot.theory.elements.terms.IFodotTerm;
import playid.domain.fodot.theory.elements.terms.aggregates.FodotAggregate;
import playid.domain.fodot.theory.elements.terms.aggregates.FodotInvertFunction;
import playid.domain.fodot.theory.elements.terms.aggregates.FodotSet;
import playid.domain.fodot.vocabulary.FodotLTCVocabulary;
import playid.domain.fodot.vocabulary.FodotVocabulary;
import playid.domain.fodot.vocabulary.elements.FodotFunctionDeclaration;
import playid.domain.fodot.vocabulary.elements.FodotFunctionFullDeclaration;
import playid.domain.fodot.vocabulary.elements.FodotPredicateDeclaration;
import playid.domain.fodot.vocabulary.elements.FodotType;
import playid.domain.fodot.vocabulary.elements.FodotTypeDeclaration;
import playid.domain.fodot.vocabulary.elements.FodotTypeFunctionDeclaration;
import playid.domain.fodot.vocabulary.elements.IFodotDomainElement;
import playid.domain.fodot.vocabulary.elements.IFodotVocabularyElement;
import playid.util.FormulaUtil;
import playid.util.NameUtil;

public class FodotElementBuilder {

	//COMMENTS
	public static FodotComment createComment(int amountOfTabs, String... comments) {
		return new FodotComment(amountOfTabs, comments);
	}

	public static FodotComment createComment(String... comments) {
		return new FodotComment(comments);
	}

	public static FodotBlankLines createBlankLines(int amountOfLines) {
		return new FodotBlankLines(amountOfLines);
	}

	// FORMULA CONNECTORS
	private static final String AND_SYMBOL = "&";

	public static FodotFormulaConnector createAnd(List<? extends IFodotFormula> forms) {
		return new FodotFormulaConnector(AND_SYMBOL, forms);
	}

	public static FodotFormulaConnector createAnd(IFodotFormula... forms) {
		return createAnd(Arrays.asList(forms));
	}

	private static final String OR_SYMBOL = "|";

	public static FodotFormulaConnector createOr(List<? extends IFodotFormula> forms) {
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

	public static FodotTermComparator createEquals(IFodotTerm term1, IFodotTerm term2) {
		return new FodotTermComparator(term1, EQUALS_SYMBOL, term2);
	}

	private static final String DISTINCT_SYMBOL = "~=";

	public static FodotTermComparator createDistinct(IFodotTerm term1, IFodotTerm term2) {
		return new FodotTermComparator(term1, DISTINCT_SYMBOL, term2);
	}

	private static final String LESS_THAN = "<";

	public static FodotTermComparator createLessThan(IFodotTerm term1, IFodotTerm term2) {
		return new FodotTermComparator(term1, LESS_THAN, term2);
	}

	public static IFodotFormula createLessThanOrEqualTo(IFodotTerm term1, IFodotTerm term2) {
		return createOr(createLessThan(term1, term2), createEquals(term1, term2));
	} 

	private static final String GREATER_THAN = ">";

	public static FodotTermComparator createGreaterThan(IFodotTerm term1, IFodotTerm term2) {
		return new FodotTermComparator(term1, GREATER_THAN, term2);
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
	private static final Map<FodotType, FodotFunctionFullDeclaration> SUCCESSOR_DECLARATIONS = new HashMap<FodotType, FodotFunctionFullDeclaration>(); 

	public static FodotFunction createSuccessor(IFodotTerm term) {
		FodotType type = term.getType();
		FodotFunctionFullDeclaration decl;
		if (SUCCESSOR_DECLARATIONS.containsKey(type)) {
			decl = SUCCESSOR_DECLARATIONS.get(type);
		} else {
			decl = createCompleteFunctionDeclaration("SUCC", Arrays.asList(type), type);
			SUCCESSOR_DECLARATIONS.put(type, decl);
		}
		return createFunction(decl, term);
	}

	private static final Map<FodotType, FodotFunctionFullDeclaration> PREDECESSOR_DECLARATIONS = new HashMap<FodotType, FodotFunctionFullDeclaration>(); 

	public static FodotFunction createPredecessor(IFodotTerm term) {
		FodotType type = term.getType();
		FodotFunctionFullDeclaration decl;
		if (PREDECESSOR_DECLARATIONS.containsKey(type)) {
			decl = PREDECESSOR_DECLARATIONS.get(type);
		} else {
			decl = createCompleteFunctionDeclaration("PRED", Arrays.asList(type), type);
			PREDECESSOR_DECLARATIONS.put(type, decl);
		}
		return createFunction(decl, term);
	}

	private static final Map<FodotType, FodotFunctionFullDeclaration> MINIMUM_DECLARATIONS = new HashMap<FodotType, FodotFunctionFullDeclaration>(); 

	public static FodotFunction createMinium(FodotType type) {
		FodotFunctionFullDeclaration decl;
		if (MINIMUM_DECLARATIONS.containsKey(type)) {
			decl = MINIMUM_DECLARATIONS.get(type);
		} else {
			decl = createCompleteFunctionDeclaration("MIN", Arrays.asList(type), type);
			MINIMUM_DECLARATIONS.put(type, decl);
		}
		return createFunction(decl);
	}

	private static final Map<FodotType, FodotFunctionFullDeclaration> MAXIMUM_DECLARATIONS = new HashMap<FodotType, FodotFunctionFullDeclaration>(); 

	public static FodotFunction createMaximum(FodotType type) {
		FodotFunctionFullDeclaration decl;
		if (MAXIMUM_DECLARATIONS.containsKey(type)) {
			decl = MAXIMUM_DECLARATIONS.get(type);
		} else {
			decl = createCompleteFunctionDeclaration("MAX", Arrays.asList(type), type);
			MAXIMUM_DECLARATIONS.put(type, decl);
		}
		return createFunction(decl);
	}

	private static final FodotFunctionFullDeclaration ABSOLUTE_VALUE_DECLARATION =
			createCompleteFunctionDeclaration("abs", Arrays.asList(getIntegerType()), getIntegerType());

	public static FodotFunction createAbsoluteValue(IFodotTerm term) {
		return createFunction(ABSOLUTE_VALUE_DECLARATION, term);
	}

	private static final FodotFunctionFullDeclaration NEGATE_INTEGER_DECLARATION =
			createCompleteFunctionDeclaration("invert", Arrays.asList(getIntegerType()), getIntegerType());

	public static FodotFunction createNegateInteger(IFodotTerm term) {
		return new FodotInvertFunction(NEGATE_INTEGER_DECLARATION, term);
	}

	//QUANTIFIERS

	private static final String FORALL_SYMBOL = "!";

	public static FodotQuantifier createForAll(Collection<? extends FodotVariable> variables, IFodotFormula formula) {
		return new FodotQuantifier(FORALL_SYMBOL, variables, formula);
	}
	public static FodotQuantifier createForAll(FodotVariable variable, IFodotFormula formula) {
		return createForAll(Arrays.asList(variable), formula);
	}

	private static final String EXISTS_SYMBOL = "?";	

	public static FodotQuantifier createExists(Collection<? extends FodotVariable> variables, IFodotFormula formula) {
		return new FodotQuantifier(EXISTS_SYMBOL, variables, formula);
	}

	public static FodotQuantifier createExists(FodotVariable variable, IFodotFormula formula) {
		return createExists(Arrays.asList(variable), formula);
	}

	//Awesome exists
	private static final String EXISTS_EXACTLY_SYMBOL = EXISTS_SYMBOL + "=";
	private static FodotQuantifier createExistsExactly(int amount, Collection<? extends FodotVariable> variables, IFodotFormula formula) {
		return new FodotQuantifier(EXISTS_EXACTLY_SYMBOL + amount, variables, formula);
	}

	public static FodotQuantifier createExistsExactly(int amount, FodotVariable var, IFodotFormula formula) {
		return createExistsExactly(amount, Arrays.asList(var), formula);		
	}

	private static final String EXISTS_LESS_THAN_SYMBOL = EXISTS_SYMBOL + "<";
	private static FodotQuantifier createExistsLessThan(int amount, Collection<? extends FodotVariable> variables, IFodotFormula formula) {
		return new FodotQuantifier(EXISTS_LESS_THAN_SYMBOL + amount, variables, formula);
	}

	public static FodotQuantifier createExistsLessThan(int amount, FodotVariable var, IFodotFormula formula) {
		return createExistsLessThan(amount, Arrays.asList(var), formula);		
	}

	private static final String EXISTS_AT_MOST_SYMBOL = EXISTS_SYMBOL + "=<";
	private static FodotQuantifier createExistsAtMost(int amount, Collection<? extends FodotVariable> variables, IFodotFormula formula) {
		return new FodotQuantifier(EXISTS_AT_MOST_SYMBOL + amount, variables, formula);
	}

	public static FodotQuantifier createExistsAtMost(int amount, FodotVariable var, IFodotFormula formula) {
		return createExistsAtMost(amount, Arrays.asList(var), formula);		
	}

	private static final String EXISTS_MORE_THAN_SYMBOL = EXISTS_SYMBOL + ">";
	private static FodotQuantifier createExistsMoreThan(int amount, Collection<? extends FodotVariable> variables, IFodotFormula formula) {
		return new FodotQuantifier(EXISTS_MORE_THAN_SYMBOL + amount, variables, formula);
	}

	public static FodotQuantifier createExistsMoreThan(int amount, FodotVariable var, IFodotFormula formula) {
		return createExistsMoreThan(amount, Arrays.asList(var), formula);		
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

	public static FodotVariable createVariable(String nameSuggestion, FodotType type, Collection<? extends String> usedNames) {
		return new FodotVariable(NameUtil.generateVariableName(nameSuggestion, type, usedNames), type);
	}

	public static FodotVariable createVariable(String nameSuggestion, FodotType type, Set<FodotVariable> usedVariables) {
		Set<String> usedNames = new HashSet<String>();
		for (FodotVariable variable : usedVariables) {
			usedNames.add(variable.getName());
		}		
		FodotVariable toReturn = createVariable(NameUtil.generateVariableName(nameSuggestion, type, usedNames), type, usedNames);
		usedVariables.add(toReturn);
		return toReturn;
	}

	public static FodotVariable createVariable(FodotType type, Set<FodotVariable> usedVariables) {
		return createVariable(null, type, usedVariables);
	}

	public static FodotPredicate createPredicate(FodotPredicateDeclaration declaration, List<IFodotTerm> arguments) {
		return new FodotPredicate(declaration, arguments);
	}

	public static FodotPredicate createPredicate(FodotPredicateDeclaration declaration, IFodotTerm... arguments) {
		return createPredicate(declaration, Arrays.asList(arguments));
	}

	public static FodotFunction createFunction(FodotFunctionDeclaration declaration, List<IFodotTerm> arguments) {
		return new FodotFunction(declaration, arguments);
	}

	public static FodotFunction createFunction(FodotFunctionDeclaration declaration, IFodotTerm... arguments) {
		return new FodotFunction(declaration, Arrays.asList(arguments));
	}

	//Aggregates
	public static FodotSet createSet(List<FodotVariable> variables, IFodotFormula formula,
			IFodotTerm term) {
		return new FodotSet(variables, formula, term);
	}

	public static FodotSet createSet(List<FodotVariable> variables, IFodotFormula formula) {
		return createSet(variables, formula, null);
	}

	public static FodotSet createSet(IFodotFormula formula) {
		return createSet(null, formula, null);
	}
	
	private static final String CARDINALITY_SYMBOL = "#";

	public static FodotAggregate createCardinality(FodotSet set) {
		return new FodotAggregate(CARDINALITY_SYMBOL, set);
	}
	
	private static final String SUM_SYMBOL = "sum";

	public static FodotAggregate createSum(FodotSet set) {
		return new FodotAggregate(SUM_SYMBOL, set);
	}
	
	private static final String PRODUCT_SYMBOL = "prod";

	public static FodotAggregate createProduct(FodotSet set) {
		return new FodotAggregate(PRODUCT_SYMBOL, set);
	}
	
	private static final String MAXIMUM_SYMBOL = "max";

	public static FodotAggregate createMaximum(FodotSet set) {
		return new FodotAggregate(MAXIMUM_SYMBOL, set);
	}
	
	private static final String MINIMUM_SYMBOL = "min";

	public static FodotAggregate createMinimum(FodotSet set) {
		return new FodotAggregate(MINIMUM_SYMBOL, set);
	}
	
	
	
	
	//ENUMERATIONS
	public static FodotFunctionEnumeration createFunctionEnumeration(FodotFunctionFullDeclaration declaration, 
			Collection<? extends FodotFunctionEnumerationElement> elements) {
		return new FodotFunctionEnumeration(declaration, elements);
	}

	public static FodotFunctionEnumeration createFunctionEnumeration(FodotFunctionFullDeclaration declaration) {
		return createFunctionEnumeration(declaration, null);
	}

	public static FodotFunctionEnumerationElement createFunctionEnumerationElement(
			FodotFunctionDeclaration declaration,
			Collection<? extends IFodotTypeEnumerationElement> elements, IFodotTypeEnumerationElement returnValue) {
		return new FodotFunctionEnumerationElement(declaration, elements, returnValue);
	}

	public static FodotConstantFunctionEnumeration createConstantFunctionEnumeration(
			FodotFunctionFullDeclaration declaration, IFodotTypeEnumerationElement value) {
		return new FodotConstantFunctionEnumeration(declaration,value);
	}
	
	public static FodotPredicateEnumeration createPredicateEnumeration(
			FodotPredicateDeclaration declaration, List<? extends IFodotPredicateEnumerationElement> elements) {
		return new FodotPredicateEnumeration(declaration, elements);
	}

	public static FodotPredicateEnumeration createPredicateEnumeration(FodotPredicateDeclaration declaration) {
		return createPredicateEnumeration(declaration, null);
	}

	public static FodotPredicateEnumerationElement createPredicateEnumerationElement(
			FodotPredicateDeclaration declaration, List<? extends IFodotTypeEnumerationElement> elements) {
		return new FodotPredicateEnumerationElement(declaration, elements);
	}

	public static FodotTypeEnumeration createTypeEnumeration(FodotType type, Collection<? extends IFodotTypeEnumerationElement> values) {
		return new FodotTypeEnumeration(type, values);
	}

	public static FodotTypeEnumeration createTypeEnumeration(FodotTypeDeclaration type, Collection<? extends IFodotTypeEnumerationElement> values) {
		return new FodotTypeEnumeration(type.getType(), values);
	}

	public static FodotTypeFunctionEnumerationElement createTypeFunctionEnumerationElement(FodotTypeFunctionDeclaration declaration,
			List<? extends IFodotTypeEnumerationElement> elements) {
		return new FodotTypeFunctionEnumerationElement(declaration,elements);
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

    public static FodotInductivePredicate createInductivePredicateHead(FodotPredicate predicate) {
        return new FodotInductivePredicate(predicate);
    }

	public static FodotInductiveSentence createInductiveSentence(IFodotInductiveDefinitionElement form) {
		form = FormulaUtil.makeVariableFreeInductive(form);
		return new FodotInductiveSentence(form);
	}

	public static FodotInductiveQuantifier createInductiveQuantifier(FodotQuantifier quantifier) {
		if (! (quantifier.getFormula() instanceof IFodotInductiveDefinitionElement) ) {
			throw new FodotException(
					"This is not an inductive definition element: " + quantifier.getFormula());
		}
		return new FodotInductiveQuantifier(
				quantifier.getSymbol(), quantifier.getVariables(),
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
	public static FodotFunctionFullDeclaration createFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType, boolean isPartial) {
		return new FodotFunctionFullDeclaration(name, argumentTypes, returnType, isPartial);
	}

	public static FodotFunctionFullDeclaration createPartialFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType) {
		return createFunctionDeclaration(name, argumentTypes, returnType, true);
	}

	public static FodotFunctionFullDeclaration createCompleteFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType) {
		return createFunctionDeclaration(name, argumentTypes, returnType, false);
	}

	public static FodotFunctionFullDeclaration createCompleteFunctionDeclaration(String name, FodotType returnType) {
		return createCompleteFunctionDeclaration(name, null, returnType);
	}

	//If "PartialFunction" isn't specified, assume that it is Complete
	public static FodotFunctionFullDeclaration createFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType returnType) {
		return createFunctionDeclaration(name, argumentTypes, returnType, false);
	}

	public static FodotFunctionFullDeclaration createFunctionDeclaration(String name, FodotType returnType) {
		return createCompleteFunctionDeclaration(name, null, returnType);
	}

	public static FodotPredicateDeclaration createPredicateDeclaration(String name, List<FodotType> argumentTypes) {
		return new FodotPredicateDeclaration(name, argumentTypes);
	}

	public static FodotTypeFunctionDeclaration createTypeFunctionDeclaration(String name, List<FodotType> argumentTypes, FodotType type) {
		return new FodotTypeFunctionDeclaration(name, argumentTypes, type);
	} 

	//Type declaration
	public static FodotTypeDeclaration createTypeDeclaration(FodotType type) {
		if (type == null)
			throw new FodotException(type + " is not a valid type!");
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

	public static FodotTermDefinition createTermDefinition( String name, FodotVocabulary vocabulary, IFodotTerm term ) {
		return new FodotTermDefinition(name, term, vocabulary);
	}
	
	//FODOT FILES
	public static IFodotFile createFodotFile(FodotIncludeHolder imports, Collection<? extends IFodotFileElement> elements) {
		return new FodotFile(imports,elements);
	}

	public static IFodotFile createFodotFile(Collection<? extends IFodotFileElement> elements) {
		return createFodotFile(null, elements);
	}

	public static IFodotFile createFodotFile(FodotVocabulary voc, FodotTheory theory, FodotStructure struc, FodotProcedures procedures, FodotIncludeHolder imports) {
		return createFodotFile(imports,Arrays.asList(voc,theory,struc,procedures));
	}

}