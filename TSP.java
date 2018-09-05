import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Map;
import java.util.HashMap;

//Patrick Leahey

/*Write a program that solves the traveling salesman problem using the brute force method. 
	 You will need your solution to problem 2 to do this.  You may or may not need to modify 
	 your solution to number 2 slightly to make this work.  The data for the program will be 
	 in tsp_input.txt which will be located in the same folder as the .class file.

	The input file I use to test your program will have the format of the tsp_input.txt file
	 bundled with the auto grader.  You should assume that the cities are numbered 0 to n-1 
	 and for this problem, we will not assume there is a home/original city.  Thus, we will 
	 print out all of the tours in lexicographic order, one per line, with the cities 
	 separated by ->.  

	The output lines should look like:
	 0->1->2->3->4->0   Score: 1164	

	After all the lines like that, we will print out the 
	 cheapest tour cost and the cheapest tour like this:

	 The cheapest tour costs: 283
	 The cheapest tour was: 0->1->2->4->3->0

	See the tsp_input.txt and sample_tsp_output.txt files for further illustration.*/

public class TSP {

	protected String optimalRoute = "";
	protected int optimalCost = 0;
	public int[][] costMatrix;
	protected ArrayList<String> permutations;
	protected int cities;

	public static ArrayList<Integer> firstPerm(int target) {
		ArrayList permutation = new ArrayList();
		for(int i=1;i<=target;i++){
			permutation.add(i);
		}
		return permutation;
	}

	public static String format(ArrayList<Integer> permutation) {
		String conversion = "";
		int size = permutation.size();
		for (int x = 0; x < size; x++) {
			conversion = conversion + permutation.get(x);
			// Check for last spot to not add extra space, otherwise add space
		}
		return conversion;
	}

	public static ArrayList<String> solver(ArrayList<Integer> permutation, int target) {
		ArrayList<String> permutations = new ArrayList();

		//Add first permutation
		String f = format(permutation);
		permutations.add(f);

		boolean consecutiveIncreasing = true;
		int index = 0;
		//Parse permutation
		for (int i = 0; i < (target - 1); i++) {
			// Compare first two numbers
			int n1 = permutation.get(i);
			int n2 = permutation.get(i + 1);

			//"Such that ai < ai+1"
			if (n1 < n2) {
				consecutiveIncreasing = true;
				index = i; // always choose the first int when consecutive
			}
		}


		while (consecutiveIncreasing == true) {
			
			

			// Check for largest index - "Find the largest index
			// j such that ai < aj"
			int largestIndex = 0;
			int nextIndex = index + 1;
			for (int j = nextIndex; j < target; j++) {
				if (permutation.get(j) > permutation.get(index)) {
					largestIndex = j;
				}
			}
	

			// "Swap ai and aj"
			int n3 = permutation.get(index);
			int n4 = permutation.get(largestIndex);
			permutation.remove(index);
			permutation.add(index,n4);
			permutation.remove(largestIndex);
			permutation.add(largestIndex,n3);

			

			// "Reverse order of elements from ai+1 to an inclusive"
			int inclusive = permutation.size() - 1;
			if ((index + 1) != inclusive) {

				// Copy elements to reverse
				ArrayList<Integer> reversePermutation = new ArrayList();
				//permutation.size() - (index + 1));
				int iterator = 0;

				// But only copy the ones from largest index on
				for (int p = (index + 1); p < permutation.size(); p++) {
					reversePermutation.add(iterator,permutation.get(p));
					iterator = iterator + 1;
				}

				// Reverse order of elements
				int reverseIterator = index + 1;
				for (int c = reverseIterator; c < permutation.size(); c++) {
					iterator = iterator - 1;
					permutation.remove(c);
					permutation.add(c,reversePermutation.get(iterator));
				}
			}

	

			// "Add to list of permutations"
			String s = format(permutation);
			permutations.add(s);

			// "Let i be the largest index such that ai < ai+1"
			
			consecutiveIncreasing = false;

			

			//Parse permutation
			for (int i = 0; i < (target - 1); i++) {
				// Compare first two numbers
				int n1 = permutation.get(i);
				int n2 = permutation.get(i + 1);

				//"Such that ai < ai+1"
				if (n1 < n2) {
					consecutiveIncreasing = true;
					index = i; // always choose the first int when consecutive
				}
			}
		}
		return permutations;
	}
	public TSP() {

		permutations = new ArrayList<String>();
		
		cities = getNumCities();
		
		ArrayList<Integer> costs = new ArrayList<Integer>();
		costs = getCosts();
		
		costMatrix = new int[cities][cities];
		costMatrix = createCostMatrix(cities, costs);

		ArrayList<Integer> first = firstPerm(cities);
		permutations = solver(first,cities);
	
		generateRoutesAndCosts(cities);

		generateOutput(optimalRoute);
	}

