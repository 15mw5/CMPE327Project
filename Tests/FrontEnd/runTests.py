import os

#LGI folder
testSummary.write("################### LGI BEGIN #####################);
dir = Frontend/LGI/
inputfiles =  list of input files in dir
for(input in inputFiles ){

    #run java program with test input and output results into temp file
    os.system("java Login < "+ input + " > CCC_outtddd_testResults.txt");

    #read expected output file line by line compare with CCC_outtddd_testResultsput files
    expectedOut = open('CCC_outddd.txt');
    CCC_outtddd_testResults = open('CCC_outtddd_testResults.txt');
    testSummary = open('testSummaryFile.txt', 'w');
    expectedOut.readline();
    CCC_outtddd_testResults.readLine();
    if(expectedOut!=CCC_outtddd_testResults){
        testSummary.write("LGI ERROR: Failed Test: test " + input + "failed\n");
        testSummary.write("\t Expecting: " + expectedOutput + "\n");
        testSummary.write("\t Recieved: " + CCC_outtddd_testResults + "\n");
    }
testSummary.write("################### LGI END #####################);
}

#CRE folder
testSummary.write("################### CRE BEGIN #####################);
dir = Frontend/CRE/
inputfiles =  list of input files in dir
for(input in inputFiles ){

    #run java program with test input and output results into temp file
    os.system("java Login < "+ input + " > CCC_outtddd_testResults.txt");

    #read expected output file line by line compare with CCC_outtddd_testResultsput files
    expectedOut = open('CCC_outddd.txt');
    CCC_outtddd_testResults = open('CCC_outtddd_testResults.txt');
    testSummary = open('testSummaryFile.txt', 'w');
    expectedOut.readline();
    CCC_outtddd_testResults.readLine();
    if(expectedOut!=CCC_outtddd_testResults){
        testSummary.write("LGI ERROR: Failed Test: test " + input + "failed\n");
        testSummary.write("\t Expecting: " + expectedOutput + "\n");
        testSummary.write("\t Recieved: " + CCC_outtddd_testResults + "\n");
    }
testSummary.write("################### CRE END #####################);
}
