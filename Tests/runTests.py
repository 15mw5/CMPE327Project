import os
import datetime
date = datetime.datetime.today().strftime('%Y_%m_%d')
passMessage = " test case passed"
failMessage = " test case failed"
########### FUNCTIONS ##########
def invert_dict(d):
    #Takes a dictionary d and inverts it into a a histogram
    #(i.e a dictionarycomprised of lists such that key's mapping
    #to the same value in the original dictionary d are placed
    #in the same list.
    #EXAMPLE {a:0,b:0,c:1,d:2,e:1} -> {0:[a,b],1:[c,e],2:[d]}
    inverse = dict()
    for key in d:
        val = d[key]
        if val not in inverse:
            inverse[val] = [key]
        else:
            inverse[val].append(key)
    return inverse

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
    testSummaryDict = dict() #Used to record passed tests and failed tests
    for inputFileName, outputFileName in zip(inputFiles,outputFiles):
        key = inputFileName
        if(inputFileName[4:7]!=outputFileName[4:7]):
            raise Exception("ERROR: Input file name:\n\t\t" +inputFileName+"\ndoes not match output file name:\n\t\t" +outputFileName)

        #run Java file with test case input file and store results in temp file
        inputFilePath = os.path.join(testFilesPath,inputFileName)
        os.system("java -classpath " + "java -cp ../Source/bin frontEnd.frontEndSystem < " + inputFilePath + " > tempOutput.txt") 

        #open the test case's expected output file and compare to results in temp file
        outputFilePath = os.path.join(testFilesPath,outputFileName)
        outputFile = open(outputFilePath)
        tempOutputFile = open('tempOutput.txt')##WHAT FILE SHOULD I BE READING

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

    #Preparartion to write out summary of test into file
    #If the directory for the current test date does not exit initalize
    #it before makeing the summarry file. SUmmary files are nameed
    #consecuitively based on the number of files currently in the test
    #date folder
    resultFilesPath = testFilesPath+'/'+code+'_ResultFiles'
    directory = resultFilesPath + '/'+ code+'_'+date
    if not os.path.exists(directory):
        os.makedirs(directory)
    numfiles = os.listdir(directory)
    num = sum(code in file for file in numfiles) + 1
    dig = '%03d' % num
    resultFile = open(directory+'/'+code+'_testResult'+dig+'.txt', 'w')
    resultFile.write("********************** "+date+" TEST CASES **********************\n\n")

    #Once file is made sort the the test results and write them into the summary file
    #passFailSummary is a dictionary of form {fail: testCaseNames[String], pass: testCaseNames[String],}
    passFailSummary = invert_dict(testSummaryDict)
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
    print("Testing done. Results stored in file: "+code+'_testResult'+dig+'.txt')

########### MAIN CODE ##########
testDirectories = []
for code in os.listdir('FrontEnd'):
    if os.path.isdir('FrontEnd/'+code):
        testDirectories.append(code);
decision = input("Would you like to test one or all transactions?\nPress (1) for one transaction (2) for all transactions or (q) to quit\n")
while decision != str(1) and decision != str(2) and decision != "q":
    print("You entered \n" + decision)
    print("That is not a valid entry, please try again using one of the following options")
    decision = input("Would you like to test one or all transactions?\nPress (1) for one transaction (2) for all transactions or (q) to quit\n")

if decision == "q":
    exit()
elif decision == str(2):
    #User opted to test all transactions. Systematically iterating through all folders
    for code in testDirectories:
        runTransactionTest(code)
elif decision == str(1):
    #This means the user opted to only test one transaction. Need user to specifiy which one 
    print("Which transaction do you want to test? \nEnter one of the transaction codes or press (q) to quit:")
    i=0;
    for code in testDirectories:
        print(code + "\t", end="")
        i=i+1
        if i%3 == 0:
            print("")
    code = input("\n")
    while code not in testDirectories and code != "q":
        print("You entered \n" + code)
        print("That is not a valid entry, please try again using one of the following options or press (q) to quit")
        i=0
        for code in testDirectories:
            print(code + "\t", end="")
            i=i+1
            if i%3 == 0:
                print("")
        code = input("\n")
    if code == "q":
        exit()
    else:
        runTransactionTest(code)
     
