
Stack.getStatistics(count, mean, min, max, std);
   print("count="+count+", mean="+mean+", min="+min+", max="+max+",std="+std);

factor1=65535/max

run("Multiply...", "value="+factor1);
run("16-bit");