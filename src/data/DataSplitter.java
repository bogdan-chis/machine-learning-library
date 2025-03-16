package data;

import java.util.ArrayList;
import java.util.List;

public class DataSplitter<T> {
    private final double trainRatio;
    private final double validateRatio;

    private List<T> trainData;
    private List<T> validateData;
    private List<T> testData;

    public DataSplitter(double trainRatio, double validateRatio) {
        this.trainRatio = trainRatio;
        this.validateRatio = validateRatio;
    }

    public void splitData(List<T> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be null or empty.");
        }

        // Determine sizes for each split
        int totalSize = data.size();
        int trainSize = (int) (totalSize * trainRatio);
        int validateSize = (int) (totalSize * validateRatio);

        // Split the data
        trainData = new ArrayList<>(data.subList(0, trainSize));
        validateData = new ArrayList<>(data.subList(trainSize, trainSize + validateSize));
        testData = new ArrayList<>(data.subList(trainSize + validateSize, totalSize));
    }

    public List<T> getTrainData() {
        return trainData;
    }

    public List<T> getValidateData() {
        return validateData;
    }

    public List<T> getTestData() {
        return testData;
    }

}