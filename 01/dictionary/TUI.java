package dictionary;

import java.util.Scanner;

// buffered reader
import java.io.BufferedReader;
import java.io.FileReader;

public class TUI {

    // read method
    public static void read(String command, dictionary.Dictionary<String, String> output){
        String[] c = command.split(" ");
        int n = 0;

        if(c.length < 2 || c.length > 3) {
            System.out.println("Invalid read command");
            return;
        }

        if (c.length == 3) {
            if(!c[1].matches("[0-9]+")) {
                System.out.println("[n] has to be a number");
                return;
            }
            n = Integer.parseInt(c[1]);
        }

        try {
            String filename = c[c.length - 1];

            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line;
            int count = 0;


            while (((line = reader.readLine()) != null)) {
                if (n > 0 && count >= n) { break; }

                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String key = parts[0];
                    String value = parts[1];
                    output.insert(key, value);
                    count++;
                }
            }
            reader.close();
            System.out.println("Read " + count + " entries from " + filename);
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }


//    Schreiben Sie eine textbasierte Benutzerschnittstelle für eine Wörterbuch-Anwendung
//    Deutsch-Englisch mit folgender Funktionalität:
//    Konsolen-KommandoBedeutung

//    create Implementierung       Legt ein Dictionary an. SortedArrayDictionary ist voreingestellt.

//     r [n] Dateiname             Liest (read) die ersten n Einträge der Datei in das Dictionary ein.
//                                 Wird n weggelassen, dann werden alle Einträge eingelesen. Einfachheitshalber kann ein JFileChooser-Dialog verwendet
//                                 werden (siehe Prog2, GUI). Dann wird aber der Dateiname im Kommando weggelassen.

//    p                            Gibt alle Einträge des Dictionary in der Konsole aus (print).
//    s deutsch                    Gibt das entsprechende englische Wort aus (search).
//    i deutsch                    englischFügt ein neues Wortpaar in das Dictionary ein (insert).
//    d deutsch                    Löscht einen Eintrag (delete).
//    exit                         beendet das Programm.

    public static void main(String[] args) {
        // while system input reading
        String input = "";
        System.out.println("Welcome to the SortedArrayDictionary TUI");
        System.out.println("Type 'help' to get a list of all commands");

        Scanner scanner = new Scanner(System.in);
        Dictionary <String, String> dictionary = null;

        while(true) {
            // read in command
            input = scanner.nextLine();

            if(input.equals("help")) {
                System.out.println("Available commands:");
                System.out.println("create implementation - Create a new dictionary");
                System.out.println("r [n] filename        - Read n entries from file");
                System.out.println("p                     - Print dictionary");
                System.out.println("s deutsch             - Search for key");
                System.out.println("i deutsch englisch    - Insert key value pair");
                System.out.println("d deutsch             - Delete key");
                System.out.println("exit                  - Exit the program");
                System.out.println();
                System.out.println("Available implementations:");
                System.out.println("SortedArrayDictionary (SAD)");
                System.out.println("LinkedHashDictionary (LHD)");
                System.out.println("OpenHashDictionary (OHD)");
                System.out.println("BinaryTreeDictionary (BTD)");
                continue;
            }

            // check if input is empty
            if(input.isEmpty()) {
                continue;
            }

            // check if input is exit
            if(input.equals("exit") || input.equals("q")) {
                System.out.println("Good bye!");
                break;
            }

            // check if input is create
            if(input.startsWith("create")) {
                String[] parts = input.split(" ");
                String type = "";

                if(parts.length > 2) {
                    System.out.println("Invalid create command");
                    continue;
                }

                if (parts.length == 2) {
                    type = parts[1];
                }


                switch(type) {
                    case "SAD":
                    case "SortedArrayDictionary":
                        dictionary = new SortedArrayDictionary<>();
                        System.out.println("Created new SortedArrayDictionary");
                        break;
                    case "LHD":
                    case "LinkedHashDictionary":
                        dictionary = new LinkedHashDictionary<>(7);
                        System.out.println("Created new LinkedHashDictionary with default size 7");
                        break;
                    case "OHD":
                    case "OpenHashDictionary":
                        dictionary = new OpenHashDictionary<>(7);
                        System.out.println("Created new OpenHashDictionary with default size 7");
                        // create new OpenHashDictionary
                        break;
                    case "BTD":
                    case "BinaryTreeDictionary":
                        dictionary = new BinaryTreeDictionary<>();
                        System.out.println("Created new BinaryTreeDictionary");
                        // create new BinaryTreeDictionary
                        break;
                    default:
                        dictionary = new SortedArrayDictionary<>();
                        System.out.println("Created new SortedArrayDictionary by default");
                }

                continue;
            }

            // check if input is read
            if(input.startsWith("r")) {
                if (dictionary == null) {
                    System.out.println("No dictionary created");
                    continue;
                }
                read(input, dictionary);
                continue;
            }

            // check if input is print
            if(input.equals("p")) {
                if (dictionary == null) {
                    System.out.println("No dictionary created");
                    continue;
                }
                if (dictionary.getClass().equals(BinaryTreeDictionary.class)) {
                    ((BinaryTreeDictionary<String, String>) dictionary).prettyPrint();
                } else {
                    System.out.println(dictionary);
                }
                continue;
            }

            // check if input is search
            if(input.startsWith("s")) {

               if (dictionary == null) {
                    System.out.println("No dictionary created");
                    continue;
                }

                String[] parts = input.split(" ");

                if(parts.length != 2) {
                    System.out.println("Invalid search command");
                    continue;
                }

                String key = parts[1];
                System.out.println("Search for " + key);
                String value = dictionary.search(key);
                if(value != null) {
                    System.out.println("Found " + key + " = " + value);
                } else {
                    System.out.println("Not found");
                }
                continue;
            }

            // check if input is insert
            if(input.startsWith("i")) {
                if (dictionary == null) {
                    System.out.println("No dictionary created");
                    continue;
                }

                String[] parts = input.split(" ");

                if(parts.length != 3) {
                    System.out.println("Invalid insert command");
                    continue;
                }

                String key = parts[1];
                String value = parts[2];
                dictionary.insert(key, value);
                System.out.println("Insert " + key + " " + value);
                continue;
            }

            // check if input is delete
            if(input.startsWith("d")) {

                if (dictionary == null) {
                    System.out.println("No dictionary created");
                    continue;
                }

                String[] parts = input.split(" ");

                if(parts.length != 2) {
                    System.out.println("Invalid delete command");
                    continue;
                }

                String key = parts[1];
                dictionary.remove(key);
                System.out.println("Delete " + key);
            }
        }
    }
}
