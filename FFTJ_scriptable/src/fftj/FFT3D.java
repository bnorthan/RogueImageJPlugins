package fftj;

import java.util.*;

import ij.*;
import ij.process.*;

/**
 * Title:        FFTJ --- A 3D FFT implementation for ImageJ
 * Description:
 * Copyright:    Copyright (c) 2001, 2002
 * Company:      Wake Forest University School of Medicine, Department of Medical Engineering
 *               Winston-Salem, North Carolina, USA
 * @author       Nick Linnenbrügger (nilin@web.de)
 */

abstract public class FFT3D implements Cloneable
{
    private ComplexNum[][][] data;
    private int dataDimX, dataDimY, dataDimZ;

    // Observation Agent that propagates state changes to registered Observers.
    // See http://w3.one.net/~jweirich/java/patterns/observer/javaobserver.html for more information.
    private PublicObservable observationAgent;

    public static final Direction FORWARD = new Direction( "FORWARD" );
    public static final Direction INVERSE = new Direction( "INVERSE" );


    public FFT3D()
    {
        this.observationAgent = new PublicObservable();
    }


    public FFT3D( ImageStack sourceReal )
    {
        this.observationAgent = new PublicObservable();
        this.setData( sourceReal, null );
    }


    public FFT3D( ImageStack sourceReal, ImageStack sourceImag )
    {
        this.observationAgent = new PublicObservable();
        this.setData( sourceReal, sourceImag );
    }


    public FFT3D( ComplexNum[][][] source )
    {
        this.observationAgent = new PublicObservable();
        this.setData( source );
    }


    abstract protected ComplexNum makeComplexNumber();


    abstract protected ComplexNum makeComplexNumber( double re, double im );


    abstract protected ComplexNum makeComplexNumber( double re );


    abstract protected ComplexNum makeComplexNumber( ComplexNum c );


    abstract public String getPrecision();


    /**
     * Returns the Observation Agent that mediates between this Object (the Observable) and registered Observers.
     * @return The Observation Agent.
     */
    public Observable getObservationAgent() { return this.observationAgent; }


    /** Manually forces this object to inform its Observation Agent about a change.
     */
    public void setChanged()
    {
        this.observationAgent.setChanged();
        this.observationAgent.notifyObservers( this );
    }


    public void setData( ImageStack sourceReal, ImageStack sourceImag )
    {
        if ( sourceReal == null )
           throw new IllegalArgumentException( "Source stack with real part cannot be 'null'." );

        if ( ( sourceImag != null ) &&
             ( ( sourceReal.getWidth() != sourceImag.getWidth() ) ||
               ( sourceReal.getHeight() != sourceImag.getHeight() ) ||
               ( sourceReal.getSize() != sourceImag.getSize() ) ) )
            throw new IllegalArgumentException( "Source stacks with real and imaginary part must be\n" +
                                                "of the same size in x-, y-, and z-direction." );

        this.dataDimX = sourceReal.getWidth();
        this.dataDimY = sourceReal.getHeight();
        this.dataDimZ = sourceReal.getSize();

        this.data = new ComplexNum[ this.dataDimX ][ this.dataDimY ][ this.dataDimZ ];

        for ( int z = 0; z < this.dataDimZ; z++ )
        {
            ImageProcessor imageReal = sourceReal.getProcessor( z + 1 );
            if ( sourceImag != null )
            {
                ImageProcessor imageImag = sourceImag.getProcessor( z + 1 );
                for ( int x = 0; x < this.dataDimX; x++ )
                    for ( int y = 0; y < this.dataDimY; y++ )
                        this.data[ x ][ y ][ z ] = this.makeComplexNumber( imageReal.getPixelValue( x, y ),
                                                                               imageImag.getPixelValue( x, y ) );
            }
            else
                for ( int x = 0; x < this.dataDimX; x++ )
                    for ( int y = 0; y < this.dataDimY; y++ )
                        this.data[ x ][ y ][ z ] = this.makeComplexNumber( imageReal.getPixelValue( x, y ) );
        }

        this.setChanged();
    }


    public void setData( ImageStack sourceReal )
    {
        this.setData( sourceReal, null );
    }


    public void setData( ComplexNum[][][] source )
    {
        if ( source == null )
           throw new IllegalArgumentException( "'source' cannot be 'null'." );

        this.dataDimX = source.length;
        this.dataDimY = source[ 0 ].length;
        this.dataDimZ = source[ 0 ][ 0 ].length;

        this.data = source;

        this.setChanged();
    }


