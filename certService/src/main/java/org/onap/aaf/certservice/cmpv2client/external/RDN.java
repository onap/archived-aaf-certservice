/*
 * ============LICENSE_START====================================================
 * org.onap.aaf
 * ===========================================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 *
 * Modifications Copyright (C) 2019 IBM.
 * ===========================================================================
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
 * limitations under the License.
 * ============LICENSE_END====================================================
 *
 */
package org.onap.aaf.certservice.cmpv2client.external;

import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.CertException;

public class RDN {

    private String tag;
    private String value;
    private ASN1ObjectIdentifier aoi;

    public String getValue() {
        return value;
    }

    public ASN1ObjectIdentifier getAoi() {
        return aoi;
    }

    public RDN(final String tag, final String value) throws CertException {
        this.tag = tag;
        this.value = value;
        this.aoi = getAoi(tag);
    }

    public RDN(final String tagValue) throws CertException {
        List<String> tv = StringUtils.splitAndTrim("=", tagValue);
        this.tag = tv.get(0);
        this.value = tv.get(1);
        this.aoi = getAoi(this.tag);
    }

    /**
     * Parse various forms of DNs into appropriate RDNs, which have the ASN1ObjectIdentifier
     *
     * @param delim
     * @param dnString
     * @return
     * @throws CertException
     */
    public static List<RDN> parse(final char delim, final String dnString) throws CertException {
        List<RDN> lrnd = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < dnString.length(); ++i) {
            char c = dnString.charAt(i);
            if (inQuotes) {
                if ('"' == c) {
                    inQuotes = false;
                } else {
                    sb.append(dnString.charAt(i));
                }
            } else {
                if ('"' == c) {
                    inQuotes = true;
                } else if (delim == c) {
                    if (sb.length() > 0) {
                        lrnd.add(new RDN(sb.toString()));
                        sb.setLength(0);
                    }
                } else {
                    sb.append(dnString.charAt(i));
                }
            }
        }
        if (sb.indexOf("=") > 0) {
            lrnd.add(new RDN(sb.toString()));
        }
        return lrnd;
    }

    @Override
    public String toString() {
        return tag + '=' + value;
    }

    private ASN1ObjectIdentifier getAoi(String tag) throws CertException {
        switch (tag.toLowerCase()) {
            case "cn":
                return BCStyle.CN;
            case "c":
                return BCStyle.C;
            case "st":
                return BCStyle.ST;
            case "l":
                return BCStyle.L;
            case "o":
                return BCStyle.O;
            case "ou":
                return BCStyle.OU;
            case "dc":
                return BCStyle.DC;
            case "gn":
                return BCStyle.GIVENNAME;
            case "sn":
                return BCStyle.SN;
            case "email":
            case "e":
            case "emailaddress":
                return BCStyle.EmailAddress;
            case "initials":
                return BCStyle.INITIALS;
            case "pseudonym":
                return BCStyle.PSEUDONYM;
            case "generationQualifier":
                return BCStyle.GENERATION;
            case "serialNumber":
                return BCStyle.SERIALNUMBER;
            default:
                throw new CertException(
                        "Unknown ASN1ObjectIdentifier for tag " + tag);
        }
    }
}
