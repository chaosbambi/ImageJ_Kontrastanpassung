package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Kontrastanpassung_PlugIn implements PlugInFilter {
    
    final int COLOR_MIN_DEFAULT = 0;
    final int COLOR_MAX_DEFAULT = 255;
    final int PERCENTAGE_DEFAULT = 1;
    int color_min, color_max;
	int percentage;
    
    @Override
    public int setup(String args, ImagePlus im) {   
        return DOES_8G; // this plugin accepts 8-bit grayscale images
    }

    @Override
    public void run(ImageProcessor ip) {
        int M = ip.getWidth();
        int N = ip.getHeight();

        int color_low = 255;
        int color_high = 0;
        
        int minThreshold = 0;
        int maxThreshold = 255;
        
        // let the user input the new min and max value for the image
        color_min = (int) IJ.getNumber("Please enter the minimal value (at least 0): ", COLOR_MIN_DEFAULT);
        color_max = (int) IJ.getNumber("Please enter the maximal value (less than 255): ", COLOR_MAX_DEFAULT);
        
        // let the user input the percentage to pull at least x% to min/max value
        percentage = (int) IJ.getNumber("Please enter the minimal value (at least 0, at most 49): ", PERCENTAGE_DEFAULT);
       
        
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
        if(percentage > 0 && percentage < 49) {
	        //get histogramm
	        int[] histogram = ip.getHistogram();
	        //calculate amount of pixels needed to reach x%
	        int pixelAmount = (int) Math.ceil(ip.getPixelCount() * percentage / 100.0);
	        
        
	        //the values that need to be colored in min/max values in the next step
	        minThreshold = 0;
	        maxThreshold = 255;
	        //the amount of pixels in the lowest/highest values
	        int minSum = histogram[0];
	        int maxSum = histogram[255];
	        //add up lowest gray shades until pixel amount needed for x% is reached
	        while(minSum < pixelAmount) {
	        	minThreshold++;
	        	minSum += histogram[minThreshold];
	        }
	        //add up highest gray shades until pixel amount needed for x% is reached
	        while(maxSum < pixelAmount) {
	        	maxThreshold--;
	        	maxSum += histogram[maxThreshold];
	        }
        } else {
        	//use normal linear contrast modification
        	 if (percentage != 0) {
                 IJ.showMessage("Error", "The percentage value was out of bounds!\n0% will be used");                    
             }
        	color_high = (int) ip.getMax();
            color_low = (int) ip.getMin();
        	
        	maxThreshold = color_high;
        	minThreshold = color_low;
        }

        int new_p;
        // iterate over all image coordinates (u,v)
        for (int u = 0; u < M; u++) {
            for (int v = 0; v < N; v++) {
                int p = ip.getPixel(u, v);
                
                if(p > minThreshold && p < maxThreshold) {
                	//use linear contrastmodifyer
                	new_p = color_min + (p-minThreshold)*(color_max-color_min)/(maxThreshold-minThreshold);	
                } else if( p >= maxThreshold){
                	new_p = color_max;
                } else {
                	new_p = color_min;
                }
                
                ip.putPixel(u, v, new_p);
            }
        }
    }
}
