import java.util.Scanner;

/**
 * Created by Furman on 16.05.2017.
 */
public class Main {

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-16");
        printMenu();
    }

    public static void printMenu(){
        System.out.println("1 - Зашифровать слово");
        System.out.println("2 - Расшифровать слово");
        System.out.println("3 - Выйти из программы:");
        System.out.println("Введите цифру(1/2/3):");
        Scanner scanner = new Scanner(System.in);
        int k = -1;
        while ((k<1) || (k>3)){
            k = scanner.nextInt();
        }
        switch (k){
            case 1:
                System.out.println();
                System.out.println("Введите ключ:");
                String key = scanner.next();
                System.out.println("Введите слово:");
                String word = scanner.next();
                System.out.println("\nРезультат:");
                System.out.println(new String(encrypt(word.getBytes(),key.getBytes())));
                System.out.println();
                printMenu();
                break;
            case 2:
                System.out.println();
                System.out.println("Введите ключ:");
                key = scanner.next();
                System.out.println("Введите слово:");
                word = scanner.next();
                System.out.println("\nРезультат:");
                System.out.println(new String(decrypt(word.getBytes(),key.getBytes())));
                System.out.println();
                printMenu();
                break;
            case 3:
                System.exit(0);
                break;
        }
    }

    public static byte[] encrypt(byte[] mas, byte[] key) {

        int k = key.length/4;
        if((key.length%4>0) | (key.length==0)) k++;
        byte[] key1 = new byte[k*4];
        for(int i=0;i<key1.length;i++){
            if(i<key.length)
                key1[i] = key[i];
            else
                key1[i]=0;
        }

        RC6 rc6 = new RC6(key1);
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

        int k = key.length/4;
        if((key.length%4>0) | (key.length==0)) k++;
        byte[] key1 = new byte[k*4];
        for(int i=0;i<key1.length;i++){
            if(i<key.length)
                key1[i] = key[i];
            else
                key1[i]=0;
        }

        RC6 rc6 = new RC6(key1);
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
