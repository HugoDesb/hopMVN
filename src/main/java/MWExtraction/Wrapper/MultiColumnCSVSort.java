package Wrapper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MultiColumnCSVSort {
    private static final String COLUMN_SEPARATOR = "\t";

    public static List<List<String>> compareHetopThenMeasure(String filename) throws Exception
    {
        InputStream inputStream = new FileInputStream(filename);
        List<List<String>> lines = readCsv(inputStream);

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

    private static List<List<String>> readCsv(
            InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream));
        List<List<String>> lines = new ArrayList<List<String>>();

        String line = null;

        // Skip header
        line = reader.readLine();

        while (true)
        {
            line = reader.readLine();
            if (line == null)
            {
                break;
            }
            List<String> list = Arrays.asList(line.split(COLUMN_SEPARATOR));
            lines.add(list);
        }
        return lines;
    }

    private static void writeCsv(
            String header, List<List<String>> lines, OutputStream outputStream)
            throws IOException
    {
        Writer writer = new OutputStreamWriter(outputStream);
        writer.write(header+"\n");
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