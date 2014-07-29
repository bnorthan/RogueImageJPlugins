title="pore_image";

IJ.run("Duplicate...", "title="+title);
IJ.run("HSB Stack");
IJ.run("Convert Stack to Images");

# rename images
IJ.selectWindow("Hue");
IJ.renameResults("0");
IJ.selectWindow("Saturation");
IJ.renameResults("1");
IJ.selectWindow("Brightness");
IJ.renameResults("2");

mins=[172, 0, 90]
maxs=[224, 255, 255]
names=["Hue", "Saturation", "Brightness"]

for i in range(0,3):
  	IJ.selectWindow(names[i])
  	IJ.setThreshold(mins[i], maxs[i])
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