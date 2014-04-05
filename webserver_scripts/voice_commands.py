import webbrowser
import re
import time
import LG

serial_port = "/dev/ttyUSB0"

def launch(query):
    print query
#    query = "volume up"
    words = re.split("[%20| |+]", query)
    print words
    if words[0].lower() == "play":
        pass
        lg = LG.LG("COM1")
        
        is_on = lg.get_power()
        print is_on
        if is_on == 0:
            lg.set_power(1)
            lg.set_volume(40)
        lg.set_input('pc')
        lg._inst.close()
        query = "%20".join(words[1:])
        url = "https://www.google.com/search?q=youtube%20+music%20+"
        url = url[:-1]+query + "&btnI=745"
        print url
        webbrowser.open(url, new=0)
    if words[0].lower() == "volume":

        lg = LG.LG(serial_port)
        if words[1].lower() == "up":
            lg.increment_volume()
        elif words[1].lower() == "down":
            lg.decrement_volume()
        else:
            lg.set_volume(int(words[1]))
        lg._inst.close()
    if words[0].lower() == "input":
        lg = LG.LG(serial_port)
        lg.set_input(words[1].lower())
    if words[0].lower() == "power":
        lg = LG.LG(serial_port)
        lg.set_power(words[1].lower())
    if words[0].lower() == 'mute':
        lg = LG.LG(serial_port)
        lg.toggle_mute()
#launch("/volume%20up")
#import urllib
#launch("play+journey")
#while(True):
#    f = urllib.urlopen('http://yahoo.com')
#    text = f.read()
#    if text != '':
#        print text
#    time.sleep(2)
#launch("v")    


