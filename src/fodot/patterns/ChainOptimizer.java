package fodot.patterns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ChainOptimizer<E, I extends IOptimizer<E>> implements IOptimizer<E> {
	private List<I> optimizers;


	/**********************************************
	 *  Constructors
	 ***********************************************/

	public ChainOptimizer(List<? extends I> optimizers) {
		setOptimizers(optimizers);
	}
	
	/**********************************************/


	/**********************************************
	 *  Optimizers methods
	 ***********************************************/
	private void setOptimizers(Collection<? extends I> argOptimizers) {
		this.optimizers = (isValidOptimizers(argOptimizers) ? new ArrayList<I>(argOptimizers)
				: new ArrayList<I>());
	}

	private boolean isValidOptimizers(Collection<? extends I> argOptimizers) {
		return argOptimizers != null;
	}

	public List<I> getOptimizers() {
		return new ArrayList<I>(optimizers);
	}

	public void addOptimizer(I argOptimizer) {
		this.optimizers.add(argOptimizer);
	}

	public void addAllOptimizers(Collection<? extends I> argOptimizers) {
		if (argOptimizers != null) {
			this.optimizers.addAll(argOptimizers);
		}
	}

	public boolean containsOptimizer(I optimizer) {
		return this.optimizers.contains(optimizer);
	}

	public boolean hasOptimizers() {
		return !optimizers.isEmpty();
	}

	public void removeOptimizer(I argOptimizer) {
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
	public E improve(E file) {
		for (I op : optimizers) {
			file = op.improve(file);
		}
		return file;
	}

}
