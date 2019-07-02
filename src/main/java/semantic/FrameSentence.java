package semantic;

import common.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrameSentence {


    private ArrayList<FrameNetTag> tokens;
    private ArrayList<Chunk> chunks;

    public FrameSentence(List<List<String>> lines) {
        this.tokens = new ArrayList<>();
        for (List<String> line :lines) {
            tokens.add(new FrameNetTag(line));
        }
        chunks = createChunks();
    }

    public ArrayList<Chunk> getChunks() {
        return chunks;
    }

    private ArrayList<Chunk> createChunks() {
        ArrayList<Chunk> chunks = new ArrayList<>();

        Chunk.Builder builder = new Chunk.Builder(tokens.get(0));
        for (int i = 1; i < tokens.size(); i++) {
            FrameNetTag token = tokens.get(i);

            if (builder.isOfSameType(token)) {
                builder.appendText(token.getWord());
            } else {
                chunks.add(builder.build());
                //new Chunk
                builder = new Chunk.Builder(token);
            }
        }
        chunks.add(builder.build());
        return chunks;
    }

    public String getSentence(){
        String sentence = "";
        for (FrameNetTag tags : tokens) {
            sentence += tags.getWord()+" ";
        }
        return sentence.trim();
    }

    public ArrayList<FrameNetTag> getTokens() {
        return tokens;
    }

    public Pair<Integer, String> getTargetIndex(){
        for (FrameNetTag tok :tokens) {
            if(!tok.getFrame().equals("_")){
                return new Pair<>(tok.getIndex(), tok.getFrame());
            }
        }
        return null;
    }

    /**
     * Finds every
     * @return
     */
    public Map<Integer, String> getFrameElementsIndexes(){
        HashMap<Integer, String> hop = new HashMap<>();

        for (FrameNetTag tok :tokens) {
            if(!tok.getFrameElement().equals("O")){
                hop.put(tok.getIndex(), tok.getFrameElement());
            }
        }

        return hop;
    }


    public int getSentenceNumber() {
        return tokens.get(0).getSentenceNumber();
    }
}
