import numpy as np
import array
import struct

def getValue(array):
    val = 0
    if (array[0] == 1):
        val = val + 128
    if (array[1] == 1):
        val = val + 64
    if (array[2] == 1):
        val = val + 32
    if (array[3] == 1):
        val = val + 16
    if (array[4] == 1):
        val = val + 8
    if (array[5] == 1):
        val = val + 4
    if (array[6] == 1):
        val = val + 2
    if (array[7] == 1):
        val = val +1

    return val



f = open("../data/Tatou.pgm", "rb")
f2 = open("../data/extracted_file.jpg", "wb")
try:
    byte = f.read(1)
    count = 0
    newByteArray = [0, 0, 0, 0, 0, 0, 0, 0]
    offset = 0
    while byte != b'':
        byte = f.read(1)
        if (count > 0 ):
            # Do stuff with byte.
            #f2.write(byte)
            for i in range(8):
                #print((byte[0] >> i) & 1)
                if (i == 6 or i == 7):
                    #print((byte[0] >> i) & 1)
                    newByteArray[offset] = ((byte[0] >> i) & 1)
                    offset = offset + 1
                    if (offset == 8):
                        print(newByteArray)
                        val = getValue(newByteArray)
                        f2.write(struct.pack('i', val))
                        #print((val.to_bytes(2, byteorder='big', signed=False) ))
                        print(val)
                        offset = 0
                        newByteArray = [0, 0, 0, 0, 0, 0, 0, 0]
                        #f2.write(newByteArray)
            # print (bits)
        count = count+1
finally:
    f.close()
    f2.close()
