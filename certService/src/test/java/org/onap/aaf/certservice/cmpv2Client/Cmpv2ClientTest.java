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
package org.onap.aaf.certservice.cmpv2Client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.bouncycastle.cert.CertException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.onap.aaf.certservice.cmpv2client.exceptions.CmpClientException;
import org.onap.aaf.certservice.cmpv2client.external.CSRMeta;
import org.onap.aaf.certservice.cmpv2client.external.RDN;
import org.onap.aaf.certservice.cmpv2client.impl.CmpClientImpl;

class Cmpv2ClientTest {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  private CSRMeta csrMeta;
  private Date notBefore;
  private Date notAfter;

  @Mock KeyPairGenerator kpg;

  @Mock X509Certificate cert;

  @Mock CloseableHttpClient httpClient;

  @Mock CloseableHttpResponse httpResponse;

  @Mock HttpEntity httpEntity;

  private static KeyPair keyPair;
  private static ArrayList<RDN> rdns;

  @BeforeEach
  void setUp()
      throws NoSuchProviderException, NoSuchAlgorithmException, IOException,
          InvalidKeySpecException {
    KeyPairGenerator keyGenerator;
    keyGenerator = KeyPairGenerator.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
    keyGenerator.initialize(2048);
    keyPair = loadKeyPair();
    rdns = new ArrayList<>();
    try {
      rdns.add(new RDN("O=CommonCompany"));
    } catch (CertException e) {
      e.printStackTrace();
    }
    initMocks(this);
  }

