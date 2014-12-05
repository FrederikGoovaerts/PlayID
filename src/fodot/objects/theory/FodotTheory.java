package fodot.objects.theory;

import java.util.HashSet;
import java.util.Set;

import com.sun.xml.internal.ws.client.SenderException;

import fodot.objects.vocabulary.FodotVocabulary;


public class FodotTheory {
	//Are Inductive Definitions also "sentences"? Do they need their own class?
	private Set<FodotSentence> sentences;
	private FodotVocabulary vocabulary;
	
	public FodotTheory(FodotVocabulary vocabulary) {
		this(vocabulary, new HashSet<FodotSentence>());
	}
	
	public FodotTheory(FodotVocabulary vocabulary, Set<FodotSentence> sentences) {
		this.vocabulary = vocabulary;
		this.sentences = sentences;
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

	/* VOCABULARY */
	public FodotVocabulary getVocabulary() {
		return vocabulary;
	}
	
	
	
}
