package generator;

import org.apache.commons.cli.*;

public class CmdGenParser {
    public static void parse(String[] args) {
        Option meanOpt = new Option("m", "mean", true, "Mean value");
        Option varOpt = new Option("v", "variance", true, "Variance value");
        Option helpOpt = new Option("h", "help", false, "Help");
        meanOpt.setArgs(1);
        varOpt.setArgs(1);
        meanOpt.setArgName("value");
        varOpt.setArgName("value");
        Options options = new Options();
        options.addOption(meanOpt);
        options.addOption(varOpt);
        options.addOption(helpOpt);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            double mean = 0.0, var = 0.0;
            if (commandLine.hasOption("m")) {
                mean = Double.parseDouble(commandLine.getOptionValue("m"));
            }
            if (commandLine.hasOption("v")) {
                var = Double.parseDouble(commandLine.getOptionValue("v"));
            }
            if (commandLine.hasOption("m") && commandLine.hasOption("v")) {
                Generator.generate(mean, var);
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
