package measure;

import measure.Color;

import java.math.BigDecimal;

public class Product {

    private BigDecimal length;
    private BigDecimal posTolerance;
    private BigDecimal negTolerance;
    private String name;

    protected BigDecimal getLength() {
        return this.length;
    }

    protected void setLength(BigDecimal productLength) {
        this.length = productLength;
    }

    protected BigDecimal getPosTolerance() {
        return this.posTolerance;
    }

    protected void setPosTolerance(BigDecimal posTolerance) {
        this.posTolerance = posTolerance;
    }

    protected BigDecimal getNegTolerance() {
        return this.negTolerance;
    }

    protected void setNegTolerance(BigDecimal negTolerance) {
        this.negTolerance = negTolerance;
    }

    protected String getName() {
        return this.name;
    }

    protected void setName(String name) {
        this.name = name;
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