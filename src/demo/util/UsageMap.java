package src.demo.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

// Keep track of the API usage
public class UsageMap {
	private final Date startTime;
	private final Map<String, Map<Date, Integer>> callCount;
	private final List<String> methodsCalled;
	private final SortedSet<Date> minuteBuckets;

	public UsageMap() {
		callCount = new HashMap<String, Map<Date, Integer>>();
		startTime = getMinuteTime();
		methodsCalled = new ArrayList<String>();
		minuteBuckets = new TreeSet<Date>();
	}
	public void addCall(String method) {

		// Get and store the correct minute for this call
		Date callTime = getMinuteTime();
		minuteBuckets.add(callTime); // A set so adding duplicates is fine.

		// Increment the number of calls
		Map<Date, Integer> callTimes = callCount.get(method);
		if (callTimes == null)
		{
			// Must be sorted so times are incrementing
			callTimes = new TreeMap<Date, Integer>();
			callCount.put(method, callTimes);
			
			// A new method is being called
			methodsCalled.add(method);
		}
		
		Integer numCalls = callTimes.get(callTime);
		if (numCalls == null) {
			callTimes.put(callTime, Integer.valueOf(1));
		} else {
			callTimes.put(callTime, Integer.valueOf(numCalls.intValue() + 1));
		}
	}
	
	// Get the total number of calls made to each API method
	public int getTotalCallsForMethod(String method) {
		int numCalls = 0;
		Map<Date, Integer> callTimes = callCount.get(method);
		if (callTimes != null) {
			for (Integer calls: callTimes.values()) {
				numCalls += calls;
			}
		}
		return numCalls;
	}
	
	// Get all the API calls made. List is in order
	// the methods were first called
	public List<String> getAllMethodsCalled() {
		return methodsCalled;
	}	
	
	// Get all minute long time buckets in which an
	// API call was made. 
	public SortedSet<Date> getAllTimeBuckets() {
		return minuteBuckets;
	}	

	// get tyhe number of times an API method was called within
	// a particular time bucket.
	public int getMethodCallsForBucket(String method, Date timeBucket) {
		int numCalls = 0;
		Map<Date, Integer> callTimes = callCount.get(method);
		if (callTimes != null) {
			Integer numCallsInteger = callTimes.get(timeBucket);
			if (numCallsInteger != null) {
				numCalls = numCallsInteger.intValue();
			}
		}
		return numCalls;
	}
	// get the millisecond time for the start of this minute
	private Date getMinuteTime() {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		return now.getTime();
	}
}
