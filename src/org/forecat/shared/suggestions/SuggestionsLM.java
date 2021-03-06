package org.forecat.shared.suggestions;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.forecat.client.exceptions.ForecatException;
import org.forecat.shared.ranker.RankerScore;
import org.forecat.shared.ranker.RankerShared;
import org.forecat.shared.suggestions.LM.IRSTLMscorer;
import org.forecat.shared.translation.SourceSegment;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Each suggestion gets their average entropy as score
 * 
 * @author Daniel Torregrosa
 * 
 */
public class SuggestionsLM extends SuggestionsShared implements IsSerializable, Serializable {

	SuggestionsShared base;
	RankerShared ranker;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1080886143337665973L;

	@Override
	public List<SuggestionsOutput> obtainSuggestions(SuggestionsInput input,
			Map<String, List<SourceSegment>> segmentPairs, Map<String, Integer> segmentCounts) {
		List<SuggestionsOutput> output = base.obtainSuggestions(input, segmentPairs, segmentCounts);

		String clippedTargetText = input.getFixedPrefix();
		int j = clippedTargetText.length() - 1;
		int numberSpaces = 0;

		for (j = j > 0 ? j : 0; j > 0; j--) {
			if (clippedTargetText.charAt(j) == ' ') {
				numberSpaces++;

			}
			if (numberSpaces == 3)
				break;
		}

		j = j > 0 ? j + 1 : j;

		clippedTargetText = clippedTargetText.substring(j, clippedTargetText.length());

		try {
			output = ranker.rankerService(input, output);
		} catch (ForecatException e) {
			e.printStackTrace();
		}

		for (SuggestionsOutput so : output) {
			so.setSuggestionFeasibility(
					IRSTLMscorer.getPerplexity(clippedTargetText + so.getSuggestionText()));
			// System.out.println(">>>" + clippedTargetText + "- " + so.getSuggestionText() + " "
			// + so.getSuggestionFeasibility());
		}
		// System.out.println("*");

		return output;
	}

	protected SuggestionsLM() {

	}

	public SuggestionsLM(SuggestionsShared base, RankerShared ranker) {
		super();
		IRSTLMscorer.init();
		this.base = base;
		this.ranker = ranker;
	}

	public SuggestionsLM(SuggestionsShared base) {
		super();
		IRSTLMscorer.init();
		this.base = base;
		this.ranker = new RankerScore();
	}

}
