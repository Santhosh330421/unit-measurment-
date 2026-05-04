class QuantityMeasurementApp {

   
    static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
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

    
    static class Inch {
        private final double value;

        public Inch(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Inch inch = (Inch) obj;
            return Double.compare(this.value, inch.value) == 0;
        }
    }

   

    public static boolean compareFeet(double value1, double value2) {
        Feet feet1 = new Feet(value1);
        Feet feet2 = new Feet(value2);
        return feet1.equals(feet2);
    }

    public static boolean compareInch(double value1, double value2) {
        Inch inch1 = new Inch(value1);
        Inch inch2 = new Inch(value2);
        return inch1.equals(inch2);
    }

    

    public static boolean testFeetEquality_SameValue() {
        return compareFeet(1.0, 1.0);
    }

    public static boolean testFeetEquality_DifferentValue() {
        return !compareFeet(1.0, 2.0);
    }

    public static boolean testFeetEquality_NullComparison() {
        Feet feet1 = new Feet(1.0);
        return !feet1.equals(null);
    }

    public static boolean testFeetEquality_NonNumericInput() {
        Feet feet1 = new Feet(1.0);
        String input = "abc";
        return !feet1.equals(input);
    }

    public static boolean testFeetEquality_SameReference() {
        Feet feet1 = new Feet(1.0);
        return feet1.equals(feet1);
    }

    public static boolean testInchEquality_SameValue() {
        return compareInch(1.0, 1.0);
    }

    public static boolean testInchEquality_DifferentValue() {
        return !compareInch(1.0, 2.0);
    }

    public static boolean testInchEquality_NullComparison() {
        Inch inch1 = new Inch(1.0);
        return !inch1.equals(null);
    }

    public static boolean testInchEquality_NonNumericInput() {
        Inch inch1 = new Inch(1.0);
        String input = "xyz";
        return !inch1.equals(input);
    }

    public static boolean testInchEquality_SameReference() {
        Inch inch1 = new Inch(1.0);
        return inch1.equals(inch1);
    }
}
