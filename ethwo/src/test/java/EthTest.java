import io.seg.kofo.ethwo.common.util.HandleErrorUtils;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionDecoder;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class EthTest {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10,TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(25,10, TimeUnit.SECONDS))
            .build();

    String URL = "http://xxx:8545";
    @Test
    public void test() throws IOException {
        for (int i = 0 ; i < 2000 ; i ++){
//            HttpService httpService = new HttpService(URL, okHttpClient, false);
            HttpService httpService = new HttpService(URL);
            Web3j web3j = new JsonRpc2_0Web3j(httpService);

            EthBlockNumber blockNumber =  web3j.ethBlockNumber().send();

            System.out.println(i);
        }


    }

    @Test
    public void test2(){
        HandleErrorUtils utils = new HandleErrorUtils();
        utils.construct();
    }

    @Test
    public void test4(){
        String blockCountStr = "0xFFFFFFFFFFFFF";
        long  aa = Long.parseLong(io.seg.kofo.ethwo.common.util.Numeric.toBigInt(blockCountStr).toString(),16);
        System.out.println(aa);
    }

    @Test
    public void test3() throws IOException {
        Credentials credentials = Credentials.create("xxx");
        String address  = credentials.getAddress();
        Web3j web3j = new JsonRpc2_0Web3j(new HttpService(URL));
        EthGetTransactionCount count = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
        System.out.println("address:"+address+" current nonce:"+count.getTransactionCount().longValue());

        String rawTransaction = "xxx";

        RawTransaction rawTransaction1 = TransactionDecoder.decode(rawTransaction);
        BigInteger originNonce = rawTransaction1.getNonce();
        System.out.println("tx nonce:"+originNonce.longValue());


        String signedRawTransaction = Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction1, credentials));

        System.out.println(signedRawTransaction);

    }
}
