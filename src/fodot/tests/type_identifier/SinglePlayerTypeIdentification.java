package fodot.tests.type_identifier;

import java.io.File;

import fodot.gdl_parser.GdlParser;
import fodot.gdl_parser.GdlVocabulary;
import fodot.gdl_parser.first_phase.GdlTypeIdentifier;
import fodot.gdl_parser.visitor.GdlInspector;
import fodot.tests.transformation.SingleplayerTransformationTest;

public class SinglePlayerTypeIdentification extends
SingleplayerTransformationTest {

	@Override
	protected void testFor(String gameName) {
		File toParse = toFile(gameName);
		GdlParser parser = new GdlParser(toParse);
		
		GdlTypeIdentifier identifier = new GdlTypeIdentifier();
		GdlInspector.inspect(parser.getGame(), identifier.createTransformer());
		GdlVocabulary gdlVocabulary = identifier.generateTranslationData();
	}

}
