
package stream;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Java 8 stream tutorial.
 * 
 * parallelStream() uses fork join underneath. It may not always quicker.
 * 
 * @author aus
 */
public class ParallelStreamTest
{
    private final long MAX = 1000000L;
    private List<Long> arrayList, linkedList;

    @Before
    public void setUp() throws Exception
    {
        linkedList = new LinkedList<>();
        arrayList = new ArrayList<>();
        for (long i = 0; i < MAX; i++)
        {
            linkedList.add(Long.valueOf(i));
            arrayList.add(Long.valueOf(i));
        }
    }

    @Test
    public void parallelIsFasterForArrayList()
    {
        System.out.println("ArrayList : parallel is faster in case 1");
        compareCase1(arrayList);
        System.out.println("");
        System.out.println("ArrayList : parallel is faster in case 2");
        compareCase2(arrayList);
    }

    @Test
    public void parallelMayNotAlwaysFaster()
    {
        System.out.println("LinkedList : parallel is faster in case 1");
        compareCase1(linkedList);
        System.out.println("");
        System.out.println("LinkedList : parallel is slower in case 2");
        compareCase2(linkedList);
    }

    private void compareCase1(List<Long> list)
    {
        long time1 = System.currentTimeMillis();
        long result = 0;
        for (int i = 0; i < 10; i++)
        {
            result += list.stream().mapToLong(l -> l).sum();
        }
        long time2 = System.currentTimeMillis();
        long parallelResult = 0;
        for (int i = 0; i < 10; i++)
        {
            parallelResult += list.parallelStream().mapToLong(l -> l).sum();
        }
        long time3 = System.currentTimeMillis();

        System.out.println("stream   time taken : " + (time2 - time1));
        System.out.println("parallel time taken : " + (time3 - time2));

        assertEquals(result, parallelResult);
    }

    private void compareCase2(List<Long> list)
    {
        long time1 = System.currentTimeMillis();

        Map<Boolean, List<Long>> result = null;
        for (int i = 0; i < 10; i++)
        {
            result =
                    list.stream().collect(Collectors.groupingBy(s -> (s % 2 == 0)));
        }

        long time2 = System.currentTimeMillis();

        Map<Boolean, List<Long>> parallelResult = null;
        for (int i = 0; i < 10; i++)
        {
            parallelResult =
                    list.parallelStream().collect(Collectors.groupingBy(s -> (s % 2 == 0)));
        }

        long time3 = System.currentTimeMillis();

        System.out.println("stream   time taken : " + (time2 - time1));
        System.out.println("parallel time taken : " + (time3 - time2));

        assertEquals(result, parallelResult);
    }

}
