# go to java directory
cd target/classes/

# use jni to make the wrapper
javah -jni com.truenorth.simpleyacudecuwrapper.YacuDecuWrapper

# copy to native directory
cp com_truenorth_simpleyacudecuwrapper_YacuDecuWrapper.h ../../../SimpleYacuDecuNative
