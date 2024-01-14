package org.example;

import de.fraunhofer.aisec.cpg.ConfigurationException;
import org.apache.commons.cli.*;
import org.example.analyzer.FunctionLevelAnalyzer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws ParseException, ConfigurationException, ExecutionException, InterruptedException {
        Options options = new Options();
        options.addOption("h", "help", false, "print options' information");
        options.addOption("f", "file", true, "path of c source file to be analyzed");
        options.addOption("d", "dir", true, "path of dir contains source files to be analyzed");

        // 创建命令行解析器
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        if(commandLine.hasOption("h")) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("Options", options);
        }

        if (!(options.hasOption("f") || options.hasOption("d"))) {
            System.out.println("please specify a file to analyze");
            exit(-1);
        }

        String sourceFile = commandLine.getOptionValue("f");
        List<String> fileNames = Collections.singletonList(sourceFile);
        // 用cpg解析
        FunctionLevelAnalyzer functionLevelAnalyzer = new FunctionLevelAnalyzer(fileNames);
        functionLevelAnalyzer.analyze();
    }
}