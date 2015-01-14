package fodot.objects.comments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fodot.objects.general.IFodotElement;
import fodot.objects.structure.elements.IFodotStructureElement;
import fodot.objects.theory.elements.IFodotTheoryElement;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.IFodotVocabularyElement;
import fodot.util.CollectionPrinter;

public class FodotComment implements IFodotElement, IFodotTheoryElement, IFodotStructureElement, IFodotVocabularyElement {

	private List<String> comments;
	
	public FodotComment(String... argComments) {
		setComments(Arrays.asList(argComments));
	}

	/**********************************************
	 *  Comments methods
	 ***********************************************/
	private void setComments(List<? extends String> argComments) {
		this.comments = new ArrayList<String>();
		if (!isValidComments(argComments)) {
			return;
		} else {
			for (String comment : argComments) {
				for (String line : comment.split("\n")) {
					this.comments.add(line.trim());
				}
			}
		}
	}

	private boolean isValidComments(Collection<? extends String> argComments) {
		return argComments != null;
	}

	public List<String> getComments() {
		return new ArrayList<String>(comments);
	}

	public void addComment(String argComment) {
		this.comments.add(argComment);
	}

	public void addAllComments(Collection<? extends String> argComments) {
		for (String comment : argComments) {
			addComment(comment);
		}
	}
	
	public boolean hasComments() {
		return !comments.isEmpty();
	}
	/**********************************************/

	
	@Override
	public String toCode() {
		if (this.comments.size() == 1) {
			return "// " + comments.get(0);
		} else {
			return CollectionPrinter.printStringList("/**\n\t * ", "\n\t */", "\n\t * ", comments);
		}
	}

	@Override
	public Set<FodotType> getPrerequiredTypes() {
		return new HashSet<FodotType>();
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public IFodotVocabularyElement getDeclaration() {
		return null;
	}

}
