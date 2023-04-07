package measure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Product {

    private BigDecimal length;
    private BigDecimal posTolerance;
    private BigDecimal negTolerance;
    private String name;
    private static final List<BigDecimal> list = new ArrayList<>();

    public BigDecimal getLength() {
        return this.length;
    }

    public void setLength(BigDecimal productLength) {
        this.length = productLength;
    }

    public BigDecimal getPosTolerance() {
        return this.posTolerance;
    }

    public void setPosTolerance(BigDecimal posTolerance) {
        this.posTolerance = posTolerance;
    }

    public BigDecimal getNegTolerance() {
        return this.negTolerance;
    }

    public void setNegTolerance(BigDecimal negTolerance) {
        this.negTolerance = negTolerance;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected List<BigDecimal> getList() {
        return list;
    }

    protected void addToList(BigDecimal input) {
        list.add(input);
    }

    @Override
    public String toString() {
        System.out.println();
        return Color.BLUE + "PRODUCT NAME = " + Color.GREEN_BOLD + name + "\n" +
                Color.BLUE + "PRODUCT LENGTH = " + Color.GREEN_BOLD + length + "mm" + "\n" +
                Color.BLUE + "POSITIVE TOLERANCE = " + Color.GREEN_BOLD + posTolerance + "mm" + "\n" +
                Color.BLUE + "NEGATIVE TOLERANCE = " + Color.GREEN_BOLD + negTolerance + "mm" + Color.RESET;
    }



}