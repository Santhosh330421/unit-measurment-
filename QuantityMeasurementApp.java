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
        if (other == null) {
            throw new IllegalArgumentException("Second operand cannot be null");
        }
        double firstInFeet = this.unit.toFeet(this.value);
        double secondInFeet = other.unit.toFeet(other.value);
        double sumInFeet = firstInFeet + secondInFeet;
        double result = this.unit.fromFeet(sumInFeet);
        return new QuantityLength(result, this.unit);
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof QuantityLength)) return false;
        QuantityLength other = (QuantityLength) obj;
        double thisInFeet = this.unit.toFeet(this.value);
        double otherInFeet = other.unit.toFeet(other.value);
        return Math.abs(thisInFeet - otherInFeet) < 0.0001;
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
        System.out.println(new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(2.0, LengthUnit.FEET)));
        System.out.println(new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH)));
        System.out.println(new QuantityLength(12.0, LengthUnit.INCH).add(new QuantityLength(1.0, LengthUnit.FEET)));
        System.out.println(new QuantityLength(1.0, LengthUnit.YARDS).add(new QuantityLength(3.0, LengthUnit.FEET)));
        System.out.println(new QuantityLength(36.0, LengthUnit.INCH).add(new QuantityLength(1.0, LengthUnit.YARDS)));
        System.out.println(new QuantityLength(2.54, LengthUnit.CENTIMETERS).add(new QuantityLength(1.0, LengthUnit.INCH)));
        System.out.println(new QuantityLength(5.0, LengthUnit.FEET).add(new QuantityLength(0.0, LengthUnit.INCH)));
        System.out.println(new QuantityLength(5.0, LengthUnit.FEET).add(new QuantityLength(-2.0, LengthUnit.FEET)));
    }

    @Test
    public void testAddition_SameUnit_FeetPlusFeet() {
        assertEquals(new QuantityLength(3.0, LengthUnit.FEET),
                new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(2.0, LengthUnit.FEET)));
    }

    @Test
    public void testAddition_SameUnit_InchPlusInch() {
        assertEquals(new QuantityLength(12.0, LengthUnit.INCH),
                new QuantityLength(6.0, LengthUnit.INCH).add(new QuantityLength(6.0, LengthUnit.INCH)));
    }

    @Test
    public void testAddition_CrossUnit_FeetPlusInches() {
        assertEquals(new QuantityLength(2.0, LengthUnit.FEET),
                new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH)));
    }

    @Test
    public void testAddition_CrossUnit_InchPlusFeet() {
        assertEquals(new QuantityLength(24.0, LengthUnit.INCH),
                new QuantityLength(12.0, LengthUnit.INCH).add(new QuantityLength(1.0, LengthUnit.FEET)));
    }

    @Test
    public void testAddition_CrossUnit_YardPlusFeet() {
        assertEquals(new QuantityLength(2.0, LengthUnit.YARDS),
                new QuantityLength(1.0, LengthUnit.YARDS).add(new QuantityLength(3.0, LengthUnit.FEET)));
    }

    @Test
    public void testAddition_CrossUnit_CentimeterPlusInch() {
        assertEquals(new QuantityLength(5.08, LengthUnit.CENTIMETERS),
                new QuantityLength(2.54, LengthUnit.CENTIMETERS).add(new QuantityLength(1.0, LengthUnit.INCH)));
    }

    @Test
    public void testAddition_Commutativity() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH));
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCH).add(new QuantityLength(1.0, LengthUnit.FEET));
        assertEquals(a, b);
    }

    @Test
    public void testAddition_WithZero() {
        assertEquals(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(5.0, LengthUnit.FEET).add(new QuantityLength(0.0, LengthUnit.INCH)));
    }

    @Test
    public void testAddition_NegativeValues() {
        assertEquals(new QuantityLength(3.0, LengthUnit.FEET),
                new QuantityLength(5.0, LengthUnit.FEET).add(new QuantityLength(-2.0, LengthUnit.FEET)));
    }

    @Test
    public void testAddition_NullSecondOperand() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityLength(1.0, LengthUnit.FEET).add(null);
        });
    }

    @Test
    public void testAddition_LargeValues() {
        assertEquals(new QuantityLength(2000000.0, LengthUnit.FEET),
                new QuantityLength(1000000.0, LengthUnit.FEET).add(new QuantityLength(1000000.0, LengthUnit.FEET)));
    }

    @Test
    public void testAddition_SmallValues() {
        assertEquals(new QuantityLength(0.003, LengthUnit.FEET),
                new QuantityLength(0.001, LengthUnit.FEET).add(new QuantityLength(0.002, LengthUnit.FEET)));
    }
}
