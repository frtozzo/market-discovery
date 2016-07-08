#!/usr/bin/python
# -*- coding: utf-8 -*-

import db_config1
import db_config
import sqlite3
import MySQLdb
from page_extractor import *
from bs4 import BeautifulSoup
import urllib
import socks
import sys

import requests
import urllib2
from lxml import html
from lxml.html.clean import clean_html
from lxml.html.clean import Cleaner


reload(sys)
sys.setdefaultencoding('utf8')


#connessione sqlite
conn=sqlite3.connect('/home/kekko/gfake/google_scraper.db')
#connessione DB BrandProtection MySql
db=MySQLdb.connect(db_config.ip,db_config.username,db_config.password,db_config.dbname)
#connessione DB UMD
db1=MySQLdb.connect(db_config1.ip,db_config1.username,db_config1.password,db_config1.dbname)
db1.set_character_set('utf8')

#cursore DB Sqlite
c=conn.cursor()

#cursore DB MySql BRANDPROTECTION
cursor=db.cursor()

#cursore DB MYSql UMD
cursore=db1.cursor()


delimiters = [",", ".", "!", "?", "/", "&", "-", ":", ";", "@", "'", "...","|","%","<",">","(","(","{","}","_","[","]","»","«"
,"+","=","#"]


#metodo che prende i link trovati dal link collector e li restituisce in una lista
def getLink(c):
	c.execute('SELECT link FROM link')
	results=c.fetchall()
	lista=[]	
	for row in results:
		lista.append(row[0])
		#print (row[0])

	return lista

#metodo che prende i link dei market del DB POSTE e li salva in un diz. con chiave il link e valore yes/no
def getLinkMarket(cursore):
	cursore.execute("SELECT url,market FROM market where stato=%s",("attivo"))
	results=cursore.fetchall()
	diz={}	
	for row in results:
		diz[row[0]]=row[1]	
	return diz

#metodo che prende i link dei market del DB POSTE
def getLinkNoMarket(cursore):
	cursore.execute("SELECT url,market FROM noMarket where stato=%s",("attivo"))
	results=cursore.fetchall()
	lista=[]	
	diz={}	
	for row in results:
		diz[row[0]]=row[1]	
	return diz


#metodo che resituisce l'html di un link no tor privo di tag
def getHtml(cursor):
	lista=getLink(cursor)
	for i in range(len(lista)):
		out=lista[i]
		testo=h.get_content(out)
		#print testo
	return testo




link='http://www.gazzetta.it/'





#metodo che restituisce html con tag di link no tor
def getHTMLtag(link):
	r = urllib.urlopen(link).read()
	soup = BeautifulSoup(r)
	out=soup.prettify()
	print (out)
	return

def cleanhtml(link):

	req=requests.get(link)
	
	soup = BeautifulSoup(req.content, 'lxml')
	text=soup.get_text()
	cleaner=Cleaner(page_structure=True, links=True,style=True,forms=True,javascript=True)
	out = cleaner.clean_html(text)
	#print out
	f = open("example.txt", "w")
	f.write(out)
	return

def test(link):
	req=requests.get(link)
	
	soup = BeautifulSoup(req.content, 'lxml')
	text=soup.get_text()
	f = open("example.txt", "w")
	f.write(text)
	return



def remove_delimiters (delimiters, s):
    new_s = s
    for i in delimiters: #replace each delimiter in turn with a space
        new_s = new_s.replace(i, ' ')
    return ' '.join(new_s.split())

def cleaning(link):
	html = requests.get(l)
	soup = BeautifulSoup(html.content)
	for script in soup(["script", "style"]):
    		script.extract()    # rip it out

	# get text
	text = soup.get_text()
	# break into lines and remove leading and trailing space on each
	lines = (line.strip() for line in text.splitlines())
	# break multi-headlines into a line each
	chunks = (phrase.strip() for line in lines for phrase in line.split("  "))
	#drop blank lines
	text = '\n'.join(chunk for chunk in chunks if chunk)
	s=text.strip('\n')
	dm=remove_delimiters(delimiters,s)
	f = open("example.txt", "w")
	f.write(dm.encode('utf-8'))
	return dm.encode('utf-8')
	

def cleanStrings(self, inStr):
  a = inStr.find('<')
  b = inStr.find('>')
  if a < 0 and b < 0:
    return inStr
  return cleanString(inStr[a:b-a])


l='http://www.corriere.it/'
#prende l'html no tor senza tag
def get_html(l):
	#req = requests.get(l,cert=('/home/kekko/cert/certs.pem','/home/kekko/certs.pem.key'))
	#requests.packages.urllib3.disable_warnings()
	#html = urllib2.urlopen(l)

	req=requests.get(l)
	#if req.status_code != 200:
        #	raise PageParserException('fail with code %s for page %s'%(req.status_code, l))
	
	

	soup = BeautifulSoup(req.content, 'lxml')
	#dammit = UnicodeDammit(soup.get_text("|", strip=True))
	#print(dammit.unicode_markup)
	#tree = clean_html(soup)
	
	#return soup.prettify(encoding='utf-8',formatter=None)
	return soup.get_text()







