package data;

import core.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public static List<Instance<Double, Boolean>> loadCSV(String filePath) {
        List<Instance<Double, Boolean>> instances = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                List<Double> predictors = new ArrayList<>();

                // Parse all tokens except the last one as predictors
                for (int i = 0; i < tokens.length - 1; i++) {
                    predictors.add(Double.parseDouble(tokens[i]));
                }

                // Parse the last token as the label
                Boolean label = tokens[tokens.length - 1].equals("1");
                instances.add(new Instance<>(predictors, label));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading the CSV file: " + filePath, e);
        }

        return instances;
    }
}