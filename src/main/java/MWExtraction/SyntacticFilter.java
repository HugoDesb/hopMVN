package MWExtraction;

public class SyntacticFilter {

    private String [][] uniGram;
    private String [][] biGram;
    private String [][] triGram;

    public String[][] getForGram() {
        return uniGram;
    }

    public String[][] getForBiGram() {
        return biGram;
    }

    public String[][] getForTriGram() {
        return triGram;
    }


    class Builder{
        private String [][] uniGram;
        private String [][] biGram;
        private String [][] triGram;
    }
}
