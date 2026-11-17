
public class FeetMeasurment {

    public static class Feet{

        private final double feet;

        Feet(double feet){
            this.feet = feet;
        }

        @Override
        public boolean equals(Object obj){
            if(this == obj ){
                return true;
            }

            if(obj==null || getClass()!=obj.getClass()){
                return false;
            }

            Feet feet = (Feet) obj;

            return Double.compare(this.feet,feet.feet)==0;
        }
    }

    public static void main(String[] args){
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(1.0);
        Feet feet3 = new Feet(2.0);

        System.out.println(feet1.equals(feet2));
        System.out.println(feet1.equals(feet3));
    }
}
