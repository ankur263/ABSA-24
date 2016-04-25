from bs4 import BeautifulSoup
import re
import string
from nltk.stem import PorterStemmer

stemmer = PorterStemmer()

f = open('../ABSA14/rest_train.xml')

f1 = open('new_categories14.txt','w')

inp = f.read()

soup = BeautifulSoup(inp,'xml')

sens = soup.find_all('sentence')

for sen in sens:
   text = sen.find('text').getText()
   tokens=[e.lower() for e in map(string.strip, re.split("(\W+)", text)) if len(e) > 0 and not re.match("\W",e)]
   tokens = list(map(lambda x: stemmer.stem(x), tokens))
   f1.write(' '.join(tokens)+'\n')
   cats = sen.find('aspectCategories').find_all('aspectCategory')
   print cats
   CATS = map(lambda x: x['category'].strip(),cats)
   f1.write("#".join(CATS)+'\n')

f1.close()
f.close()



