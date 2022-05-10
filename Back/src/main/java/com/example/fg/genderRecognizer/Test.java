package com.example.fg.genderRecognizer;

import com.example.fg.genderRecognizer.weightedPixel.WeightedStandardPixelTrainer;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FilenameFilter;


public class Test {
    public static void main(String[] args) {
        nu.pattern.OpenCV.loadLocally();

        WeightedStandardPixelTrainer weightedStandardPixelTrainer = new WeightedStandardPixelTrainer();
        weightedStandardPixelTrainer.load("src/main/resources/knowledge/Knowledge.log");
        //sample file
        String testFolderPath = "src/main/resources/trainingData";

        File testFolder = new File(testFolderPath);
        String[] testSubfolderPaths = testFolder.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });


        int id = 0;    //label
        int right = 0;
        int wrong = 0;
        for (String SubfolderPath : testSubfolderPaths) {
            File[] files = new File(testFolderPath + "\\" + SubfolderPath).listFiles();

            for (File file : files) {
                String imageFilePath = file.getAbsolutePath();
                Mat mat = Imgcodecs.imread(imageFilePath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

                int prediction = weightedStandardPixelTrainer.predict(mat);

                if (prediction == id) {
                    right++;
                    System.out.print("R");
                } else {
                    wrong++;
                    System.out.println("W");
                }
            }

            id++;
        }

        System.out.println("Percentage of error: " + wrong * 100 / (wrong + right));

        System.out.println("Operation Successful!!!");
    }
}
