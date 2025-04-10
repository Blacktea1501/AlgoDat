/*
 * Test der verscheidenen Dictionary-Implementierungen
 *
 * O. Bittel
 * 29.11.2024
 */
package dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Static test methods for different Dictionary implementations.
 * @author oliverbittel
 */
public class DictionaryTest {

	/**
	 * @param args not used.
	 */
	public static void main(String[] args)  {
		//testSortedArrayDictionary();
		testLinkedHashDictionary();
		//testOpenHashWithQuadraticProbingDictionary();
		//testBinaryTreeDictionary();

		 cpuTime(1);
	}

	private static void cpuTime(int version) {
		// Dictionary<String, String> dict = new SortedArrayDictionary<>();
		//Dictionary<String, String> dict = new LinkedHashDictionary<>(7);
		// Dictionary<String, String> dict = new OpenHashDictionary<>(7);

		//ArrayList<String> lines = readFile("dictionary/dtengl.txt");

		// test for all lines
		//insertTest(lines, dict, lines.size());
		//searchTest(lines, dict, lines.size());
        //System.out.println();
		// test for first 8000 lines
		// dict = new SortedArrayDictionary<>();
		//dict = new LinkedHashDictionary<>(7);
		// dict = new OpenHashDictionary<>(7);
		//insertTest(lines, dict, 8000);
		//searchTest(lines, dict, 8000);


        ArrayList<String> lines = readFile("01/dictionary/dtengl.txt");
        if (lines.isEmpty()) {
            lines = readFile("dictionary/dtengl.txt");
        }
        Dictionary<String, String> dictL;
        Dictionary<String, String> dictS;
        switch (version) {
            case 0:
                dictL = new SortedArrayDictionary<>();
                dictS = new SortedArrayDictionary<>();
                break;
            case 1:
                dictL = new LinkedHashDictionary<>(7);
		        dictS = new LinkedHashDictionary<>(7);
                break;
            case 2:
                dictL = new OpenHashDictionary<>(7);
                dictS = new OpenHashDictionary<>(7);
                break;
            //case 3:
                //break;
            default:
                dictL = new SortedArrayDictionary<>();
                dictS = new SortedArrayDictionary<>();                
        }
        insertTest(lines, dictL, lines.size());
		searchTest(lines, dictL, lines.size());
        System.out.println();
        insertTest(lines, dictS, 8000);
		searchTest(lines, dictS, 8000);
	}

	private static void testSortedArrayDictionary() {
		Dictionary<String, String> dict = new SortedArrayDictionary<>();
		testDict(dict);
	}

	private static void testLinkedHashDictionary() {
		Dictionary<String, String> dict = new LinkedHashDictionary<>(7);
		testDict(dict);
	}

	private static void testOpenHashWithQuadraticProbingDictionary() {
		Dictionary<String, String> dict = new OpenHashDictionary<>(7);
		testDict(dict);
	}
	
	private static void testBinaryTreeDictionary() {
		Dictionary<String, String> dict = new BinaryTreeDictionary<>();
		testDict(dict);
        
        // Test für BinaryTreeDictionary mit prettyPrint 
        // (siehe Aufgabe 10; Programmiertechnik 2).
        // Pruefen Sie die Ausgabe von prettyPrint auf Papier nach.
        BinaryTreeDictionary<Integer, Integer> btd = new BinaryTreeDictionary<>();
        btd.insert(10, 0);
        btd.insert(20, 0);
        btd.insert(50, 0);
        System.out.println("insert:");
        btd.prettyPrint();

        btd.insert(40, 0);
        btd.insert(30, 0);
        System.out.println("insert:");
        btd.prettyPrint();

        btd.insert(21, 0);
        System.out.println("insert:");
        btd.prettyPrint();

        btd.insert(35, 0);
        btd.insert(45, 0);
        System.out.println("insert:");
        btd.prettyPrint();

        System.out.println("For Each Loop:");
        for (Dictionary.Entry<Integer, Integer> e : btd) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }

