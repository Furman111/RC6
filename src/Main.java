
/**
 * Created by Furman on 16.05.2017.
 */
public class Main {

    public static void main(String[] args){
        String ss = "123";
        byte[] mas = ss.getBytes();
        byte[] block = new byte[16];
        for(int i=0;i<16;i++){
            if(i<mas.length)
                block[i] = mas[i];
            else
                block[i] = 0;
        }
        RC6 rc6 = new RC6("1234".getBytes());
        RC6 newRC = new RC6("1234".getBytes());
        System.out.println(new String(rc6.decryptBlock(newRC.encryptBlock(block))));
    }

    public static void printByteMas(byte[] input){
        for(int i=0;i<input.length;i++){
            System.out.print(input[i]+" ");
        }
        System.out.println();
    }


}
