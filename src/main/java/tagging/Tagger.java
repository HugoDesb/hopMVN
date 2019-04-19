package tagging;

import document.TextDocument;
import tagging.RNNTagger.TaggedSentence;

import java.util.ArrayList;

public interface Tagger {

    public ArrayList<TaggedSentence> tag(TextDocument textDocument);
}
