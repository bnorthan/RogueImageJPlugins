table=ResultsTable()

*# Create a hidden ROI manager, to store a ROI for each blob or cell
roim = RoiManager(True)
# Create a ParticleAnalyzer, with arguments:
# 1. options (could be SHOW_ROI_MASKS, SHOW_OUTLINES, SHOW_MASKS, SHOW_NONE, ADD_TO_MANAGER, and others; combined with bitwise-or)
# 2. measurement options (see [http://rsb.info.nih.gov/ij/developer/api/ij/measure/Measurements.html Measurements])
# 3. a ResultsTable to store the measurements
# 4. The minimum size of a particle to consider for measurement
# 5. The maximum size (idem)
# 6. The minimum circularity of a particle
# 7. The maximum circularity
pa = ParticleAnalyzer(ParticleAnalyzer.ADD_TO_MANAGER, Measurements.AREA, table, 0, Double.POSITIVE_INFINITY, 0.0, 1.0)
pa.setHideOutputImage(True)
 
if pa.analyze(imp):
  print "All ok"
else:
  print "There was a problem in analyzing", blobs
 
# The measured areas are listed in the first column of the results table, as a float array:
areas = table.getColumn(0)


table=new ResultsTable();
