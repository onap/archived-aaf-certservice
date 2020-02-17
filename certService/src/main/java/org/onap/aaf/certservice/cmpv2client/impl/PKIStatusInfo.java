/*
 * Copyright (C) 2019 Nordix Foundation. All rights reserved.
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

import org.bouncycastle.asn1.cmp.PKIFreeText;

public class PKIStatusInfo {

  private final int status;

  private final int pkiFailureInfo;

  private final String statusMessage;

  public PKIStatusInfo(int status, int pkiFailureInfo, String statusMessage) {
    this.status = status;
    this.pkiFailureInfo = pkiFailureInfo;
    this.statusMessage = statusMessage;
  }

  public PKIStatusInfo(int status) {
    this.status = status;
    this.pkiFailureInfo = 0;
    this.statusMessage = null;
  }

  public PKIStatusInfo(org.bouncycastle.asn1.cmp.PKIStatusInfo bcPkiStatusInfo) {
    CmpUtil.notNull(bcPkiStatusInfo, "bcPkiStatusInfo");

    this.status = bcPkiStatusInfo.getStatus().intValue();
    this.pkiFailureInfo =
        (bcPkiStatusInfo.getFailInfo() == null) ? 0 : bcPkiStatusInfo.getFailInfo().intValue();
    PKIFreeText text = bcPkiStatusInfo.getStatusString();
    this.statusMessage = (text == null) ? null : text.getStringAt(0).getString();
  }

  public int status() {
    return status;
  }

  public int pkiFailureInfo() {
    return pkiFailureInfo;
  }

  public String statusMessage() {
    return statusMessage;
  }

  @Override
  public String toString() {
    return CmpFailureUtil.formatPkiStatusInfo(status, pkiFailureInfo, statusMessage);
  }
}
