package fftj;

/**
 * Title:        FFTJ --- A 3D FFT implementation for ImageJ
 * Description:
 * Copyright:    Copyright (c) 2001, 2002
 * Company:      Wake Forest University School of Medicine, Department of Medical Engineering
 *               Winston-Salem, North Carolina, USA
 * @author       Nick Linnenbrügger (nilin@web.de)
 */

public class ComplexValueType
{
    private String name;
    private static String enumerationDescription = new String( "Complex Value Type" );

    private ComplexValueType( String name ) { this.name = name; }

    public String toString() { return name; }

    public String getEnumerationDescription() { return enumerationDescription; }

    public boolean equals( Object obj )
    {
        if ( !( obj instanceof ComplexValueType ) )
           return false;
        else
            return ( this.name == ( ( ComplexValueType ) obj ).name );
    }

    public static final ComplexValueType ABS = new ComplexValueType( "Abs" );
    public static final ComplexValueType IMAG_PART = new ComplexValueType( "Imaginary Part" );
    public static final ComplexValueType REAL_PART = new ComplexValueType( "Real Part" );
    public static final ComplexValueType FREQUENCY_SPECTRUM = new ComplexValueType( "Frequency Spectrum" );
    public static final ComplexValueType FREQUENCY_SPECTRUM_LOG =
                                         new ComplexValueType( "Frequency Spectrum (logarithmic)" );
    public static final ComplexValueType PHASE_SPECTRUM = new ComplexValueType( "Phase Spectrum" );
    public static final ComplexValueType POWER_SPECTRUM = new ComplexValueType( "Power Spectrum" );
    public static final ComplexValueType POWER_SPECTRUM_LOG =
                                         new ComplexValueType( "Power Spectrum (logarithmic)" );
}
