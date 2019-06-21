package MWExtraction.Wrapper;

import java.io.*;
import java.util.*;

public class MultiColumnCSVSort {
    private static final String COLUMN_SEPARATOR = "\t";

    public static List<List<String>> compareHetopThenMeasure(String filename) throws Exception {

        List<List<String>> lines = readCsv(filename);

        // Create a comparator that compares the elements from column 1,
        // in descending order
        Comparator<List<String>> c0 = createDescendingComparator(1);

        // Create a comparator that compares the elements from column 2,
        // in descending order
        Comparator<List<String>> c1 = createDescendingComparator(2);

        // Create a comparator that compares primarily by using c0,
        // and secondarily by using c1
        Comparator<List<String>> comparator = createComparator(c0, c1);
        Collections.sort(lines, comparator);

        return lines;
    }

    public static List<List<String>> readCsv(String filename) {
        List<List<String>> lines = new ArrayList<List<String>>();

        try {
            InputStream inputStream = new FileInputStream(filename);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream));


            String line = null;

            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                List<String> list = Arrays.asList(line.split(COLUMN_SEPARATOR));
                lines.add(list);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    private static void writeCsv(List<List<String>> lines, String filename) {
        try{
            OutputStream outputStream = new FileOutputStream(new File(filename));
            Writer writer = new OutputStreamWriter(outputStream);
            for (List<String> list : lines)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    writer.write(list.get(i));
                    if (i < list.size() - 1)
                    {
                        writer.write(COLUMN_SEPARATOR);
                    }
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SafeVarargs
    private static <T> Comparator<T>
    createComparator(Comparator<? super T>... delegates)
    {
        return (t0, t1) ->
        {
            for (Comparator<? super T> delegate : delegates)
            {
                int n = delegate.compare(t0, t1);
                if (n != 0)
                {
                    return n;
                }
            }
            return 0;
        };
    }

    private static <T extends Comparable<? super T>> Comparator<List<T>>
    createAscendingComparator(int index)
    {
        return createListAtIndexComparator(Comparator.naturalOrder(), index);
    }

    private static <T extends Comparable<? super T>> Comparator<List<T>>
    createDescendingComparator(int index)
    {
        return createListAtIndexComparator(Comparator.reverseOrder(), index);
    }

    private static <T> Comparator<List<T>>
    createListAtIndexComparator(Comparator<? super T> delegate, int index)
    {
        return (list0, list1) ->
                delegate.compare(list0.get(index), list1.get(index));
    }

}