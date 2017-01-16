package collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import collections.FileBasedCollection.FileBasedIterator;

public class FileBasedCollectionTest {

	private FileBasedCollection<Integer> coll;
	
	@Before
	public void setUp() {
		coll = new FileBasedCollection<>(2);
	}
	
	@After
	public void tearDown() {
		coll.close();
	}

	@Test
	public void testAdd() {
		assertTrue(coll.add(1));
		assertEquals(1, coll.size());
	}
	
	@Test
	public void testAddAll() {
		final int max = 10;
		ArrayList<Integer> check = new ArrayList<>(max);
		for(int i = 1; i <= max ; i++) {
			check.add(i);
		}
		assertTrue(coll.addAll(check));
		assertEquals(10, coll.size());
	}
	
	@Test
	public void testIsEmpty() {
		assertTrue(coll.isEmpty());
	}
	
	
	@Test
	public void testClear() {
		coll.add(1);
		coll.add(2);
		coll.add(3);
		coll.clear();
		assertTrue(coll.isEmpty());
	}
	
	@Test
	public void testGetSizeWithCache_chunkborder() throws IOException {
		coll.add(1);
		coll.add(2);
		coll.add(3);
		try (FileBasedIterator<Integer> iter = coll.iterator()) {
			assertEquals(3, coll.size());
		}
	}
	
	
	@Test
	public void testGetSizeWithCache_single_item() throws IOException {
		coll.add(1);
		try (FileBasedIterator<Integer> iter = coll.iterator()) {
			assertEquals(1, coll.size());
		}
	}
	
	@Test
	public void testGetSizeWithCache_empty() {
		coll.iterator();
		assertEquals(0, coll.size());
	}
	
	@Test
	public void testGetSizeWithCache_full_chunk() {
		coll.add(1);
		coll.add(2);
		coll.iterator();
		assertEquals(2, coll.size());
	}
	
	@Test
	public void testRetainAll() {
		Collection<Integer> all = new ArrayList<>(2);
		coll.add(1);
		coll.add(2);
		all.add(1);
		all.add(2);
		assertFalse(coll.retainAll(all));
		assertEquals(2, coll.size());
	}
	
	@Test
	public void testRetain_this() {
		coll.add(1);
		coll.add(2);
		assertFalse(coll.retainAll(coll));
		assertEquals(2, coll.size());
	}
	
	@Test
	public void testRetain_One() {
		Collection<Integer> all = new ArrayList<>(2);
		coll.add(1);
		coll.add(2);
		all.add(1);
		assertTrue(coll.retainAll(all));
		assertEquals(1, coll.size());
		assertTrue(coll.contains(1));
	}
	
	@Test
	public void testRetain_None() {
		Collection<Integer> all = new ArrayList<>(2);
		coll.add(1);
		coll.add(2);
		assertTrue(coll.retainAll(all));
		assertTrue(coll.isEmpty());
	}
	
