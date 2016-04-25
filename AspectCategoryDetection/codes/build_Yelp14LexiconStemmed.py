import math

reviews = []
cats = []
fo = open('../ABSA14/new_categories14.txt')

i = 0

for line in fo:
   if i%2==0:
      reviews.append(line.strip())
   else:
      cats.append(list(set(map(lambda x: x.split('#')[0],line.strip().split(',')))))
   i = i + 1

fo.close()


#Experiment for finding N = total number of tokens in corpus
cnt = 0

for line in reviews:
   cnt = cnt + len(line.split())

N = cnt

CNT1 = 0
CNT2 = 0

def PMI(w,cat,N):
   freq1 = 0
   for i in range(len(reviews)):
      if cat in cats[i]:
	 for word in reviews[i].split():
	     if word == w:
	       freq1 = freq1 + 1
   #freq2 = number of times w occurs in all reviews
   #freq2 = for every review , for every word in review , if word == w return 1 else return 0 , and then add these ones
   #Note i did not use sum(map(lambda rev: 1 if w in rev else 0, reviews)) because w can occur multiple times
   freq2 = sum(map(lambda rev: sum(map(lambda word: 1 if word==w else 0,rev.split())),reviews))
   freq3 = sum(map(lambda x: len(x[0].split()) if cat in x[1] else 0,zip(reviews,cats)))
   value = (freq1*N)/float(freq2*freq3)

   if value == 0:
      global CNT1
      CNT1 = CNT1 + 1
      #print 'PMI '+w+' '+cat+' '+str(value)
      #print N,freq1,freq2,freq3
      return 0

   return float(math.log(value,2))


def not_PMI(w,c,N):
   freq1 = 0
   for rev,cat in zip(reviews,cats):
      if c not in cat:
	 for word in rev.split():
	     if word == w:
	       freq1 = freq1 + 1

   freq2 = sum(map(lambda rev: sum(map(lambda word: 1 if word==w else 0,rev.split())),reviews))
   freq3 = sum(map(lambda x: len(x[0].split()) if c not in x[1] else 0,zip(reviews,cats)))
   value = (freq1*N)/float(freq2*freq3)

   if value == 0:
      global CNT2
      CNT2 = CNT2 + 1
      #print 'NOT '+w+' '+c+' '+str(value)
      #print N,freq1,freq2,freq3
      return 0
   return float(math.log(value,2))



#catmap = {"FOOD":'1', "DRINKS":'2', "SERVICE":'3', "AMBIENCE":'4', "LOCATION":'5', "RESTAURANT":'6'}
catmap = {"food":'1', "service":'2', "price":'3', "ambience":'4',"anecdotes/miscellaneous":'5' }
CatMap = map(lambda x: x,catmap.keys())



words = {}

for rev in reviews:
   for w in rev.split():
      words[w] = 0

for CAT in CatMap:
   fo = open('../ABSA14/NEW_LEXICONS/'+'_'.join(CAT.split('/'))+'_lex.txt','w')
   for word in words.keys():
      score1 = float(PMI(word,CAT,N))
      score2 = float(not_PMI(word,CAT,N))
      fo.write(word+','+str(score1-score2)+'\n')
      print CAT+" "+word
   fo.close()

print CNT1
print CNT2
