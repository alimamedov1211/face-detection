package com.example.fg.genderRecognizer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import com.example.fg.genderRecognizer.weightedPixel.WeightedStandardImage;
import com.example.fg.genderRecognizer.weightedPixel.WeightedStandardPixelTrainer;
import org.opencv.core.Core;

public class Train {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//list of image files//////////////////////////////////////////////////////////////////////////////////////////
		String trainningFolderPath = "src/main/resources/trainingData";
		
		File trainningFolder = new File(trainningFolderPath);
		String[] trainningSubfolderPaths = trainningFolder.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		
		//String[] stringFilePaths = {"resource/Face_Male_Female/images1", "resource/Face_Male_Female/images2"};
		
		ArrayList<String> filePathList = new ArrayList<String>();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		
		int id=0;	//label
		for(String SubfolderPath: trainningSubfolderPaths){
			File[] files = new File(trainningFolderPath+"\\"+SubfolderPath).listFiles();
			
			int limitedNumberOfSamples = 0;
			for(File file: files){
				filePathList.add(file.getAbsolutePath());
				idList.add(id);
				
				limitedNumberOfSamples++;
				if(limitedNumberOfSamples > 1000) break;
			}
			
			id++;
		}
		
		String[] filePaths = new String[filePathList.size()];
		filePathList.toArray(filePaths);
		Integer[] ids = new Integer[idList.size()];
		idList.toArray(ids);
		
		
		///test
		/*/for(int i=filePaths.length-1; i>=0; i--){
			System.out.println("filePaths: " + filePaths[i]);
			System.out.println("ids: " + ids[i]);
		}/**/
		
		
		//train////////////////////////////////////////////////////////////////////////////////////////////////////////
		WeightedStandardPixelTrainer weightedStandardPixelTrainer = new WeightedStandardPixelTrainer();
		weightedStandardPixelTrainer.train(filePaths, ids);
		WeightedStandardImage weightedStandardImage = weightedStandardPixelTrainer.getWeightedStandardImage();
		
		weightedStandardImage.saveKnowledge("src/main/resources/knowledge/Knowledge.log");
		
		System.out.println("Operation successful!!!");
	}
}
