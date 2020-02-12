/**
 * ============LICENSE_START====================================================
 * org.onap.aaf
 * ===========================================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.aaf.certservice.client.cert.external;

/**
 * A Trans is like an Env, however, it's purpose it to track the Transient 
 * Data associated with Transactions, or other short term elements.
 * 
 * Any Object implementing Trans should expect to go in an out of scope quickly
 * 
 * Implementations should also overload the concepts of "Start", etc and build up
 * and Audit Log, so it can implement "metric" below
 * 
 * All Transactions (i.e. a call to a service) will need these items.
 * 
 * @author Jonathan
 *
 */
public interface Trans {

    /**
     * Start a Time Trail with differentiation by flag.  This can be Defined By above flags or combined with
     * app flag definitions
     */
    TimeTaken start(String name, int flag, Object ... values);

}