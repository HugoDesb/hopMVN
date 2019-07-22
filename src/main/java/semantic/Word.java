package semantic;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Word {

    private String text;
    private int index;
    private String lemma;
    private String pos_tag;

    public Word(String text, int index, String lemma, String pos_tag) {
        this.text = text;
        this.index = index;
        this.lemma = lemma;
        this.pos_tag = pos_tag;
    }


    public Word(List<String> line) {
        this.index = Integer.parseInt(line.get(0));
        this.text = line.get(1);
        this.lemma = line.get(3);
        this.pos_tag = line.get(5);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getText() {
        return text;
    }

    public int getIndex() {
        return index;
    }

    public String getLemma() {
        return lemma;
    }

    public String getPos_tag() {
        return pos_tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()){
            if(o.getClass() == Integer.class){
                Integer i = (Integer) o;
                return i.equals(index);
            }else{
                return false;
            }
        }
        Word word = (Word) o;
        return index == word.index &&
                Objects.equals(text, word.text) &&
                Objects.equals(lemma, word.lemma) &&
                Objects.equals(pos_tag, word.pos_tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, index, lemma, pos_tag);
    }

    public static Comparator<Word> indexComparator;

    static {
        indexComparator = new Comparator<Word>() {
            @Override
            public int compare(Word t1, Word t2) {

                return (t2.getIndex() < t1.getIndex() ? 1 :
                        (t2.getIndex() == t1.getIndex() ? 0 : -1));
            }
        };
    }

}
