import serial
import re
import time

class LG():
    def __init__(self, comport, baudrate = 9600, address = 0):
        self.comport = comport
        self.baudrate = baudrate
        self.address = address
        self._inst = serial.Serial(self.comport, baudrate=self.baudrate)
   
    def write_set(self, message):
        print message
        self._inst.write(message)
   
    def write_get(self, message):
        self._inst.write(message)
        ret = self._inst.read(9)
        print ret
        #should parse ret
        #pick last field, strip off 'OK' and '\n'
        fields = re.split(' +', ret)
        
        return int(fields[-1][2:],16)

    def set_power(self, v):
		if v == 0 or v == "off":
			self.write_set("ka %d 0\r" % (self.address))
		if v == 1 or v == "on":
			self.write_set("ka %d 1\r" % (self.address))
			time.sleep(10)
   
    def get_power(self):
	#needs work
        return self.write_get("ka %d ff\r" % (self.address))

   
    def set_input(self, input):
        input = input.lower()
        input_dict = {
                      'cable':'01',
                      'tv':'01',
                      'av1':'02',
                      'av2':'03',
                      'component':'04',
                      'hdmi':'08',
                      'hdmi1':'08',
                      'hdmi2':'09',
                      'pc':'07',
                      'computer':'07',
                      'rgb-pc':'07'
                      }
       
        self.write_set("kb %d %s\r" % (self.address, input_dict[input]))
   
    def get_input(self):
        return self.write_get("kb %d ff\r" % (self.address))
    
    def set_volume(self, level):
        #convert level to hex
        level = hex(level)[2:]
        self.write_set("kf %d %s\r" % (self.address, level))
        
    def get_volume(self):
        return self.write_get("kf %d ff\r" % (self.address))
        
    def increment_volume(self):
        level = self.get_volume()
        self.set_volume(level + 1)
    
    def decrement_volume(self):
        level = self.get_volume()
        self.set_volume(level - 1)
    
    def toggle_mute(self):
        v = self.write_get("ke %d ff\r" % (self.address))
        print v
        if v == 0:
            self.set_mute(1)
        else:
            self.set_mute(0)
    
    def set_mute(self, b):
        # b == 1-->turn off mute
        # b == 0-->turn on mute
        self.write_set("ke %d %s\r" % (self.address, "0" + str(b)))
    
        






#lg = LG("/dev/ttyUSB0")

#lg.set_power("on")
#lg.toggle_mute()
#lg.set_input('tv')
#time.sleep(2)
#lg.set_input("pc")

