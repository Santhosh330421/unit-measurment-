public class QuantityMeasurementApp {

    
    static class Feet {
        private final double value;

        
        public Feet(double value) {
            this.value = value;
        }

       
        public double getValue() {
            return value;
        }

        
        @Override
        public boolean equals(Object obj) {

            
            if (this == obj) {
                return true;
            }

            
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            
            Feet feet = (Feet) obj;

            
            return Double.compare(this.value, feet.value) == 0;
        }
    }

    
    public static void main(String[] args) {
        testEquality_SameValue();
        testEquality_DifferentValue();
        testEquality_NullComparison();
        testEquality_NonNumericInput();
        testEquality_SameReference();
        System.out.println("All tests passed!");
    }

    public static void testEquality_SameValue() {
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(1.0);

        assert feet1.equals(feet2);
    }

    public static void testEquality_DifferentValue() {
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(2.0);

        assert !feet1.equals(feet2);
    }

    public static void testEquality_NullComparison() {
        Feet feet1 = new Feet(1.0);

        assert !feet1.equals(null);
    }

    public static void testEquality_NonNumericInput() {
        Feet feet1 = new Feet(1.0);
        String input = "abc";

        assert !feet1.equals(input);
    }

    public static void testEquality_SameReference() {
        Feet feet1 = new Feet(1.0);

        assert feet1.equals(feet1);
    }
}
