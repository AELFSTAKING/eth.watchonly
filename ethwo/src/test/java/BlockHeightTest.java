import io.seg.kofo.ethwo.common.util.ERC20FunctionUtils;
import io.seg.kofo.ethwo.common.util.GethRpcClient;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.TimeUnit;

/**
 * @author: ZhuYuanxiang
 * @create: 2019-04-23 10:34
 */
public class BlockHeightTest {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100,TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(25,200, TimeUnit.SECONDS))
            .build();
    String URL = "http://xxx:8545";

    String TOKEN = "xx" ; //usdt

    @Test
    public void getHeight() {
        String nodeUrlString = "xxx:8545";
        GethRpcClient gethRpcClient = new GethRpcClient(nodeUrlString);
        long blockCount = gethRpcClient.getBlockCount();
        System.out.println(blockCount);
    }

    @Test
    public void allowanceTest() throws Exception {
        HttpService httpService = new HttpService(URL, okHttpClient, false);
//        HttpService httpService = new HttpService(URL);
        Web3j web3j = new JsonRpc2_0Web3j(httpService);

        ERC20FunctionUtils utils = new ERC20FunctionUtils();
        utils.setWeb3j(web3j);



        String owner = "xx";
        String spender = "xx";
        String response =  utils.allowance(TOKEN,owner,spender);

        System.out.println(response);

    }
}

