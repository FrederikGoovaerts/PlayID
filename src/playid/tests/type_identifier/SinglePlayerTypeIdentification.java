package playid.tests.type_identifier;

import java.io.File;

import playid.domain.gdl_transformers.GdlParser;
import playid.domain.gdl_transformers.first_phase.GdlTypeIdentifier;
import playid.domain.gdl_transformers.visitor.GdlInspector;
import playid.tests.transformation.SingleplayerTransformationTest;

public class SinglePlayerTypeIdentification extends
SingleplayerTransformationTest {

	@Override
	protected void testFor(String gameName) {
		File toParse = toFile(gameName);
		GdlParser parser = new GdlParser(toParse);
		
		GdlTypeIdentifier identifier = new GdlTypeIdentifier();
		GdlInspector.inspect(parser.getGame(), identifier.createTransformer());
		System.out.println(identifier.generateTranslationData());
	}

}
