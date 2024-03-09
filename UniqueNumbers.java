package testcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

public class UniqueNumbers {

	public enum StructureType {ARRAY, ARRAY_LIST, ORDERED_SET, ORDERED_SET_COMPARATOR, SET_SORT_FIRST}
	public static void main(String[] args)
	{
		//Set the test criteria
		int testCount = 100;
		int upperBound = 1000000;
		int listSize = 1000000;
		
		//Run a test for every type
		for(StructureType type : StructureType.values())
		{

			//Run the tests and get the average execution time
			long averageExecutionTime = getAverageExecutionTime(type, testCount, upperBound, listSize);
		
			//Print the results
			formatAndPrintTestResults(type, testCount, upperBound, listSize, averageExecutionTime);
		}
		
		//Test the validity of each implementation
		uniqueNumbersOrderedSetTest(50, 75);
		uniqueNumbersOrderedSetComparatorTest(50, 75);
		uniqueNumbersSetSortFirstTest(50, 75);
		uniqueNumbersArrayTest(50, 75);
		uniqueNumbersListTest(50, 75);
	}

	
	public static long getAverageExecutionTime(StructureType type, int testCount, int upperBound, int listSize) 
	{	
		switch(type)
		{
			case ARRAY:
				return getAvgArrayExecutionTime(testCount, upperBound, listSize);
			case ARRAY_LIST:
				return getAvgListExecutionTime(testCount, upperBound, listSize);
			case ORDERED_SET:
				return getAvgOrderedSetExecutionTime(testCount, upperBound, listSize);
			case ORDERED_SET_COMPARATOR:
				return getAvgOrderedSetComparatorExecutionTime(testCount, upperBound, listSize);
			case SET_SORT_FIRST:
				return getAvgSetSortFirstExecutionTime(testCount, upperBound, listSize);
			default:
				return 0;
		}
		
	}
	
	//---------------------SOLUTIONS TO FINDING UNIQUE NUMBERS FROM A LIST---------------------
	public static int[] findUniqueNumbersArray(int[] list)
	{
		//STEP 1: Create an ArrayList from the int array,
		//would not be necessary if argument was an ArrayList
		// (enables use of Collections class)
		ArrayList<Integer> sortedList = new ArrayList<>();
		for(int i = 0; i < list.length; i++)
		{
			sortedList.add(list[i]);
		}
		
		//STEP 2: Sort the list
		Collections.sort(sortedList, Collections.reverseOrder());

		//STEP 3: Remove duplicate entries
		int cursor = 0;
		while(cursor < sortedList.size()-1) 
		{
			
			//Check if the item next to the cursor are the same
			if(sortedList.get(cursor+1) == sortedList.get(cursor))
				sortedList.remove(cursor+1); //Remove it if they are
			else cursor+=1;	//Skip to the next item in the list
			
		}
		
		//STEP 4: Create the return array
		int[] result = new int[sortedList.size()];
		for(int i = 0; i< result.length; i++)
		{
			result[i] = sortedList.get(i);
		}
		
		//STEP 5:
		return result;
	}
	
	public static ArrayList<Integer> findUniqueNumbersArrayList(ArrayList<Integer> list)
	{
		//STEP 1: Copy list contents
		ArrayList<Integer> uniqueNumbers = new ArrayList<>();
		for(int i = 0; i < list.size(); i++)
			uniqueNumbers.add(list.get(i));
		
		//STEP 2: Sort the list
		Collections.sort(uniqueNumbers, Collections.reverseOrder());
		
		//STEP 3: Remove duplicate entries
		int cursor = 0;
		while(cursor < uniqueNumbers.size()-1) 
		{
			//Check if the item next to the cursor are the same
			if(uniqueNumbers.get(cursor+1) == uniqueNumbers.get(cursor))
				uniqueNumbers.remove(cursor+1); //Remove it if they are
			else cursor+=1;	//Skip to the next item in the list
		}
		
		return uniqueNumbers;
	}
	
	public static SortedSet<Integer> findUniqueNumbersSet(ArrayList<Integer> randomNums) 
	{
		
		SortedSet<Integer> uniqueNumbers = new ConcurrentSkipListSet<Integer>(randomNums);
		
		return ((NavigableSet<Integer>) uniqueNumbers).descendingSet();
	}
	
	public static SortedSet<Integer> findUniqueNumbersSetComparator(ArrayList<Integer> randomNums) 
	{
		//STEP 1: Create a comparator to make the set be in descending order
		Comparator<Integer> comparator = new Comparator<Integer>()
		{
			@Override
			public int compare(Integer o1, Integer o2) {
				if(o1 == o2) return 0;
				if(o1 > o2) return -1;
				else return 1;
			}
		};
		
		//STEP 2: Create the SortedSet object with the comparator
		SortedSet<Integer> uniqueNumbers = new ConcurrentSkipListSet<Integer>(comparator);
		
		//STEP 3: Add all the items to the set
		for(int i = 0; i < randomNums.size(); i++)
		{
			uniqueNumbers.add(randomNums.get(i));
		}
		
		//STEP 3:
		return uniqueNumbers;
	}
	
