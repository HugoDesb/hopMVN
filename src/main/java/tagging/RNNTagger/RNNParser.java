package tagging.RNNTagger;

public class RNNParser {

    public static RNNTag parse(String line){
        String [] hop = line.split("\\t");
        if(hop.length != 3){
            throw new IllegalArgumentException("Line does not match standard RNNTagger line : should be 3 infos separated by tabs.\nLine is : '"+line+" .");
        }
        return new RNNTag(hop[0], hop[1], hop[2]);
    }
}