    /**
     * Provides a shallow copy of this object.
     * That is, just the reference to the data and not the data itself is copied.
     * The only thing that changes is that the clone has a newly created observation agent.
     *
     * @return A shallow copy of this object.
     */
    public Object clone()
    {
        try
        {
            FFT3D clone = ( FFT3D ) super.clone();
            clone.observationAgent = new PublicObservable();
            return clone;
        }
        catch ( CloneNotSupportedException exc ) { throw new RuntimeException( exc.getMessage() ); }
    }


    /**
     * Provides a deep copy of this object.
     * That is, all data associated with this object is actually is copied.
     * The only thing that changes is that the clone has a newly created observation agent and
     * that the soft reference to a rearranged copy of references is set to 'null'.
     *
     * @return A deep copy of this object.
     */
    public FFT3D deepClone()
    {
        FFT3D clone = ( FFT3D ) this.clone();

        clone.data = new ComplexNum[ this.dataDimX ][ this.dataDimY ][ this.dataDimZ ];
        for ( int x = 0; x < this.dataDimX; x++ )
            for ( int y = 0; y < this.dataDimY; y++ )
                for ( int z= 0; z < this.dataDimZ; z++ )
                    clone.data[ x ][ y ][ z ] = this.makeComplexNumber( this.data[ x ][ y ][ z ] );

        return clone;
    }


    public boolean equals( Object obj )
    {
        if ( !( obj instanceof FFT3D ) )
           return false;
        else
        {
            FFT3D other = ( FFT3D ) obj;
            if ( this.getData() == null )
               if ( other.getData() == null )
                  return true;
                else
                    return false;

            if ( ( this.dataDimX != other.dataDimX ) || ( this.dataDimY != other.dataDimY ) ||
                 ( this.dataDimZ != other.dataDimZ ) )
               return false;

            for ( int x = 0; x < this.dataDimX; x++ )
                for ( int y = 0; y < this.dataDimY; y++ )
                    for ( int z = 0; z < this.dataDimZ; z++ )
                        if ( !this.data[ x ][ y ][ z ].equals( other.data[ x ][ y ][ z ] ) )
                           return false;
        }

        return true;
    }


    public boolean isOfEqualSize( FFT3D transformer )
    {
        if ( this.getData() == null )
           if ( transformer.getData() == null )
              return true;
            else
                return false;

        if ( ( this.dataDimX != transformer.dataDimX ) || ( this.dataDimY != transformer.dataDimY ) ||
             ( this.dataDimZ != transformer.dataDimZ ) )
           return false;
        else
            return true;
    }


    public void fft( Direction direction )
    {
        if ( this.getData() == null )
           throw new IllegalStateException( "Data to process must be set first. Use method setData(). ");

        this.FFT3D( direction );
    }


    public void fft() { this.fft( this.FORWARD ); }


    public void ifft() { this.fft( this.INVERSE ); }


    public ComplexNum[][][] getData()
    {
        return this.data;
    }


