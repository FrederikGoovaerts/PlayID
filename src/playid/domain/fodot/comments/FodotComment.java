package playid.domain.fodot.comments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import playid.domain.fodot.file.IFodotFileElement;
import playid.domain.fodot.general.FodotElement;
import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.structure.elements.IFodotStructureElement;
import playid.domain.fodot.theory.elements.IFodotTheoryElement;
import playid.domain.fodot.vocabulary.elements.FodotType;
import playid.domain.fodot.vocabulary.elements.IFodotVocabularyElement;
import playid.util.CollectionPrinter;

public class FodotComment extends FodotElement implements IFodotElement, IFodotFileElement, IFodotTheoryElement, IFodotStructureElement, IFodotVocabularyElement {

	private static final int DEFAULT_AMOUNT_OF_TABS = 1;
	
	private List<String> comments;	
	private int amountOfTabs;

	public FodotComment(int amountOfTabs, String... argComments) {
		setComments(Arrays.asList(argComments));
		this.amountOfTabs = amountOfTabs;
	}
	
	public FodotComment(String... argComments) {
		this(DEFAULT_AMOUNT_OF_TABS, argComments);
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
			String tabs = "";
			for (int i = 0; i < amountOfTabs; i++) {
				tabs += "\t";
			}
			return CollectionPrinter.printStringList("/**\n"+tabs+" * ", "\n"+tabs+" */", "\n"+tabs+" * ", comments);
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

	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return new HashSet<IFodotElement>();
	}

	@Override
	public Set<IFodotFileElement> getPrerequiredElements() {
		return null;
	}

}
