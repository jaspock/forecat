package org.forecat.shared.suggestions;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SuggestionsOutput
		implements Serializable, Comparable<SuggestionsOutput>, IsSerializable {

	String suggestionText;
	double suggestionFeasibility;
	String originalText;

	/**
	 * Position of origin of the suggestion (in source language)
	 */
	int wordPosition;
	int charPosition;

	/**
	 * Number of words of the origin suggestion
	 */
	int numberWords;

	/**
	 * Id of the SourceSegment
	 */
	String id;

	/**
	 * From Serializable documentation
	 * (http://docs.oracle.com/javase/1.5.0/docs/api/java/io/Serializable.html): "To allow subtypes
	 * of non-serializable classes to be serialized, the subtype may assume responsibility for
	 * saving and restoring the state of the supertype's public, protected, and (if accessible)
	 * package fields. The subtype may assume this responsibility only if the class it extends has
	 * an accessible no-arg constructor to initialize the class's state. It is an error to declare a
	 * class Serializable if this is not the case. The error will be detected at runtime."
	 */
	protected SuggestionsOutput() {
	}

	public SuggestionsOutput(String suggestionText, String origText, double suggestionFeasibility,
			String id, int wordPosition, int charPosition) {
		this.suggestionText = suggestionText;
		this.originalText = origText;
		this.suggestionFeasibility = suggestionFeasibility;
		this.id = id;
		this.wordPosition = wordPosition;
		this.charPosition = charPosition;
	}

	public void setSuggestionText(String suggestionText) {
		this.suggestionText = suggestionText;
	}

	public String getSuggestionText() {
		return suggestionText;
	}

	public void setSuggestionFeasibility(double suggestionFeasibility) {
		this.suggestionFeasibility = suggestionFeasibility;
	}

	public double getSuggestionFeasibility() {
		return suggestionFeasibility;
	}

	public String getId() {
		return id;
	}

	public int getWordPosition() {
		return wordPosition;
	}

	public int getCharPosition() {
		return charPosition;
	}

	public int getSuggestionWordLength() {
		if ("".equals(suggestionText))
			return 0;
		return suggestionText.split(" ").length;
	}

	public int getSuggestionCharLength() {
		return suggestionText.length();
	}

	public String getOriginal() {
		return originalText;
	}

	public int getOriginalWordLength() {
		if ("".equals(originalText))
			return 0;
		return originalText.split(" ").length;
	}

	public int getOriginalCharLength() {
		return originalText.length();
	}

	/**
	 * Suggestions with higher feasibilities go first. If equal, promote shortest ones.
	 * 
	 * @param s
	 * @return
	 */
	@Override
	public int compareTo(SuggestionsOutput s) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		if (this == s) {
			return EQUAL;
		}

		int comparison = Double.compare(this.suggestionFeasibility, s.suggestionFeasibility);
		if (comparison != EQUAL) {
			return -comparison;
		}

		if (this.suggestionText.length() < s.suggestionText.length()) {
			return BEFORE;
		}
		if (this.suggestionText.length() > s.suggestionText.length()) {
			return AFTER;
		}

		return this.suggestionText.compareTo(s.suggestionText);
		//
		// if (this.suggestionText.equals(s.suggestionText))
		// return EQUAL;
		// return BEFORE;
	}
}
