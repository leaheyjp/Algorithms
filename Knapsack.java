import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Map;
import java.util.HashMap;

public class Knapsack {

	// Objects that can potentially go into knapsack
    public static class KnapsackObject {
    	String name;
    	int weight;
    	int value;

        public String getName() {
            return name;
        }

        public int getWeight() {
            return weight;
        }

        public int getValue() {
            return value;
        }

		public KnapsackObject(String objectName, int objectWeight, int objectValue) {
            name = objectName;
            weight = objectWeight;
            value = objectValue;
        }
    }

    // Read in input from the command line using System
    public static int getInput() {
        Scanner sc = new Scanner(System.in);
        int weight = sc.nextInt();
        return weight;
    }

    // Read in objects from knap_input.txt and put them into an array composed on KnapsackObjects
    public static ArrayList<KnapsackObject> importTxt() {
        ArrayList items = new ArrayList();
        String loc = "./knap_input.txt";
        String inputString = "";

        // Uses BufferedReader to read each line, assigns to variable, then creates
        // a KnapsackObject using that information. Returns an array of KnapsackObject's.
        try (BufferedReader reader = new BufferedReader(new FileReader(loc))) {
            while ((inputString = reader.readLine()) != null) {
                String itemName = inputString;
                int itemWeight = Integer.parseInt(reader.readLine());
                int itemValue = Integer.parseInt(reader.readLine());

                KnapsackObject newItem = new KnapsackObject(itemName, itemWeight, itemValue);
                items.add(newItem);
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }

        return (items);
    }

    public static ArrayList solver(ArrayList<KnapsackObject> knapsack, int weightLimit){
    	int n = knapsack.size();
    	Double numCombos = Math.pow(2, n);
    	ArrayList<String> combinations = new ArrayList();
    	for(int i=1;i<=numCombos;i++){
    		String binary = Integer.toBinaryString(i);
    		int len = binary.length();
    		int toAdd = n - len;
    		String filled = "";
    		for(int j=1;j<=toAdd;j++){
    			filled = filled + "0";
    		}
    		String fin = filled + binary;
    		combinations.add(fin);
    	}

    	Map<Integer, String> dictionary = new HashMap<Integer, String>();
		
    	int highestScore = 0;
    	int weightOfHighest = 0;

    	for(int i=0; i<combinations.size(); ++i)
		{
    		String s = combinations.get(i);

    		int score = 0;
    		int weight = 0;

    		Character ch = new Character('1');

			for(int j = 0; j < s.length(); j++){
   				char c = s.charAt(j);
   				if (c == ch){
   					score = score + knapsack.get(j).getValue();
   					weight = weight + knapsack.get(j).getWeight();
   				}
			}
			if (weight <= weightLimit && score >= highestScore) {
				highestScore = score;
				weightOfHighest = weight;
				dictionary.put(score,s);
			}
		}
		String winnerString = dictionary.get(highestScore);

		ArrayList answer = new ArrayList();
		answer.add(winnerString);
		answer.add(weightOfHighest);
		answer.add(highestScore);

		return answer;
    }

    // Generates output of solution
	public static void output(ArrayList result, ArrayList<KnapsackObject> knapsack) {
		Object bestValue = result.get(2);
		Object bestWeight = result.get(1);
		String combo = (String) result.get(0);

		System.out.println("Best set of items to take:");
		System.out.println("--------------------------");

		for (int i = 0;i < combo.length(); i++){
			if (combo.charAt(i) == '1') {
				System.out.println(knapsack.get(i).getName());
			}
		}

		System.out.println("--------------------------");
		System.out.println("Best Value: "  + bestValue);
		System.out.println("Best Weight: "  + bestWeight);
	}

    public static void main(String[] args) {
        int weight = Integer.parseInt(args[0]);

        ArrayList<KnapsackObject> knapsack = new ArrayList();
        knapsack = importTxt();
        ArrayList answer = solver(knapsack, weight);
        output(answer, knapsack);
    }

}

