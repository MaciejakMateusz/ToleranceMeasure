package measure;

import java.math.BigDecimal;
import java.util.List;

public class Messages {
    protected static final String ENTER_MEASUREMENTS = "Enter measurements."
            + "\n" + Color.GREEN + "0" + Color.RESET + " - to confirm measurements" + "\n"
            + Color.GREEN + "-1" + Color.RESET + " - to delete last measurement (undo)";

    protected static void enterMeasurements() {
        System.out.println(ENTER_MEASUREMENTS);
    }

    protected static void measurementsAvg_Quantity(List<BigDecimal> measurementsList, BigDecimal measurementsQuantity, BigDecimal avg) {
        System.out.println(Color.BLUE + "All measurements(mm): " + Color.RESET + measurementsList + "\n");
        System.out.println(Color.BLUE + "Amount measured: " + Color.RESET + measurementsQuantity + " piece(s)");
        System.out.println(Color.BLUE + "Average of all measurements: " + Color.RESET + avg + "mm");
        System.out.println();
    }

    protected static void tolerancesData(BigDecimal productLength, BigDecimal posTolerance, BigDecimal negTolerance, BigDecimal measurementsBad, BigDecimal measurementsOutsidePositive, BigDecimal measurementsOutsideNegative, BigDecimal measurementsInTolerance) {
        System.out.println("TOLERANCE DATA");
        System.out.println(Color.BLUE + "Outside tolerance: " + Color.RED + measurementsBad + " piece(s)" + Color.RESET);
        System.out.println(Color.BLUE + "Inside tolerance: " + Color.GREEN + measurementsInTolerance + " piece(s)" + Color.RESET);
        System.out.println(Color.BLUE + "Bigger than " + (productLength.add(posTolerance)) + "mm" + ": " + Color.RESET + measurementsOutsidePositive + " piece(s)");
        System.out.println(Color.BLUE + "Smaller than " + (productLength.add(negTolerance)) + "mm" + ": " + Color.RESET + measurementsOutsideNegative + " piece(s)");
        System.out.println();
    }

    protected static void biggestSmallest_Measurement(List<BigDecimal> measurementsList) {
        System.out.println(Color.BLUE + "Biggest measurement: " + Color.RESET + measurementsList.get(measurementsList.size() - 1) + "mm");
        System.out.println(Color.BLUE + "Smallest measurement: " + Color.RESET + measurementsList.get(0) + "mm");
    }

    protected static void measurementsDifferences_SortedList(List<BigDecimal> measurementsList, BigDecimal biggestDifference) {
        System.out.println(Color.BLUE + "Difference between the smallest and the biggest measurement: " + Color.CYAN_BOLD + biggestDifference + "mm" + Color.RESET);
        System.out.println();
        System.out.println(Color.BLUE + "Measurements sorted ascending: " + Color.RESET + measurementsList);
        System.out.println();
    }

}
