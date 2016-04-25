from bs4 import BeautifulSoup
import re
import string
from nltk.stem import PorterStemmer

def find_ngrams(inp_list, n):
   return zip(*[inp_list[i:] for i in range(n)])


f = open('../ABSA15/YelpRests.xml')
inp_doc = f.read()
f.close()

onegrams = {}
bigrams = {}
trigrams = {}

soup = BeautifulSoup(inp_doc)

stemmer = PorterStemmer()
fo = open('../ABSA15/categories.txt','w')
f1 = open('../ABSA15/bigrams.txt','w')
f2 = open('../ABSA15/trigrams.txt','w')
f3 = open('../ABSA15/onegrams.txt','w')
RLEN = 0

LEN1 = 0
LEN2 = 0
LEN3 = 0

for sent in soup.find_all('sentence'):
   text = sent.find_all('text')[0].get_text()
   review = text.strip()
   cats = []
   for ops in sent.find_all('opinion'):
      cats.append(ops.get('category'))

   if (len(cats)>0):
      RLEN = RLEN + 1
      tokens=[e.lower() for e in map(string.strip, re.split("(\W+)", review)) if len(e) > 0 and not re.match("\W",e)]
      tokens = list(map(lambda x: stemmer.stem(x), tokens))
      fo.write(' '.join(tokens)+'\n')
      fo.write(','.join(cats).strip()+'\n')

      '''
      LEN1 = LEN1 + len(find_ngrams(tokens, 1))
      LEN2 = LEN2 + len(find_ngrams(tokens, 2))
      LEN3 = LEN3 + len(find_ngrams(tokens, 3))
      '''

      for line in find_ngrams(tokens, 1):
	 onegrams[' '.join(line)] = 0
      for line in find_ngrams(tokens, 2):
	 bigrams[' '.join(line)] = 0
      for line in find_ngrams(tokens, 3):
	 trigrams[' '.join(line)] = 0

print 'No of Reviews'
print RLEN
#print "for bigrams"
#print len(bigrams)
#print LEN2

#print "for trigrams"
#print len(trigrams)
#print LEN3

for key in bigrams.keys():
   f1.write(key+'\n')

for key in trigrams.keys():
   f2.write(key+'\n')

for key in onegrams.keys():
   f3.write(key+'\n')

fo.close()
f1.close()
f2.close()
f3.close()


