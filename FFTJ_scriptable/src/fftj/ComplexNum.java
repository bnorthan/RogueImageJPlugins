package fftj;

/**
 * Title:        FFTJ --- A 3D FFT implementation for ImageJ
 * Description:
 * Copyright:    Copyright (c) 2001, 2002
 * Company:      Wake Forest University School of Medicine, Department of Medical Engineering
 *               Winston-Salem, North Carolina, USA
 * @author       Nick Linnenbrügger (nilin@web.de)
 */

abstract public class ComplexNum implements Cloneable
{
    public ComplexNum() { this.setValue( 0, 0 ); }

    public ComplexNum( double re, double im ) { this.setValue( re, im ); }

    public ComplexNum( double re ) { this.setValue( re, 0 ); }

    public ComplexNum( ComplexNum c ) { this.setValue( c ); }

    abstract public double getRealValue();

    abstract public double getImagValue();

    abstract public void setRealValue( double re );

    abstract public void setImagValue( double im );

    public Object clone()
    {
        try { return super.clone(); }
        catch ( CloneNotSupportedException exc ) { throw new RuntimeException( exc.getMessage() ); }
    }

    public boolean equals( Object obj )
    {
        if ( !( obj instanceof ComplexNum ) )
           return false;
        else
            return ( this.getRealValue() == ( ( ComplexNum ) obj ).getRealValue() ) &&
                   ( this.getImagValue() == ( ( ComplexNum ) obj ).getImagValue() );
    }

    public void setValue( double re, double im )
    {
        this.setRealValue( re );
        this.setImagValue( im );
    }

    public void setValue( ComplexNum c ) { this.setValue( c.getRealValue(), c.getImagValue() ); }

    public void makeComplexConjugate() { this.setImagValue( - this.getImagValue() ); }

    public ComplexNum getComplexConjugate()
    {
        ComplexNum clone = ( ComplexNum ) this.clone();
        clone.setImagValue( - clone.getImagValue() );
        return clone;
    }

    public void addValue( ComplexNum c )
    {
        this.setValue( this.getRealValue() + c.getRealValue(), this.getImagValue() + c.getImagValue() );
    }

    public void addValue( double re, double im )
    {
        this.setValue( this.getRealValue() + re, this.getImagValue() + im );
    }

    public void addValue( double re )
    {
        this.setRealValue( this.getRealValue() + re );
    }

    public void divideByValue( ComplexNum c )
    {
        double divisor = c.getRealValue() * c.getRealValue() + c.getImagValue() * c.getImagValue();
        this.setValue( ( this.getRealValue() * c.getRealValue() + this.getImagValue() * c.getImagValue() ) / divisor,
                       ( this.getImagValue() * c.getRealValue() - this.getRealValue() * c.getImagValue() ) / divisor );
    }

    public void divideByValue( double realDivisor )
    {
        this.setValue( this.getRealValue() / realDivisor, this.getImagValue() / realDivisor );
    }

    public void multiplyByValue( ComplexNum c )
    {
        this.setValue( this.getRealValue() * c.getRealValue() - this.getImagValue() * c.getImagValue(),
                       this.getRealValue() * c.getImagValue() + this.getImagValue() * c.getRealValue() );
    }

    public void multiplyByValue( double realMultiplier )
    {
        this.setValue( this.getRealValue() * realMultiplier, this.getImagValue() * realMultiplier );
    }

    public double getAbs()
    {
        return Math.sqrt( this.getRealValue() * this.getRealValue() + this.getImagValue() * this.getImagValue() );
    }

    public double getFourierFrequencySpectrum() { return this.getAbs(); }

    public double getFourierFrequencySpectrumLogarithmic() { return Math.log( 1d + this.getAbs() ); }

    public double getFourierPhaseSpectrum() { return Math.atan2( this.getImagValue(), this.getRealValue() ); }

    public double getFourierPowerSpectrum()
    {
        return this.getRealValue() * this.getRealValue() + this.getImagValue() * this.getImagValue();
    }

    public double getFourierPowerSpectrumLogarithmic() { return Math.log( 1d + this.getFourierPowerSpectrum() ); }

    public double getValue( ComplexValueType type )
    {
        if ( type == ComplexValueType.ABS )
           return this.getAbs();
        else if ( type == ComplexValueType.FREQUENCY_SPECTRUM )
             return this.getFourierFrequencySpectrum();
        else if ( type == ComplexValueType.FREQUENCY_SPECTRUM_LOG )
             return this.getFourierFrequencySpectrumLogarithmic();
        else if ( type == ComplexValueType.IMAG_PART )
             return this.getImagValue();
        else if ( type == ComplexValueType.PHASE_SPECTRUM )
             return this.getFourierPhaseSpectrum();
        else if ( type == ComplexValueType.POWER_SPECTRUM )
             return this.getFourierPowerSpectrum();
        else if ( type == ComplexValueType.POWER_SPECTRUM_LOG )
             return this.getFourierPowerSpectrumLogarithmic();
        else
            return this.getRealValue();
    }

    public String toString()
    {
        return this.getRealValue() + " + " + this.getImagValue() + "i";
    }

    public static void normalizeToMaxAbsValue( ComplexNum[][][] data, double value )
    {
        double max = Double.MIN_VALUE;

        for ( int x = 0; x < data.length; x++ )
            for ( int y = 0; y < data[ 0 ].length; y++ )
                for ( int z = 0; z < data[ 0 ][ 0 ].length; z++ )
                    max = Math.max( max, data[ x ][ y ][ z ].getAbs() );

        for ( int x = 0; x < data.length; x++ )
            for ( int y = 0; y < data[ 0 ].length; y++ )
                for ( int z = 0; z < data[ 0 ][ 0 ].length; z++ )
                    data[ x ][ y ][ z ].multiplyByValue( value / max );
    }
}