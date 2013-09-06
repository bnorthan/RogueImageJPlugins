package fftj;

/**
 * Title:        FFTJ --- A 3D FFT implementation for ImageJ
 * Description:
 * Copyright:    Copyright (c) 2001, 2002
 * Company:      Wake Forest University School of Medicine, Department of Medical Engineering
 *               Winston-Salem, North Carolina, USA
 * @author       Nick Linnenbrügger (nilin@web.de)
 */

// class is final for performance reasons
final public class SinglePrecComplNum extends ComplexNum
{
    private float re, im;


    public SinglePrecComplNum() { super(); }

    public SinglePrecComplNum( double re, double im ) { super( re, im ); }

    public SinglePrecComplNum( double re ) { super( re ); }

    public SinglePrecComplNum( ComplexNum c ) { super( c ); }


    public double getRealValue() { return this.re; }

    public double getImagValue() { return this.im; }

    public void setRealValue( double re ) { this.re = ( float ) re; }

    public void setImagValue( double im ) { this.im = ( float ) im; }
}