	public static SortedSet<Integer> findUniqueNumbersSetSortFirst(ArrayList<Integer> randomNums) {
		//STEP 1: Copy list contents
		ArrayList<Integer> randomNumsCopy = new ArrayList<>();
		for(int i = 0; i < randomNums.size(); i++)
		{
			randomNumsCopy.add(randomNums.get(i));
		}
		//STEP 2: Sort the list.  *Collections.reverseOrder() not used because it doesnt matter when creating the set from an arraylist
		Collections.sort(randomNumsCopy); 
		
		//STEP 3: Create the set from the sorted list
		SortedSet<Integer> uniqueNumbers = new TreeSet<Integer>(randomNumsCopy);
		
		//STEP 4: Returning descendingSet because without a comparator that reverses the order, sorted set returns natural ordering
		return ((NavigableSet<Integer>) uniqueNumbers).descendingSet();
	}	

	//--------------AVERAGE EXECUTION TIME FUNCTIONS-----------------------------
	public static long getAvgSetSortFirstExecutionTime(int testCount, int upperBound, int listSize) {
		long averageExecutionTime = 0;
		
		// Run the tests generating new data set every time
		for(int test = 0; test < testCount; test++)
		{
			ArrayList<Integer> randomNums = generateRandomNumbersList(upperBound, listSize);
			
			long startTime = System.currentTimeMillis();
			SortedSet<Integer> uniqueNums = findUniqueNumbersSetSortFirst(randomNums);
			long endTime = System.currentTimeMillis();
			
			long executionTime =  endTime - startTime;
			averageExecutionTime += executionTime;
		}
		
		averageExecutionTime = averageExecutionTime / testCount;
		return averageExecutionTime;
	}
	
	public static long getAvgOrderedSetComparatorExecutionTime(int testCount, int upperBound, int listSize) {
		long averageExecutionTime = 0;
		
		// Run the tests generating new data set every time
		for(int test = 0; test < testCount; test++)
		{
			ArrayList<Integer> randomNums = generateRandomNumbersList(upperBound, listSize);
			
			long startTime = System.currentTimeMillis();
			SortedSet<Integer> uniqueNums = findUniqueNumbersSetComparator(randomNums);
			long endTime = System.currentTimeMillis();
			
			long executionTime =  endTime - startTime;
			averageExecutionTime += executionTime;
		}
		
		averageExecutionTime = averageExecutionTime / testCount;
		return averageExecutionTime;
	}
	public static long getAvgOrderedSetExecutionTime(int testCount, int upperBound, int listSize) {
		long averageExecutionTime = 0;
		
		// Run the tests generating new data set every time
		for(int test = 0; test < testCount; test++)
		{
			ArrayList<Integer> randomNums = generateRandomNumbersList(upperBound, listSize);
			
			long startTime = System.currentTimeMillis();
			SortedSet<Integer> uniqueNums = findUniqueNumbersSetComparator(randomNums);
			long endTime = System.currentTimeMillis();
			
			long executionTime =  endTime - startTime;
			averageExecutionTime += executionTime;
		}
		
		averageExecutionTime = averageExecutionTime / testCount;
		return averageExecutionTime;
	}
	
	public static long getAvgListExecutionTime(int testCount, int upperBound, int listSize) {
		long averageExecutionTime = 0;
		
		// Run the tests generating new data set every time
		for(int test = 0; test < testCount; test++)
		{
			ArrayList<Integer> randomNums = generateRandomNumbersList(upperBound, listSize);
			
			long startTime = System.currentTimeMillis();
			ArrayList<Integer> uniqueNums = findUniqueNumbersArrayList(randomNums);
			long endTime = System.currentTimeMillis();
			
			long executionTime =  endTime - startTime;
			averageExecutionTime += executionTime;
		}
		
		averageExecutionTime = averageExecutionTime / testCount;
		return averageExecutionTime;
	}

	public static long getAvgArrayExecutionTime(int testCount, int upperBound, int listSize) 
	{
		long averageExecutionTime = 0;
		
		// Run the tests generating new data set every time
		for(int test = 0; test < testCount; test++)
		{
			int[] randomNums = generateRandomNumbersArray(upperBound, listSize);
			
			long startTime = System.currentTimeMillis();
			int[] uniqueNums = findUniqueNumbersArray(randomNums);
			long endTime = System.currentTimeMillis();
			
			long executionTime =  endTime - startTime;
			averageExecutionTime += executionTime;
		}
		
		averageExecutionTime = averageExecutionTime / testCount;
		return averageExecutionTime;
	}
	
