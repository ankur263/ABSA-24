# coding: utf-8
f = open('Restaurants_Train.xml')
from bs4 import BeautifulSoup
soup = BeautifulSoup()
inp = f.read()
soup = BeautifulSoup(inp,'xml')
sens = soup.find_all('sentence')
#len(sens)
#3044*.75
sTrain = BeautifulSoup("",'xml')
main = sTrain.new_tag('sentences')
for s in sens[:2740]:
    main.insert(1,s)

fo = open('rest_train_90_10.xml','w')
fo.write(main.prettify())
fo.close()
#get_ipython().system(u'ls -F --color ')
#get_ipython().system(u'less rest_train.xml')
sTest = BeautifulSoup("",'xml')
main = sTest.new_tag('sentences')
for s in sens[2740:]:
    main.insert(1,s)

fo = open('rest_test_90_10.xml','w')
fo.write(main.prettify())
fo.close()
