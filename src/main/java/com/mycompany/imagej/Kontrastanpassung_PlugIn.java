package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Kontrastanpassung_PlugIn implements PlugInFilter {
    
	final int BLACK = 0;
	final int WHITE = 255;
	
    final int COLOR_MIN_DEFAULT = BLACK;
    final int COLOR_MAX_DEFAULT = WHITE;
    final int PERCENTAGE_DEFAULT = 1;
    
    int color_min, color_max;
	int percentage;
	
	int[] histogram;
    
    @Override    
    public int setup(String args, ImagePlus im) {  
    	// this plugin accepts 8-bit grayscale images
        return DOES_8G; 
    }

    @Override
    public void run(ImageProcessor ip) {
        int M = ip.getWidth();
        int N = ip.getHeight();
        
        histogram = ip.getHistogram();
        
        int minThreshold = BLACK;
        int maxThreshold = WHITE;  
        
        if(getAndCheckPercentage()) {
	        // calculate amount of pixels needed to reach x%
	        int pixelAmount = (int) Math.ceil(ip.getPixelCount() * percentage / 100.0);
	        
	        // the values that need to be colored in min/max values in the next step
	        minThreshold = BLACK;
	        maxThreshold = WHITE;	
	        
	        // the contrast should be expanded to the whole range of greyscales
	        color_max = WHITE;
	        color_min = BLACK;
	        
	        // the amount of pixels in the lowest/highest values
	        int minSum = histogram[minThreshold];
	        int maxSum = histogram[maxThreshold];
	        
	        // add up lowest gray shades until pixel amount needed for x% is reached
	        while(minSum < pixelAmount) {
	        	minThreshold++;
	        	minSum += histogram[minThreshold];
	        }
	        
	        // add up highest gray shades until pixel amount needed for x% is reached
	        while(maxSum < pixelAmount) {
	        	maxThreshold--;
	        	maxSum += histogram[maxThreshold];
	        }
        } else {
        	// use normal linear contrast modification  
        	getAndCheckValues();
        	    	
        	maxThreshold = getMaximalValue();
        	minThreshold = getMinimalValue();
        }

        int new_p;
        
        // iterate over all image coordinates (u,v)
        for (int u = 0; u < M; u++) {
            for (int v = 0; v < N; v++) {
                int p = ip.getPixel(u, v);
                
                if(p > minThreshold && p < maxThreshold) {
                	//scale values linear over new range
                	new_p = color_min + (p - minThreshold) * (color_max - color_min) / (maxThreshold - minThreshold);	
                } else {
                	new_p = (p >= maxThreshold) ? color_max : color_min;
                } 
            
                ip.putPixel(u, v, new_p);
            }
        }
    }
    
    // let the user input the new min and max value for the image
    private void getAndCheckValues() {    	
        color_min = (int) IJ.getNumber("Please enter the minimal value (at least 0): ", COLOR_MIN_DEFAULT);
        color_max = (int) IJ.getNumber("Please enter the maximal value (less than 255): ", COLOR_MAX_DEFAULT);       
        
        // check if the minimal value is less than the maximal value or <0 or >255
        if (color_min >= color_max) {
            IJ.showMessage("Error", "The minimal value is higher than the maximal value!\nThe Default Values (0 & 255) will be used");
            color_min = COLOR_MIN_DEFAULT;
            color_max = COLOR_MAX_DEFAULT;                    
        }
        
        if (color_min < 0) {
            IJ.showMessage("Error", "The minimal value is lower than 0!\nThe Default Value (0) will be used");
            color_min = COLOR_MIN_DEFAULT;                    
        }
        
        if (color_max > 255) {
            IJ.showMessage("Error", "The maximal value is higher than 255!\nThe Default Value (255) will be used");
            color_max = COLOR_MAX_DEFAULT;                    
        }
    }
    
    // let the user input the percentage to pull at least x% to min/max value
    // checks if the entered value is in a suitable area
    private boolean getAndCheckPercentage() {              
        percentage = (int) IJ.getNumber("Please enter the percentage (between 0 and 49). If 0 is entered, linear contrast modification will be used: ", PERCENTAGE_DEFAULT);
        
        if(percentage > 0 && percentage <= 49) {
        	return true;
        }  else {
    		IJ.showMessage("Error", "The percentage value was out of bounds!\nNow linear modification will be used"); 
        	return false;
        }	   	
    }
    
    // returns the minimal grayscale found in the image
    private int getMinimalValue() {
    	int count = BLACK;
    	while (histogram[count] == 0) {
    		count++;
    	}
    	// IJ.log("Minimal Value: " +  count);
    	return count;
    }
    
    // returns the maximal grayscale found in the image
    private int getMaximalValue() {
    	int count = WHITE;
    	while (histogram[count] == 0) {
    		count--;
    	}
    	// IJ.log("Maximal Value: " +  count);
    	return count;
    }
}
