package com.example.fg.controller;

import com.example.fg.genderRecognizer.Predict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class Base64Controller {

    @Autowired
    Predict predict;


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/api/base", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<Object> predict(@RequestParam(value = "image", required = false) MultipartFile mainPhoto) throws Exception {
        String s = predict.generateResult(mainPhoto);
        return new ResponseEntity(s, HttpStatus.OK);
    }


}
