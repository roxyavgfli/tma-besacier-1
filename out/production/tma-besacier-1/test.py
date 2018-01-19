import struct

f = open("../data/Tatou.pgm", 'rb')
f2 = open("../data/jetaiscache.jpg", 'wb')
f.readline()
f.readline()
f.readline()
num_pixel = 32
size = ""
for _ in range(16):
    bit_hexa = f.read(1)
    binary = bin(bit_hexa[0])
    two_lsb = binary[-2] + binary[-1]
    size += two_lsb
print("taille de l'image :", int(size, 2))

name = ""
for _ in range(14):
    byte = ""
    for _ in range(4):
        bit_hexa = f.read(1)
        binary = bin(bit_hexa[0])
        two_lsb = binary[-2] + binary[-1]
        byte += two_lsb
    decode_bin = bin(int(byte, 2))
    decode_dec = int(decode_bin, 2)
    print("pixels", num_pixel, 'à', num_pixel + 4, ":", chr(decode_dec))
    num_pixel += 4
    name += chr(decode_dec)
print("nom de l'image :", name)

content = ""
for _ in range(72):
    #nothing
    i = 0

bit_hexa = b'x'
count = 0
while(bit_hexa != b'' and count < (int(size, 2))*4):    
    byte = ""
    for _ in range(4):
        bit_hexa = f.read(1)
        #print(bit_hexa)
        binary = bin(bit_hexa[0])
        two_lsb = binary[-2] + binary[-1]
        byte += two_lsb
    #print(binary)
    #f2.write(binary)
    decode_bin = bin(int(byte, 2))
    #arr = bytearray(decode_bin, 'utf-8')
    #print(arr)
    #print (decode_bin)
    decode_dec = int(decode_bin,2)
    if (count > 68):
        f2.write(struct.pack("B",decode_dec))
    count = count + 4
    print("pixels", num_pixel, 'à', num_pixel + 4, ":", chr(decode_dec))
    #print("pixels", num_pixel, 'à', num_pixel + 4, ":", chr(decode_dec))
    num_pixel += 4
print(content)

