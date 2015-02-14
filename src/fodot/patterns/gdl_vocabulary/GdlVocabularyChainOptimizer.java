package fodot.patterns.gdl_vocabulary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fodot.gdl_parser.GdlVocabulary;
import fodot.patterns.ChainOptimizer;

public class GdlVocabularyChainOptimizer extends ChainOptimizer<GdlVocabulary, IGdlVocabularyOptimizer> implements IGdlVocabularyOptimizer {
	private static final List<IGdlVocabularyOptimizer> DEFAULT_OPTIMIZERS = new ArrayList<IGdlVocabularyOptimizer>();

	
	/**********************************************
	 *  Constructors
	 ***********************************************/

	public GdlVocabularyChainOptimizer(List<? extends IGdlVocabularyOptimizer> optimizers) {
		super(optimizers);
	}
	
	public GdlVocabularyChainOptimizer(IGdlVocabularyOptimizer... optimizers) {
		this(Arrays.asList(optimizers));
	}
	
	public GdlVocabularyChainOptimizer() {
		this(DEFAULT_OPTIMIZERS);
	}

	/**********************************************/

}
