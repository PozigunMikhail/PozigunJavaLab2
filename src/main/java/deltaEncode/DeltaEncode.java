package deltaEncode;

import java.io.*;

import org.apache.commons.cli.*;

public class DeltaEncode {
    private static final int BLOCK_SIZE = 10;

    private static void readAndWriteByBuffer(InputStream iStream, OutputStream oStream) throws IOException {
        byte[] buf = new byte[BLOCK_SIZE];
        int bufSize;
        while ((bufSize = iStream.read(buf, 0, buf.length)) != -1) {
            oStream.write(buf, 0, bufSize);
        }
    }

    public static void deltaDecode(String inFilename, String outFilename) throws IOException {
        try (InputStream iStream = new FileInputStream(inFilename);
             OutputStream oStream = new DeltaEnOutputStream(new FileOutputStream(outFilename))) {
            readAndWriteByBuffer(iStream, oStream);
        }
    }

    public static void deltaDecode() throws IOException {
        try (OutputStream oStream = new DeltaEnOutputStream(System.out)) {
            readAndWriteByBuffer(System.in, oStream);
        }
    }

    public static void deltaEncode(String inFilename, String outFilename) throws IOException {
        try (InputStream iStream = new DeltaEnInputStream(new FileInputStream(inFilename));
             OutputStream oStream = new FileOutputStream(outFilename)) {
            readAndWriteByBuffer(iStream, oStream);
        }
    }

    public static void deltaEncode() throws IOException {
        try (InputStream iStream = new DeltaEnInputStream(System.in)) {
            readAndWriteByBuffer(iStream, System.out);
        }
    }

    public static void main(String[] args) {
        Option encode = new Option("e", "encode", true, "Encoding"),
                decode = new Option("d", "decode", true, "Decoding"),
                out = new Option("out", true, "Output file"),
                help = new Option("h", "help", false, "Help");
        encode.setArgs(1);
        decode.setArgs(1);
        out.setArgs(1);
        encode.setArgName("input filename or stdin");
        decode.setArgName("decoded filename or stdout");
        out.setArgName("output filename");
        Options options = new Options();
        options.addOption(encode);
        options.addOption(decode);
        options.addOption(out);
        options.addOption(help);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("e")) {
                if (commandLine.getOptionValue("e").equals("stdin")) {
                    DeltaEncode.deltaEncode();
                } else {
                    if (commandLine.hasOption("out")) {
                        DeltaEncode.deltaEncode(commandLine.getOptionValue("e"), commandLine.getOptionValue("out"));
                    } else {
                        String inputFilename = commandLine.getOptionValue("e"),
                                outputFilename = inputFilename.substring(0, inputFilename.lastIndexOf(".")) + ".diff";
                        DeltaEncode.deltaEncode(inputFilename, outputFilename);
                    }
                }
            }
            if (commandLine.hasOption("d")) {
                if (commandLine.getOptionValue("d").equals("stdout")) {
                    DeltaEncode.deltaDecode();
                } else {
                    if (commandLine.hasOption("out")) {
                        DeltaEncode.deltaDecode(commandLine.getOptionValue("d"), commandLine.getOptionValue("out"));
                    } else {
                        String inputFilename = commandLine.getOptionValue("d"),
                                outputFilename = inputFilename.substring(0, inputFilename.lastIndexOf(".")) + ".txt";
                        DeltaEncode.deltaDecode(inputFilename, outputFilename);
                    }
                }
            }
            if (commandLine.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("deltaEncode", options, true);
            }

        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("I/O error.");
        }
    }
}
