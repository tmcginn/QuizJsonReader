/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author tmcginn
 */
public class QuizJsonReader {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java QuizJsonReader <input json question file> <output text file>");
            System.exit(-1);
        }

        System.out.println("Opening this file for reading: " + args[0]);
        System.out.println("Writing to this file:          " + args[1]);

        JSONParser parser = new JSONParser();

        try {
            // Open the file for writing
            File file = new File(args[1]);

            // creates the file
            file.createNewFile();

            // open the JSON file for conversion
            try (FileWriter writer = new FileWriter(file)) {
                // open the JSON file for conversion
                Object obj = parser.parse(new FileReader(args[0]));

                // Create a JSON object with the content of the file
                JSONObject jsonObject = (JSONObject) obj;

                // loop array
                JSONArray msg = (JSONArray) jsonObject.get("questions");
                // Loop through the questions
                for (int j = 0; j < msg.size(); j++) {
                    JSONObject question = (JSONObject) msg.get(j);

                    // Write questions with a Q
                    writer.write("Q" + (j + 1) + ": ");
                    writer.write((String) question.get("text"));

                    // Check if there is code
                    if (question.containsKey("question_details")) {
                        String code = (String) question.get("question_details");
                        String[] codeLines = code.trim().split("\n");
                        writer.write(System.lineSeparator()); //new line
                        writer.write("Code:" + codeLines.length);
                        for (String line : codeLines) {
                            writer.write(System.lineSeparator()); //new line
                            writer.write(line);
                        }
                    }
                    writer.write(System.lineSeparator()); //new line
                    JSONArray answers = (JSONArray) question.get("answers");
                    for (int i = 0; i < answers.size(); i++) {
                        JSONObject distractor = (JSONObject) answers.get(i);
                        writer.write((String) ((i + 1) + ". " + distractor.get("label")));
                        if (distractor.get("correct_answer_yn").equals("Y")) {
                            writer.write("*");
                        }
                        writer.write(System.lineSeparator()); //new line
                    }
                    writer.write(System.lineSeparator()); //new line
                }
                writer.flush();
            }

        } catch (FileNotFoundException e) {
        } catch (IOException | ParseException e) {
            System.out.println("Exception writing text file: " + e);
        }

    }

}
