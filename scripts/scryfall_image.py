import sys
import urllib.request 
import urllib.parse 
import json

# read card script
with open(sys.argv[1], "r") as cs:
    lines = cs.readlines()

# extract card name
card_name = lines[0].split("=")[1].strip()

print("query", card_name)

# get card details https://api.scryfall.com/cards/named?exact=%22Wipe%20Away%22
url = 'https://api.scryfall.com/cards/named'
query = urllib.parse.urlencode({'exact': card_name})
with urllib.request.urlopen(url + '?' + query) as response: 
    response_text = response.read() 

resp = json.loads(response_text)
if 'image_uris' in resp:
    img_url = resp['image_uris']['border_crop']
else:
    for face in resp['card_faces']:
        if face['name'] == card_name:
            img_url = face['image_uris']['border_crop']

lines[1] = "image=" + img_url + '\n'

# replace image
with open(sys.argv[1], "w") as cs:
    cs.writelines(lines)

print("updated", card_name)
