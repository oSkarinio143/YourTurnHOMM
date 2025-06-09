package oskarinio143.heroes3;

public class IncorrectNumber extends RuntimeException {
    public IncorrectNumber(String message) {
        super(message);
    }

    public IncorrectNumber (int minNumb, int maxNumb){
        super("Number must be in range: " + minNumb + " - " + maxNumb);
    }
}
