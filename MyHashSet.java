import java.io.*;
import java.util.*;

public class MyHashSet implements HS_Interface {
	private int numBuckets;
	private Node[] bucketArray;
	private int size; // total # keys stored in set right now

	// THIS IS A TYPICAL AVERAGE BUCKET SIZE. IF YOU GET A LOT BIGGER THEN YOU ARE
	// MOVING AWAY FROM (1)
	private final int MAX_ACCEPTABLE_AVE_BUCKET_SIZE = 35;

	public MyHashSet(int numBuckets) {
		size = 0;
		this.numBuckets = numBuckets;
		bucketArray = new Node[numBuckets]; // array of linked lists
		System.out.format(
				"IN CONSTRUCTOR: INITIAL TABLE LENGTH=%d RESIZE WILL OCCUR EVERY TIME AVE BUCKET LENGTH EXCEEDS %d\n",
				numBuckets, MAX_ACCEPTABLE_AVE_BUCKET_SIZE);
	}

	private boolean add3(String key) { // doesn't check for dupes
		int hashCode = hashOf(key, numBuckets);

		if (bucketArray[hashCode] == null) {
			bucketArray[hashCode] = new Node(key, null); // add to front of empty linked list
		} else {
			Node head = new Node(key, null);

			head.next = bucketArray[hashCode]; // add to front of existing linked list
			bucketArray[hashCode] = head;
		}

		return true; // a key was added
	}

	private boolean add2(String key) {
		int hashCode = hashOf(key, numBuckets);

		if (contains2(key, hashCode) == true) {
			return false; // no key was added because it's already in the table
		} else {
			if (bucketArray[hashCode] == null) {
				bucketArray[hashCode] = new Node(key, null); // add to front of empty linked list
			} else {
				Node head = new Node(key, null);

				head.next = bucketArray[hashCode]; // add to front of existing linked list
				bucketArray[hashCode] = head;
			}

			return true; // a key was added
		}
	} // END ADD2

	public boolean add(String key) { // ADD THE KEY TO THE TABLE
		// tests for uniques
		++size;
		if (size > MAX_ACCEPTABLE_AVE_BUCKET_SIZE * this.numBuckets) {
			upSize_ReHash_AllKeys(); // DOUBLE THE ARRAY .length THEN REHASH ALL KEYS
			
			return add2(key);
		} else {
			return add2(key);
		}

	} // END ADD

	private void upSize_ReHash_AllKeys() {
		System.out.format("KEYS HASHED=%10d UPSIZING TABLE FROM %8d to %8d REHASHING ALL KEYS\n", size,
			bucketArray.length, bucketArray.length * 2);
		Node[] biggerArray = new Node[bucketArray.length * 2]; // makes new array that's double the size

		int tempNumBuckets = numBuckets;
		this.numBuckets = biggerArray.length; // <== DONT FORGET TO DO THIS AS SOON AS YOU UPSIZE

		Node[] tempBucketArray = bucketArray;
		this.bucketArray = biggerArray;

		for (int i = 0; i < tempNumBuckets; i++) { // loop through old array's indexes
			if (tempBucketArray[i].next != null) { // if old bucketArray[i]'s linked list is not at size 1
				Node curr = tempBucketArray[i];

				add3(curr.data); // adds head of old bucketArray[i]'s linked list to new array

				while (curr.next != null) {
					add3(curr.next.data); // adds the rest of the linked list
					curr = curr.next;
				}
			} else {
				add3(tempBucketArray[i].data); // adds head of old bucketArray[i]'s linked list to new array
			}
		}
	} // END UPSIZE

	public boolean remove(String key) {
		int hashCode = hashOf(key, numBuckets);

		if (bucketArray[hashCode] == null) {
			return false;
		} else {
			Node curr = bucketArray[hashCode];

			if (curr.data.equals(key)) { // checks first node in linked list
				bucketArray[hashCode] = curr.next; // remove from front
				curr.next = null;
				return true;
			}

			while (curr.next != null) {
				if (curr.next.data.equals(key) && curr.next.next != null) {
					// if curr.next's data equals the key and curr.next is not the end of the list
					curr.next = curr.next.next;
					return true;
				} else if (curr.next.data.equals(key) && curr.next.next == null) {
					// if curr.next's data equals the key and curr.next is the end of the list
					curr.next = null; // remove from tail
					return true;
				}
				curr = curr.next;
			}
		}

		return false;
	} // END REMOVE

	private boolean contains2(String key, int hashCode) {
		if (bucketArray[hashCode] == null) {
			return false;
		} else {
			Node curr = bucketArray[hashCode];

			if (curr.data.equals(key)) { // checks first node in linked list
				return true;
			}

			while (curr.next != null) {
				if (curr.next.data.equals(key)) {
					return true;
				}
				curr = curr.next;
			}

			return false; // return false if none of the node's data is equal to the key
		}
	} // END CONTAINS2

	public boolean contains(String key) {
		int hashCode = hashOf(key, numBuckets);
		Node curr = bucketArray[hashCode];

		if (curr.data.equals(key)) { // checks first node in linked list
			return true;
		}

		while (curr.next != null) {
			if (curr.next.data.equals(key)) {
				return true;
			}
			curr = curr.next;
		}

		return false; // return false if none of the node's data is equal to the key
	} // END CONTAINS

	public boolean isEmpty() {
		if (size() != 0) {
			clear();
		}

		return true;
	}

	public int size() {
		return size;
	}

	public void clear() {
		// make temp Node[] of the same size, but it doesn't have anything in it
		Node[] tempBucketArray = new Node[numBuckets];
		bucketArray = tempBucketArray;
	}

	private int hashOf(String key, int numBuckets) // h MUST BE IN [0..numBuckets-1]
	{
		long returnVal = 0;

		for (int i = 0; i < key.length(); i++) {
			returnVal = ((returnVal * 13) + Character.valueOf(key.charAt(i)));
		}

		if (returnVal < 0) {
			returnVal *= -1;
		}

		if (returnVal > numBuckets) {
			returnVal %= numBuckets;
		}

		return (int) returnVal;
	}

} // END MyHashSet CLASS

class Node {
	String data;
	Node next;

	public Node(String data, Node next) {
		this.data = data;
		this.next = next;
	}
}
