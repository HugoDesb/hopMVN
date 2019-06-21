package MWExtraction.Wrapper;

import java.io.File;

public class Closing {

    public static boolean cleanAll(){
        boolean success = true;

        success = success && (new File("./tmp/tmp.txt")).delete();

        return success;
    }
}
