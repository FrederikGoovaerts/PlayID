package playid.domain.patterns.fodot_file;

import java.util.ArrayList;
import java.util.List;

import playid.domain.fodot.file.IFodotFile;
import playid.domain.patterns.ChainOptimizer;

/**
 * This class can be used to chain FO(.) optimizers together and still pretend it's only one optimizer.
 * @author Thomas
 *
 */
public class FodotChainOptimizer extends ChainOptimizer<IFodotFile, IFodotOptimizer> implements IFodotOptimizer {
	private static final List<IFodotOptimizer> DEFAULT_OPTIMIZERS = new ArrayList<IFodotOptimizer>();

	
	/**********************************************
	 *  Constructors
	 ***********************************************/

	public FodotChainOptimizer(List<? extends IFodotOptimizer> optimizers) {
		super(optimizers);
	}
	
	public FodotChainOptimizer() {
		this(DEFAULT_OPTIMIZERS);
	}

	/**********************************************/

}
