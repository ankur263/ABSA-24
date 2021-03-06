from bs4 import BeautifulSoup
import string
import re
from nltk.stem import PorterStemmer

stemmer = PorterStemmer()

f = open(r'C:\Users\CRYPTOK\Documents\NetBeansProjects\WebApplication1\output\output.xml')
inp = f.read()
f.close()

soup = BeautifulSoup(inp)

FOO = {}
SER = {}
PRI = {}
AMB = {}
ANEC = {}

f = open(r'C:\Users\CRYPTOK\Documents\NetBeansProjects\WebApplication1\output\LEXICONS\food_lex.txt')
for line in f:
   line = line.split(',')
   FOO[line[0]] = float(line[1])
f.close()

f = open(r'C:\Users\CRYPTOK\Documents\NetBeansProjects\WebApplication1\output\LEXICONS\service_lex.txt')
for line in f:
   line = line.split(',')
   SER[line[0]] = float(line[1])
f.close()

f = open(r'C:\Users\CRYPTOK\Documents\NetBeansProjects\WebApplication1\output\LEXICONS\price_lex.txt')
for line in f:
   line = line.split(',')
   PRI[line[0]] = float(line[1])
f.close()

f = open(r'C:\Users\CRYPTOK\Documents\NetBeansProjects\WebApplication1\output\LEXICONS\ambience_lex.txt')
for line in f:
   line = line.split(',')
   AMB[line[0]] = float(line[1])
f.close()

f = open(r'C:\Users\CRYPTOK\Documents\NetBeansProjects\WebApplication1\output\LEXICONS\anecdotes_miscellaneous_lex.txt')
for line in f:
   line = line.split(',')
   ANEC[line[0]] = float(line[1])
f.close()

cats = [FOO,SER,PRI,AMB,ANEC]
CATS = ["food","service","price","ambience","anecdotes/miscellaneous"]

def find_results(rev):
   tokens=[e.lower() for e in map(string.strip, re.split("(\W+)", rev)) if len(e) > 0 and not re.match("\W",e)]
   tokens = list(map(lambda x: stemmer.stem(x), tokens))
   foo = ser = pri = amb = anec = 0
   print tokens
   for word in tokens:
      if word in FOO:
	 foo = foo + FOO[word]
      if word in SER:
	 ser = ser + SER[word]
      if word in PRI:
	 pri = pri + PRI[word]
      if word in AMB:
	 amb = amb + AMB[word]
      if word in ANEC:
	 anec = anec + ANEC[word]
   return [foo,ser,pri,amb,anec]

def GetCats(terms):
#  results = map(lambda c: c[stemmer.stem(term)] if stemmer.stem(term) in c else 0,cats)
   print terms
   results = find_results(terms)
   print results
   ResCats = map(lambda y: y[0],filter(lambda x: x[1]>0,zip(CATS,results)))
   print ResCats
   return '#'.join(ResCats)

sens = soup.find_all('sentence')

for sen in sens:
   text = sen.find('text').getText()

   main_tag = soup.new_tag('aspectCategories')
   asCats = GetCats(text).split("#")
   for cat in asCats:
      new_tag = soup.new_tag('aspectCategory')
      new_tag["category"] = cat
      main_tag.insert(1,new_tag)
   sen.insert(4,main_tag)

'''
for line in soup.find_all('aspectterm'):
   if 'term' in line.attrs:
      terms = line['term']
      line['categories'] = GetCats(terms)
      if line['categories'] == []:
	 line['categories'] = "NULL"
'''

f = open('C:\Users\CRYPTOK\Documents\NetBeansProjects\WebApplication1\output\FO1.xml','w')
f.write(soup.prettify())
f.close()

