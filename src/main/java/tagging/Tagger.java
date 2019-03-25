package tagging;

import document.TextDocument;

public interface Tagger {

    public TaggedSentence tag(TextDocument textDocument);
}
