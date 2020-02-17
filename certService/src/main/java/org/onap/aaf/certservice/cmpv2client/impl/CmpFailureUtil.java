/*
 * Copyright (C) 2019 Ericsson Software Technology AB. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.onap.aaf.certservice.cmpv2client.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.cmp.PKIFreeText;
import org.bouncycastle.asn1.cmp.PKIStatus;

public class CmpFailureUtil {

  private static final Map<Integer, String> STATUS_TEXT_MAP = new HashMap<>();

  private static final String[] FAILUREINFO_TEXTS =
      new String[] {
        // 0 - 3
        "incorrectData", "wrongAuthority", "badDataFormat", "badCertId",
        // 4 - 7
        "badTime", "badRequest", "badMessageCheck", "badAlg",
        // 8 - 11
        "unacceptedPolicy", "timeNotAvailable", "badRecipientNonce", "wrongIntegrity",
        // 12 - 15
        "certConfirmed", "certRevoked", "badPOP", "missingTimeStamp",
        // 16 - 19
        "notAuthorized", "unsupportedVersion", "transactionIdInUse", "signerNotTrusted",
        // 20 - 23
        "badCertTemplate", "badSenderNonce", "addInfoNotAvailable", "unacceptedExtension",
        // 24 -27
        "-", "-", "-", "-",
        // 28 - 31
        "-", "duplicateCertReq", "systemFailure", "systemUnavail"
      };

  static {
    STATUS_TEXT_MAP.put(-1, "ResponseError");
    STATUS_TEXT_MAP.put(PKIStatus.GRANTED, "Accepted");
    STATUS_TEXT_MAP.put(PKIStatus.REJECTION, "Rejection");
    STATUS_TEXT_MAP.put(PKIStatus.REVOCATION_NOTIFICATION, "RevocationNotification");
  }

  private CmpFailureUtil() {}

  public static String formatPkiStatusInfo(org.bouncycastle.asn1.cmp.PKIStatusInfo pkiStatusInfo) {
    int status = CmpUtil.notNull(pkiStatusInfo, "pkiStatusInfo").getStatus().intValue();
    int failureInfo = pkiStatusInfo.getFailInfo().intValue();
    PKIFreeText text = pkiStatusInfo.getStatusString();
    String statusMessage = (text == null) ? null : text.getStringAt(0).getString();
    return formatPkiStatusInfo(status, failureInfo, statusMessage);
  }

  public static String formatPkiStatusInfo(int status, int failureInfo, String statusMessage) {
    return CmpUtil.concatObjects(
        200,
        "PKIStatusInfo {status = ",
        status,
        " (",
        STATUS_TEXT_MAP.get(status),
        "), ",
        "failureInfo = ",
        failureInfo,
        " (",
        getFailureInfoText(failureInfo),
        "), ",
        "statusMessage = ",
        statusMessage,
        "}");
  }

  public static String getFailureInfoText(int failureInfo) {
    BigInteger bi = BigInteger.valueOf(failureInfo);
    final int n = Math.min(bi.bitLength(), FAILUREINFO_TEXTS.length);

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++) {
      if (bi.testBit(i)) {
        sb.append(", ").append(FAILUREINFO_TEXTS[i]);
      }
    }

    return (sb.length() < 3) ? "" : sb.substring(2);
  }
}
