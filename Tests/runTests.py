import os
passMessage = " test case passed"
failMessage = " test case failed"
testSummaryDict = dict()
########### FUNCTIONS ##########
def invert_dict(d):
    inverse = dict()
    for key in d:
        print(key)
        val = d[key]
        print(val)
        if val not in inverse:
            inverse[val] = [key]
        else:
            inverse[val].append(key)
    return inverse

########### MAIN CODE ##########
testDirectories = []
for code in os.listdir('FrontEnd'):
    if os.path.isdir('FrontEnd/'+code):
        testDirectories.append(code);

def runTransactionTest(code):
    testFilesPath = 'FrontEnd/' + code
    #Get A list of the input file names and the expected output file names
    inputFiles = []
    outputFiles =  []
    for file in os.listdir(testFilesPath):
        if 'input.txt' in file:
            inputFiles.append(file)
        elif 'output.txt' in file:
            outputFiles.append(file)

    print(inputFiles)
    print(outputFiles)
    for inputFileName, outputFileName in zip(inputFiles,outputFiles):
        key = inputFileName
        if(inputFileName[4:7]!=outputFileName[4:7]):
            raise Exception("ERROR: Input file name:\n\t\t" +inputFileName+"\ndoes not match output file name:\n\t\t" +outputFileName)

        #run Java file with test case input file and store results in temp file
        inputFilePath = os.path.join(testFilesPath,inputFileName)
        os.system("java -classpath " + "../Source/bin" + " CRE_testClass < " + inputFilePath + " > tempOutput.txt") 

        #open the test case's expected output file and compare to results in temp file
        outputFilePath = os.path.join(testFilesPath,outputFileName)
        outputFile = open(outputFilePath)
        tempOutputFile = open('tempOutput.txt')

        outputFileContents = outputFile.read()
        outputFileContents = outputFileContents.strip() #does not get rid of \n in middle of files, only ends
    
        tempOutputFileContents = tempOutputFile.read()
        tempOutputFileContents = tempOutputFileContents.strip()      
        #If the file contents above are the same, the test passed else it failed. Store result with pass/fail message
        if outputFileContents == tempOutputFileContents:
            testSummaryDict[inputFileName[:len(inputFileName)-4]+passMessage] = "pass"
        else:
            message = inputFileName[:len(inputFileName)-4]+failMessage + "\n   Expected out was\n\t"+ outputFileContents+ "\n   Test Output was\n\t"+ tempOutputFileContents 
            testSummaryDict[message] = "fail"

    #Sort the the test results and write into the summary file
    #passFailSummary is a dictionar of form {fail: testCaseNames[String], pass: testCaseNames[String],}
    passFailSummary = invert_dict(testSummaryDict)
    resultFile = open('FrontEnd/'+code+'/'+code+'_ResultFiles/'+code+'_testResult.txt', 'w')
    resultFile.write("============== FAILED TEST CASES ==============\n")
    if 'fail' in passFailSummary:
        failedTests = passFailSummary['fail'] 
        failedTests.sort()
        for result in failedTests:
            resultFile.write(result+'\n')
    else:
        resultFile.write('NO FAILED TESTS\n')

    resultFile.write("============== PASSED TEST CASES ==============\n")
    if 'pass' in passFailSummary:
        passedTests = passFailSummary['pass']
        passedTests.sort()
        for result in passedTests:
            resultFile.write(result+'\n')
    else:
        resultFile.write('ALL TESTS FAILED\n')

    resultFile.close()

runTransactionTest("CRE")
