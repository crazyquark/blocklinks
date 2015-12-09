package blocklinks.provisioner.ethereum;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;

public class RpcClient {
	private RpcInterface rpc;
	public static String hostname = "localhost:8545";
	
	public RpcClient() {
		URL url;
		try {
			url = new URL("http://" + hostname + "/");
			JsonRpcHttpClient rpcClient = new JsonRpcHttpClient(url);
			rpc = ProxyUtil.createClientProxy(
						getClass().getClassLoader(),
						RpcInterface.class,
						rpcClient
					);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 
	}
	
	public String getCoinbase() {
		return rpc.eth_coinbase();
	}
	
	public boolean unlockAccount(String address, String secret) {
		return rpc.personal_unlockAccount(address, secret);
	}
	
	public String sendTransaction(String from, String fromSecret, String to, BigInteger valueWei) {
		boolean unlock = rpc.personal_unlockAccount(from, fromSecret);
		
		if (unlock) {
			return rpc.eth_sendTransaction(from, to, valueWei);
		}
		
		return null;
	}
	
	public BigInteger getBalance(String address) {
		String balance = rpc.eth_getBalance(address);
	
		// remove 0x
		if (balance.startsWith("0x")) {
			balance = balance.substring(2);
		}
		
		// remove starting zeros
		while (balance.length() > 0 &&  balance.charAt(0) == '0') {
			balance = balance.substring(1);
		}
		
		return new BigInteger(balance, 16);
	}
}
