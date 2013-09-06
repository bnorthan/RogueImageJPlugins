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
final public class SinglePrecFFT3D extends FFT3D
{
    public SinglePrecFFT3D() { super(); }

    public SinglePrecFFT3D( ImageStack sourceReal ) { super( sourceReal ); }

    public SinglePrecFFT3D( ImageStack sourceReal, ImageStack sourceImag )
    {
        super( sourceReal, sourceImag );
    }

    public SinglePrecFFT3D( ComplexNum[][][] source ) { super( source ); }


    protected ComplexNum makeComplexNumber()
    {
        return new SinglePrecComplNum();
    }

    protected ComplexNum makeComplexNumber( ComplexNum c )
    {
        return new SinglePrecComplNum( c );
    }

    protected ComplexNum makeComplexNumber( double re, double im )
    {
        return new SinglePrecComplNum( re, im );
    }

    protected ComplexNum makeComplexNumber( double re )
    {
        return new SinglePrecComplNum( re );
    }

    public String getPrecision() { return "Single Precision"; }
}