    /**
     * a b c d e   --> c d e a b
     * a b c d e f --> d e f a b c
     * (in all three dimensions)
     *
     * Do rearrangement in place by rearranging the data in each direction, separately. That is, first swap the
     * data in x-direction, then swap in y-direction, and finally swap in z-direction. By doing so, only a small
     * buffer, which caches temporary data for the swapping process is necessary.
     *
     * In contrast, getRearrangedCopyOfCubeData() rearranges cube data while creating a complete new copy of
     * the data. In cases, where a copy is not needed that is an enormous waste of memory. In addition,
     * rearrangeCubeData() is much faster than getRearrangedCopyOfCubeData().
     */
    public void rearrangeData()
    {
        if ( this.getData() == null )
           throw new IllegalStateException( "Data to process must be set first. Use method setData(). ");

        int halfDimXRounded = ( int ) Math.round( this.dataDimX / 2d );
        int halfDimYRounded = ( int ) Math.round( this.dataDimY / 2d );
        int halfDimZRounded = ( int ) Math.round( this.dataDimZ / 2d );

        ComplexNum[] buffer = new ComplexNum[ MyMathLib.max( this.dataDimX / 2, this.dataDimY / 2,
                                                                   this.dataDimZ / 2 ) ];

        // swap data in x-direction
        for	( int y = 0; y < this.dataDimY; y++ )
            for ( int z = 0; z < this.dataDimZ; z++ )
            {
                // cache first "half" to buffer
                for ( int x = 0; x < this.dataDimX / 2; x++ )
                    buffer[ x ] = this.data[ x ][ y ][ z ];
                // move second "half" to first "half"
                for ( int x = 0; x < halfDimXRounded; x++ )
                    this.data[ x ][ y ][ z ] = this.data[ x + this.dataDimX / 2 ][ y ][ z ];
                // move data in buffer to second "half"
                for ( int x = halfDimXRounded; x < this.dataDimX; x++ )
                    this.data[ x ][ y ][ z ] = buffer[ x - halfDimXRounded ];
            }

        // swap data in y-direction
        for	( int x = 0; x < this.dataDimX; x++ )
            for ( int z = 0; z < this.dataDimZ; z++ )
            {
                // cache first "half" to buffer
                for ( int y = 0; y < this.dataDimY / 2; y++ )
                    buffer[ y ] = this.data[ x ][ y ][ z ];
                // move second "half" to first "half"
                for ( int y = 0; y < halfDimYRounded; y++ )
                    this.data[ x ][ y ][ z ] = this.data[ x ][ y + this.dataDimY / 2 ][ z ];
                // move data in buffer to second "half"
                for ( int y = halfDimYRounded; y < this.dataDimY; y++ )
                    this.data[ x ][ y ][ z ] = buffer[ y - halfDimYRounded ];
            }

        // swap data in z-direction
        for	( int x = 0; x < this.dataDimX; x++ )
            for ( int y = 0; y < this.dataDimY; y++ )
            {
                // cache first "half" to buffer
                for ( int z = 0; z < this.dataDimZ / 2; z++ )
                    buffer[ z ] = this.data[ x ][ y ][ z ];
                // move second "half" to first "half"
                for ( int z = 0; z < halfDimZRounded; z++ )
                    this.data[ x ][ y ][ z ] = this.data[ x ][ y ][ z + this.dataDimZ / 2 ];
                // move data in buffer to second "half"
                for ( int z = halfDimZRounded; z < this.dataDimZ; z++ )
                    this.data[ x ][ y ][ z ] = buffer[ z - halfDimZRounded ];
            }

        this.setChanged();
    }


    /**
     * a b c d e   --> c d e a b
     * a b c d e f --> d e f a b c
     * (in all three dimensions)
     *
     * Rearrangement happens by simply creating a new array of references to complex numbers and assigning
     * references to the complex numbers associated with this object to the newly created array in a
     * rearranged manner.
     */
    public ComplexNum[][][] getRearrangedDataReferences()
    {
        if ( this.getData() == null )
           throw new IllegalStateException( "Data to process must be set first. Use method setData(). ");

        int halfDimXRounded = ( int ) Math.round( this.dataDimX / 2d );
        int halfDimYRounded = ( int ) Math.round( this.dataDimY / 2d );
        int halfDimZRounded = ( int ) Math.round( this.dataDimZ / 2d );

        ComplexNum[][][] rearrangedCopy = new ComplexNum[ this.dataDimX ][ this.dataDimY ][ this.dataDimZ ];

        for ( int x = 0; x < halfDimXRounded; x++ )
            for ( int y = 0; y < halfDimYRounded; y++ )
                for ( int z = 0; z < halfDimZRounded; z++ )
                    rearrangedCopy[ x ][ y ][ z ] = this.data[ x + this.dataDimX / 2 ][ y + this.dataDimY / 2 ]
                                                             [ z + this.dataDimZ / 2 ];

        for ( int x = halfDimXRounded; x < this.dataDimX; x++ )
            for ( int y = 0; y < halfDimYRounded; y++ )
                for ( int z = 0; z < halfDimZRounded; z++ )
                    rearrangedCopy[ x ][ y ][ z ] = this.data[ x - halfDimXRounded ][ y + this.dataDimY / 2 ]
                                                             [ z + this.dataDimZ / 2 ];

        for ( int x = 0; x < halfDimXRounded; x++ )
            for ( int y = halfDimYRounded; y < this.dataDimY; y++ )
                for ( int z = 0; z < halfDimZRounded; z++ )
                    rearrangedCopy[ x ][ y ][ z ] = this.data[ x + this.dataDimX / 2 ][ y - halfDimYRounded ]
                                                             [ z + this.dataDimZ / 2 ];

        for ( int x = halfDimXRounded; x < this.dataDimX; x++ )
            for ( int y = halfDimYRounded; y < this.dataDimY; y++ )
                for ( int z = 0; z < halfDimZRounded; z++ )
                    rearrangedCopy[ x ][ y ][ z ] = this.data[ x - halfDimXRounded ][ y - halfDimYRounded ]
                                                             [ z + this.dataDimZ / 2 ];

        for ( int x = 0; x < halfDimXRounded; x++ )
            for ( int y = 0; y < halfDimYRounded; y++ )
                for ( int z = halfDimZRounded; z < this.dataDimZ; z++ )
                    rearrangedCopy[ x ][ y ][ z ] = this.data[ x + this.dataDimX / 2 ][ y + this.dataDimY / 2 ]
                                                             [ z - halfDimZRounded ];

        for ( int x = halfDimXRounded; x < this.dataDimX; x++ )
            for ( int y = 0; y < halfDimYRounded; y++ )
                for ( int z = halfDimZRounded; z < this.dataDimZ; z++ )
                    rearrangedCopy[ x ][ y ][ z ] = this.data[ x - halfDimXRounded ][ y + this.dataDimY / 2 ]
                                                             [ z - halfDimZRounded ];

        for ( int x = 0; x < halfDimXRounded; x++ )
            for ( int y = halfDimYRounded; y < this.dataDimY; y++ )
                for ( int z = halfDimZRounded; z < this.dataDimZ; z++ )
                    rearrangedCopy[ x ][ y ][ z ] = this.data[ x + this.dataDimX / 2 ][ y - halfDimYRounded ]
                                                             [ z - halfDimZRounded ];

        for ( int x = halfDimXRounded; x < this.dataDimX; x++ )
            for ( int y = halfDimYRounded; y < this.dataDimY; y++ )
                for ( int z = halfDimZRounded; z < this.dataDimZ; z++ )
                    rearrangedCopy[ x ][ y ][ z ] = this.data[ x - halfDimXRounded ][ y - halfDimYRounded ]
                                                             [ z - halfDimZRounded ];

        return( rearrangedCopy );
    }


