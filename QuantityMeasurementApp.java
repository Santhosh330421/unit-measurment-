package org.example;

import org.junit.jupiter.api.Test;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

enum LengthUnit {
    FEET(1.0),
    INCH(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(0.393701 / 12.0);

    private final double conversionFactorToFeet;

    LengthUnit(double conversionFactorToFeet) {
        this.conversionFactorToFeet = conversionFactorToFeet;
    }

    public double toFeet(double value) {
        return value * conversionFactorToFeet;
    }

    public double fromFeet(double feetValue) {
        return feetValue / conversionFactorToFeet;
    }
}

class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        this.value = value;
        this.unit = unit;
    }

    public static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException("Source or Target unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        double inFeet = sourceUnit.toFeet(value);
        return targetUnit.fromFeet(inFeet);
    }

    public QuantityLength convertTo(LengthUnit targetUnit) {
        return new QuantityLength(convert(this.value, this.unit, targetUnit), targetUnit);
    }

    public QuantityLength add(QuantityLength other) {
        return add(other, this.unit);
    }

    public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Second operand cannot be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double firstInFeet = this.unit.toFeet(this.value);
        double secondInFeet = other.unit.toFeet(other.value);
        double sumInFeet = firstInFeet + secondInFeet;
        double result = targetUnit.fromFeet(sumInFeet);
        return new QuantityLength(result, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof QuantityLength)) return false;
        QuantityLength other = (QuantityLength) obj;
        double thisInFeet = this.unit.toFeet(this.value);
        double otherInFeet = other.unit.toFeet(other.value);
        return Math.abs(thisInFeet - otherInFeet) < 0.001;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.toFeet(value));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}

public class QuantityMeasurementAppTest {

    public static void main(String[] args) {
        System.out.println(new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.FEET));
        System.out.println(new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.INCH));
        System.out.println(new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARDS));
        System.out.println(new QuantityLength(1.0, LengthUnit.YARDS).add(new QuantityLength(3.0, LengthUnit.FEET), LengthUnit.YARDS));
        System.out.println(new QuantityLength(36.0, LengthUnit.INCH).add(new QuantityLength(1.0, LengthUnit.YARDS), LengthUnit.FEET));
        System.out.println(new QuantityLength(2.54, LengthUnit.CENTIMETERS).add(new QuantityLength(1.0, LengthUnit.INCH), LengthUnit.CENTIMETERS));
        System.out.println(new QuantityLength(5.0, LengthUnit.FEET).add(new QuantityLength(0.0, LengthUnit.INCH), LengthUnit.YARDS));
        System.out.println(new QuantityLength(5.0, LengthUnit.FEET).add(new QuantityLength(-2.0, LengthUnit.FEET), LengthUnit.INCH));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Feet() {
        assertEquals(new QuantityLength(2.0, LengthUnit.FEET),
                new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.FEET));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Inches() {
        assertEquals(new QuantityLength(24.0, LengthUnit.INCH),
                new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.INCH));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Yards() {
        assertEquals(new QuantityLength(0.6667, LengthUnit.YARDS),
                new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARDS));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Centimeters() {
        assertEquals(new QuantityLength(5.08, LengthUnit.CENTIMETERS),
                new QuantityLength(1.0, LengthUnit.INCH).add(new QuantityLength(1.0, LengthUnit.INCH), LengthUnit.CENTIMETERS));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {
        assertEquals(new QuantityLength(3.0, LengthUnit.YARDS),
                new QuantityLength(2.0, LengthUnit.YARDS).add(new QuantityLength(3.0, LengthUnit.FEET), LengthUnit.YARDS));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {
        assertEquals(new QuantityLength(9.0, LengthUnit.FEET),
                new QuantityLength(2.0, LengthUnit.YARDS).add(new QuantityLength(3.0, LengthUnit.FEET), LengthUnit.FEET));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Commutativity() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCH).add(new QuantityLength(1.0, LengthUnit.FEET), LengthUnit.YARDS);
        assertEquals(a, b);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_WithZero() {
        assertEquals(new QuantityLength(1.6667, LengthUnit.YARDS),
                new QuantityLength(5.0, LengthUnit.FEET).add(new QuantityLength(0.0, LengthUnit.INCH), LengthUnit.YARDS));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_NegativeValues() {
        assertEquals(new QuantityLength(36.0, LengthUnit.INCH),
                new QuantityLength(5.0, LengthUnit.FEET).add(new QuantityLength(-2.0, LengthUnit.FEET), LengthUnit.INCH));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH), null);
        });
    }

    @Test
    public void testAddition_ExplicitTargetUnit_LargeToSmallScale() {
        assertEquals(new QuantityLength(18000.0, LengthUnit.INCH),
                new QuantityLength(1000.0, LengthUnit.FEET).add(new QuantityLength(500.0, LengthUnit.FEET), LengthUnit.INCH));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SmallToLargeScale() {
        assertEquals(new QuantityLength(0.6667, LengthUnit.YARDS),
                new QuantityLength(12.0, LengthUnit.INCH).add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARDS));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_PrecisionTolerance() {
        assertEquals(new QuantityLength(0.8333, LengthUnit.YARDS),
                new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(18.0, LengthUnit.INCH), LengthUnit.YARDS));
    }
}
