import urllib
import urlparse
import re

# this script will read in cards from a text file, search the magiccards.info site, and
# add urls for card images and info if they don't exist

#####################################################
# variables to be changed

path = "../resources/magic/data/"

# input card files
ifile1 = open(path + "cards - Copy.txt", "r");
ifile2 = open(path + "cards2 - Copy.txt", "r");

# output card files (these should be different from input files)
ofile1 = open(path + "cards.txt", "w");
ofile2 = open(path + "cards2.txt", "w");


#####################################################

def url_fix(s, charset='utf-8'):
    if isinstance(s, unicode):
        s = s.encode(charset, 'ignore')
    scheme, netloc, path, qs, anchor = urlparse.urlsplit(s)
    path = urllib.quote(path, '/%')
    qs = urllib.quote_plus(qs, ':&=')
    return urlparse.urlunsplit((scheme, netloc, path, qs, anchor))

def write_out_card(ofile, name, prevCardStr, foundImageUrl, foundInfoUrl):
	# write out info for previous card
	
	ofile.write(">" + name + "\n") # name
	
	# generate urls
	if((not foundImageUrl) or (not foundInfoUrl)):
		queryUrl = url_fix("http://magiccards.info/query?q=" + name + "&v=card&s=cname")
		
		html = urllib.urlopen(queryUrl).read()
		
		# card info url
		if (not foundInfoUrl):
			print "generating info url for " + name
			
			pattern = "<a href=\"([^\"]*)\">" + name + "</a>"
			m = re.findall(pattern, html, re.IGNORECASE)
			
			if (len(m) > 0):
				ofile.write("url=http://magiccards.info" + m[0] + "\n")
			else:
				print "Unable to get info url for " + name
				
		# card image url
		if (not foundImageUrl):
			print "generating image url for " + name
			
			pattern = "img src=\"([^\"]*jpg)\""
			m = re.findall(pattern, html, re.IGNORECASE)
			
			if (len(m) > 0):
				ofile.write("image=" + m[0] + "\n")
			else:
				print "Unable to get image url for " + name						
		
	# write out rest of info to file
	ofile.write(prevCardStr)
	
	
def generateURLs(ifile, ofile):
	prevCardStr = ""
	foundImageUrl = False
	foundInfoUrl = False
	name = ""

	while ifile:
		line = ifile.readline();
		if(len(line) == 0):
			if(len(name) > 0):
				write_out_card(ofile, name, prevCardStr, foundImageUrl, foundInfoUrl)
			break;
		# ofile.write(line);
		
		i = line.find(">");
		if(i > -1):
			# new card
			
			if(len(name) > 0):
				write_out_card(ofile, name, prevCardStr, foundImageUrl, foundInfoUrl)
				
			# reset variables
			name = line[1:-1] # set name to new card name
			prevCardStr = ""
			foundImageUrl = False
			foundInfoUrl = False
		else:
			# card property
			
			prevCardStr += line
			
			if(line.find("url=") > -1):
				foundInfoUrl = True
			
			if(line.find("image=") > -1):
				foundImageUrl = True

generateURLs(ifile1, ofile1);
generateURLs(ifile2, ofile2);