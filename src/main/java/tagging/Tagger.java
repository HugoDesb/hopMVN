package tagging;

import document.TextDocument;
import tagging.RNNTagger.TaggedSentence;

import java.util.ArrayList;

public interface Tagger<T> {

    public ArrayList<TaggedSentence<T>> tag(TextDocument textDocument);
}
