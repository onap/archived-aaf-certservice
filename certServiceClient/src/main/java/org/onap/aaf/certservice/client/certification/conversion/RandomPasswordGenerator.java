/*============LICENSE_START=======================================================
 * aaf-certservice-client
 * ================================================================================
 * Copyright (C) 2020 Nokia. All rights reserved.
 * ================================================================================
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
 * ============LICENSE_END=========================================================
 */

package org.onap.aaf.certservice.client.certification.conversion;

import java.security.SecureRandom;
import org.apache.commons.lang3.RandomStringUtils;

class RandomPasswordGenerator {

    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARS = "_$#";
    private static final char[] SET_OF_CHARS = (ALPHA + ALPHA.toUpperCase() + NUMBERS + SPECIAL_CHARS).toCharArray();

    String generate(int passwordLength) {
        final char startPositionInAsciiChars = 0;
        final char endPositionInAsciiChars = 0;
        final boolean useLettersOnly = false;
        final boolean useNumbersOnly = false;

        return RandomStringUtils.random(
            passwordLength,
            startPositionInAsciiChars,
            endPositionInAsciiChars,
            useLettersOnly,
            useNumbersOnly,
            SET_OF_CHARS,
            new SecureRandom());
    }
}

