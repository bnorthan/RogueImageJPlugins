import fftj.*;
import ij.*;
import ij.gui.*;
import ij.plugin.*;

/**
 * Title:        FFTJ --- A 3D FFT implementation for ImageJ
 * Description:
 * Copyright:    Copyright (c) 2001, 2002
 * Company:      Wake Forest University School of Medicine, Department of Medical Engineering
 *               Winston-Salem, North Carolina, USA
 * @author       Nick Linnenbrügger (nilin@web.de)
 */

public class FFTJ_ implements PlugIn
{
    private static final String plugInTitle = "FFTJ --- A 3D FFT implementation";
    private static final String outputFrameTitlePrefix = "FFTJ --- ";

    public void run( String arg )
    {
        if ( arg.equalsIgnoreCase( "about" ) )
        {
            showAbout();
            return;
        }

        // check if appropriate version of ImageJ is installed
        if ( IJ.versionLessThan( "1.24t" ) )
           return;

        // check if Java, ver. 1.1.8 or higher is installed
        if ( System.getProperty( "java.version" ).compareTo( "1.1.8" ) < 0 )
        {
            IJ.error( "This plugin has been developed and tested with Java, version 1.1.8 and higher.\n" +
                      "Please upgrade your JVM." );
            return;
        }

        int[] wList = WindowManager.getIDList();
        if ( wList == null )
        {
            IJ.noImage();
            return;
        }

        String[] titles1 = new String[ wList.length ];
        for ( int i = 0; i < wList.length; i++ )
        {
            ImagePlus imp = WindowManager.getImage( wList[ i ] );
            if ( imp != null )
                titles1[ i ] = imp.getTitle();
            else
                titles1[ i ] = "";
        }

        // add option '<none>' to image titles
        String[] titles2 = new String[ wList.length + 1 ];
        titles2[ 0 ] = "<none>";
        for ( int i = 0; i < wList.length; i++ )
        {
            ImagePlus imp = WindowManager.getImage( wList[ i ] );
            if ( imp != null )
                titles2[ i + 1 ] = imp.getTitle();
            else
                titles2[ i + 1 ] = "";
        }

        ComplexNumPrecision[] cnPrecisions = { ComplexNumPrecision.SINGLE, ComplexNumPrecision.DOUBLE };

        GenericDialog gd = new GenericDialog( plugInTitle, IJ.getInstance() );
        gd.addChoice( "Real part of input:", titles1, titles1[ 0 ] );
        gd.addChoice( "Imaginary part of input:", titles2, titles2[ 0 ] );
        gd.addChoice( "Complex Number Precision:",
                      new String[] { cnPrecisions[ 0 ].toString(), cnPrecisions[ 1 ].toString() },
                      cnPrecisions[ 0 ].toString() );
        gd.addChoice( "FFT Direction:", new String[] { "forward", "inverse" }, "forward" );
        gd.showDialog();

        if ( gd.wasCanceled() )
            return;

        ImagePlus real = WindowManager.getImage( wList[ gd.getNextChoiceIndex() ] );
        ImagePlus imaginary = null;
        int choiceIndex2 = gd.getNextChoiceIndex();
        if ( choiceIndex2 != 0 )
           imaginary = WindowManager.getImage( wList[ choiceIndex2 - 1 ] );

        if ( real.lockSilently() == false )
        {
            IJ.error( "The specified real part already is in use." );
            return;
        }

        if ( ( imaginary != null ) && ( imaginary.lockSilently() == false ) && ( imaginary != real ) )
        {
            IJ.error( "The specified imaginary part already is in use." );
            real.unlock();
            return;
        }

        try
        {
            if ( !isGrayscale( real ) ||
                 ( ( imaginary != null ) &&
                   ( !isGrayscale( imaginary ) ||
                   ( real.getWidth() != imaginary.getWidth() ) ||
                   ( real.getHeight() != imaginary.getHeight() ) ||
                   ( real.getStackSize() != imaginary.getStackSize() ) ) ) )
            {
                IJ.error( "Real part of input must be a gray-scale image or image stack.\n" +
                          "If an imaginary part is specified, it must be a gray-scale image or image\n" +
                          "stack of the same size as the real part in all three dimensions." );
                return;
            }

            new DisclaimerWindow();

            FFT3D transformer = null;
            if ( gd.getNextChoiceIndex() == 0 )
               transformer = new SinglePrecFFT3D(
                                    real.getStack(), ( imaginary != null ) ? imaginary.getStack() : null );
            else
               transformer = new DoublePrecFFT3D(
                                    real.getStack(), ( imaginary != null ) ? imaginary.getStack() : null );

            if ( gd.getNextChoiceIndex() == 0 )
            {
               IJ.showStatus( "Calculating Fourier Transform ..." );
               transformer.fft();
            }
            else
            {
                IJ.showStatus( "Calculating Inverse Fourier Transform ..." );
                transformer.ifft();
            }

            IJ.showStatus( "" );
            // create plugIn frame that gives choices for various output
            new OutputFrame( outputFrameTitlePrefix + "Real input: " + real.getTitle() + "; Imaginary input: " +
                                ( ( imaginary != null ) ? imaginary.getTitle() : "<none>"), transformer );
        }
        finally
        {
            real.unlock();
            if ( imaginary != null )
               imaginary.unlock();
        }
    }


    static public void showAbout()
    {
        IJ.showMessage( "About FFTJ (version 2.0) ...",
                        "This plug-in has been written by\n" +
                        "Nick I. Linnenbruegger for Wake Forest University School of Medicine,\n" +
                        "Department of Medical Engineering since March 2001.\n" +
                        "The author is a student at Aachen University of Technology (RWTH Aachen),\n" +
                        "Germany and can be contacted under nilin@web.de." );
    }


    private static boolean isGrayscale( ImagePlus image )
    {
        return ( ( image.getType() == ImagePlus.GRAY8 ) || ( image.getType() == ImagePlus.GRAY16 ) ||
                 ( image.getType() == ImagePlus.GRAY32 ) );
    }
}