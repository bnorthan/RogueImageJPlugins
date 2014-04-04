// Example Script showing how to generate the power spectrum of each pixel in an image through time

// !!!!!! change this to local directory with the input image !!!!!!
inputDirectory="/home/bnorthan/Brian2014/Projects/RogueImageJPlugins/FFTJ_scriptable/test images/";

// !!!!!! change this to local directory !!!!!!!!
outputDirectory="/home/bnorthan/Brian2014/Projects/Case Studies/FFTJ Power Spectrum/spectrums/";

// name of input image
inputName="FakeTracks.tif";

outputName="Spectrums.tif";

// open the image
open(inputDirectory+inputName);

// get the size of the image
Stack.getDimensions(width, height, channels, slices, frames); 

bitdepth=bitDepth();

print(width);
print(height);
print(channels);
print(slices);
print(frames);
print(bitdepth);

if (bitdepth=8)
{
	print("8-bit"(;
	type="8-bit";  
}
else if (bitdepth=16)
{
	print("16-bit");
	type="16-bit";
}
else if (bitdepth=32)
{
	print("32-bit");
	type="32-bit";	
}

// create a temp image to store the profiles in
newImage("temp", type, width, height, depth)
		

// duplicate the input to create the output
run("Duplicate...", "title=FakeTracks-1.tif duplicate range=1-"+frames);
saveAs("Tiff", outputDirectory+outputName);

// refocus the input
selectWindow(inputName);

// loop through all collumns and rows

baseName="profile";

for (x=0;x<20;x++)
{
	for (y=0;y<20;y++)
	{
		
		// duplicate the time series
		run("Duplicate...", "title=FakeTracks-1.tif duplicate range=1-"+frames);
		
		// make an ROI around the current pixel
		makeRectangle(x, y, 1, 1);
		
		// crop to get the profile
		run("Crop");
		cropID=getImageID();

		// reslice and rotate so it is a horizontal profile 
		run("Reslice [/]...", "output=1.000 start=Top rotate avoid");

		// name and save the current
		profileName=getTitle();//baseName+x+y+".tif";
		//saveAs("Tiff", outputDirectory+profileName);

		// run the scriptable version of FFTJ showing the power spectrum
		run("FFTJ Script", "real="+profileName+" imaginary=<none> complex344=[Single Precision] fft=forward fourier=[At (0,0,0)] show_power_spectrum");

		// name and save the power spectrum	
		//spectrumName=baseName+x+y+"powerSpectrum"+".tif";
		//saveAs("Tiff", outputDirectory+spectrumName);
		spectrumID=getImageId();

		// copy spectrum to output
		for (t=1;t<=frames;t++)
		{
			selectImage(spectrumID);
			//selectWindow(spectrumName);
			value=getPixel(0,t-1);
			
			selectWindow(outputName);
			Stack.setFrame(t);
			setPixel(x, y, value);
		}

		// close profile that was created and spectrum
		close(profileName);
		close(spectrumName);

		selectImage(cropID);
		close();
		
		// open and focus the input image again so it is available next time through the loop
		//open(inputDirectory+inputName);
		selectWindow(inputName);
	}
}

