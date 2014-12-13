package fodot.objects.theory;

import java.util.HashSet;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.objects.theory.definitions.FodotInductiveDefinitionBlock;
import fodot.objects.vocabulary.FodotVocabulary;


public class FodotTheory implements IFodotElement {
	private String name;
	private FodotVocabulary vocabulary;
	private Set<FodotInductiveDefinitionBlock> definitions;
	private Set<FodotSentence> sentences;

	public FodotTheory(String name, FodotVocabulary vocabulary, Set<FodotSentence> sentences) {
		this.name = name;
		this.vocabulary = vocabulary;
		this.sentences = sentences;
	}
	
	public FodotTheory(String name, FodotVocabulary vocabulary) {
		this(name, vocabulary, new HashSet<FodotSentence>());
	}
	
	private static final String DEFAULT_NAME = "T";
	
	public FodotTheory(FodotVocabulary voc) {
		this(DEFAULT_NAME, voc);
	}

	/* VOCABULARY */
	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}
	
	/* DEFINITIONS */
	public void addInductiveDefinition(FodotInductiveDefinitionBlock definition) {
		definitions.add(definition);
	}
	
	public void removeInductiveDefinition(FodotInductiveDefinitionBlock definition) {
		definitions.remove(definition);
	}
	
	public Set<FodotInductiveDefinitionBlock> getInductiveDefinitions() {
		return new HashSet<FodotInductiveDefinitionBlock>(definitions);
	}
	
	/* SENTENCES */
	public void addSentence(FodotSentence sentence) {
		sentences.add(sentence);
	}
	
	public void removeSentence(FodotSentence sentence) {
		sentences.remove(sentence);
	}
	
	public Set<FodotSentence> getSentences() {
		return new HashSet<FodotSentence>(sentences);
	}

	/* NAME */
	public String getName() {
		return name;
	}
	
	@Override
	public String toCode() {
		StringBuilder builder = new StringBuilder();
		builder.append("theory " + getName() + ": " + getVocabulary().getName() + " {");
		
		//Codify inductive definitions
		for (FodotInductiveDefinitionBlock definition : getInductiveDefinitions()) {
			builder.append(definition.toCode() + "\n");
		}
		
		//Codify sentences
		for (FodotSentence sentence : getSentences()) {
			builder.append(sentence.toCode() + "\n");
		}	
		
		builder.append("}");
		return builder.toString();
	}

	public void merge(FodotTheory other) {
		definitions.addAll(other.getInductiveDefinitions());
		sentences.addAll(other.getSentences());
	}
	
}
