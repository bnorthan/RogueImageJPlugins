
// duplicate image
run("Duplicate...", "title=rgbimage");
// split channels
run("Split Channels");
// Combine channels into a stack
run("Concatenate...", "  title=[Concatenated Stacks] image1=[rgbimage (red)] image2=[rgbimage (green)] image3=[rgbimage (blue)] image4=[-- None --]");
// run PCA
run("PCA ")