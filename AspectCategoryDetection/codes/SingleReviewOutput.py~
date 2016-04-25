from nltk.stem import PorterStemmer
import re
import string
from sklearn.externals import joblib

stemmer = PorterStemmer()

def find_ngrams(inp_list, n):
   return zip(*[inp_list[i:] for i in range(n)])

def text_input(review):
   row_inst = []
   #cats has unique categories for a review
   tokens=[e.lower() for e in map(string.strip, re.split("(\W+)", review)) if len(e) > 0 and not re.match("\W",e)]
   tokens = list(map(lambda x: stemmer.stem(x), tokens))
   ogs = map(lambda x: ' '.join(list(x)),find_ngrams(tokens, 1))
   for o in onegrams:
      if o in ogs:
	 row_inst.append('1')
      else:
	 row_inst.append('0')

   bgs = map(lambda x: ' '.join(list(x)),find_ngrams(tokens, 2))
   for b in bigrams:
      if b in bgs:
	 row_inst.append('1')
      else:
	 row_inst.append('0')

   tgs = map(lambda x: ' '.join(list(x)),find_ngrams(tokens, 3))
   for t in trigrams:
      if t in tgs:
	 row_inst.append('1')
      else:
	 row_inst.append('0')

   return row_inst


onegrams = []
bigrams = []
trigrams = []

f = open('../ABSA15/onegrams.txt')
onegrams = f.read().split('\n')
f.close()

f = open('../ABSA15/bigrams.txt')
bigrams = f.read().split('\n')
f.close()

f = open('../ABSA15/trigrams.txt')
trigrams = f.read().split('\n')
f.close()


row = (text_input("judg from previou post thi use to be a good place but not ani longer"))

print len(row)

clf1 = joblib.load('../ABSA15/MODELS/cat1/cat1_model.pkl')
clf2 = joblib.load('../ABSA15/MODELS/cat2/cat2_model.pkl')
clf3 = joblib.load('../ABSA15/MODELS/cat3/cat3_model.pkl')
clf4 = joblib.load('../ABSA15/MODELS/cat4/cat4_model.pkl')
clf5 = joblib.load('../ABSA15/MODELS/cat5/cat5_model.pkl')
clf6 = joblib.load('../ABSA15/MODELS/cat6/cat6_model.pkl')

print clf1.predict(row)
print clf2.predict(row)
print clf3.predict(row)
print clf4.predict(row)
print clf5.predict(row)
print clf6.predict(row)

