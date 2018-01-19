import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Decodeur {
    static int startTitle = 16;
    static int startFile = 144;

    public static int getSize(byte[] origine){
        int res = 0;
        for(int i=0; i<16; i++){
            res = res + (origine[i]>>1 & 1)*(int)Math.pow(2, (15-i)*2+1) + (origine[i] &1)*(int)Math.pow(2, (15-i)*2);
        }
        return res;
    }

    public static String readTitle(byte[] origine){
        char[] endtitle = new char[4];
        endtitle[0] =' ';
        endtitle[1] =' ';
        endtitle[2] =' ';
        endtitle[3] =' ';
        byte[] tabcharTitle = new byte[startFile-startTitle];
        int i = 0;
        while((endtitle[0]!='.' && endtitle[1]!='j' && endtitle[2]!='p' && endtitle[3]!='g') || (endtitle[0]!='.' && endtitle[1]!='p' && endtitle[2]!='n' && endtitle[3]!='g')){
            tabcharTitle[i] = (byte) (((origine[(i*4)+startTitle]&0x3)<<6) |
                    ((origine[(i*4)+startTitle+1]&0x3)<<4) |
                    ((origine[(i*4)+startTitle+2]&0x3)<<2) |
                    (origine[(i*4)+startTitle+3]&0x3));
            endtitle[0]= endtitle[1];
            endtitle[1]= endtitle[2];
            endtitle[2]= endtitle[3];
            endtitle[3]= (char)tabcharTitle[i];
            i++;
        }
        byte[] titre = new byte[i];
        for(int j=0; j<i; j++){
            titre[j] = tabcharTitle[j];
        }
        String title = new String(titre);
        return title;
    }
    
    public static byte[] decode(byte[] origine, int hiddenFileSize){
        byte[] tabcharTitle = new byte[hiddenFileSize];
        for (int i=0; i<hiddenFileSize; i++){
            tabcharTitle[i] = (byte) (((origine[(i*4)+startFile]&0x3)<<6) |
                    ((origine[(i*4)+startFile+1]&0x3)<<4) |
                    ((origine[(i*4)+startFile+2]&0x3)<<2) |
                    (origine[(i*4)+startFile+3]&0x3));
        }
        return tabcharTitle;
    }

    public static void saveImage(String name, byte[] resultByte){
        try {
            File file = new File(name);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(resultByte);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            BytePixmap origineBytePixmap = new BytePixmap("result/cache.pgm");
            byte[] origineByte = origineBytePixmap.getBytes();
            int size = getSize(origineByte);
            String name = "result/" + readTitle(origineByte);
            byte[] resultByte = decode(origineByte, size);

            saveImage(name,resultByte);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

