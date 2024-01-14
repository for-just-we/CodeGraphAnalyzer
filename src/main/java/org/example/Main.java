package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.fraunhofer.aisec.cpg.ConfigurationException;
import org.apache.commons.cli.*;
import org.example.analyzer.FunctionLevelAnalyzer;
import org.example.utils.ToJsonUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws ParseException, ConfigurationException, ExecutionException, InterruptedException, IOException {
        Options options = new Options();
        options.addOption("h", "help", false, "print options' information");
        options.addOption("f", "file", true, "path of c source file to be analyzed");
        options.addOption("d", "dir", true, "path of dir contains source files to be analyzed");
        options.addOption("o", "output_file", true, "output json file store dumped cpg");

        // 创建命令行解析器
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);
        String outputFile = commandLine.getOptionValue("o");

        if(commandLine.hasOption("h")) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("Options", options);
        }

        if (!(options.hasOption("f") || options.hasOption("d"))) {
            System.out.println("please specify a file to analyze");
            exit(-1);
        }
        
        List<String> fileNames = null;
        if (options.hasOption("f")) {
            String sourceFile = commandLine.getOptionValue("f");
            fileNames = Collections.singletonList(sourceFile);
        }
        else if (options.hasOption("d"))
            fileNames = getAllCFilesInDirectory(commandLine.getOptionValue("d"));
        
        // 用cpg解析
        FunctionLevelAnalyzer functionLevelAnalyzer = new FunctionLevelAnalyzer(fileNames);
        functionLevelAnalyzer.analyze();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(functionLevelAnalyzer.serializedCPGs);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(jsonString);
            System.out.println("Data has been written to: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static List<String> getAllCFilesInDirectory(String dirPath) throws IOException {
        List<String> fileNames = new ArrayList<>();
        Files.walk(Paths.get(dirPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".c") || path.toString().endsWith(".cc") 
                        || path.toString().endsWith(".h") || path.toString().endsWith(".cpp")
                        || path.toString().endsWith(".hpp"))
                .forEach(path -> fileNames.add(path.toString()));
        return fileNames;
    }
}