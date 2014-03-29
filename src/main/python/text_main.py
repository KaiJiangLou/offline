#! /usr/bin/python
# vim: set fileencoding=utf-8

import sys
import re
sys.path.append('/Users/king/Documents/WhatIHaveDone/Scripts/python')

from text_line_reader import *
from text_line_writer import *
#from text_preprocessor import *
global ALLNUM_RE, CH_EN
ALLNUM_RE = re.compile('^\d+$')
CH_EN = re.compile(ur"[^\u4e00-\u9fa5a-zA-Z]")

class TextFileReaderAndWriter:
    def __init__(self, lineReader):
        self.lineReader = lineReader
        self.lineWriter = TextLineWriter("\t", ",", ":")
        self.word2IdDict = {}
        self.word2FreqDict = {}

    def readFile(self, inputFileName, outputFileName):
        file = open(inputFileName)
        outputFile = open(outputFileName, 'w')
        resDict = {}
        for line in file:
            # The return should be like (uid, {word1:freq1, word2:freq2, ...}).
            lineResult = self.lineReader.readLine(line)
            if lineResult is not None:
                #resDict[lineResult[0]] = self._parseWords(lineResult[1])
                self.lineWriter.writeLine((lineResult[0], self._parseWords(lineResult[1])), outputFile)
        file.close()
        outputFile.close()
        return resDict

    def _parseWords(self, words, minWordLength=2):
        global CH_EN
        resultDict = {}
        for (word, freq) in words.iteritems():
            #word = word.strip()
            word = CH_EN.sub('', word.decode('utf-8'))
            #if not word:
            if len(word) < minWordLength:
                continue
            #start from index 1
            wordId = self.word2IdDict.setdefault(word, len(self.word2IdDict) + 1)
            resultDict[wordId] = freq
            #
            curVal = self.word2FreqDict.setdefault(word, 0)
            self.word2FreqDict[word] = curVal + 1
        return resultDict

    def saveWordToIdDict(self, fileName):
        self.saveDict(self.word2IdDict, fileName, False)

    def saveWordToFreqDict(self, fileName):
        self.saveDict(self.word2FreqDict, fileName, True)

    def saveDict(self, theDict, fileName, isRev = False):
        file = open(fileName, 'w')
        sortedList = theDict.items()
        sortedList.sort(key=lambda x: x[1], reverse=isRev)
        for (toid, num) in sortedList:
            file.write(str(toid.encode('utf-8')) + '\t' + str(num) + '\n')
        file.close()


def cutNumDict(numDict, lowerThresholdNums = 0, upperThresholdNums = sys.maxint):
    newDict = {}
    for (toid, num) in numDict.iteritems():
        if num >= lowerThresholdNums and num <= upperThresholdNums:
            newDict[toid] = len(newDict) + 1 # index starts from 1, NOT 0
    return newDict

def saveToFileAfterCut(vectorDict, itemDict, fileName, ratingThreshold = 3):
    file = open(fileName, 'w')
    for (uid, items) in vectorDict.iteritems():
        count = 0
        line = str(uid) + '\t'
        for (toid, val) in items.iteritems():
            if toid not in itemDict or val[0] < ratingThreshold:
                continue
            line += str(itemDict[toid]) + ':' + str(val[0]) + ','
            count += 1
        if count > 0:
            file.write(line[:-1] + '\n')
    file.close()



if __name__ == "__main__":
    if len(sys.argv) != 3:
            print "Usage:", sys.argv[0], "<input_file> <output_file>"
            sys.exit(-1)
    inputFileName = sys.argv[1]
    outputFileName = sys.argv[2]
    numFileName = outputFileName+".num"

    #regex = re.compile(ur"[^\u4e00-\u9fa5a-zA-Z]")
    #line = ':,移动,云,应用,-,企业,演讲,用户,开发者,IBM,数据,如何,计算,产品,互联网,数据中心,业务,解决方案,会议,的 ,服务,实现,运营,Unity,时代,您,：,分享,—,行业,更,创新,新,客户,腾讯,分析,技术,开放平台,管理,社交,签到,主题,总经理,CEO,百度,案例,议题,天翼,GPU,•,22,#,~,  , '
    #print regex.sub('', line.decode('utf-8'))
    #sys.exit(1)

    lineReader = TextLineReader("\t", "|")
    fileReader = TextFileReaderAndWriter(lineReader)
    fileReader.readFile(inputFileName, outputFileName)
    fileReader.saveWordToFreqDict(outputFileName + ".wordfreq")
    fileReader.saveWordToIdDict(outputFileName + ".dict")
    ###
    #ratingThreshold = 4
    #numDict = getAllItemIds(vectorDict, ratingThreshold)
    #saveDict(numDict, numFileName, True)
    #lowerThresholdNums = 5
    #upperThresholdNums = int(len(vectorDict) * 0.05)
    #print "lowerThresholdNums =", lowerThresholdNums, ", and upperThresholdNums =", upperThresholdNums
    #itemDict = cutNumDict(numDict, lowerThresholdNums, upperThresholdNums)
    #print "Number of items after cut is ", len(itemDict)
    #saveDict(itemDict, outputFileName+".dict", False)
    ###
    #print "ratingThreshold = ", ratingThreshold
    #saveToFileAfterCut(vectorDict, itemDict, outputFileName, ratingThreshold)


