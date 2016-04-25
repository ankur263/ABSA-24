from bs4 import BeautifulSoup
import string
import re
from nltk.stem import PorterStemmer

stemmer = PorterStemmer()

f = open('../ABSA15/output.xml')
inp = f.read()
f.close()

soup = BeautifulSoup(inp)

AMB = {}
DRI = {}
FOO = {}
LOC = {}
RES = {}
SERV = {}

f = open('../ABSA15/LEXICONS/AMBIENCE_lex.txt')
for line in f:
   line = line.split(',')
   AMB[line[0]] = float(line[1])
f.close()

f = open('../ABSA15/LEXICONS/DRINKS_lex.txt')
for line in f:
   line = line.split(',')
   DRI[line[0]] = float(line[1])
f.close()

f = open('../ABSA15/LEXICONS/FOOD_lex.txt')
for line in f:
   line = line.split(',')
   FOO[line[0]] = float(line[1])
f.close()

f = open('../ABSA15/LEXICONS/LOCATION_lex.txt')
for line in f:
   line = line.split(',')
   LOC[line[0]] = float(line[1])
f.close()

f = open('../ABSA15/LEXICONS/RESTAURANT_lex.txt')
for line in f:
   line = line.split(',')
   RES[line[0]] = float(line[1])
f.close()


f = open('../ABSA15/LEXICONS/SERVICE_lex.txt')
for line in f:
   line = line.split(',')
   SERV[line[0]] = float(line[1])
f.close()

cats = [AMB,DRI,FOO,LOC,RES,SERV]
CATS = ["AMBIENCE","DRINKS","FOOD","LOCATION","RESTAURANT","SERVICE"]

def find_results(rev):
   tokens=[e.lower() for e in map(string.strip, re.split("(\W+)", rev)) if len(e) > 0 and not re.match("\W",e)]
   tokens = list(map(lambda x: stemmer.stem(x), tokens))
   amb = dri = foo = loc = res = ser = 0
   print tokens
   for word in tokens:
      if word in AMB:
	 amb = amb + AMB[word]
      if word in DRI:
	 dri = dri + DRI[word]
      if word in FOO:
	 foo = foo + FOO[word]
      if word in LOC:
	 loc = loc + LOC[word]
      if word in RES:
	 res = res + RES[word]
      if word in SERV:
	 ser = ser + SERV[word]
   return [amb,dri,foo,loc,res,ser]

def GetCats(terms):
#  results = map(lambda c: c[stemmer.stem(term)] if stemmer.stem(term) in c else 0,cats)
   print terms
   results = find_results(terms)
   print results
   ResCats = map(lambda y: y[0],filter(lambda x: x[1]>0,zip(CATS,results)))
   MAX = -100
   MAXi = -1
   for i in range(len(results)):
      if results[i]>MAX:
	 MAX = results[i]
	 MAXi = i
   if MAXi == -1:
      return []
   print CATS[MAXi]
   #return '#'.join(ResCats)
   return CATS[MAXi]

for line in soup.find_all('aspectterm'):
   if 'term' in line.attrs:
      terms = line['term']
      line['categories'] = GetCats(terms)
      if line['categories'] == []:
	 line['categories'] = "NULL"


f = open('../ABSA15/FINOUT/FO1.xml','w')
f.write(soup.prettify())
f.close()

