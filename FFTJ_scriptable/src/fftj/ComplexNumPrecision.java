package fftj;

/**
 * Title:        FFTJ --- A 3D FFT implementation for ImageJ
 * Description:
 * Copyright:    Copyright (c) 2001, 2002
 * Company:      Wake Forest University School of Medicine, Department of Medical Engineering
 *               Winston-Salem, North Carolina, USA
 * @author       Nick Linnenbrügger (nilin@web.de)
 */

public class ComplexNumPrecision
{
    private String name;
    private static String enumerationDescription = new String( "Complex Number Precision" );

    private ComplexNumPrecision( String name ) { this.name = name; }

    public String toString() { return name; }

    public String getEnumerationDescription() { return enumerationDescription; }

    public boolean equals( Object obj )
    {
        if ( !( obj instanceof ComplexNumPrecision ) )
           return false;
        else
            return ( this.name == ( ( ComplexNumPrecision ) obj ).name );
    }

    public static final ComplexNumPrecision SINGLE = new ComplexNumPrecision( "Single Precision" );
    public static final ComplexNumPrecision DOUBLE = new ComplexNumPrecision( "Double Precision" );
}
