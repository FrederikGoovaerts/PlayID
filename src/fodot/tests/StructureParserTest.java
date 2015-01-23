package fodot.tests;

import static fodot.objects.FodotElementBuilder.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.*;

import fodot.fodot_parser.FodotStructureParser;
import fodot.objects.general.sorting.FodotElementComparators;
import fodot.objects.file.IFodotFile;
import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.elements.IFodotStructureElement;
import fodot.objects.structure.elements.predicateenum.elements.FodotPredicateEnumerationElement;
import fodot.objects.structure.elements.typenum.FodotNumericalTypeRangeEnumeration;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

public class StructureParserTest {	
	FodotType t1;
	FodotType t2;
	FodotType t3;
	FodotConstant t1c1;
	FodotConstant t1c2;
	FodotConstant t1c3;
	FodotConstant t2c1;
	FodotConstant t2c2;
	FodotConstant t2c3;
	
	@Before
	public void before() {
		t1 = createType("t1",FodotType.INTEGER);
		t2 = createType("t2");
		t3 = createType("t3");
		t1c1 = createConstant("1", t2);
		t1c2 = createConstant("2", t2);
		t1c3 = createConstant("3", t2);
		t2c1 = createConstant("a", t2);
		t2c2 = createConstant("b", t2);
		t2c3 = createConstant("c", t2);
	}

	private FodotStructure toStringAndParse(FodotStructure input) {
		IFodotFile file = createFodotFile(Arrays.asList(input.getVocabulary(), input));
		return FodotStructureParser.parse(file, input.toCode());
	}


	//TESTS

	@Test
	public void simple_structure_test() {
		//VOC
		FodotVocabulary voc = createVocabulary("defvoc");

		//TYPES
		voc.addAllElements(Arrays.asList(t1.getDeclaration(),t2.getDeclaration(),t3.getDeclaration()));

		//PREDS
		FodotPredicateDeclaration pred1 = createPredicateDeclaration("pred1", new ArrayList<FodotType>());
		FodotPredicateDeclaration pred2 = createPredicateDeclaration("pred2", Arrays.asList(t1,t2));
		FodotPredicateDeclaration pred3 = createPredicateDeclaration("pred3", Arrays.asList(t1,t2,t3));
		voc.addAllElements(Arrays.asList(pred1,pred2,pred3));

		//Enumerations
		FodotPredicateEnumerationElement p2e1 = createPredicateEnumerationElement(
				pred2, Arrays.asList(t1c1,t2c1));
		FodotPredicateEnumerationElement p2e2 = createPredicateEnumerationElement(
				pred2, Arrays.asList(t1c1,t2c2));
		FodotPredicateEnumerationElement p2e3 = createPredicateEnumerationElement(
				pred2, Arrays.asList(t1c2,t2c3));

		//simple structure
		FodotStructure simpleStructure = createStructure("simplestruc", voc);
		simpleStructure.addAllElements(Arrays.asList(
				createPredicateEnumeration(pred1),
				createPredicateEnumeration(pred2, Arrays.asList(p2e1, p2e2, p2e3)),
				createPredicateEnumeration(pred3)				
				));


		
		assertEqualContent(simpleStructure.getElements(), toStringAndParse(simpleStructure).getElements());
	}
	
	@Test
	public void constant_function_test() {
		//VOC
		FodotVocabulary voc = createVocabulary("defvoc");

		//TYPES
		voc.addElement(t2.getDeclaration());

		//PREDS
		FodotFunctionDeclaration f1 = createFunctionDeclaration("f1", t2);
		voc.addElement(f1);

		//Enumerations
		//simple structure
		FodotStructure simpleStructure = createStructure("simplestruc", voc);
		simpleStructure.addAllElements(Arrays.asList(
				createConstantFunctionEnumeration(f1,t2c1)			
				));

		assertEqualContent(simpleStructure.getElements(), toStringAndParse(simpleStructure).getElements());
	}
	
	@Test
	public void ranged_type_test() {
		//VOC
		FodotVocabulary voc = createVocabulary("defvoc");

		//TYPES
		voc.addElement(t1.getDeclaration());

		//Enumerations
		//simple structure
		FodotStructure simpleStructure = createStructure("simplestruc", voc);
		simpleStructure.addAllElements(Arrays.asList(
				createNumericalTypeRangeEnumeration(t1, t1c1, t1c3)			
				));

		assertEqualContent(simpleStructure.getElements(), toStringAndParse(simpleStructure).getElements());
	}

	private void assertEqualContent(Collection<? extends IFodotStructureElement> before,
			Collection<? extends IFodotStructureElement> after) {
		List<IFodotStructureElement> beforeElements = new ArrayList<IFodotStructureElement>(before);
		List<IFodotStructureElement> afterElements = new ArrayList<IFodotStructureElement>(after);
		Collections.sort(beforeElements, FodotElementComparators.ELEMENT_TOCODE_COMPARATOR);
		Collections.sort(afterElements, FodotElementComparators.ELEMENT_TOCODE_COMPARATOR);
		
		assertEquals("Size of elements doesn't match before and after", beforeElements.size(), afterElements.size());
		for (int i = 0; i < beforeElements.size(); i++) {
			assertEquals("Element "+i+" doesn't match.\n",beforeElements.get(i).toCode(), afterElements.get(i).toCode());
		}
	}

}
