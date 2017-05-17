
/**
 * Created by Furman on 16.05.2017.
 */
public class Main {

    public static void main(String[] args) {
        try {
            String key = "dsadasdfasgfwaet235214123";
            RC6 rc6 = new RC6(key.getBytes());
            printByteMas(rc6.decryptBlock(rc6.encryptBlock(new byte[]{24,43,45,42,-42,-67,-42,-52,-23,127,23,24,23,87,45,91})));
        }
        catch (Exception e){}
    }

    public static byte[] encrypt(byte[] mas, byte[] key) {
        RC6 rc6 = new RC6(key);
        byte[] block = new byte[16];
        byte[] res = new byte[0];
        int n = 0;
        n += mas.length / 16;
        if (mas.length % 16 != 0) n++;
        for (int i = 0, off = 0; i < n; i++) {
            for (int j = 0; j < 16; j++) {
                if (off < mas.length)
                    block[j] = mas[off++];
                else
                    block[j] = 0;
            }
            res = append(res,rc6.encryptBlock(block));
        }
        return res;
    }

    public static byte[] decrypt(byte[] mas, byte[] key) {
        RC6 rc6 = new RC6(key);
        byte[] block = new byte[16];
        byte[] res = new byte[0];
        int n = 0;
        n += mas.length / 16;
        if (mas.length % 16 != 0) n++;
        for (int i = 0, off = 0; i < n; i++) {
            for (int j = 0; j < 16; j++) {
                if (off < mas.length)
                    block[j] = mas[off++];
                else
                    block[j] = 0;
            }
            res = append(res,rc6.decryptBlock(block));
        }
        return res;
    }

    public static byte[] append(byte[] current, byte[] add){
        byte[] tmp = new byte[current.length+add.length];
        for(int i=0;i<current.length;i++)
            tmp[i] = current[i];
        for(int i=0;i<add.length;i++)
            tmp[i+current.length] = add[i];
        return tmp;
    }

    public static void printByteMas(byte[] input) {
        for (int i = 0; i < input.length; i++) {
            System.out.print(input[i] + " ");
        }
        System.out.println();
    }


}
