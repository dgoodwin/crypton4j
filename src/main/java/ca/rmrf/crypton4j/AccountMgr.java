package ca.rmrf.crypton4j;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

import org.spongycastle.crypto.PBEParametersGenerator;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.params.KeyParameter;

public class AccountMgr {

    private static final int SIGN_KEY_BIT_LENGTH = 384;

    private static final int [] TEST_SALT = { 632771362,-2080748983,-33610805,864647320,-1281357746,-1878116635,182594016,-1559821437 };

    public Account createAccount() {
//        byte [] keypairSalt = randomBytes(32);
        byte [] keypairSalt = getSalt();
        System.out.println("keypairSalt = " + keypairSalt);
        System.out.println("keypairSalt length = " + keypairSalt.length);
//        System.out.println(Arrays.toString(keypairSalt));
        printBytes(keypairSalt);

        PBEParametersGenerator generator = new PKCS5S2ParametersGenerator(new SHA256Digest());
        generator.init(PBEParametersGenerator.PKCS5PasswordToBytes(("password").toCharArray()), keypairSalt, 1000);
        KeyParameter params = (KeyParameter) generator.generateDerivedMacParameters(256);
        System.out.println("key length = " + params.getKey().length);

        // Expected: -808591712,1780309511,1958824215,1639404619,-1871403641,-2126414256,-1609900554,-1252485205
        printBytes(params.getKey());

        // From stack overflow:
//        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
//        gen.init("password".getBytes("UTF-8"), "salt".getBytes(), 4096);
//        byte[] dk = ((KeyParameter) gen.generateDerivedParameters(256)).getKey();

        return new Account();
    }

    public byte[] randomBytes(int randomBytes) {
        byte[] b = new byte[randomBytes];
        new Random().nextBytes(b);
        return b;
    }

    public byte [] getSalt() {
        ByteBuffer buf = ByteBuffer.allocate(32);
        for (int i = 0; i < TEST_SALT.length; i++) {
            buf.putInt(TEST_SALT[i]);
        }
        return buf.array();
    }

    // Print byte array as integers. (4 bytes per)
    public void printBytes(byte [] bytes) {
//        System.out.println(Arrays.toString(bytes));
        for (int i = 0; i < bytes.length; i = i + 4) {
            byte [] subBytes = Arrays.copyOfRange(bytes, i, i + 4);
//            System.out.println(Arrays.toString(subBytes));
            System.out.println(ByteBuffer.wrap(subBytes).getInt());
        }

    }

}
