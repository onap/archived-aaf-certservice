.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2020 NOKIA

Configuration
=============


Configuring EJBCA server for testing
------------------------------------

To instantiate an EJBCA server for testing purposes with an OOM deployment, cmpv2Enabled and cmpv2Testing have to be changed to true in oom/kubernetes/aaf/values.yaml.

cmpv2Enabled has to be true to enable aaf-cert-service to be instantiated and used with an external Certificate Authority to get certificates for secure communication.

If cmpv2Testing is enabled then an EJBCA test server will be instantiated in the OOM deployment as well, and will come pre-configured with a test CA to request a certificate from.

Currently the recommended mode is single-layer RA mode.


Default Values:

+---------------------+---------------------------------------------------------------------------------------------------------------------------------+
|  Name               | Value                                                                                                                           |
+=====================+=================================================================================================================================+
| Request URL         | http://aaf-ejbca:8080/ejbca/publicweb/cmp/cmpRA                                                                              |
+---------------------+---------------------------------------------------------------------------------------------------------------------------------+
| Response Type       | PKI Response                                                                                                                    |
+---------------------+---------------------------------------------------------------------------------------------------------------------------------+
| caMode              | RA                                                                                                                              |
+---------------------+---------------------------------------------------------------------------------------------------------------------------------+
| alias               | cmpRA                                                                                                                           |
+---------------------+---------------------------------------------------------------------------------------------------------------------------------+


If you wish to configure the EJBCA server, you can find Documentation for EJBCA here: https://doc.primekey.com/ejbca/

If you want to understand how CMP works on EJBCA in more detail, you can find Details here: https://download.primekey.com/docs/EJBCA-Enterprise/6_14_0/CMP.html

