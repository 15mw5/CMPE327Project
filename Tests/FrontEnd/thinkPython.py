import os
cwd = os.getcwd()
print(cwd)
fin = open('words.txt')
fin. readline()

def histogram(s):
    d = dict()
    for i in s:
        temp = d.get(i,0)
        temp+=1
        d[i] = temp
    return d

dictionary = histogram("primrose")
print(dictionary)

def invertD(d):
    newD = dict()
    for k in d:
        temp = d[k]
        myList = newD.get(temp,[])
        myList.append(k)
        newD[temp] = myList
    return newD

rev = invertD(dictionary)
print(rev)