  public KeyPair loadKeyPair()
      throws IOException, NoSuchAlgorithmException, InvalidKeySpecException,
          NoSuchProviderException {

    final InputStream privateInputStream = this.getClass().getResourceAsStream("/privateKey");
    final InputStream publicInputStream = this.getClass().getResourceAsStream("/publicKey");
    BufferedInputStream bis = new BufferedInputStream(privateInputStream);
    byte[] privateBytes = IOUtils.toByteArray(bis);
    bis = new BufferedInputStream(publicInputStream);
    byte[] publicBytes = IOUtils.toByteArray(bis);

    KeyFactory keyFactory = KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicBytes);
    PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateBytes);
    PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

    return new KeyPair(publicKey, privateKey);
  }

  @Test
  void shouldReturnValidPkiMessageWhenCreateCertificateRequestMessageMethodCalledWithValidCsr()
      throws Exception {
    // given
    Date beforeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2019/11/11 12:00:00");
    Date afterDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2020/11/11 12:00:00");
    setCsrMetaValuesAndDateValues(
        rdns,
        "CN=CommonName",
        "CN=ManagementCA",
        "CommonName.com",
        "CommonName@cn.com",
        "mypassword",
        "http://127.0.0.1/ejbca/publicweb/cmp/cmp",
        "senderKID",
        beforeDate,
        afterDate);
    when(httpClient.execute(any())).thenReturn(httpResponse);
    when(httpResponse.getEntity()).thenReturn(httpEntity);

    try (final InputStream is =
            this.getClass().getResourceAsStream("/ReturnedSuccessPKIMessageWithCertificateFile");
        BufferedInputStream bis = new BufferedInputStream(is)) {

      byte[] ba = IOUtils.toByteArray(bis);
      doAnswer(
              invocation -> {
                OutputStream os = (ByteArrayOutputStream) invocation.getArguments()[0];
                os.write(ba);
                return null;
              })
          .when(httpEntity)
          .writeTo(any(OutputStream.class));
    }
    CmpClientImpl cmpClient = spy(new CmpClientImpl(httpClient));
    // when
    List<List<X509Certificate>> cmpClientResult =
        cmpClient.createCertificate("data", "RA", csrMeta, cert, notBefore, notAfter);
    // then
    assertNotNull(cmpClientResult);
  }

  @Test
  void
      shouldThrowCmpClientExceptionWhenCreateCertificateRequestMessageMethodCalledWithWrongProtectedBytesInResponse()
          throws Exception {
    // given
    Date beforeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2019/11/11 12:00:00");
    Date afterDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2020/11/11 12:00:00");
    setCsrMetaValuesAndDateValues(
        rdns,
        "CN=CommonName",
        "CN=ManagementCA",
        "CommonName.com",
        "CommonName@cn.com",
        "password",
        "http://127.0.0.1/ejbca/publicweb/cmp/cmp",
        "senderKID",
        beforeDate,
        afterDate);
    when(httpClient.execute(any())).thenReturn(httpResponse);
    when(httpResponse.getEntity()).thenReturn(httpEntity);

    try (final InputStream is =
            this.getClass().getResourceAsStream("/ReturnedSuccessPKIMessageWithCertificateFile");
        BufferedInputStream bis = new BufferedInputStream(is)) {

      byte[] ba = IOUtils.toByteArray(bis);
      doAnswer(
              invocation -> {
                OutputStream os = (ByteArrayOutputStream) invocation.getArguments()[0];
                os.write(ba);
                return null;
              })
          .when(httpEntity)
          .writeTo(any(OutputStream.class));
    }
    CmpClientImpl cmpClient = spy(new CmpClientImpl(httpClient));
    // then
    Assertions.assertThrows(
        CmpClientException.class,
        () -> cmpClient.createCertificate("data", "RA", csrMeta, cert, notBefore, notAfter));
  }

  @Test
  void shouldThrowCmpClientExceptionWithPkiErrorExceptionWhenCmpClientCalledWithBadPassword()
      throws Exception {
    // given
    Date beforeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2019/11/11 12:00:00");
    Date afterDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2020/11/11 12:00:00");
    setCsrMetaValuesAndDateValues(
        rdns,
        "CN=CommonName",
        "CN=ManagementCA",
        "CommonName.com",
        "CommonName@cn.com",
        "password",
        "http://127.0.0.1/ejbca/publicweb/cmp/cmp",
        "senderKID",
        beforeDate,
        afterDate);
    when(httpClient.execute(any())).thenReturn(httpResponse);
    when(httpResponse.getEntity()).thenReturn(httpEntity);

    try (final InputStream is =
            this.getClass().getResourceAsStream("/ReturnedFailurePKIMessageBadPassword");
        BufferedInputStream bis = new BufferedInputStream(is)) {

      byte[] ba = IOUtils.toByteArray(bis);
      doAnswer(
              invocation -> {
                OutputStream os = (ByteArrayOutputStream) invocation.getArguments()[0];
                os.write(ba);
                return null;
              })
          .when(httpEntity)
          .writeTo(any(OutputStream.class));
    }
    CmpClientImpl cmpClient = spy(new CmpClientImpl(httpClient));

    // then
    Assertions.assertThrows(
        CmpClientException.class,
        () -> cmpClient.createCertificate("data", "RA", csrMeta, cert, notBefore, notAfter));
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhencreateCertificateCalledWithInvalidCsr()
      throws ParseException {
    // given
    Date beforeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2020/11/11 12:00:00");
    Date afterDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2019/11/11 12:00:00");
    setCsrMetaValuesAndDateValues(
        rdns,
        "CN=CommonName",
        "CN=ManagementCA",
        "CommonName.com",
        "CommonName@cn.com",
        "password",
        "http://127.0.0.1/ejbca/publicweb/cmp/cmp",
        "senderKID",
        beforeDate,
        afterDate);
    CmpClientImpl cmpClient = new CmpClientImpl(httpClient);
    // then
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> cmpClient.createCertificate("data", "RA", csrMeta, cert, notBefore, notAfter));
  }

  @Test
  void shouldThrowIOExceptionWhenCreateCertificateCalledWithNoServerAvailable()
      throws IOException, ParseException {
    // given
    Date beforeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2019/11/11 12:00:00");
    Date afterDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2020/11/11 12:00:00");
    setCsrMetaValuesAndDateValues(
        rdns,
        "CN=Common",
        "CN=CommonCA",
        "Common.com",
        "Common@cn.com",
        "myPassword",
        "http://127.0.0.1/ejbca/publicweb/cmp/cmpTest",
        "sender",
        beforeDate,
        afterDate);
    when(httpClient.execute(any())).thenThrow(IOException.class);
    CmpClientImpl cmpClient = spy(new CmpClientImpl(httpClient));
    // then
    Assertions.assertThrows(
        CmpClientException.class,
        () -> cmpClient.createCertificate("data", "RA", csrMeta, cert, notBefore, notAfter));
  }

  private void setCsrMetaValuesAndDateValues(
      List<RDN> rdns,
      String cn,
      String issuerCn,
      String san,
      String email,
      String password,
      String externalCaUrl,
      String senderKid,
      Date notBefore,
      Date notAfter) {
    csrMeta = new CSRMeta(rdns);
    csrMeta.setCn(cn);
    csrMeta.addSan(san);
    csrMeta.setPassword(password);
    csrMeta.setEmail(email);
    csrMeta.setIssuerCn(issuerCn);
    when(kpg.generateKeyPair()).thenReturn(keyPair);
    csrMeta.getKeyPairOrGenerateIfNull();
    csrMeta.setCaUrl(externalCaUrl);
    csrMeta.setSenderKid(senderKid);
    this.notBefore = notBefore;
    this.notAfter = notAfter;
  }
}