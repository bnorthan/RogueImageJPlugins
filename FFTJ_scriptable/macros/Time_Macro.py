# Example Script showing how to generate the power spectrum of each pixel in an image through time

from fftj import SinglePrecFFT3D
from fftj import ComplexValueType
from fftj import FourierDomainOrigin

# !!!!!! change this to local directory with the input image !!!!!!
inputDirectory="/home/bnorthan/Brian2014/Projects/RogueImageJPlugins/FFTJ_scriptable/test images/";

# !!!!!! change this to local directory !!!!!!!!
outputDirectory="/home/bnorthan/Brian2014/Projects/Case Studies/FFTJ Power Spectrum/spectrums/";

# name of input image
inputName="FakeTracks_32.tif";

outputName="Spectrums.tif";

# open the image
IJ.open(inputDirectory+inputName);

# Get current ImagePlus
ip = WindowManager.getCurrentImage()
stack = ip.getStack()

width = ip.getWidth();
height = ip.getHeight();
frames = ip.getNFrames();
slices = ip.getNSlices();

frames=max(frames, slices);
bitdepth = ip.getBitDepth();

print(width);
print(height);
print(frames);
print(bitdepth);

out_stack=ImageStack.create(width, height, frames, 32)
out_plus=ImagePlus("Power Spectrum", out_stack) 

# create imageplus to store time profiles
if bitdepth == 8:
	timeprocessor = ByteProcessor(frames, 1)
elif bitdepth == 16:
	timeprocessor = ShortProcessor(frames, 1)
elif bitdepth == 32:
	timeprocessor = FloatProcessor(frames, 1)

timeprofile=ImagePlus("TimeProfile", timeprocessor)
timeprofile_pix=timeprocessor.getPixels();

IJ.log("running")

for x in range(0,width):
	for y in range(0,height):
		IJ.log( "x is "+str(x)+ " y is "+str(y))

		# loop through time collecting pixels at x,y and form profile
		for t in range(1,frames+1):
			pix=stack.getProcessor(t).getPixels()
			timeprofile_pix[t-1]=pix[x+y*width]

		# call FFTJ on profile

		# below code is a hack -- calls plugin through IJ "run" interface.  VERY SLOW. 
		#IJ.run("FFTJ Script", "real=TimeProfile imaginary=<none> complex344=[Single Precision] fft=forward fourier=[At (0,0,0)] show_power_spectrum")
		#spectrum_plus=IJ.getImage()
		#spectrum_pix=spectrum_plus.getProcessor().getPixels()

		# new code starts here.  Use SinglePrecFFT3D interface directly
		transformer= SinglePrecFFT3D(timeprofile.getStack(), None)
		transformer.fft()

		spectrum_plus=transformer.toImagePlus(ComplexValueType.POWER_SPECTRUM, FourierDomainOrigin.AT_ZERO );
		spectrum_pix=spectrum_plus.getProcessor().getPixels()
		
		# copy profile to output time series
		for t in range(1, frames+1):
			pix=out_stack.getProcessor(t).getPixels()
			pix[x+y*width]=spectrum_pix[t-1]


out_plus.show()
		