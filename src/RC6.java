/**
 * Created by Furman on 16.05.2017.
 */
public class RC6 {

    public static final int W = 32;
    public static final int R = 20;
    private static final int Pw = 0xb7e15163, Qw = 0x9e3779b9;

    private byte[] key;
    private int[] S;

    public RC6(byte[] key) {
        this.key = key.clone();S = generateSubkeys(this.key);
    }

    public byte[] encryptBlock(byte[] input){

        byte[] tmp = new byte[input.length];
        int t,u;
        int aux;
        int[] data = new int[input.length/4];
        for(int i =0;i<data.length;i++)
            data[i] = 0;
        int off = 0;
        for(int i=0;i<data.length;i++){
            data[i] = ((input[off++]&0xff))|
                    ((input[off++]&0xff) << 8)|
                    ((input[off++]&0xff) << 16)|
                    ((input[off++]&0xff) << 24);
        }
        int A = data[0],B = data[1],C = data[2],D = data[3];

        B = B + S[0];
        D = D + S[1];
        for(int i = 1;i<=R;i++){
            t = rotl(B*(2*B+1),5);
            u = rotl(D*(2*D+1),5);
            A = rotl(A^t,u)+S[2*i];
            C = rotl(C^u,t)+S[2*i+1];

            aux = A;
            A = B;
            B = C;
            C = D;
            D = aux;
        }
        A = A + S[2*R+2];
        C = C + S[2*R+3];
        data[0] = A;data[1] = B;data[2] = C;data[3] = D;

        for(int i = 0;i<tmp.length;i++){
            tmp[i] = (byte)((data[i/4] >>> (i%4)*8) & 0xff);
        }

        return tmp;
    }

    public byte[] decryptBlock(byte[] input){
        byte[] tmp = new byte[input.length];
        int t,u;
        int aux;
        int[] data = new int[input.length/4];
        for(int i =0;i<data.length;i++)
            data[i] = 0;
        int off = 0;
        for(int i=0;i<data.length;i++){
            data[i] = ((input[off++]&0xff))|
                    ((input[off++]&0xff) << 8)|
                    ((input[off++]&0xff) << 16)|
                    ((input[off++]&0xff) << 24);
        }

        int A = data[0],B = data[1],C = data[2],D = data[3];

        C = C - S[2*R+3];
        A = A - S[2*R+2];
        for(int i = R;i>=1;i--){
            aux = D;
            D = C;
            C = B;
            B = A;
            A = aux;

            u = rotl(D*(2*D+1),5);
            t = rotl(B*(2*B + 1),5);
            C = rotr(C-S[2*i + 1],t) ^ u;
            A = rotr(A-S[2*i],u) ^ t;
        }
        D = D - S[1];
        B = B - S[0];

        data[0] = A;data[1] = B;data[2] = C;data[3] = D;

        for(int i = 0;i<tmp.length;i++){
            tmp[i] = (byte)((data[i/4] >>> (i%4)*8) & 0xff);
        }

        return tmp;
    }

    private static int rotl(int val, int pas) {
        return (val << pas) | (val >>> (32 - pas));
    }

    private static int rotr(int val, int pas) {
        return (val >>> pas) | (val << (32 - pas));
    }

    private static int[] convBytesWords(byte[] key, int c) {
        int[] tmp = new int[c];
        for (int i = 0; i < tmp.length; i++)
            tmp[i] = 0;

        for (int i = 0, off = 0; i < c; i++)
            tmp[i] = ((key[off++] & 0xFF))|
                    ((key[off++] & 0xFF) << 8)|
                    ((key[off++] & 0xFF) << 16)|
                    ((key[off++] & 0xFF) << 24);

        return tmp;
    }

    private static int[] generateSubkeys(byte[] key) {

        int u = W / 8;
        int c = key.length / u;
        int t = 2 * R + 4;

        int[] L = convBytesWords(key, c);

        int[] S = new int[t];
        S[0] = Pw;
        for (int i = 1; i < t; i++)
            S[i] = S[i - 1] + Qw;

        int A = 0;
        int B = 0;
        int k = 0, j = 0;

        int v = 3 * Math.max(c, t);

        for (int i = 0; i < v; i++) {
            A = S[k] = rotl((S[k] + A + B), 3);
            B = L[j] = rotl(L[j] + A + B, A + B);
            k = (k + 1) % t;
            j = (j + 1) % c;

        }

        return S;
    }
}
