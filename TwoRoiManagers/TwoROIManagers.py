from ij.measure import ResultsTable
from ij.measure import Measurements
from ij.plugin.frame import RoiManager
from ij.plugin.filter import ParticleAnalyzer
from ij import IJ

# get active image
imp=IJ.getImage()

# set up first ROI manager
table1 = ResultsTable()
roim1=RoiManager()
pa1 = ParticleAnalyzer(ParticleAnalyzer.ADD_TO_MANAGER, Measurements.AREA|Measurements.MEAN|Measurements.ELLIPSE, table1, 0, 100, 0, 1)
pa1.setRoiManager(roim1)
pa1.analyze(imp)

# set up second ROI manager
table2 = ResultsTable()
# Pass true to second ROI manager so it will not be seen
roim2=RoiManager(True)
pa2 = ParticleAnalyzer(ParticleAnalyzer.ADD_TO_MANAGER, Measurements.AREA|Measurements.MEAN|Measurements.ELLIPSE, table2, 100, 500, 0, 1)
pa2.setRoiManager(roim2)
pa2.analyze(imp)

print "rois from first manager:"
for roi in roim1.getRoisAsArray(): print roi

print 
print "rois from second manager:"
for roi in roim2.getRoisAsArray(): print roi


