# Example Script showing how to generate the power spectrum of each pixel in an image through time

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

# duplicate and convert to 32-bit to form output
IJ.run("Duplicate...", "title=Spectrum.tif duplicate range=1-"+str(frames));
IJ.run("32-bit");

out_plus=WindowManager.getCurrentImage()
out_stack= out_plus.getStack()

# create imageplus to store time profiles
if bitdepth == 8:
	timeprocessor = ByteProcessor(frames, 1)
elif bitdepth == 16:
	timeprocessor = ShortProcessor(frames, 1)
elif bitdepth == 32:
	timeprocessor = FloatProcessor(frames, 1)

timeprofile=ImagePlus("TimeProfile", timeprocessor)
timeprofile.show()

timeprofile_pix=timeprocessor.getPixels();

IJ.log("running")

for x in range(0,10):
	for y in range(0,10):
		progress=(x*5+y)/25.0
		IJ.showProgress(progress)
		#IJ.log( "x is "+str(x)+ " y is "+str(y))
		
		for t in range(1,frames+1):
			pix=stack.getProcessor(t).getPixels()
			timeprofile_pix[t-1]=pix[x+y*width]

		IJ.run("FFTJ Script", "real=TimeProfile imaginary=<none> complex344=[Single Precision] fft=forward fourier=[At (0,0,0)] show_power_spectrum")
		spectrum_plus=IJ.getImage()
		spectrum_pix=spectrum_plus.getProcessor().getPixels()

		for t in range(1, frames+1):
			pix=out_stack.getProcessor(t).getPixels()
			pix[x+y*width]=spectrum_pix[t-1]

		spectrum_plus.changes=False
		spectrum_plus.close()

out_plus.updateAndDraw()
		