	//-------------------VALIDITY TESTS--------------------------
	public static void uniqueNumbersArrayTest(int upperBound, int listSize)
	{
		System.out.println("Finding Unique Numbers using Array implementation...");
		int[] randomNumbers = generateRandomNumbersArray(upperBound, listSize);
		
		//TEST 1
		System.out.println("Original List:");
		printArray(randomNumbers);
		
		int[] uniqueNumbers = findUniqueNumbersArray(randomNumbers);
		
		System.out.println("Processed List:");
		printArray(uniqueNumbers);
		System.out.println();
	}
	
	public static void uniqueNumbersListTest(int upperBound, int listSize)
	{	
		System.out.println("Finding Unique Numbers using List implementation...");
		ArrayList<Integer> randomNumbers = generateRandomNumbersList(upperBound, listSize);
		
		System.out.println("Original list:");
		System.out.println(randomNumbers.toString());
		
		ArrayList<Integer> uniqueNumbers = findUniqueNumbersArrayList(randomNumbers);
		
		System.out.println("Processed list:");
		System.out.println(uniqueNumbers.toString()+"\n");
	}
	
	public static void uniqueNumbersOrderedSetTest(int upperBound, int listSize)
	{
		System.out.println("Finding Unique Numbers using OrderedSet implementation...");
		ArrayList<Integer> randomNumbers = generateRandomNumbersList(upperBound, listSize);
		
		System.out.println("Original list:");
		System.out.println(randomNumbers.toString());
		
		SortedSet<Integer> uniqueNumbers = findUniqueNumbersSet(randomNumbers);
		
		System.out.println("Processed list:");
		System.out.println(uniqueNumbers.toString()+"\n");
	}
	public static void uniqueNumbersOrderedSetComparatorTest(int upperBound, int listSize)
	{
		System.out.println("Finding Unique Numbers using OrderedSet Comparator implementation...");
		ArrayList<Integer> randomNumbers = generateRandomNumbersList(upperBound, listSize);
		
		System.out.println("Original list:");
		System.out.println(randomNumbers.toString());
		
		SortedSet<Integer> uniqueNumbers = findUniqueNumbersSetComparator(randomNumbers);
		
		System.out.println("Processed list:");
		System.out.println(uniqueNumbers.toString()+"\n");
	}
	public static void uniqueNumbersSetSortFirstTest(int upperBound, int listSize)
	{
		System.out.println("Finding Unique Numbers using Set Sort First implementation...");
		ArrayList<Integer> randomNumbers = generateRandomNumbersList(upperBound, listSize);
		
		System.out.println("Original list:");
		System.out.println(randomNumbers.toString());
		
		SortedSet<Integer> uniqueNumbers = findUniqueNumbersSetSortFirst(randomNumbers);
		
		System.out.println("Processed list:");
		System.out.println(uniqueNumbers.toString()+"\n");
	}
	
	//--------------------UTILITY FUNCTIONS----------------------
	public static void formatAndPrintTestResults(StructureType type, int testCount, int upperBound, int listSize,
			long averageExecutionTime) {
		switch(type)
		{
		case ARRAY:
			System.out.println("Testing Array implementation: ");
			break;
		case ARRAY_LIST:
			System.out.println("Testing ArrayList implementation: ");
			break;
		case ORDERED_SET:
			System.out.println("Testing Ordered Set implementation: ");
			break;
		case ORDERED_SET_COMPARATOR:
			System.out.println("Testing Ordered Set with Comparator implementation: ");
			break;
		case SET_SORT_FIRST:
			System.out.println("Testing Set Sort First implementation: ");
			break;
		}
		System.out.println("\nTest criteria: ");
		System.out.println("Number of Tests: " + testCount);
		System.out.println("Random number generator bounds: 0 - " + upperBound);
		System.out.println("Random number data set size: " + listSize);
		System.out.println("Average execution time: " + averageExecutionTime + " (ms)\n");
	}
	
	public static int[] generateRandomNumbersArray(int upperBound, int listSize)
	{
		Random randomGenerator = new Random();
		int[] randomNumbers = new int[listSize];
		for(int i = 0; i < listSize; i++)
		{
			randomNumbers[i] = randomGenerator.nextInt(upperBound);
		}
		return randomNumbers;
	}
	
	public static ArrayList<Integer> generateRandomNumbersList(int upperBound, int listSize)
	{
		Random randomGenerator = new Random();
		ArrayList<Integer> randomNumbers = new ArrayList<>();
		for(int i = 0; i < listSize; i++)
		{
			randomNumbers.add(randomGenerator.nextInt(upperBound));
		}
		return randomNumbers;
	}
	
	public static void printArray(int[] array)
	{
		System.out.print("[");
		for(int i = 0; i < array.length; i++)
		{
			System.out.print(array[i]);
			if(i != array.length -1) {System.out.print(", ");}
		}
		System.out.print("]\n");
	}
	
	public static void printListComparison(int[] originalList, int[] processedList)
	{
		System.out.println("Original list:");
		printArray(originalList);
		System.out.println("Processed list:");
		printArray(processedList);
	}

}
