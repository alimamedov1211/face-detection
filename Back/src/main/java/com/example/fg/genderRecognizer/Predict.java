package com.example.fg.genderRecognizer;

import com.example.fg.genderRecognizer.weightedPixel.WeightedStandardPixelTrainer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Service
public class Predict {
    public Result generateResult(MultipartFile mainPhoto) throws IOException {

        nu.pattern.OpenCV.loadLocally();

        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(mainPhoto.getBytes(), false)));
        String s = sb.toString();

        String base64Image = s.split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));


        File outputfile = new File("image.jpg");
        ImageIO.write(img, "jpg", outputfile);

        WeightedStandardPixelTrainer weightedStandardPixelTrainer = new WeightedStandardPixelTrainer();

        //sample file
        String imageFilePath = outputfile.getAbsolutePath();
        Mat[] faces = new FaceDetector().snipFace(imageFilePath, new Size(90, 90));

        //experience file
        weightedStandardPixelTrainer.load("src/main/resources/knowledge/Knowledge.log");
        Result result1 = new Result();
        result1.setGender("No face");
        result1.setAccuracy(0);

        int faceNo = 1;
        for (Mat face : faces) {
            Result result = weightedStandardPixelTrainer.predict(face);
            result.setGender("No face");
            int prediction = result.getId();
            if (prediction == -1) {
                System.out.println("I think " + faceNo + " is not a face.");
                Imgcodecs.imwrite("src/main/resources/sample/" + faceNo + "_noface.jpg", face);
            } else if (prediction == 0) {
                System.out.println("I think " + faceNo + " is a female.");
                result.setGender("female");
                Imgcodecs.imwrite("src/main/resources/sample/" + faceNo + "_female.jpg", face);
            } else {
                System.out.println("I think " + faceNo + " is a male.");
                result.setGender("male");
                Imgcodecs.imwrite("src/main/resources/sample/" + faceNo + "_male.jpg", face);
            }

            faceNo++;
            result1 = result;
        }
        System.out.println("Operation Successful!!!");

        return result1;
    }
}
