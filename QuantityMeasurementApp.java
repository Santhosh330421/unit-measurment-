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
}


class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.value = value;
        this.unit = unit;
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
}


public class QuantityMeasurementAppTest {

    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);

        System.out.println("Input: Quantity(1.0, YARDS) and Quantity(3.0, FEET)");
        System.out.println("Output: Equal (" + q1.equals(q2) + ")");

        QuantityLength q3 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q4 = new QuantityLength(36.0, LengthUnit.INCH);

        System.out.println("Input: Quantity(1.0, YARDS) and Quantity(36.0, INCH)");
        System.out.println("Output: Equal (" + q3.equals(q4) + ")");

        QuantityLength q5 = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength q6 = new QuantityLength(0.393701, LengthUnit.INCH);

        System.out.println("Input: Quantity(1.0, CENTIMETERS) and Quantity(0.393701, INCH)");
        System.out.println("Output: Equal (" + q5.equals(q6) + ")");
    }

 

    @Test
    public void testEquality_FeetToFeet_SameValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(1.0, LengthUnit.FEET));
    }

    @Test
    public void testEquality_InchToInch_SameValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.INCH),
                new QuantityLength(1.0, LengthUnit.INCH));
    }

    @Test
    public void testEquality_FeetToInch_EquivalentValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCH));
    }

    @Test
    public void testEquality_InchToFeet_EquivalentValue() {
        assertEquals(new QuantityLength(12.0, LengthUnit.INCH),
                new QuantityLength(1.0, LengthUnit.FEET));
    }

 

    @Test
    public void testEquality_YardToYard_SameValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(1.0, LengthUnit.YARDS));
    }

    @Test
    public void testEquality_YardToYard_DifferentValue() {
        assertNotEquals(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(2.0, LengthUnit.YARDS));
    }

    @Test
    public void testEquality_YardToFeet_EquivalentValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(3.0, LengthUnit.FEET));
    }

    @Test
    public void testEquality_FeetToYard_EquivalentValue() {
        assertEquals(new QuantityLength(3.0, LengthUnit.FEET),
                new QuantityLength(1.0, LengthUnit.YARDS));
    }

    @Test
    public void testEquality_YardToInch_EquivalentValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(36.0, LengthUnit.INCH));
    }

    @Test
    public void testEquality_InchToYard_EquivalentValue() {
        assertEquals(new QuantityLength(36.0, LengthUnit.INCH),
                new QuantityLength(1.0, LengthUnit.YARDS));
    }

    @Test
    public void testEquality_YardToFeet_NonEquivalentValue() {
        assertNotEquals(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(2.0, LengthUnit.FEET));
    }

    @Test
    public void testEquality_CentimeterToCentimeter_SameValue() {
        assertEquals(new QuantityLength(2.0, LengthUnit.CENTIMETERS),
                new QuantityLength(2.0, LengthUnit.CENTIMETERS));
    }

    @Test
    public void testEquality_CentimeterToInch_EquivalentValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.CENTIMETERS),
                new QuantityLength(0.393701, LengthUnit.INCH));
    }

    @Test
    public void testEquality_CentimeterToFeet_NonEquivalentValue() {
        assertNotEquals(new QuantityLength(1.0, LengthUnit.CENTIMETERS),
                new QuantityLength(1.0, LengthUnit.FEET));
    }

  

    @Test
    public void testEquality_AllUnits_ComplexScenario() {
        QuantityLength yard = new QuantityLength(2.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(6.0, LengthUnit.FEET);
        QuantityLength inch = new QuantityLength(72.0, LengthUnit.INCH);

        assertEquals(yard, feet);
        assertEquals(feet, inch);
        assertEquals(yard, inch);
    }

    @Test
    public void testEquality_MultiUnit_TransitiveProperty() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength c = new QuantityLength(36.0, LengthUnit.INCH);

        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }



    @Test
    public void testEquality_SameReference() {
        QuantityLength q = new QuantityLength(1.0, LengthUnit.YARDS);
        assertEquals(q, q);
    }

    @Test
    public void testEquality_NullComparison() {
        QuantityLength q = new QuantityLength(1.0, LengthUnit.YARDS);
        assertNotEquals(q, null);
    }

    @Test
    public void testEquality_DifferentType() {
        QuantityLength q = new QuantityLength(1.0, LengthUnit.YARDS);
        assertNotEquals(q, "Not Quantity");
    }

    @Test
    public void testEquality_NullUnit() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new QuantityLength(1.0, null);
        });
        assertEquals("Unit cannot be null", exception.getMessage());
    }
}
