import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * A kNN classification algorithm implementation.
 * 
 */

public class KNN {

	class distanceComparator implements Comparator<Item> {
		Item tempItem;
		@Override
		public int compare(Item o1, Item o2) {
			double d1 = 0.0;
			double d2 = 0.0;
			if (tempItem != null) {
				d1 = getD(tempItem, o1);
				d2 = getD(tempItem, o2);
				if (d1 < d2) 
					return 1;
				else return -1;
			}
			return 0;
		}
		public distanceComparator(Item o3) {
			this.tempItem = o3; 
		}
		
	}
	/**
	 * In this method, you should implement the kNN algorithm. You can add 
	 * other methods in this class, or create a new class to facilitate your
	 * work. If you create other classes, DO NOT FORGET to include those java
   * files when preparing your code for hand in.
   *
	 * Also, Please DO NOT MODIFY the parameters or return values of this method,
   * or any other provided code.  Again, create your own methods or classes as
   * you need them.
	 * 
	 * @param trainingData
	 * 		An Item array of training data
	 * @param testData
	 * 		An Item array of test data
	 * @param k
	 * 		The number of neighbors to use for classification
	 * @return
	 * 		The object KNNResult contains classification accuracy, 
	 * 		category assignment, etc.
	 */
	public KNNResult classify(Item[] trainingData, Item[] testData, int k) {
		
		/* ... YOUR CODE GOES HERE ... */
		
    // for each test item in testData

      // find kNN in trainingData
      
      // get predicted category, save in KNNResult.categoryAssignment

      // save kNN in KNNResult.nearestNeighbors

    // calculate accuracy
		int errorCount = 0;
		int nationCount = 0;
		int machineCount = 0;
		int fruitCount = 0;
		KNNResult result = new KNNResult();
		Item tempItem = new Item();
		String[] categoryAssignment = new String[testData.length];
		String[][] nearestNeighbors = new String[testData.length][k]; 
		PriorityQueue<Item> queue;
		Comparator<Item> comparator;
		for (int i = 0; i < testData.length; i++) {			
			comparator = new distanceComparator(testData[i]);
			queue = new PriorityQueue<Item>(k, comparator);
			for (int j = 0; j < trainingData.length; j++) {				
				if (queue.size() < k)
					queue.add(trainingData[j]);
				else if (comparator.compare(queue.peek(), trainingData[j]) < 0) {
					queue.poll();
					queue.add(trainingData[j]);
				}
			}
//			System.out.println("**************************");
//			System.out.println(testData[i].category);
			for (int l = 0; l < k; l++) {
				tempItem = queue.poll();
				nearestNeighbors[i][k-1-l] = tempItem.name;
				if (tempItem.category.equals("fruit"))
					fruitCount++;
				else if (tempItem.category.equals("nation"))
					nationCount++;
				else if (tempItem.category.equals("machine"))
					machineCount++;
			}

			if (nationCount >= machineCount && nationCount >= fruitCount) {
				categoryAssignment[i] = "nation";
			}
			else if (machineCount > nationCount && machineCount >= fruitCount) {
				categoryAssignment[i] = "machine";
			}			
			else if (fruitCount > nationCount && fruitCount > machineCount) {
				categoryAssignment[i] = "fruit";
			}
//			System.out.println(categoryAssignment[i]);
			fruitCount = 0;
			nationCount = 0;
			machineCount = 0;
		}
		for (int m = 0; m < testData.length; m++) {
			if (!testData[m].category.equals(categoryAssignment[m]))
				errorCount++;				
		}
		result.accuracy = (double) (1.0 - (double)errorCount/testData.length);
		result.categoryAssignment = categoryAssignment;
		result.nearestNeighbors = nearestNeighbors;
		return result;
	}
	
	public double getD(Item test, Item data) {
		double distance = 0.0;
		for (int i = 0; i < 3; i++) {
			distance += (double)((test.features[i] - data.features[i])*(test.features[i] - data.features[i]));
		}
		return (double)Math.sqrt(distance);
	}
}

