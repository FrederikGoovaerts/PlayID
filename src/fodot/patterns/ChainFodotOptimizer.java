package fodot.patterns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fodot.objects.file.IFodotFile;

/**
 * This class can be used to chain FO(.) optimizers together and still pretend it's only one optimizer.
 * @author Thomas
 *
 */
public class ChainFodotOptimizer implements IFodotOptimizer {
	private static final List<IFodotOptimizer> DEFAULT_OPTIMIZERS = new ArrayList<IFodotOptimizer>();
	private List<IFodotOptimizer> optimizers;

	
	/**********************************************
	 *  Constructors
	 ***********************************************/

	public ChainFodotOptimizer(List<IFodotOptimizer> optimizers) {
		setOptimizers(optimizers);
	}
	
	public ChainFodotOptimizer() {
		this(DEFAULT_OPTIMIZERS);
	}

	/**********************************************/

	
	/**********************************************
	 *  Optimizers methods
	 ***********************************************/
	private void setOptimizers(Collection<? extends IFodotOptimizer> argOptimizers) {
		this.optimizers = (isValidOptimizers(argOptimizers) ? new ArrayList<IFodotOptimizer>(argOptimizers)
				: new ArrayList<IFodotOptimizer>());
	}

	private boolean isValidOptimizers(Collection<? extends IFodotOptimizer> argOptimizers) {
		return argOptimizers != null;
	}

	public List<IFodotOptimizer> getOptimizers() {
		return new ArrayList<IFodotOptimizer>(optimizers);
	}

	public void addOptimizer(IFodotOptimizer argOptimizer) {
		this.optimizers.add(argOptimizer);
	}

	public void addAllOptimizers(Collection<? extends IFodotOptimizer> argOptimizers) {
		if (argOptimizers != null) {
			this.optimizers.addAll(argOptimizers);
		}
	}

	public boolean containsOptimizer(IFodotOptimizer optimizer) {
		return this.optimizers.contains(optimizer);
	}

	public boolean hasOptimizers() {
		return !optimizers.isEmpty();
	}

	public void removeOptimizer(IFodotOptimizer argOptimizer) {
		this.optimizers.remove(argOptimizer);
	}

	public int getAmountOfOptimizers() {
		return this.optimizers.size();
	}
	/**********************************************/


	/**
	 * Improves the given file by applying all the optimizers in it contains.
	 */
	@Override
	public IFodotFile improve(IFodotFile file) {
		for (IFodotOptimizer op : optimizers) {
			file = op.improve(file);
		}
		return file;
	}
}
