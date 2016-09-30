package generator;

import java.util.Random;

public class Generator {
    private static final int RANDOM_NUMBERS_COUNT = 10;

    public static void generate(double mean, double var) {
        Random rand = new Random();
        for (int i = 0; i < RANDOM_NUMBERS_COUNT; i++) {
            double x = rand.nextDouble();
            int res = (int) (100 / Math.sqrt(2 * var * Math.PI) * Math.exp(-(x - mean) * (x - mean) / var / 2));
            System.out.print(res + " ");
        }
    }

    public static void main(String[] args) {
        CmdGenParser.parse(args);
    }
}