        btd.remove(30);
        System.out.println("remove:");
        btd.prettyPrint();

        btd.remove(35);
        btd.remove(40);
        btd.remove(45);
        System.out.println("remove:");
        btd.prettyPrint();
		
		btd.remove(50);
        System.out.println("remove:");
        btd.prettyPrint();

		System.out.println("For Each Loop:");
		for (Dictionary.Entry<Integer, Integer> e : btd) {
			System.out.println(e.getKey() + ": " + e.getValue());
		}
    }

	private static ArrayList<String> readFile(String filename) {
		ArrayList<String> lines = new ArrayList<>();

		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;

			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (Exception e) {
			System.out.println("Error reading file: " + e.getMessage());
		}

        return lines;
	}

	private static void insertTest(ArrayList<String> lines, Dictionary<String, String> dict, int n) {
		String [] german = new String[n];
		String [] english = new String[n];

		for  (int i = 0; i < n; i++) {
			String[] parts = lines.get(i).split(" ");
			if (parts.length == 2) {
				german[i] = parts[0];
				english[i] = parts[1];
			}
		}

		long start = System.nanoTime();
		for (int i = 0; i < n; i++) {
			dict.insert(german[i], english[i]);
		}
		long end = System.nanoTime();
		System.out.println("CPU time for insert into " + dict.getClass() + " " + n + " entries: " + ((end - start)/1000) + " microseconds");
	}

private static void searchTest(ArrayList<String> lines, Dictionary<String, String> dict, int n) {
		String [] german = new String[n];
		String [] english = new String[n];

		for  (int i = 0; i < n; i++) {
			String[] parts = lines.get(i).split(" ");
			if (parts.length == 2) {
				german[i] = parts[0];
				english[i] = parts[1];
			}
		}

		long start = System.nanoTime();
		for (int i = 0; i < n; i++) {
			dict.search(german[i]);
		}
		long end = System.nanoTime();
		System.out.println("CPU time for successful search in " + dict.getClass() + " " + n + " entries: " + ((end - start)/1000) + " microseconds");

		start = System.nanoTime();
		for (int i = 0; i < n; i++) {
			dict.search(english[i]);
		}
		end = System.nanoTime();
		System.out.println("CPU time for failed search in " + dict.getClass() + " " + n + " entries: " + ((end - start)/1000) + " microseconds");
}

	private static void testDict(Dictionary<String, String> dict) {
		System.out.println("===== New Test Case ========================");
		System.out.println("test " + dict.getClass());
		System.out.println(dict.insert("gehen", "go") == null);		// true
		String s = new String("gehen");
		System.out.println(dict.search(s) != null);					// true
		System.out.println(dict.search(s).equals("go"));			// true
		System.out.println(dict.insert(s, "walk").equals("go"));	// true
		System.out.println(dict.search("gehen").equals("walk"));	// true
		System.out.println(dict.remove("gehen").equals("walk"));	// true
		System.out.println(dict.remove("gehen") == null); // true
		dict.insert("starten", "start");
		dict.insert("gehen", "go");
		dict.insert("schreiben", "write");
		dict.insert("reden", "say");
		dict.insert("arbeiten", "work");
		dict.insert("lesen", "read");
		dict.insert("singen", "sing");
		dict.insert("schwimmen", "swim");
		dict.insert("rennen", "run");
		dict.insert("beten", "pray");
		dict.insert("tanzen", "dance");
		dict.insert("schreien", "cry");
		dict.insert("tauchen", "dive");
		dict.insert("fahren", "drive");
		dict.insert("spielen", "play");
		dict.insert("planen", "plan");
		dict.insert("diskutieren", "discuss");
		System.out.println(dict.size());
		for (Dictionary.Entry<String, String> e : dict) {
			System.out.println(e.getKey() + ": " + e.getValue() + " search: " + dict.search(e.getKey()));
		}
	}
	
}
