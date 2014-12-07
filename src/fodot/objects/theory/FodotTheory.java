package fodot.objects.theory;

import java.util.HashSet;
import java.util.Set;

import fodot.objects.IFodotElement;
import fodot.objects.theory.definitions.FodotInductiveDefinition;
import fodot.objects.vocabulary.FodotVocabulary;


public class FodotTheory implements IFodotElement {
	private String name;
	private FodotVocabulary vocabulary;
	private Set<FodotInductiveDefinition> definitions;
	private Set<FodotSentence> sentences;
	
	public FodotTheory(String name, FodotVocabulary vocabulary) {
		this(name, vocabulary, new HashSet<FodotSentence>());
	}
	
	public FodotTheory(String name, FodotVocabulary vocabulary, Set<FodotSentence> sentences) {
		this.name = name;
		this.vocabulary = vocabulary;
		this.sentences = sentences;
	}

	/* VOCABULARY */
	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}
	
	/* DEFINITIONS */
	public void addInductiveDefinition(FodotInductiveDefinition definition) {
		definitions.add(definition);
	}
	
	public void removeInductiveDefinition(FodotInductiveDefinition definition) {
		definitions.remove(definition);
	}
	
	public Set<FodotInductiveDefinition> getInductiveDefinitions() {
		return new HashSet<FodotInductiveDefinition>(definitions);
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
		for (FodotInductiveDefinition definition : getInductiveDefinitions()) {
			builder.append(definition.toCode() + "\n");
		}
		
		//Codify sentences
		for (FodotSentence sentence : getSentences()) {
			builder.append(sentence.toCode() + "\n");
		}	
		
		builder.append("}");
		return builder.toString();
	}
	
}
