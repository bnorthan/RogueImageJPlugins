from ij import IJ
from ij import WindowManager
from ij import ImagePlus
from ij.plugin import ImageCalculator
from java.lang import Math
from ij.process import FloatProcessor
from ij.plugin.filter import ParticleAnalyzer
from ij.measure import Measurements
from ij.measure import ResultsTable
from ij.plugin.frame import RoiManager
from java.lang import Double

w = WindowManager 

inputImage='B013-D0-L-UV_cropped2.tif'
#IJ.selectWindow(inputImage)

title="pore_image";
IJ.run("Duplicate...", "title="+title)

IJ.run("RGB Stack");
IJ.selectWindow("pore_image")

IJ.run("Convert Stack to Images");

win = w.getWindow("Blue") 
win.removeNotify()

IJ.selectWindow("Red")
IJ.run("16-bit");
red=IJ.getImage()

IJ.selectWindow("Green")
IJ.run("16-bit")
green=IJ.getImage()

calc = ImageCalculator()
calc.calculate("Add create", red,green);

IJ.run("Subtract Background...", "rolling=50");
IJ.run("LoG 3D");

imp = IJ.getImage()
pix = imp.getProcessor().convertToFloat().getPixels()
 
# find out the minimal pixel value
min = reduce(Math.min, pix)
 
# create a new pixel array with the minimal value subtracted
pix2 = map(lambda x: x - min, pix)

name="log"
ImagePlus(name, FloatProcessor(imp.width, imp.height, pix2, None)).show()
IJ.run("16-bit");

IJ.selectWindow(name)
IJ.run("16-bit")
IJ.run("Auto Threshold", "method=Otsu");

'''IJ.run("Make Binary");

table = ResultsTable()
# Create a hidden ROI manager, to store a ROI for each blob or cell
roim = RoiManager(True)
pa = ParticleAnalyzer(ParticleAnalyzer.ADD_TO_MANAGER, Measurements.AREA, table, 0, Double.POSITIVE_INFINITY, 0.0, 1.0)

print len(table)'''
