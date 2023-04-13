package measure;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileManager {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String MAIN_DIRECTORY = "Measurements/";

    protected static void productDataWriter(String productName,
                                            BigDecimal productLength,
                                            BigDecimal posTolerance,
                                            BigDecimal negTolerance,
                                            BigDecimal measuresBad,
                                            BigDecimal measuresGood,
                                            BigDecimal avg,
                                            BigDecimal measuresOutsidePositive,
                                            BigDecimal measuresOutsideNegative,
                                            List<BigDecimal> allMeasures) {

        LocalDateTime now = LocalDateTime.now();

        Path path = Paths.get(MAIN_DIRECTORY + productName);
        Path path_file = Paths.get(MAIN_DIRECTORY + productName + "/" + productName + "_" + DTF.format(now));

        List<String> outList = new ArrayList<>();

        outList.add("Measurement session date: " + DTF.format(now));
        outList.add("");
        outList.add("PRODUCT NAME = " + productName);
        outList.add("PRODUCT LENGTH = " + productLength + "mm");
        outList.add("POSITIVE TOLERANCE = " + posTolerance + "mm");
        outList.add("NEGATIVE TOLERANCE = " + negTolerance + "mm");
        outList.add("");
        outList.add("All measurements(mm): " + allMeasures.toString());
        outList.add("Amount measured: " + allMeasures.size() + " piece(s)");
        outList.add("Average of all measurements: " + avg + "mm");
        outList.add("");
        outList.add("TOLERANCE DATA");
        outList.add("Outside tolerance: " + measuresBad + " piece(s)");
        outList.add("Inside tolerance: " + measuresGood + " piece(s)");
        outList.add("Bigger than " + (productLength.add(posTolerance)) + "mm: " + measuresOutsidePositive + " piece(s)");
        outList.add("Smaller than " + (productLength.add(negTolerance)) + "mm: " + measuresOutsideNegative + " piece(s)");
        outList.add("");
        outList.add("Biggest measurement: " + allMeasures.get(allMeasures.size() - 1) + "mm");
        outList.add("Smallest measurement: " + allMeasures.get(0) + "mm");
        Collections.sort(allMeasures);
        BigDecimal biggestDifference = allMeasures.get(allMeasures.size() - 1).subtract(allMeasures.get(0));
        outList.add("Difference between the smallest and the biggest measurement: " + biggestDifference + "mm");
        outList.add("");
        outList.add("Measurements sorted ascending: " + allMeasures);

        try {
            if (Files.notExists(path)) {
                Files.createDirectory(path);
                Files.write(path_file, outList);
            } else {
                Files.write(path_file, outList);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println(Color.RED_BOLD + "Can't create file" + Color.RESET);
        }
    }

}