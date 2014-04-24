package com.truenorth.simpleyacudecuwrapper;

import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.iterator.LocalizingZeroMinIntervalIterator;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;

public class SimpleYacuDecuWrapperUtilities 
{
	/** a main that launches imagej for debugging */
	public static void main(final String... args) throws Exception 
	{
		// Launch ImageJ
		imagej.Main.launch(args);
	}
	
	public static<T extends RealType<T>> Img<T> convertFloatBufferToImage(float[] buffer, int[] size, ImgFactory<T> imgFactory, T type)
	{
		// use the image factory to create an img
		Img<T> image = imgFactory.create(size, type);
					
		// get a cursor so we can iterate through the image
		final Cursor<T> cursor = image.cursor();
					
		int i=0;
					
		// iterate through the image and copy from the psf buffer
		while (cursor.hasNext())
		{
			cursor.fwd();
						
			cursor.get().setReal(buffer[i]);
						
			i++;
		}
					
		return image;
	}
	
	public static<T extends RealType<T>> float[] convertImageToFloatBuffer(Img<T> image)
	{
		int size=1;
		
		for (int d=0;d<image.numDimensions();d++)
		{
			size*=image.dimension(d);
		}
		
		float[] buffer=new float[size];
					
		// get a cursor so we can iterate through the image
		final Cursor<T> cursor = image.cursor();
					
		int i=0;
					
		// iterate through the image and copy from the psf buffer
		while (cursor.hasNext())
		{
			cursor.fwd();
						
			buffer[i]=cursor.get().getRealFloat();
						
			i++;
		}
					
		return buffer;
	}
	
	public static<T extends RealType<T>> Img<T> FlipPSFQuadrants(RandomAccessibleInterval<T> input, ImgFactory<T> imageFactory, int outputDim[])
	{
		final int numDimensions = outputDim.length;
		
		int[] inputDim = new int[numDimensions];
		
		for ( int d = 0; d < numDimensions; ++d )
		{
			inputDim[d]=(int)input.dimension(d);
		}
		
		final long startTime = System.currentTimeMillis();
		
		T kernelType = Util.getTypeFromInterval( input );
		Img<T> output = imageFactory.create(outputDim, kernelType.createVariable());
		
		final RandomAccess<T> inputCursor = input.randomAccess();
		final RandomAccess<T> outputCursor = output.randomAccess();
		
		final LocalizingZeroMinIntervalIterator cursorDim = new LocalizingZeroMinIntervalIterator( input );
		
		final int[] position = new int[ numDimensions ];
		final int[] position2 = new int[ numDimensions ];
		
		while ( cursorDim.hasNext() )
		{
			cursorDim.fwd();
			cursorDim.localize( position );
			
			for ( int d = 0; d < numDimensions; ++d )
			{
				// the kernel might not be zero-bounded
				position2[ d ] = position[ d ] + (int)input.min( d );
				
				position[ d ] = ( position[ d ] - inputDim[ d ]/2 + outputDim[ d ] ) % outputDim[ d ];
			}
			
			inputCursor.setPosition( position2 );				
			outputCursor.setPosition( position );
			outputCursor.get().set( inputCursor.get() );
		}
		
		return output;
	}
}
