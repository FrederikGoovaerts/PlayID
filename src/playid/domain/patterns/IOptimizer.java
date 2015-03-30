package playid.domain.patterns;

public interface IOptimizer<E> {
	E improve(E object);
}