def cleaningHTML(l):
	html = requests.get(l)
	soup = BeautifulSoup(html.content)
	for script in soup(["script", "style"]):
    		script.extract()    # rip it out

	# get text
	text = soup.get_text()
	# break into lines and remove leading and trailing space on each
	lines = (line.strip() for line in text.splitlines())
	# break multi-headlines into a line each
	chunks = (phrase.strip() for line in lines for phrase in line.split("  "))
	#drop blank lines
	text = '\n'.join(chunk for chunk in chunks if chunk)
	s=text.strip('\n')
	dm=remove_delimiters(delimiters,s)
	return dm.encode('utf-8')


def remove_delimiters (delimiters, s):
    new_s = s
    for i in delimiters: #replace each delimiter in turn with a space
        new_s = new_s.replace(i, ' ')
    return ' '.join(new_s.split())


#metodo prova MYSQL BRANDPROTECTION
def stampaMysql(cursor):
	cursor.execute('SELECT link FROM link')
	results=cursor.fetchall()
	lista=list(results)
	for i in range(len(lista)):
		out=lista[i]
		print (out)
			
	return


#metodo che inserisce link no tor nel db
def inserisciLink(cursor,cursore):
	lista=getLink(cursor)
	for i in range(len(lista)):
		out=lista[i]
			
		sql="INSERT INTO pages(idLink) VALUES('%s')" %(out)
		cursore.execute(sql)
		db1.commit()		
	return

#metodo per inserire codice html nel DB
def inserisciHtmlTag(link,cursore):
	html_code=get_html(link)
	sql="INSERT INTO pages(bodyData) VALUES('%s') " % db1.escape_string(html_code)
	cursore.execute(sql)
	db1.commit()
	return


def isTor(link):
	str='.onion'
	return link.find(str)


def create_connection(address, timeout=None, source_address=None):
    sock = socks.socksocket()
    sock.connect(address)
    return sock


#socks.setdefaultproxy(socks.PROXY_TYPE_SOCKS5, "127.0.0.1", 9050, True)
#socket.socket = socks.socksocket
#socket.create_connection = create_connection
url = 'http://wikitjerrta4qgz4.onion/'
def getHTMLtor(url):
	data = urllib.urlopen(url).read()
	return data
#inserisce i link dei market nel DB UMD.pages
def insertMarket(cursore):
	dizionario=getLinkMarket(cursore)
	for k in dizionario:
		v=dizionario[k] #etichetta si o no
		result =cleaningHTML(k)
		html_code=get_html(k)
		sql="INSERT INTO pages(idLink,bodyData,tor,marketLabel,cleanbody) VALUES('%s','%s','%s','%s','%s')" %(k,db1.escape_string(html_code),0,v,db1.escape_string(result))
		cursore.execute(sql)
		db1.commit()
	return

def yes(k):
	html_code=get_html(k)
	v="y"
	result = cleaningHTML(k)
	sql="INSERT INTO pages(idLink,bodyData,tor,marketLabel,cleanbody) VALUES('%s','%s','%s','%s','%s')" %(k,db1.escape_string(html_code),0,v,db1.escape_string(result))
	
	cursore.execute(sql)
	db1.commit()
	return

	

def insertNOMarket(cursore):
	dizionario=getLinkNoMarket(cursore)
	for k in dizionario:
		v=dizionario[k] #etichetta si o no
		html_code=get_html(k)
		result =cleaningHTML(k)
		sql="INSERT INTO pages(idLink,bodyData,tor,marketLabel,cleanbody) VALUES('%s','%s','%s','%s','%s')" %(k,db1.escape_string(html_code),0,v,db1.escape_string(result))
		cursore.execute(sql)
		db1.commit()
	return


def truncateSpace(cursore):
	sql='UPDATE pages SET cleanbody = TRIM(REPLACE(REPLACE(cleanbody,CHAR(13),' '),CHAR(10),' '));'
	cursore.execute(sql)
	db1.commit()
	return


def insert_DB(c,cursore):
	lista=getLink(c)
	for i in range(len(lista)):
		out=lista[i]
		boolTor=isTor(out)
		if boolTor==-1:
			html_code=get_html(out)
			sql="INSERT INTO pages(idLink,bodyData,tor) VALUES('%s','%s','%s')" %(out,db1.escape_string(html_code),0)

			cursore.execute(sql)
			db1.commit()
		else:
			html_code=getHTMLtor(url)
			sql="INSERT INTO pages(idLink,bodyData,tor) VALUES('%s','%s','%s')" %(out,db1.escape_string(html_code),1)
			cursore.execute(sql)
			db1.commit()
	return



#getHtml(cursor);

#getHTMLtag(link);

#inserisciHtml(cursor,cursore)

#getHTMLtor(url);

#get_html(l);

#insert_DB(c,cursore);
#inserisciHtmlTag(l,cursore)

#inserisciHtml(cursore);

#getLinkMarket(cursore);

insertMarket(cursore);

insertNOMarket(cursore);
#li='http://www.vipapk.com'
#yes(li);

#cleanhtml(l);
#test(l);
#cleaning(l);
#cleaningHTML(l);
#truncateSpace(cursore)
