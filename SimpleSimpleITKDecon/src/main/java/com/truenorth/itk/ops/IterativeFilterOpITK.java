package com.truenorth.itk.ops;

import imagej.ops.AbstractFunction;
import imagej.ops.Contingent;
import imagej.ops.Op;

import net.imglib2.RandomAccessibleInterval;

import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ItemIO;
import org.scijava.Priority;

/**
 * base class for ops that wrap iterative itk deconvolution filters
 * @author bnorthan
 *
 * @param <T>
 * @param <S>
 */
public abstract class IterativeFilterOpITK<T extends RealType<T>, S extends RealType<S>> 
			implements Op
{
	// ITK specific boundary strategies
	public static enum ITKBoundaryCondition{ZERO_PAD, PERIODIC_PAD, ZERO_FLUX_NEUMANN_PAD}; 
		
	@Parameter
	protected Img<T> input;
	
	@Parameter
	protected Img<T> kernel;
	
	@Parameter
	protected int numIterations;
	
	@Parameter(type = ItemIO.OUTPUT, required=false)
	protected Img<T> output;  
		
}

