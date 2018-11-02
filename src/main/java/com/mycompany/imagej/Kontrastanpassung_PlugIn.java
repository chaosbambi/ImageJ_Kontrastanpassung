package com.mycompany.imagej;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Kontrastanpassung_PlugIn implements PlugInFilter {

	int color_min=0;
	int color_max=255;
	
public int setup(String args, ImagePlus im) {
	return DOES_8G; // this plugin accepts 8-bit grayscale images
}

public void run(ImageProcessor ip) {
	int M = ip.getWidth();
	int N = ip.getHeight();

	int color_low=255;
	int color_high=0;
	
	// iterate over all image coordinates (u,v)
	for (int u = 0; u < M; u++) {
		for (int v = 0; v < N; v++) {
			int p = ip.getPixel(u, v);
			//get maximum and minimum
			if ( p < color_low) {
				color_low = p;
			}
			if(p>color_high) {
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
