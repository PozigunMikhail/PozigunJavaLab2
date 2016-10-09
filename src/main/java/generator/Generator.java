package generator;

import org.apache.commons.cli.*;
import java.util.Random;

public class Generator {
    private static void generate(double mean, double var, int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            double x = rand.nextGaussian() * Math.sqrt(var) + mean;
            int res = Math.abs((int)(100 * x));
            System.out.print(res + " ");
        }
    }

    public static void main(String[] args) {
        Option meanOpt = new Option("m", "mean", true, "Mean value"),
                varOpt = new Option("v", "variance", true, "Variance value"),
                helpOpt = new Option("h", "help", false, "Help"),
                numCountOpt = new Option("c", "count", true, "Numbers count");
        meanOpt.setArgs(1);
        varOpt.setArgs(1);
        numCountOpt.setArgs(1);
        meanOpt.setArgName("value");
        varOpt.setArgName("value");
        numCountOpt.setArgName("value");
        Options options = new Options();
        options.addOption(meanOpt);
        options.addOption(varOpt);
        options.addOption(helpOpt);
        options.addOption(numCountOpt);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("m") && commandLine.hasOption("v") && commandLine.hasOption("c")) {
                double mean = Double.parseDouble(commandLine.getOptionValue("m")),
                        var = Double.parseDouble(commandLine.getOptionValue("v"));
                int count = Integer.parseInt(commandLine.getOptionValue("c"));
                Generator.generate(mean, var, count);
            }
            if (commandLine.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Generator", options, true);
            }
        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }
    }
}
