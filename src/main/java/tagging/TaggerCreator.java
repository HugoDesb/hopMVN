package tagging;

public class TaggerCreator {

    public static Tagger init(TaggerTypes type){

        switch(type){
            case RNNTAGGER:
                return new RNNTagger();
            //case TreeTagger:
        }
        return null;
    }


}
