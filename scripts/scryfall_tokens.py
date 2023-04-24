import sys
import urllib.request 
import urllib.parse 
import json

# read card script
with open(sys.argv[1], "r") as cs:
    lines = cs.readlines()

props = {}
for line in lines:
    toks = line.strip().split("=")
    props[toks[0]] = toks[1]

# extract card name
card_name = props['name']

print("query", card_name)

# get card details https://api.scryfall.com/cards/named?exact=%22Wipe%20Away%22
with open("oracle.json") as of:
    cards = json.load(of)
    
expected_type = 'Token ' + props['type'].replace(',', ' ') + ' — ' + props['subtype'].replace(',', ' ')
expected_color = set(props['color'].lower()) 
expected_oracle = props.get('oracle', '').lower()
if expected_oracle == "none":
    expected_oracle = ''
print(expected_type, expected_color)

found = 0
for card in cards:
    if 'power' not in card:
        continue
    type_line = card['type_line']
    color_line = set(c.lower() for c in card['colors'])
    pt_line = card['power'] + '/' + card['toughness']
    if type_line == expected_type and color_line == expected_color and pt_line == props['pt'] and card['oracle_text'].lower() == expected_oracle:
        found += 1
        img_url = card['image_uris']['border_crop']
        print(card)

if found == 1:
    lines[2] = "image=" + img_url + '\n'

# replace image
    with open(sys.argv[1], "w") as cs:
        cs.writelines(lines)

    print("updated", card_name)
elif found == 0:
    print("not found")
else:
    print("not unique")
