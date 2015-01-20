package fodot.objects.general.sorting;

import java.util.Collection;

public interface PrerequisiteExtractor<E> {
	Collection<? extends E> getPrerequisitesOf(E element);
}
