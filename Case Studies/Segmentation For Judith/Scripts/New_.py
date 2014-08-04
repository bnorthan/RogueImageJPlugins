IJ.run("Convert Stack to Images");

mins=[172, 0, 90]
maxs=[224, 255, 255]
names=["Hue", "Saturation", "Brightness"]

for i in range(0,3):
  	IJ.selectWindow(names[i])
  	IJ.setThreshold(mins[i], maxs[i])
  	print "test"
  	IJ.run("Convert to Mask")

calc = ImageCalculator()

img1=WindowManager.getImage(names[0])
img2=WindowManager.getImage(names[1])
img3=WindowManager.getImage(names[2])

temp1=calc.run("AND create", img1, img2)
result=calc.run("AND create", temp1, img3) 
result.show()

for i in range(0,3):
	win=WindowManager.getWindow(names[i])
	win.removeNotify()
#IJ.renameResult("test")

table=ResultsTable()

roim = RoiManager(True)

pa = ParticleAnalyzer(ParticleAnalyzer.ADD_TO_MANAGER, Measurements.AREA, table, 0, Double.POSITIVE_INFINITY, 0.0, 1.0)
pa.setHideOutputImage(True)
 
if pa.analyze(result):
  print "All ok"
else:
  print "There was a problem in analyzing", blobs
 
# The measured areas are listed in the first column of the results table, as a float array:
areas = table.getColumn(0)