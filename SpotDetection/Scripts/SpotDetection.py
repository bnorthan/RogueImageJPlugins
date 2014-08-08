# @DatasetService data
# @DisplayService display
# @net.imagej.ops.OpService ops

from ij import IJ
from ij import ImagePlus
from net.imglib2 import FinalInterval
from jarray import zeros
from jarray import array
from net.imglib2.type.numeric.real import FloatType
from net.imglib2.type.numeric.integer import UnsignedShortType
from net.imglib2.type.logic import BitType

from net.imagej.ops.convert import ConvertPixCopy
from net.imglib2.meta import ImgPlus
from net.imglib2.img.display.imagej import ImageJFunctions
from ij.plugin.filter import BackgroundSubtracter
from fiji.plugin.trackmate.detection import DetectionUtils
from net.imagej.ops.threshold import Otsu

inputDirectory='/home/bnorthan/Brian2014/Projects/RogueImageJPlugins/SpotDetection/Images/'
inputName='B013-D0-L-UV_cropped2.tif'

dataset=data.open(inputDirectory+inputName)
display.createDisplay(dataset.getName(), dataset)

dimensions2D=array( [dataset.dimension(0), dataset.dimension(1)], 'l')
cropIntervalRed=FinalInterval( array([0,0,0], 'l'), array([dataset.dimension(0)-1,dataset.dimension(1)-1,0],'l') )
cropIntervalGreen=FinalInterval( array([0,0,1], 'l'), array([dataset.dimension(0)-1,dataset.dimension(1)-1,1],'l') )

red=ops.crop(cropIntervalRed, None, dataset.getImgPlus() ) 
green=ops.crop(cropIntervalGreen, None, dataset.getImgPlus() ) 

display.createDisplay("red", data.create(red))
display.createDisplay("green", data.create(green))

red32=ImgPlus( ops.create( dimensions2D, FloatType()) )
ops.convert(red32, red, ConvertPixCopy() )

green32=ImgPlus( ops.create( dimensions2D, FloatType()) )
ops.convert(green32, green, ConvertPixCopy() )

redgreen= ops.add(red32,green32)
display.createDisplay("redgreen", data.create(redgreen))

# make a copy of the red + green image
copy=redgreen.copy()
# wrap as ImagePlus
imp=ImageJFunctions.wrap(copy, "wrapped")

# create and call background subtractor
bgs=BackgroundSubtracter()
bgs.rollingBallBackground(imp.getProcessor(), 50.0, False, False, True, True, True) 

# wrap as Img and display
iplus=ImagePlus("bgs", imp.getProcessor())
print type(imp)
imgBgs=ImageJFunctions.wrapFloat(iplus)
display.createDisplay("back_sub", data.create(ImgPlus(imgBgs))) 

kernel = DetectionUtils.createLoGKernel( 3.0, 2, array([1.0, 1.0], 'd' ) )

print type(kernel)
print type(imgBgs)
print type(red32.getImg())

log = ops.convolve(ops.create( dimensions2D, FloatType()), imgBgs, kernel)
display.createDisplay("log", data.create(ImgPlus(log)))

otsu=ops.run("threshold", ops.create( dimensions2D, BitType()), log, Otsu())

display.createDisplay("thresholded", data.create(ImgPlus(otsu)))

#IJ.run("LoG 3D");

#IJ.run("Duplicate...", "title="+"test")
#IJ.run("RGB Stack");
#IJ.run("Convert Stack to Images");