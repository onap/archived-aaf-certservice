/*
 * ============LICENSE_START=======================================================
 * PROJECT
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

package org.onap.aaf.certservice.cmpv2client.external;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

    @Test
    public void shouldCorrectlySplitAndTrimString() {
        //given
        String value1 = " T  =  Test";
        List<String> expected1 = Arrays.asList("T", "Test");

        String value2 = "This 123 is 99 tested 12345 string";
        List<String> expected2 = Arrays.asList("This", "is 99 tested", "string");

        //when
        List<String> actual1 = StringUtils.splitAndTrim("=", value1);
        List<String> actual2 = StringUtils.splitAndTrim("[0-9]{3,}", value2);

        //then
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

}