	@Test
	public void testRetain_null() {
		coll.add(1);
		coll.add(2);
		assertEquals(false, coll.retainAll(null));
		assertEquals(2, coll.size());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testToArray() {
		coll.toArray();
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void tesToArrayTyped() {
		coll.toArray(new Integer [] {});
		
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testRemove() {
		coll.remove(0);
	}
	
	@Test
	public void testContains() {
		coll.add(1);
		assertTrue(coll.contains(1));
		
	}
	
	@Test
	public void testNotContains() {
		coll.add(2);
		assertFalse(coll.contains(1));
		
	}
	
	@Test
	public void testContainsAll() {
		coll.add(1);
		coll.add(2);
		ArrayList<Integer> all = new ArrayList<>(2);
		all.add(1);
		all.add(2);
		assertTrue(coll.containsAll(all));
		
	}
	
	@Test
	public void testContainsAllChunkBoundary() {
		coll.add(1);
		coll.add(2);
		coll.add(3);
		ArrayList<Integer> all = new ArrayList<>(3);
		all.add(1);
		all.add(2);
		all.add(3); 
		assertTrue(coll.containsAll(all));
		
	}
	
	@Test
	public void testContainsAllChunkBoundary2() {
		coll.add(1);
		coll.add(2);
		coll.add(3);
		coll.add(4);
		ArrayList<Integer> all = new ArrayList<>(3);
		all.add(1);
		all.add(2);
		all.add(3);
		all.add(4);
		assertTrue(coll.containsAll(all));
		
	}
	
	@Test
	public void testRemoveNonExistentElement() {
		coll.add(1);
		List<Integer> toRemove = Collections.singletonList(2);
		assertFalse(coll.removeAll(toRemove));
	}
	
	@Test
	public void testRemoveAll() {
		final int max = 10;
		ArrayList<Integer> check = new ArrayList<>(max);
		for(int i = 1; i <= max ; i++) {
			coll.add(i);
			check.add(i);
		}
		assertTrue(coll.removeAll(check));
		assertEquals(true, coll.isEmpty());
	}

	@Test
	public void testRemoveElement() {
		coll.add(1);
		List<Integer> toRemove = Collections.singletonList(1);
		assertTrue(coll.removeAll(toRemove));
		assertEquals(true, coll.isEmpty());
	}
	
	@Test
	public void testRemoveMultipleElements() {
		coll.add(1);
		coll.add(2);
		coll.add(3);
		List<Integer> toRemove = Arrays.asList(1, 2, 3);
		assertTrue(coll.removeAll(toRemove));
		assertEquals(true, coll.isEmpty());
	}
	
	@Test
	public void testRemoveSomeElements() {
		coll.add(1);
		coll.add(2);
		coll.add(3);
		List<Integer> toRemove = Arrays.asList(1, 3);
		assertTrue(coll.removeAll(toRemove));
		assertEquals(1, coll.size());
		assertTrue(coll.contains(2));
	}
	
	@Test
	public void testRemoveSomeElements_FBC() {
		coll.add(1);
		coll.add(2);
		coll.add(3);
		FileBasedCollection<Integer> toRemove = new FileBasedCollection<>(2);
		toRemove.add(1);
		toRemove.add(2);
		assertTrue(coll.removeAll(toRemove));
		assertEquals(1, coll.size());
		assertTrue(coll.contains(3));
	}
	
	@Test
	public void testRemoveSomeElementsFromMultipleChunks_diff_less_then_chunkSize() {
		coll.add(1);
		coll.add(2);
		coll.add(3);
		coll.add(4);
		coll.add(5);
		coll.add(6);
		List<Integer> toRemove = Arrays.asList(1, 2, 3, 4, 6);
		assertTrue(coll.removeAll(toRemove));
		
		assertEquals(1, coll.size());
		assertTrue(coll.contains(5));
	}
	
	@Test
	public void testRemoveSomeElementsFromMultipleChunks_diff_equals_chunkSize() {
		coll.add(1);
		coll.add(2);
		coll.add(3);
		coll.add(4);
		coll.add(5);
		coll.add(6);
		List<Integer> toRemove = Arrays.asList(2, 3, 4, 6);
		assertTrue(coll.removeAll(toRemove));
		assertEquals(2, coll.size());
		assertTrue(coll.contains(1));
		assertTrue(coll.contains(5));
	}
	
	@Test
	public void testRemoveSomeElementsFromMultipleChunks_diff_greater_chunkSize() {
		coll.add(1);
		coll.add(2);
		coll.add(3);
		coll.add(4);
		coll.add(5);
		coll.add(6);
		List<Integer> toRemove = Arrays.asList(2, 4, 6);
		assertTrue(coll.removeAll(toRemove));
		assertEquals(3, coll.size());
		assertTrue(coll.contains(1));
		assertTrue(coll.contains(3));
		assertTrue(coll.contains(5));
	}
	
	@Test
	public void testRemoveAllElementsFromChunk() {
		coll.add(1);
		coll.add(2);
		List<Integer> toRemove = Arrays.asList(1, 2);
		assertTrue(coll.removeAll(toRemove));
		assertEquals(0, coll.size());
	}
	
	@Test
	public void testRemoveEmptyCollection() {
		coll.add(1);
		coll.add(2);
		coll.add(3);
		List<Integer> toRemove = Arrays.asList(new Integer[] {});
		assertEquals(false, coll.removeAll(toRemove));
		assertEquals(3, coll.size());
	}
	
	@Test
	public void testRemoveNullCollection() {
		coll.add(1);
		coll.add(2);
		coll.add(3);
		assertEquals(false, coll.removeAll(null));
		assertEquals(3, coll.size());
	}
	
	@Test
	public void testRemoveThisCollection() {
		FileBasedCollection<Integer> coll2 = coll;
		coll.add(1);
		coll.add(2);
		coll.add(3);
		assertEquals(true, coll.removeAll(coll2));
		assertEquals(0, coll.size());
		assertEquals(0, coll2.size());
		
	}
	
		
	@Test
	public void testOneElement() throws IOException {
		coll.add(1);
		
		try (FileBasedIterator<Integer> iter = coll.iterator()) {
			Integer i = iter.next();
			assertEquals(Integer.valueOf(1), i);
		}
	}
	
	
	@Test
	public void testZeroElement() throws IOException {
		try (FileBasedIterator<Integer> iter = coll.iterator()) {
			assertFalse(iter.hasNext()); // the iterator is expected to be empty so this code mustn't be reachable
		}
		
	}

	@Test
	public void testWriteAndReadFromCache() throws IOException {
		final int max = 10;
		
		for(int i = 1; i <= max ; i++) {
			coll.add(i);
		}
		
		try (FileBasedIterator<Integer> iter = coll.iterator()) {
			for(int i = 1; i <= max ; i++) {
				int l = iter.next();
				assertEquals(i, l);
			}
		}
	}
}
