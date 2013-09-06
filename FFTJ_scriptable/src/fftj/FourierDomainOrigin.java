package fftj;

/**
 * Title:        FFTJ --- A 3D FFT implementation for ImageJ
 * Description:
 * Copyright:    Copyright (c) 2001, 2002
 * Company:      Wake Forest University School of Medicine, Department of Medical Engineering
 *               Winston-Salem, North Carolina, USA
 * @author       Nick Linnenbrügger (nilin@web.de)
 */

public class FourierDomainOrigin
{
    private String name;
    private static String enumerationDescription = new String( "Origin of Fourier Domain" );

    private FourierDomainOrigin( String name ) { this.name = name; }

    public String toString() { return this.name; }

    public String getEnumerationDescription() { return enumerationDescription; }

    public boolean equals( Object obj )
    {
        if ( !( obj instanceof FourierDomainOrigin ) )
           return false;
        else
            return ( this.name == ( ( FourierDomainOrigin ) obj ).name );
    }

    public static final FourierDomainOrigin AT_ZERO = new FourierDomainOrigin( "At (0,0,0)" );
    public static final FourierDomainOrigin AT_CENTER = new FourierDomainOrigin( "At Volume-Center" );
}