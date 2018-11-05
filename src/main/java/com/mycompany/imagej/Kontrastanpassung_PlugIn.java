package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Kontrastanpassung_PlugIn implements PlugInFilter {
    
    final int COLOR_MIN_DEFAULT = 0;
    final int COLOR_MAX_DEFAULT = 255;
    int color_min, color_max;
	
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

        // let the user input the new min and max value for the image
        color_min = (int) IJ.getNumber("Please enter the minimal value (at least 0): ", COLOR_MIN_DEFAULT);
        color_max = (int) IJ.getNumber("Please enter the maximal value (less than 255): ", COLOR_MAX_DEFAULT);
               
        // check if the minimal value is less than the maximal value
        // TODO: Check if really necessary
        if (color_min >= color_max) {
            IJ.showMessage("Error", "The minimal value is higher than the maximal value!\nThe Default Values (0 & 255) will be used");
            color_min = COLOR_MIN_DEFAULT;
            color_max = COLOR_MAX_DEFAULT;                    
        }

        // iterate over all image coordinates (u,v)
        for (int u = 0; u < M; u++) {
            for (int v = 0; v < N; v++) {
                int p = ip.getPixel(u, v);
                //get maximum and minimum
                if (p < color_low) {
                        color_low = p;
                }
                if (p > color_high) {
                        color_high = p;
                }
            }
        }

        int new_p;
        // iterate over all image coordinates (u,v)
        for (int u = 0; u < M; u++) {
            for (int v = 0; v < N; v++) {
                int p = ip.getPixel(u, v);
                //use linear contrastmodifyer
                new_p = color_min + (p-color_low)*(color_max-color_min)/(color_high-color_low);
                ip.putPixel(u, v, new_p);
            }
        }
    }
}
