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

public class Rrd {

    private String tag;
    private String value;
    private ASN1ObjectIdentifier aoi;

    public String getValue() {
        return value;
    }

    public ASN1ObjectIdentifier getAoi() {
        return aoi;
    }

    public Rrd(final String tagValue) throws CertException {
        String[] tv = Split.splitTrim('=', tagValue);
        switch (tv[0].toLowerCase()) {
            case "cn":
                aoi = BCStyle.CN;
                break;
            case "c":
                aoi = BCStyle.C;
                break;
            case "st":
                aoi = BCStyle.ST;
                break;
            case "l":
                aoi = BCStyle.L;
                break;
            case "o":
                aoi = BCStyle.O;
                break;
            case "ou":
                aoi = BCStyle.OU;
                break;
            case "dc":
                aoi = BCStyle.DC;
                break;
            case "gn":
                aoi = BCStyle.GIVENNAME;
                break;
            case "sn":
                aoi = BCStyle.SN;
                break;
            case "email":
            case "e":
            case "emailaddress":
                aoi = BCStyle.EmailAddress;
                break; // should be SAN extension
            case "initials":
                aoi = BCStyle.INITIALS;
                break;
            case "pseudonym":
                aoi = BCStyle.PSEUDONYM;
                break;
            case "generationQualifier":
                aoi = BCStyle.GENERATION;
                break;
            case "serialNumber":
                aoi = BCStyle.SERIALNUMBER;
                break;
            default:
                throw new CertException(
                        "Unknown ASN1ObjectIdentifier for " + tv[0] + " in " + tagValue);
        }
        tag = tv[0];
        value = tv[1];
    }

    /**
     * Parse various forms of DNs into appropriate RDNs, which have the ASN1ObjectIdentifier
     *
     * @param delim
     * @param dnString
     * @return
     * @throws CertException
     */
    public static List<Rrd> parse(final char delim, final String dnString) throws CertException {
        List<Rrd> lrnd = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < dnString.length(); ++i) {
            char currentCharacter = dnString.charAt(i);
            if (inQuotes) {
                if ('"' == currentCharacter) {
                    inQuotes = false;
                } else {
                    sb.append(dnString.charAt(i));
                }
            } else {
                if ('"' == currentCharacter) {
                    inQuotes = true;
                } else if (delim == currentCharacter) {
                    if (sb.length() > 0) {
                        lrnd.add(new Rrd(sb.toString()));
                        sb.setLength(0);
                    }
                } else {
                    sb.append(dnString.charAt(i));
                }
            }
        }
        if (sb.indexOf("=") > 0) {
            lrnd.add(new Rrd(sb.toString()));
        }
        return lrnd;
    }

    @Override
    public String toString() {
        return tag + '=' + value;
    }
}