    public ImagePlus toImagePlus( ComplexValueType type )
    {
        return this.toImagePlus( type, FourierDomainOrigin.AT_ZERO );
    }


    public ImagePlus toImagePlus( ComplexValueType type, FourierDomainOrigin fdOrigin )
    {
        if ( this.getData() == null )
           throw new IllegalStateException( "Data to process must be set first. Use method setData(). ");

        ComplexNum[][][] data;
        if ( fdOrigin == FourierDomainOrigin.AT_CENTER )
           data = this.getRearrangedDataReferences();
        else
            data = this.getData();

        ImageStack stack = new ImageStack( this.dataDimX, this.dataDimY );
        for ( int z = 0; z < this.dataDimZ; z++ )
        {
            // output data is of FLOAT precision by default
            FloatProcessor slice = new FloatProcessor( this.dataDimX, this.dataDimY );

            for ( int x = 0; x < this.dataDimX; x++ )
                for ( int y = 0; y < this.dataDimY; y++ )
                    slice.putPixelValue( x, y, data[ x ][ y ][ z ].getValue( type ) );

            stack.addSlice( null, slice );
        }

        ImagePlus imp = new ImagePlus( type.toString() + " With Origin " + fdOrigin.toString() +
                                       " (" + this.getPrecision() + ")", stack );
        imp.changes = true;
        return imp;
    }


    private void FFT3D( Direction direction )
    {
        int nx = this.data.length;
        boolean nxPowerOf2 = isPowerOfTwo( nx );
        int ny = this.data[ 0 ].length;
        boolean nyPowerOf2 = isPowerOfTwo( ny );
        int nz = this.data[ 0 ][ 0 ].length;
        boolean nzPowerOf2 = isPowerOfTwo( nz );

        ComplexNum referenceBuffer = this.makeComplexNumber();

        ComplexNum[] objectBuffer = new ComplexNum[ MyMathLib.max( nx, ny, nz ) ];
        for ( int i = 0; i < objectBuffer.length; i++ )
            objectBuffer[ i ] = this.makeComplexNumber();

        ComplexNum[] temporarySource = new ComplexNum[ MyMathLib.max( nx, ny, nz ) ];

        for ( int z = 0; z < nz; z++ )
            for ( int y = 0; y < ny; y++ )
            {
                for ( int x = 0; x < nx; x++ )
                    temporarySource[ x ] = this.data[ x ][ y ][ z ];

                if ( nxPowerOf2 )
                   FFT1D( direction, temporarySource, nx, referenceBuffer );
                else
                    DFT1D( direction, temporarySource, nx, objectBuffer );

                for ( int x = 0; x < nx; x++ )
                    this.data[ x ][ y ][ z ] = temporarySource[ x ];
            }

        for ( int z = 0; z < nz; z++ )
            for ( int x = 0; x < nx; x++ )
            {
                for ( int y = 0; y < ny; y++ )
                    temporarySource[ y ] = this.data[ x ][ y ][ z ];

                if ( nyPowerOf2 )
                   FFT1D( direction, temporarySource, ny, referenceBuffer );
                else
                    DFT1D( direction, temporarySource, ny, objectBuffer );

                for ( int y = 0; y < ny; y++ )
                    this.data[ x ][ y ][ z ] = temporarySource[ y ];
            }

        for ( int y = 0; y < ny; y++ )
            for ( int x = 0; x < nx ; x++ )
                if ( nzPowerOf2 )
                   FFT1D( direction, this.data[ x ][ y ], nz, referenceBuffer );
                else
                    DFT1D( direction, this.data[ x ][ y ], nz, objectBuffer );

        this.setChanged();
    }


