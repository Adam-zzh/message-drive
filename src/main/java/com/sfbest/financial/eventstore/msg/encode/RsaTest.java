package com.sfbest.financial.eventstore.msg.encode;


import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * 〈一句话功能简述〉<br>
 * 〈p12文件研究〉
 *
 * @author ZZH
 * @create 2022/3/18
 * @since 1.0.0
 */
public class RsaTest {


    public static String testpfx = "zzh.p12"; //商户证书私钥路径
    public static String testpass = "123456"; //商户证书私钥密码


    public static void main(String[] args) throws Exception {
        String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQChiT56lqhRJ39Vp4AXObWmAof2c2YD//HRUlzncosZnAprOY0VyG7Aigjw58nc+B+OTd/UP/JYt5ecG53LascEfiJXLTCXXoIbpzTXsYWNKcVUjjBSO28wX+4lrpq7TsbCA1voTSrHY4lZygKyYHCSCHyhgom4d60Zh/3sPU/5Y2pTzlCrs1wnJbOO6ibUdFxSkvPhdxqVikmqgFWTCK8K8LDJJVTuTN7FB7m96KH31zX5LAyyc8n+NUqmOgXk1sFgEmcy6Tc56Z3j30Gn+dnzRJUVhOp5yXmhVIXx98HxLmzYBHKZJuDoYXgtk3ym9GXOTjTcOYmW8VnoFVhQ/ojFAgMBAAECggEAE9rXWnYI0Ot2k+6h/eN9V58EBd9/8InrDSbUfdZEZqzD/b311hxBZgURaEWf4uXUlcct9dwOS6f1SJ4pslu42UKFB2C/POZu6OXlHDCZERV18xivCSHTtDHLscdZBCDV/qQvDcnpB4JNMEN2OrqZMOSTfRPwcu4IJr/iZBZ9LVKVfgYyb3E4ddZ5LeboJRPkRKxStTkyc35V0Alehtii72UGx8uZI9t01W5Wl0VYIhxmpvNeIqlyU6guN4l3PWHsWeZw48xIN0G137A3N2oHR7gC4vf0LDQCGfULkMlpmcwY7twmBoM+VpUihqAnsp4oVuFnRYRivtnu3g+A1tZzWQKBgQDXGWAzhcqr5prKE83TyD64IsrsYsPhehoAZH6cjSv/T3LhyGo0JSLYNwQSrJwG8h6NQmbgcen+8U/VYgiZu5Xo6xsVXkWSNA1Huzdhcn6cPJEg622995nxnJk1onvMOnr+0Yp0rZfHdSwnabaXCn0xMc3zuRKY9/oz0ZkiP+XHBwKBgQDAQIVHESglYaYrFZ+IYBY9/Bvy2iqe+PbCeKXhnCU3PftrZhkE9rJiDbDldDOEpS1lK3zT7GXJXNw+7mWJ2/gkFALnp/CzVrDN3LLCHT8csQY2GS2Nn7FwZXSQo5CP766ImwECIvF8KjGzHy1wVsQFA5U5exaTS9P81ly4YDsS0wKBgElkHh/WQY2flCbo1OyywTugm25GUKFpQN/ragvOWzm0iPUipaH/HcF6HAinHmF8URJPmrxwAa/79ApRda/GgmLDfS0wSjqVSga0iTPVlf+G5tm9mmcbyUnX7aqQMv8+4OJ66mvfIw9qGlOkTSmB6a9UV/oVppPM4HC3zjESbjsXAoGAY+9mGGoXt07KRO0pj4d7KF9r29KCgz6hbreuatdzFQM4NTzOVqEyWvwI7013FkWSkluHuNXSl1iY+HGOAJqCo6ZvWCIZYe/3WhuWJId1biGrO+XcoxjW8LkcXa9YjymlTUddv9GQO93nFdhoE1RW8tBI+qc8Nfcdy/lWG1WSf5cCgYBcEk9ndmgEvz2u8eNUi+IrgzKc2A4IcL+31ZiBfQUvYV+xnRlKfjCQox3KhdL3lAvTVGNv9WJi1k6G6WBZARdx3qAaGuOaTB5ILt5MW/8NPb9UlTzo5/p7yhwai0tnupQGMz/GDVKviIUyX62L6ujAwGM93skt5y0VLhd16hu1kQ==";
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey1 = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes())));
        System.out.println(privateKey1);
        System.out.println(Base64.encodeBase64String(keyFactory.translateKey(privateKey1).getEncoded()));



        /*p12证书解析*/
        /*KeyStore ks = KeyStore.getInstance("PKCS12");
		InputStream fiKeyFile = RsaTest.class.getClassLoader().getResourceAsStream(testpfx);
		try {
			ks.load(fiKeyFile, testpass.toCharArray());
		} catch (Exception ex) {
			if (fiKeyFile != null)
				fiKeyFile.close();
			throw new RuntimeException("加载证书时出错" + ex.getMessage(), ex);
		}
		Enumeration<String> myEnum = ks.aliases();
		String keyAlias = null;
		RSAPrivateCrtKey prikey = null;
		while (myEnum.hasMoreElements()) {
			keyAlias = (String) myEnum.nextElement();
			if (ks.isKeyEntry(keyAlias)) {
				prikey = (RSAPrivateCrtKey) ks.getKey(keyAlias, testpass.toCharArray());
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				System.out.println(Base64.encode(keyFactory.translateKey(prikey).getEncoded()).toString());
			}
		}*/

    }

}