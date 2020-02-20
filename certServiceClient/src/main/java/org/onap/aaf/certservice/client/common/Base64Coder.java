package org.onap.aaf.certservice.client.common;

import org.bouncycastle.util.encoders.Base64;

public class Base64Coder {
    public static String encode(String string){
        return new String(Base64.encode(string.getBytes()));
    }
}
