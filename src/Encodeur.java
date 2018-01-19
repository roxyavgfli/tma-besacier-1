import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class Encodeur {
    
    static int titlePosition = 16;
    static int mainFilePosition = 128+titlePosition;

    public static byte[] extractBytes (String ImageName) throws IOException {
        File file = new File(ImageName);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return ( fileContent );
    }

    public static byte[] encodeSize(int size, byte[] source){
        byte[] hiddenByteSize = new byte[titlePosition];
        for(int i=0; i<titlePosition; i++){
            hiddenByteSize[i] = (byte)( (source[i]&0xfc) | ( (size>>(15-i)*2) & 0x3 ) );
        }
        return hiddenByteSize;
    }

    public static byte[] encodeName(String name, byte[] source){
        byte[] nameByte = new byte[mainFilePosition-titlePosition];
        for (int i=0; i<name.length(); i++){
            int codechar = (int) name.charAt(i);
            nameByte[4*i] =(byte) ( (source[(4*i)+titlePosition]&0xfc) | ((codechar>>6)&0x3) );
            nameByte[4*i+1] = (byte) ( (source[(4*i)+1+titlePosition]&0xfc) | ((codechar>>4)&0x3) );
            nameByte[4*i+2] = (byte) ( (source[(4*i)+2+titlePosition]&0xfc) | ((codechar>>2)&0x3) );
            nameByte[4*i+3] = (byte) ( (source[(4*i)+3+titlePosition]&0xfc) | (codechar&0x3) );
        }
        for (int i=4*name.length(); i<mainFilePosition-titlePosition; i++){
            nameByte[i] = source[i+titlePosition];
        }
        return nameByte;
    }

    public static byte[] encodeFile(int size, byte[] source, byte[] data){
        byte[] fileByte = new byte[size*4];
        for (int i=0; i<size; i++){
            fileByte[4*i] = (byte) ( (source[4*i + mainFilePosition] & 0xfc) | ((data[i]>>6)&0x3) );
            fileByte[4*i+1] = (byte) ( (source[4*i+1 + mainFilePosition] & 0xfc) | ((data[i]>>4) & 0x3) );
            fileByte[4*i+2] = (byte) ( (source[4*i+2 + mainFilePosition] & 0xfc) | ((data[i]>>2) & 0x3) );
            fileByte[4*i+3] = (byte) ( (source[4*i+3 + mainFilePosition] & 0xfc) | (data[i]& 0x3) );
        }
        return fileByte;
    }

    public static byte[] encodeAll(byte[] hiddenByteSize, byte[] nameByte, byte[] fileByte, byte[] source){
        int endFile = mainFilePosition + fileByte.length;
        byte[] hiddenByte = new byte[source.length];
        for (int i=0; i<titlePosition; i++){
            hiddenByte[i] = hiddenByteSize[i];
        }
        for (int i=titlePosition; i<mainFilePosition; i++){
            hiddenByte[i] = nameByte[i-titlePosition];
        }
        for (int i=mainFilePosition; i<endFile; i++){
            hiddenByte[i] = fileByte[i-mainFilePosition];
        }
        for (int i=endFile; i<source.length; i++){
            hiddenByte[i] = source[i];
        }
        return hiddenByte;
    }

    public static void main(String[] args) {
        try {
            int taillehiddenBytemina1 = 61393;
            BytePixmap sourceBytePixmap = new BytePixmap("data/Lena.pgm");
            int height = sourceBytePixmap.height;
            int width = sourceBytePixmap.width;
            byte[] imgToHide = extractBytes("data/tatoumina1.jpg");
            byte[] sourceByte = sourceBytePixmap.getBytes();
            byte[] hiddenByteSize = encodeSize(taillehiddenBytemina1, sourceByte);
            byte[] nameByte = encodeName("tatoumina1.jpg", sourceByte);
            byte[] fileByte = encodeFile(taillehiddenBytemina1, sourceByte, imgToHide);
            byte[] hiddenByte = encodeAll(hiddenByteSize, nameByte, fileByte, sourceByte);

            BytePixmap resulthiddenByte = new BytePixmap(width, height, hiddenByte);
            resulthiddenByte.write("result/cache.pgm");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}