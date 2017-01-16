package collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import org.junit.Test;

public class FileBasedCollectionSorterTest {

    private static final char[] CODES = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };


    @Test
    public void sortCollection_manyElements_noDuplicates() throws Exception {
        int numOfElements = 10_000;

        FileBasedCollection<String> unsorted = new FileBasedCollection<>(100);
        for(int i=0;i<numOfElements;i++){
            unsorted.add("random@line.constant.prefix{" + i + "}");
        }

        unsorted.sort();

        assertSorted(unsorted);
    }

    @Test
    public void sortCollection_manyElements_withDuplicates() throws Exception {
        int numOfElements = 10_000;
        int numOfDimensions = 6;
        Random random = new Random();

        FileBasedCollection<String> unsorted = new FileBasedCollection<>(100);
        for(int i=0;i<numOfElements;i++){
            unsorted.add(nextLine(random, numOfDimensions));
        }

        unsorted.sort();

        assertSorted(unsorted);
    }

    private void assertSorted(FileBasedCollection<String> sorted) throws IOException {
        try(FileBasedCollection.FileBasedIterator<String> iterator = sorted.iterator()){
            String lastLocator = null;
            while(iterator.hasNext()){
                String currentLocator = iterator.next();
                if(lastLocator != null){
                    assertTrue(currentLocator.compareTo(lastLocator) > 0);
                }
                lastLocator = currentLocator;
            }
        }
    }

    private String nextLine(Random random, int numDimensions) {
        StringBuilder sb = new StringBuilder(40);
        sb.append("random@line.constant.prefix{");
        int r = random.nextInt();
        for (int j = 0; j < numDimensions; j++) {
            if (j > 0) {
                sb.append(",");
            }
            sb.append(CODES[r & 15]);
            r >>= 4;
        }
        sb.append("}");
        return sb.toString();
    }

    @Test
    public void sortCollection_elementsFiltered_String() throws Exception {
        FileBasedCollection<String> unsorted = createUnsortedCollection("C", "D", "A", "E", "B");
        unsorted.sort();
        assertOrder(unsorted, "A", "B", "C", "D", "E");
    }
    
    @Test
    public void sortCollection_elementsFiltered_Integer() throws Exception {
        FileBasedCollection<Integer> unsorted = createUnsortedCollection(5, 4, 8, 1);
        unsorted.sort();
        assertOrder(unsorted, 1, 4, 5, 8);
    }

    @Test
    public void sortCollection_duplicatedValues_filteredOut_String() throws Exception {
        FileBasedCollection<String> unsorted = createUnsortedCollection("B", "A", "B", "C", "A");
        unsorted.sort();
        assertOrder(unsorted, "A", "B", "C");
    }
    
    @Test
    public void sortCollection_duplicatedValues_filteredOut_Integer() throws Exception {
        FileBasedCollection<Integer> unsorted = createUnsortedCollection(1, 5, 3, 1, 6);
        unsorted.sort();
        assertOrder(unsorted, 1, 3, 5, 6);
    }

  
    private <E extends Serializable & Comparable<E>> void assertOrder(FileBasedCollection<E> sorted, E... elementsInOrder) throws IOException {
        try(FileBasedCollection.FileBasedIterator<E> iterator = sorted.iterator()){
            for(E element : elementsInOrder){
                assertTrue(iterator.hasNext());
                assertEquals(element, iterator.next());
            }
            assertFalse(iterator.hasNext());
        }finally {
            sorted.close();
        }
    }

    private <E extends Serializable & Comparable<E>> FileBasedCollection<E> createUnsortedCollection(E... elements){
        FileBasedCollection<E> unsorted = new FileBasedCollection<>(100);
        for(E element : elements){
            unsorted.add(element);
        }
        return unsorted;
    }

  
}