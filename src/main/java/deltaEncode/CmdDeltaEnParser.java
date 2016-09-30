package deltaEncode;

import org.apache.commons.cli.*;

import java.io.IOException;

public class CmdDeltaEnParser {
    public static void parse(String[] args) {
        Option encodeOpt = new Option("e", "encode", true, "Encoding");
        Option decodeOpt = new Option("d", "decode", true, "Decoding");
        Option helpOpt = new Option("h", "help", false, "Help");
        encodeOpt.setArgs(1);
        decodeOpt.setArgs(1);
        encodeOpt.setArgName("filename or stdin");
        decodeOpt.setArgName("filename or stdout");
        Options options = new Options();
        options.addOption(encodeOpt);
        options.addOption(decodeOpt);
        options.addOption(helpOpt);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("e")) {
                if (commandLine.getOptionValue("e").equals("stdin")) {
                    DeltaEncode.deltaEncode();
                } else {
                    DeltaEncode.deltaEncode(commandLine.getOptionValue("e"));
                }
            }
            if (commandLine.hasOption("d")) {
                if (commandLine.getOptionValue("d").equals("stdout")) {
                    DeltaEncode.deltaDecode();
                } else {
                    DeltaEncode.deltaDecode(commandLine.getOptionValue("d"));
                }
            }
            if (commandLine.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("deltaEncode", options, true);
            }
        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
