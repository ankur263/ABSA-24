from xml.dom import minidom
from nltk.stem import PorterStemmer
import string
import re

def find_ngrams(inp_list, n):
   return zip(*[inp_list[i:] for i in range(n)])

stemmer = PorterStemmer()

xmldoc = minidom.parse('../ABSA15_RestaurantsTrain/YelpRests.xml')

tList = xmldoc.getElementsByTagName('text')

print len(tList)
#fo = open('RestReviews.txt','w')

ngrams = {}

LEN = 0

for text in tList:
   review = text.childNodes[0].nodeValue
   tokens=[e.lower() for e in map(string.strip, re.split("(\W+)", review)) if len(e) > 0 and not re.match("\W",e)]
   tokens = list(map(lambda x: stemmer.stem(x), tokens))
   #fo.write(' '.join(tokens)+'\n')
   LEN = LEN + len(find_ngrams(tokens, 3))
   for line in find_ngrams(tokens, 3):
      ngrams[' '.join(line)] = 0

#fo.close()
print(len(ngrams))
print LEN
fo = open('trigrams.txt','w')

for key in ngrams.keys():
   fo.write(key+'\n')

fo.close()



