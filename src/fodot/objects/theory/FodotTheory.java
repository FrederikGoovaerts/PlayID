package fodot.objects.theory;

import java.util.HashSet;
import java.util.Set;

import com.sun.xml.internal.ws.client.SenderException;

import fodot.objects.theory.definitions.FodotInductiveDefinition;
import fodot.objects.vocabulary.FodotVocabulary;


public class FodotTheory {
	private FodotVocabulary vocabulary;
	private Set<FodotInductiveDefinition> definitions;
	private Set<FodotSentence> sentences;
	
	public FodotTheory(FodotVocabulary vocabulary) {
		this(vocabulary, new HashSet<FodotSentence>());
	}
	
	public FodotTheory(FodotVocabulary vocabulary, Set<FodotSentence> sentences) {
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
	
}
