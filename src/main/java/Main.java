import com.opencsv.CSVReader;

import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static String movementListFile = "src/main/resources/movementList.csv";
    private static String dateFormat = "dd.MM.yyyy";
/*
Написать код парсинга банковской выписки (файл movementsList.csv).
Код должен выводить сводную информацию по этой выписке:
общий приход, общий расход, а также разбивку расходов.
 */

    public static void main(String[] args) {
        ArrayList<Operation> operations = loadOperationListFromFile();
        System.out.println("Общий приход: " +
                operations.stream().mapToDouble(o -> o.incoming).sum());

        System.out.println("Общий расход: " +
                operations.stream().mapToDouble(o -> o.expense).sum());

        printHashMapExpenses(makeHashMapExpenses(operations));

    }

    private static ArrayList<Operation> loadOperationListFromFile() {
        ArrayList<Operation> operations = new ArrayList<>();
        try {
            Path path = Paths.get(movementListFile);
            Charset charset = StandardCharsets.UTF_8;
            String content = new String(Files.readAllBytes(path), charset);
            content = content.replace('\\', '/');
            Files.write(path, content.getBytes(charset));

            CSVReader reader = new CSVReader(new FileReader(movementListFile));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (reader.getLinesRead() == 1) {
                    continue;
                }
                String income = nextLine[6].replace(',', '.');
                String expense = nextLine[7].replace(',', '.');

                operations.add(new Operation(
                        nextLine[0], nextLine[1], nextLine[2],
                        new SimpleDateFormat(dateFormat).parse(nextLine[3]),
                        nextLine[4], nextLine[5],
                        Double.parseDouble(income),
                        Double.parseDouble(expense)
                ));
            }
//            List<String> strings = Files.readAllLines(Paths.get(movementListFile));
//            strings.remove(0);


//            for (String string : strings) {
//                String[] fragments = string.split(",");
//                if (fragments.length != 8) {
//                    System.out.println("String wrong format!\n\t" + string);
//                    continue;
//                }
//                operations.add(new Operation(
//                        fragments[0], fragments[1], fragments[2],
//                        new SimpleDateFormat(dateFormat).parse(fragments[3]),
//                        fragments[4], fragments[5],
//                        Integer.parseInt(fragments[6]),
//                        Integer.parseInt(fragments[7])
//                ));
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return operations;
    }

    private static HashMap<String, Double> makeHashMapExpenses(ArrayList<Operation> operations) {
        HashMap<String, Double> hashMapExpenses = new HashMap<>();

        for (Operation operation : operations) {
            String[] fragments = operation.description.split("\\s\\s\\s\\s");

            String key = fragments[1].substring(fragments[1].lastIndexOf('/') + 1).trim();
//            System.out.println(key);
            if (hashMapExpenses.containsKey(key)) {
                double value = hashMapExpenses.get(key);
                hashMapExpenses.replace(key, value + operation.expense);
            } else
                hashMapExpenses.put(key, operation.expense);
        }

        return hashMapExpenses;
    }

    private static void printHashMapExpenses(HashMap<String, Double> hashMapExpenses) {
        System.out.println("Расходы по группам:");
        Set<String> keyset = hashMapExpenses.keySet();
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.addAll(keyset);
        for (String key : treeSet) {
            System.out.printf("\t%-30s  %.2f%n", key, hashMapExpenses.get(key));

        }
    }
}
