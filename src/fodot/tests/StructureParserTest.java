package fodot.tests;

import static org.junit.Assert.*;
import static fodot.objects.FodotPartBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fodot.fodot_parser.FodotStructureParser;
import fodot.objects.file.IFodotFile;
import fodot.objects.structure.FodotStructure;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

public class StructureParserTest {

	//defaults
	private static FodotVocabulary defaultVoc;
	private static FodotType t1;
	private static FodotType t2;
	private static FodotType t3;
	private static FodotPredicateDeclaration pred1;
	private static FodotPredicateDeclaration pred2;
	private static FodotPredicateDeclaration pred3;
	
	
	private FodotStructure simpleStructure;
	
	
	@BeforeClass
	public static void beforeClass() {
		//VOC
		defaultVoc = createVocabulary("defvoc");
		
		//TYPES
		t1 = createType("t1");
		t2 = createType("t2");
		t3 = createType("t3");
		defaultVoc.addAllElements(Arrays.asList(t1.getDeclaration(),t2.getDeclaration(),t3.getDeclaration()));
		
		//PREDS
		pred1 = createPredicateDeclaration("pred1", new ArrayList<FodotType>());
		pred2 = createPredicateDeclaration("pred2", Arrays.asList(t1,t2));
		pred3 = createPredicateDeclaration("pred3", Arrays.asList(t1,t2,t3));
		defaultVoc.addAllElements(Arrays.asList(pred1,pred2,pred3));
		
	}
			
	@Before
	public void before() {
		//simple structure
		simpleStructure = createStructure("simplestruc", defaultVoc);
		simpleStructure.addAllElements(Arrays.asList(
				createPredicateEnumeration(pred1),
				createPredicateEnumeration(pred2),
				createPredicateEnumeration(pred3)				
				));
		
		
	}
	
	private FodotStructure toStringAndParse(FodotStructure input) {
		IFodotFile file = createFodot(defaultVoc, null, input, null);
		return FodotStructureParser.parse(file, input.toCode());
	}
	
	
	//TESTS
	
	@Test
	public void simple_structure_test() {
		assertEquals(simpleStructure.getElements(), toStringAndParse(simpleStructure).getElements());
	}

}
