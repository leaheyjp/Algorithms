import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Map;
import java.util.HashMap;

//Patrick Leahey

public class Permutation{

	/* Write a program that uses the algorithm on page 146 to 
	generate all the permutations for the numbers 1 to n.  
	The user will enter a command line argument on the command 
	line when they run the program. You may assume it is an int 
	and that it is 2 or greater.  Print all the permutations.  
	Each line of the output file should contain one permutation, 
	where each number in the permutation is separated by one blank space.  
	See sample_perm_output.txt for what the file would look like if the user 
	enters 4 as the command line argument.  Note there is no blank line
	at the end of the output.*/

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
			if (x != (size - 1)) {
				conversion = conversion + " ";
			}
		}
		return conversion;
	}

	public static void printAnswer(ArrayList<String> answer){
		for (int done = 0; done < answer.size(); done++){
			System.out.println(answer.get(done));
		}

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
    public static void main(String[] args){
        int target = Integer.parseInt(args[0]);
        ArrayList<Integer> first = firstPerm(target);
        ArrayList<String> answer = solver(first,target);
        printAnswer(answer);

    }

}
