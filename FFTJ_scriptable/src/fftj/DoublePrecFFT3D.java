package fftj;

import ij.*;

/**
 * Title:        FFTJ --- A 3D FFT implementation for ImageJ
 * Description:
 * Copyright:    Copyright (c) 2001, 2002
 * Company:      Wake Forest University School of Medicine, Department of Medical Engineering
 *               Winston-Salem, North Carolina, USA
 * @author       Nick Linnenbrügger (nilin@web.de)
 */

// class is final for performance reasons
final public class DoublePrecFFT3D extends FFT3D
{
    public DoublePrecFFT3D() { super(); }

    public DoublePrecFFT3D( ImageStack sourceReal ) { super( sourceReal ); }

    public DoublePrecFFT3D( ImageStack sourceReal, ImageStack sourceImag )
    {
        super( sourceReal, sourceImag );
    }

    public DoublePrecFFT3D( ComplexNum[][][] source ) { super( source ); }


    protected ComplexNum makeComplexNumber()
    {
        return new DoublePrecComplNum();
    }

    protected ComplexNum makeComplexNumber( ComplexNum c )
    {
        return new DoublePrecComplNum( c );
    }

    protected ComplexNum makeComplexNumber( double re, double im )
    {
        return new DoublePrecComplNum( re, im );
    }

    protected ComplexNum makeComplexNumber( double re )
    {
        return new DoublePrecComplNum( re );
    }

    public String getPrecision() { return "Double Precision"; }
}