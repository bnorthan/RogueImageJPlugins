package fftj;

import java.util.*;

/**
 * Title:        FFTJ --- A 3D FFT implementation for ImageJ
 * Description:
 * Copyright:    Copyright (c) 2001, 2002
 * Company:      Wake Forest University School of Medicine, Department of Medical Engineering
 *               Winston-Salem, North Carolina, USA
 * @author       Nick Linnenbrügger (nilin@web.de)
 *
 * See http://w3.one.net/~jweirich/java/patterns/observer/javaobserver.html for more information.
 */

public class PublicObservable extends Observable
{
    public void setChanged() { super.setChanged(); }
}