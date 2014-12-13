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

	public FodotTheory(String name,
					   FodotVocabulary vocabulary,
					   Set<FodotSentence> sentences,
					   Set<FodotInductiveDefinitionBlock> definitions) {
		super();
		setName(name);
		setVocabulary(vocabulary);
		setSentences(sentences);
		setInductiveDefinitions(definitions);
	}

	public FodotTheory(String name, FodotVocabulary vocabulary, Set<FodotSentence> sentences) {
		this(name,vocabulary,sentences,null);
	}
	
	public FodotTheory(String name, FodotVocabulary vocabulary) {
		this(name, vocabulary, new HashSet<FodotSentence>());
	}
	
	private static final String DEFAULT_NAME = "T";
	
	public FodotTheory(FodotVocabulary voc) {
		this(DEFAULT_NAME, voc);
	}

	/* VOCABULARY */
	private void setVocabulary(FodotVocabulary voc) {
		this.vocabulary = (voc == null? new FodotVocabulary() : voc);
	}

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

	private void setInductiveDefinitions(Set<FodotInductiveDefinitionBlock> def) {
		this.definitions = (def == null ? new HashSet<FodotInductiveDefinitionBlock>() : def);
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

	private void setSentences(Set<FodotSentence> sent) {
		this.sentences = (sent == null ? new HashSet<FodotSentence>() : sent);
	}
	
	public Set<FodotSentence> getSentences() {
		return new HashSet<FodotSentence>(sentences);
	}

	/* NAME */
	public void setName(String name) {
		this.name = (name == null ? DEFAULT_NAME : name);
	}

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
