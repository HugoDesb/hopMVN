package MWExtraction.dope;

public class MWEFilter {

    private String [][] uniGram;
    private String [][] biGram;
    private String [][] triGram;

    public MWEFilter() {
        uniGram = new String[][]{{""}, {""}};
        biGram = new String[][]{{"",""}, {"",""}};
        triGram = new String[][]{{"","",""}, {"","",""}};
    }

    public String[][] getForGram() {
        return uniGram;
    }

    public String[][] getForBiGram() {
        return biGram;
    }

    public String[][] getForTriGram() {
        return triGram;
    }
}
