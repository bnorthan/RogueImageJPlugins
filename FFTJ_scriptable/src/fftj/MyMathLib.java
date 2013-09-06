package fftj;

/**
 * Title:        FFTJ --- A 3D FFT implementation for ImageJ
 * Description:
 * Copyright:    Copyright (c) 2001, 2002
 * Company:      Wake Forest University School of Medicine, Department of Medical Engineering
 *               Winston-Salem, North Carolina, USA
 * @author       Nick Linnenbrügger (nilin@web.de)
 */

public abstract class MyMathLib
{
    public static double log( double argument, double base )
    {
        return Math.log( argument ) / Math.log ( base );
    }

    public static long nextHigherPower2Number( long number )
    {
        if ( number < 1 )
           throw new IllegalArgumentException( "Argument must be greater or equal 1." );

        return ( long ) Math.pow( 2, Math.ceil( log( number, 2 ) ) );
    }

    public static int nextHigherPower2Number( int number )
    {
        return ( int ) nextHigherPower2Number( ( long ) number );
    }

    public static long nextSmallerPower2Number( long number )
    {
        if ( number < 1 )
           throw new IllegalArgumentException( "Argument must be greater or equal 1." );

        return ( long ) Math.pow( 2, Math.floor( log( number, 2 ) ) );
    }

    public static int nextSmallerPower2Number( int number )
    {
        return ( int ) nextSmallerPower2Number( ( long ) number );
    }

    public static double max( double a, double b, double c )
    {
        return Math.max( Math.max( a, b ), c );
    }

    public static float max( float a, float b, float c )
    {
        return Math.max( Math.max( a, b ), c );
    }

    public static long max( long a, long b, long c )
    {
        return Math.max( Math.max( a, b ), c );
    }

    public static int max( int a, int b, int c )
    {
        return Math.max( Math.max( a, b ), c );
    }

    public static double min( double a, double b, double c )
    {
        return Math.min( Math.min( a, b ), c );
    }

    public static float min( float a, float b, float c )
    {
        return Math.min( Math.min( a, b ), c );
    }

    public static long min( long a, long b, long c )
    {
        return Math.min( Math.min( a, b ), c );
    }

    public static int min( int a, int b, int c )
    {
        return Math.min( Math.min( a, b ), c );
    }
}