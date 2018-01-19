import struct

f = open("../data/Lena.pgm", 'rb')
f3 = open("../data/tatoumina1.jpg", 'rb')
f2 = open("../data/disumlation.pgm", 'wb')
f.readline()
f.readline()
f.readline()


byte = b'x'
byteHide = b'x'
bytefinal = b'x'
while(byte != b''):
    byteHide = ""
    byteHide = f3.read(1)
    i = 0
    for _ in range(8):
        destinationByte = f.read(1)
        print("origin")
        print(destinationByte)
        destinationByte = destinationByte[0] & 253
        destinationByte = destinationByte & (byteHide[0] >> i)
        print("encoded")
        print(struct.pack("B",destinationByte))
        f2.write(struct.pack("B",decode_bin))

while(bytefinal != b''):
    bytefinal = ""
    bytefinal = f.read(1)
    f2.write(bytefinal)