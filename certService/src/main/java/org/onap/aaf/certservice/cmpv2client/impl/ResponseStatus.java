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

import java.io.Serializable;

/**
 * Encapsulates the possible values for the status of a certificate response. Original response
 * codes from the SCEP protocol.
 */
public final class ResponseStatus implements Serializable {

  private static final long serialVersionUID = -1424581065308042345L;

  /** Request granted */
  public static final ResponseStatus SUCCESS = new ResponseStatus(0);

  /**
   * Request granted with mods. Indicates the requester got something like what you asked for. The
   * requester is responsible for ascertaining the differences.
   */
  public static final ResponseStatus GRANTED_WITH_MODS = new ResponseStatus(1);

  /** Request rejected */
  public static final ResponseStatus FAILURE = new ResponseStatus(2);

  /** Request pending for approval */
  public static final ResponseStatus PENDING = new ResponseStatus(3);

  /** The value actually encoded into the response message as a pkiStatus attribute */
  private final int value;

  private ResponseStatus(final int value) {
    this.value = value;
  }

  /**
   * Gets the value embedded in the response message as a pkiStatus attribute
   *
   * @return the value to use
   */
  public String getStringValue() {
    return Integer.toString(value);
  }

  public int getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object o) {
    boolean ret = false;
    if (this == o) {
      ret = true;
    } else {
      if (o instanceof ResponseStatus) {
        final ResponseStatus status = (ResponseStatus) o;
        if (value == status.getValue()) {
          ret = true;
        }
      }
    }
    return ret;
  }

  @Override
  public int hashCode() {
    return value;
  }
}
