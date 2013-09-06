package fftj;

import ij.text.*;

/**
 * Title:        FFTJ --- A 3D FFT implementation for ImageJ
 * Description:
 * Copyright:    Copyright (c) 2001, 2002
 * Company:      Wake Forest University School of Medicine, Department of Medical Engineering
 *               Winston-Salem, North Carolina, USA
 * @author       Nick Linnenbrügger (nilin@web.de)
 */

public class DisclaimerWindow extends TextWindow
{
    private static String disclaimerMessage = "Calculations have been started. Please be patient ...\n\n\n" +
                                              "This software was produced under a contract from the\n" +
                                              "Wake Forest University School of Medicine supported\n" +
                                              "by a grant from the National Institutes of Health\n" +
                                              "(RO1 DE 12227). Both the application and source are\n" +
                                              "hereby made available to the scientific community for\n" +
                                              "research purposes with the explicit understanding that\n" +
                                              "Wake Forest University and National Institutes of Health\n" +
                                              "are absolved from any responsibility for consequences\n" +
                                              "associated with its use, its suitability for any intended\n" +
                                              "application, and the presumed integrity of its code.";

    public DisclaimerWindow()
    {
        super( "Disclaimer", disclaimerMessage, 350, 330 );
    }
}