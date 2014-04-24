package com.truenorth.itk.ops;

import imagej.ops.Op;

import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;

import org.itk.simple.Image;
import org.itk.simple.RichardsonLucyDeconvolutionImageFilter;
import org.itk.simple.RichardsonLucyDeconvolutionImageFilter.BoundaryConditionType;
import org.scijava.plugin.Plugin;
import org.scijava.Priority;

import com.truenorth.itk.SimpleItkImagejUtilities;

/**
 * 
 * An op that wraps the itk implementation of Richardson Lucy
 * 
 * @author bnorthan
 *
 * @param <T>
 * @param <S>
 */
@Plugin(type = Op.class, name = "RichardsonLucyITK", priority = Priority.HIGH_PRIORITY + 1)
public class RichardsonLucyITK<T extends RealType<T>, S extends RealType<S>> 
		extends IterativeFilterOpITK<T,S>
{
	org.itk.simple.RichardsonLucyDeconvolutionImageFilter itkRL;
	
	public void run()
	{
		// in this "simple" example we are assuming the images are 3D volumes
		// A more realistic assumption is that the input is a ND dataset
		// Code for itk decon integration into a more complete framework can be found here (for now)
		// https://github.com/bnorthan/projects/tree/master/truenorthJ/ImageJ2Plugins/itk-imagej-tn
		
		// convert input to itk Images
		Image itkImage=SimpleItkImagejUtilities.simple3DITKImageFromInterval(input);
		Image itkPsf=SimpleItkImagejUtilities.simple3DITKImageFromInterval(kernel);
		
		itkRL=new RichardsonLucyDeconvolutionImageFilter();
		
		// call itk rl using simple itk wrapper
		Image out=itkRL.execute(itkImage, itkPsf, numIterations, true, BoundaryConditionType.ZERO_PAD, 
				RichardsonLucyDeconvolutionImageFilter.OutputRegionModeType.SAME);
		
		T inputType=Util.getTypeFromInterval(input);
		
		// convert output to ImageJ Img
		output=SimpleItkImagejUtilities.simple3DITKImageToImg(out, input.factory(), inputType);
		
	}	
}