    private static boolean isPowerOfTwo( int value )
    {
        int i;
        for ( i = 2; i < value; i *= 2 );
        return ( i == value );
    }


    private static int calculateLog2( int length )
    {
        int log = 1;
        int i;
        for ( i = 2; i < length; i *= 2 )
            log++;

        if ( i != length )
           throw new IllegalArgumentException( "Argument 'length' is zero or not a power of 2." );

        return log;
    }


    // 'buffer' is expected to be an object of type ComplexNumber. In this method, the content of 'buffer'
    // will be changed.
    static private void FFT1D( Direction direction, ComplexNum[] source, int sourceLength, ComplexNum buffer )
    {
        int lengthLog2 = calculateLog2( sourceLength );

        long i2 = sourceLength >> 1;
        int j = 0;
        for ( int i = 0; i < sourceLength - 1; i++ )
        {
            if ( i < j )
            {
                ComplexNum temp = source[ i ];
                source[ i ] = source[ j ];
                source[ j ] = temp;
            }
            long k = i2;
            while( k <= j )
            {
                j -= k;
                k >>= 1;
            }
            j += k;
        }

        double c1 = -1, c2 = 0;
        long l2 = 1;
        for ( long l = 0; l < lengthLog2; l++ )
        {
            long l1 = l2;
            l2 <<= 1;
            double u1 = 1, u2 = 0;
            for ( j = 0; j < l1; j++ )
            {
                for ( int i = j; i < sourceLength; i += l2 )
                {
                    int i1 = ( int )( i + l1 );
                    buffer.setValue( u1 * source[ i1 ].getRealValue() - u2 * source[ i1 ].getImagValue(),
                                   u1 * source[ i1 ].getImagValue() + u2 * source[ i1 ].getRealValue() );
                    source[ i1 ].setValue( source[ i ].getRealValue() - buffer.getRealValue(),
                                           source[ i ].getImagValue() - buffer.getImagValue());
                    source[ i ].addValue( buffer );
                }
                double z = u1 * c1 - u2 * c2;
                u2 = u1 * c2 + u2 * c1;
                u1 = z;
            }
            c2 = Math.sqrt( ( 1 - c1 ) / 2d );
            if ( direction == FORWARD )
               c2 = - c2;
            c1 = Math.sqrt( ( 1 + c1 ) / 2d );
        }

        // scaling for forward transformation
        if ( direction == FORWARD )
           for ( int i = 0; i < sourceLength; i++ )
               source[ i ].divideByValue( sourceLength );
    }


    // 'buffer' is expected to be an array with existing objects of Type ComplexNumber associated with it,
    // which can be used as a temporary buffer.
    // The purpose of passing 'buffer' as an argument is to reduce frequent creation of temporary objects.
    static private void DFT1D( Direction direction, ComplexNum[] source, int sourceLength, ComplexNum[] buffer )
    {
        for ( int i = 0; i < sourceLength; i++ )
        {
            buffer[ i ].setValue( 0, 0 );
            double arg = - 2d * Math.PI * i / sourceLength;
            if ( direction == INVERSE )
               arg = - arg;
            for ( int k = 0; k < sourceLength; k++ )
            {
                double cosarg = Math.cos( k * arg );
                double sinarg = Math.sin( k * arg );
                buffer[ i ].addValue( source[ k ].getRealValue() * cosarg - source[ k ].getImagValue() * sinarg,
                                      source[ k ].getRealValue() * sinarg + source[ k ].getImagValue() * cosarg );
            }
        }

        // scaling for forward transformation
        if ( direction == FORWARD )
           for ( int i = 0; i < sourceLength; i++ )
              buffer[ i ].divideByValue( sourceLength );

        // Copy the data back
        for ( int i = 0; i < sourceLength; i++ )
            source[ i ].setValue( buffer[ i ] );
    }


    public static class Direction
    {
        private String name;
        private Direction( String name ) { this.name = name; }
        public String toString() { return this.name; }
    }
}