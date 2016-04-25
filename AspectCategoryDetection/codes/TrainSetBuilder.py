
from nltk.stem import PorterStemmer

stemmer = PorterStemmer()

def find_ngrams(inp_list, n):
   return zip(*[inp_list[i:] for i in range(n)])


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

reviews = []

f = open('../ABSA15/categories.txt')
reviews = f.read().split('\n')
f.close()

fo = open('../ABSA15/DATASETS/train2.csv','w')

row_inst = []

for i in range(len(onegrams)):
   row_inst.append('1_'+str(int(i)))


for i in range(len(bigrams)):
   row_inst.append('2_'+str(int(i)))

for i in range(len(trigrams)):
   row_inst.append('3_'+str(int(i)))

row_inst.append('CATEGORY')
fo.write(','.join(row_inst)+'\n')

for i in range(0, len(reviews)-1, 2):
   row_inst = []
   #cats has unique categories for a review
   cats = list(set(map(lambda x: x.split('#')[0],reviews[i+1].split(','))))
   tokens = reviews[i].split(' ')
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

   CatMap = {"FOOD":'1', "DRINKS":'2', "SERVICE":'3', "AMBIENCE":'4', "LOCATION":'5', "RESTAURANT":'6'}

   for c in cats:
	 fo.write(','.join(row_inst)+','+str(CatMap[c])+'\n')


fo.close()

print "Feature Count: "+str(len(onegrams)+len(bigrams)+len(trigrams)+1)




