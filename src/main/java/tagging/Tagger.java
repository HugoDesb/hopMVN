package tagging;

import common.document.Sentence;
import common.document.TextDocument;
import tagging.RNNTagger.RNNTag;

import java.util.List;

public interface Tagger {

    List<RNNTag> tag (Sentence sentence);
    void tag(TextDocument textDocument);
}
