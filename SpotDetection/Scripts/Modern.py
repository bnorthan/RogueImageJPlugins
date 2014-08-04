# @DatasetService data
# @DisplayService display
# @IOService io
# @OpService opsi

from ij import IJ
from net.imglib2.view import Views

print data
print display
#print io
print opsi
'''
# define a local directory to get the images from
directory="/home/bnorthan/Brian2014/Projects/RogueImageJPlugins/Case Studies/Segmentation For Judith/Images/"

imageName="B013-D0-L-UV_cropped2.tif"

# open the image
image=data.open(directory+imageName)
display.createDisplay(image.getName(), image);	

for d in range(0, image.numDimensions()):
	print image.axis(d).type()

imgPlus=image.getImgPlus()
img=imgPlus.getImg()

IJ.run("HSB Stack");

for d in range(0, image.numDimensions()):
	print image.axis(d).type()
	imgslice=Views.hyperSlice(img, 2, d)
	print type(imgslice) 
	iterable=Views.iterable(imgslice)
	print type(iterable)
#	print ops.mean(iterable)

print image.numDimensions()'''