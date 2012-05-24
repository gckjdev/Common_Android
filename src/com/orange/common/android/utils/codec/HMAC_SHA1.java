package com.orange.common.android.utils.codec;

/*
 * Copyright 2007 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * The HMAC-SHA1 signature method.
 * 
 * @author John Kristian
 */
public class HMAC_SHA1 {

    private static final String MAC_NAME = "HmacSHA1";

    public static byte[] encode(String utf8String, String signatureKey)
            throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] keyBytes = signatureKey.getBytes("UTF-8"); /** ISO-8859-1 or US-ASCII would work, too. */
        SecretKey key = new SecretKeySpec(keyBytes, MAC_NAME);
        Mac mac = Mac.getInstance(MAC_NAME);
        mac.init(key);
        byte[] text = utf8String.getBytes("UTF-8");
        return mac.doFinal(text);
    }

}
