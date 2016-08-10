/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.canterbury.csse.a440.snakes.minecraft;

import android.content.Context;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


/**
 * @author pi
 */
public class SSLClientHardCoded extends SSLClient {


    public SSLClientHardCoded(Context applicationContext, char[] pwd) {
        super(applicationContext, pwd);
    }


    @Override
    protected void setupSSLKeysAndTrusts(Context applicationContext, char[] pwd) {
        try {


            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            String serverCertificate = "-----BEGIN CERTIFICATE-----\n" +
                    "MIIDRTCCAi2gAwIBAgIEYts01jANBgkqhkiG9w0BAQsFADBTMQswCQYDVQQGEwJOWjEKMAgGA1UE\n" +
                    "CBMBYzEKMAgGA1UEBxMBYzEKMAgGA1UEChMBbzELMAkGA1UECxMCb3UxEzARBgNVBAMTCnNvbWVz\n" +
                    "ZXJ2ZXIwHhcNMTYwNzIxMTA0NjQzWhcNMTcwNzE2MTA0NjQzWjBTMQswCQYDVQQGEwJOWjEKMAgG\n" +
                    "A1UECBMBYzEKMAgGA1UEBxMBYzEKMAgGA1UEChMBbzELMAkGA1UECxMCb3UxEzARBgNVBAMTCnNv\n" +
                    "bWVzZXJ2ZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCte1EyLPnervfaOaqaIHU9\n" +
                    "yHXragrn4ZKi26XnLzLnCbdtIn68ktOGarGjO561FB1sPzJ9Hk1eNgxX2WfK8TwTMaB/6CTXQCG9\n" +
                    "qkcCyXaWjXfO0udXZ80kwe8vqImWEbj50KmSvCLI8666Lj2mVoTNW7qRoZABpBVfLbrTmISwt2aq\n" +
                    "upRGI79GAbabSb+isO3+7sEQ6IK46f8B96pD5oiPYWZoXfVSibQDSzzcieThPgLW0H7SsakKjkXG\n" +
                    "Quk0dkw4LGCmJ/Q4Oist6i9of75yauwuudNjYtJwHXQJ2bXTMk2Lv9Xyd9RaOQfoqIkhd+BQsNBb\n" +
                    "1nrTAwCwXdrTakHzAgMBAAGjITAfMB0GA1UdDgQWBBT8QY+bdAdV/tDI2RcIzj0twjI/pTANBgkq\n" +
                    "hkiG9w0BAQsFAAOCAQEAUnrg7wCcgs1qjBXNgi0UGLqo2i6FPJvdVVK2y4Dvm1l2fmoIo+u4dVkC\n" +
                    "Q56NMifJbH9/0vYYtP+cphEFKMMGFg+ZpB51oMBqFHk7utPPYSSyhDpo2rTp5eLhvPbDUp+KM4ds\n" +
                    "Py0To1CNbQY1/THKtPfI3e3xNwy6kkU3C/bH1oboIdcQ1DQFYKZ/NxCcoPPK6mPTKb09pGSZPTuD\n" +
                    "J2rheGQpyebwX2taNc5GKzo9DSp2U08SeZZPy/u9EmB9qE8fgS68gIVHFcXR/JwsnFEciYSmm3gH\n" +
                    "xykaEEqdlhFLdl1YfKQbjc6A9yOeXcu0LuOkTgVw+U3ycuUnuhP6qXcilg==\n" +
                    "-----END CERTIFICATE-----";
            InputStream caInput = new ByteArrayInputStream(serverCertificate.getBytes(StandardCharsets.UTF_8));
            Certificate serverCA;
            try {
                serverCA = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }

            // Create a memory KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);

            keyStore.load(null, null);//nb creates a new one which is empty
            keyStore.setCertificateEntry("serverCA", serverCA);

            String clientPkeyString = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwWumwRHAmJkQANg68IJSCEELK0ZK6mREEvpQnyf7TAoLTD/MeePUvZ0AinMj2dEo91jHcHeQdSRS2xP1GoDWXHqrAwMkI3sBmVBsuPfsd3KYZ9NL5/AKwfdCSB66EcSMz26TmdhB3jNfqpvXhf6T1TCQ5YT/TiXorrNuED6aoUOFUde+OMKdzKmEu7BjGrKE+NUM0Gi5CQ3jVcfPo/uZDhk7RNdzmbu73KMmZHxJN5y9AWb4iHlbVlFwiZhED1ZcmZrY+JluoqtjN8yY7kp9cCoMkVJGuVjSSNB/BECwlcmjpysBvWWSfzp3N3W1fqVBuWkbNwY0LXPpFlWL85PHXAgMBAAECggEAHjdfr5A7BSDEZb5mBdKWuO8wZm0IZDr//7exynrDdWWYas5Tgx4zvLzfPDq3rPwbpUB7Ti/X0LKyTvPe33Uep5vsXYUfAOV4DKVAMYULP8rQeuzjZRgAgo2ene0nTHEousk/JtXq5gynzFEnEi1I4Kms2CXYr1BWMo1tn2GUUw5xlkt5Z0GMQbw35KAeL/DyRT8RNcM8dwpfbdf0crkrzIXOzl9TsNCfKnI3NZ1X8DOWVFCnZiJMpyfVYDK4oreor6I70OEp2uczG+JoneKZelgwgLYAjjcr8mqCWnc27CoZ8k9oH1OjNKdF6ZxoICu1mlPEb6lZeNjJEUn90PNL4QKBgQD6Rh7+p4Is8x7QZPXl/tbiJ7IQCcqFHyoT089OycvpbejVh3KWHHRdD+CiJp+Tlap4I7Fam/4neznZMcJFWOos7N4OzI5/awHueQPnatPtfMliU+2c3Jr6KIGWZM7QmA2ZEfSyya+nr6da1aobbeeYoG+HCv8JZBzWx+X/yrUpsQKBgQD12rJNIWCose5+diE9EEJN8yRyrmIcN9wzISlFDt4Fc2jb727iPIfSl7nzEMqUBJhcQuBjj8Rv0AnFRvQ+1kFosXEuUpWRupEFVJuVrs7PTHtcgYbD972VpvzeCBhskV7H0FY96Ojj7JCzk7xS09b6Y9ObpGDaBtG2XV5hVWMuBwKBgQDYXtUfDgyh9dJ3EkHxBKAcR3tPGFfpPScwmxcII7hR6D66lG5BTvpfFoH7Te76NhN56EnFb0WMNqGtn7I4KAXUrzfPjZPInue9lwwD/zyXfiHRC4RK8AJgMbLPJfoTJtHiuz5Vb76X92l1Q3HcYukt785b7urM4Kt6GLpEqah0EQKBgQDqsprR3NIsWKfHG7hBVdsmFL2vqN9J9t2EBd0i6r1yUKlChADDgmta07MU4euxf+1+7ezNvroUz3H0XPbfYaPRcMsOIJJeKs80Wn+oVddht65wMcpYG5FlYqM0xl+ijOgBMdaShVyF8Rh2Bcua+lXYdCHXDAXVPPtH7zwhCOWEUwKBgEPdt4iiXYkaUk/3brRHiFO8svRiwiClpjZyaDRG7VLQHwXYYX0yjQy4Xhbrp0D2QmGKsJmFK7vudHrwwX9/NrRUcnMPoA2CTrWVJjBlMZFiRYsVIJbkly2gNVxgulKTPL/+ZmLkGLOCBgP2QZTJ5LvUeWegf4k3sqfiUEwWTWHn";

            String clientCert = "MIIDRTCCAi2gAwIBAgIELqU3HDANBgkqhkiG9w0BAQsFADBTMQswCQYDVQQGEwJOWjEKMAgGA1UECBMBYzEKMAgGA1UEBxMBYzEKMAgGA1UEChMBbzELMAkGA1UECxMCb3UxEzARBgNVBAMTCnNvbWVjbGllbnQwHhcNMTYwNzIxMTA0NzQ3WhcNMTcwNzE2MTA0NzQ3WjBTMQswCQYDVQQGEwJOWjEKMAgGA1UECBMBYzEKMAgGA1UEBxMBYzEKMAgGA1UEChMBbzELMAkGA1UECxMCb3UxEzARBgNVBAMTCnNvbWVjbGllbnQwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDwWumwRHAmJkQANg68IJSCEELK0ZK6mREEvpQnyf7TAoLTD/MeePUvZ0AinMj2dEo91jHcHeQdSRS2xP1GoDWXHqrAwMkI3sBmVBsuPfsd3KYZ9NL5/AKwfdCSB66EcSMz26TmdhB3jNfqpvXhf6T1TCQ5YT/TiXorrNuED6aoUOFUde+OMKdzKmEu7BjGrKE+NUM0Gi5CQ3jVcfPo/uZDhk7RNdzmbu73KMmZHxJN5y9AWb4iHlbVlFwiZhED1ZcmZrY+JluoqtjN8yY7kp9cCoMkVJGuVjSSNB/BECwlcmjpysBvWWSfzp3N3W1fqVBuWkbNwY0LXPpFlWL85PHXAgMBAAGjITAfMB0GA1UdDgQWBBSKCqQ8IUXAGcJ0TXbiUbT/N38XBDANBgkqhkiG9w0BAQsFAAOCAQEAJi9uhFSSn8tlLEUuXxRlbesz57iMMTkZNDfhLF78byKPy2CDj1GK6KaAUl0maqvNLSNlcVSQyx/47/wzo9mUb+5BE65YV5kG/7OpEnyLHZ2+/DSrhmQLpj8YXng46NKZXuoaDvztN7RAl4v1Xr36w0Rn9tLXjtzBqddYR1CM6NP4/Pf6kqLB4llfmwx49WW0Hgs9H/vr51eGZU05GLdPPoUBLTuqoon0pyz/jPuqvToa97YOmL4W/9hX7TZoi0Qy3AHWqNuoI+W6O8G8mEy40MCzg52Xr8LfEIaKjVQpbxnkOrvOQLS4IJupJS10eRtXWk0pWYR6O87hrIdrQtKhsA==";

            Certificate certificate = cf.generateCertificate(new ByteArrayInputStream(Base64.decode(clientCert, 0)));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // or "EC" or whatever

            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(clientPkeyString, 0)));

            keyStore.setKeyEntry("flubber", privateKey, pwd, new Certificate[]{certificate});

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

            kmf.init(keyStore, pwd);

            // Create an SSLContext that uses our kmf and TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            socketFactory = context.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            //System.exit(1);// may be a bit harsh
        }
    }
}
