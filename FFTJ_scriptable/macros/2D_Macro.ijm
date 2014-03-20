// Example Script showing how to generate the power spectrum of each line in an image

// !!!!!! change this to local directory with the input image !!!!!!
inputDirectory="/home/bnorthan/Brian2014/Projects/RogueImageJPlugins/FFTJ_scriptable/test images/";

// !!!!!! change this to local directory !!!!!!!!
outputDirectory="/home/bnorthan/Brian2014/Projects/Case Studies/FFTJ Power Spectrum/spectrums/";

// name of input image
inputName="particles.tif";

// base name for outputs
baseName="horizontalProfile";

// open the image
open(inputDirectory+inputName);

// get the size of the image
height=getHeight();
width=getWidth();

print(height);
print(width);

// loop through all rows
for (h=0;h<height;h++)
{
	// make an ROI around the current row
	makeRectangle(0, h, width, 1);
	// crop the current row
	run("Crop");
	
	// name and save the current row
	profileName=baseName+h+".tif";
	saveAs("Tiff", outputDirectory+profileName);

	// run the scriptable version of FFTJ showing the power spectrum
	run("FFTJ Script", "real="+profileName+" imaginary=<none> complex344=[Single Precision] fft=forward fourier=[At (0,0,0)] show_power_spectrum");

	// name and save the power spectrum	
	spectrumName=baseName+h+"powerSpectrum"+".tif";
	saveAs("Tiff", outputDirectory+spectrumName);

	// close the current spectrum
	close(profileName);
	close(spectrumName);

	// open and focus the input image again so it is available next time through the loop
	open(inputDirectory+inputName);
	selectWindow(inputName);
}