	// Return Number of cities 
	public static int getNumCities() {
		int numCities = 0;
		try {
			BufferedReader read = new BufferedReader(new FileReader("./tsp_input.txt"));
			String processor = read.readLine();
			// Strip out "Cities " characters and return number
			numCities = Integer.parseInt(processor.substring(7));
		} catch (IOException e) {
			System.out.println("Error reading file.");
		}
		return (numCities);
	}

	// Generate the travel costs 
	public static ArrayList getCosts() {
		ArrayList<Integer> travelCosts = new ArrayList<Integer>();

		try {
			BufferedReader read = new BufferedReader(new FileReader("./tsp_input.txt"));
			
			//Skip first line 
			read.readLine();
			String calculator;
			
			//Split each line and assign to Arraylist. 
			while ((calculator = read.readLine()) != null) {
				String line[] = calculator.split(" ");
				for (int l = 0; l < line.length; l++) {
					travelCosts.add(Integer.parseInt(line[l]));
				}
			}

		} catch (IOException e) {
			System.out.println("Error reading file.");
		}

		return travelCosts;
	}


	public int[][] createCostMatrix(int cityCounter, ArrayList<Integer> costs) {
		int cityTracker = 0;
		costMatrix = new int[cityCounter][cityCounter];
		// Go through each row and then each column to correctly
		// assign costs to the right city. 
		for (int x = 0; x < cityCounter; x++) {
			for (int y = 0; y < cityCounter; y++) {
				costMatrix[x][y] = costs.get(cityTracker);
				cityTracker++;
			}
		}
		return (costMatrix);
	}

	public void generateRoutesAndCosts(int cities) {
		// find all possible and then cheapest 

		// Check the route of any given two cities
		int city1;
		int city2;
		int temporaryCost;
		int tripCost;

		//Recommended on stackoverflow I couldn't figure out how to get around
		// an error
		for (String pem : permutations) {
			temporaryCost = 0;

			// Associate the permutation 
			for (int i = 0; i < cities; i++) {
				if ( i == (cities - 1)) {
					city1 = Character.getNumericValue(pem.charAt(i));
					city2 = Character.getNumericValue(pem.charAt(0));

				} else { 
					city1 = Character.getNumericValue(pem.charAt(i));
					city2 = Character.getNumericValue(pem.charAt(i + 1));
				}

				// Generate costs
				tripCost = costMatrix[city1-1][city2-1];
				temporaryCost = temporaryCost + tripCost;
			}

			// Now do comparisons 
			String result = "";
			for (int city = 0; city < pem.length(); city++) {
				// Convert variables types and build Routes
				int abrev = Character.getNumericValue(pem.charAt(city));
				result = result + (abrev - 1) + "->";
			}

			// Route creation
			int first = Character.getNumericValue(pem.charAt(0));
			int anchor = first - 1;
			String line = anchor + "   Score: " + temporaryCost;
			result = result + line;
			System.out.println(result);

			//Calculate optimal Cost and Route
			if (optimalCost == 0) {
				optimalCost = temporaryCost;
				optimalRoute = pem;
			} else if (temporaryCost < optimalCost) {
				optimalCost = temporaryCost;
				optimalRoute = pem;
			}
		}
	}

	//Generate Output
	public void generateOutput(String bestRoute) {
		String tourRoute = "";
		int routeLength = bestRoute.length();
		for (int b = 0; b < routeLength; b++) {
			int convert = Character.getNumericValue(bestRoute.charAt(b));
			int c = convert - 1;
			tourRoute = tourRoute + c + "->";
		}
		int con = Character.getNumericValue(bestRoute.charAt(0));
		int one = con - 1;
		tourRoute = tourRoute + one;

		System.out.println("The cheapest tour costs: " + optimalCost);
		System.out.println("The cheapest tour was: " + tourRoute);
	}

	public static void main(String args[]) {
		TSP test = new TSP();
 	}
}