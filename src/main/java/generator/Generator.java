package generator;

import org.apache.commons.cli.*;

import java.util.Random;

public class Generator {
    private static void generate(double mean, double var, int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            double x = rand.nextGaussian() * Math.sqrt(var) + mean;
            int res = Math.abs((int) (100 * x));
            System.out.print(res + " ");
        }
    }

    public static void main(String[] args) {
        Option mean = new Option("m", "mean", true, "Mean value");
        Option var = new Option("v", "variance", true, "Variance value");
        Option help = new Option("h", "help", false, "Help");
        Option numCount = new Option("c", "count", true, "Numbers count");
        mean.setArgs(1);
        var.setArgs(1);
        numCount.setArgs(1);
        mean.setArgName("value");
        var.setArgName("value");
        numCount.setArgName("value");
        Options options = new Options();
        options.addOption(mean);
        options.addOption(var);
        options.addOption(help);
        options.addOption(numCount);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("m") && commandLine.hasOption("v") && commandLine.hasOption("c")) {
                double meanValue = Double.parseDouble(commandLine.getOptionValue("m"));
                double varValue = Double.parseDouble(commandLine.getOptionValue("v"));
                int count = Integer.parseInt(commandLine.getOptionValue("c"));
                Generator.generate(meanValue, varValue, count);
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
