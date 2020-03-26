.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2020 NOKIA

Logging
=======

Cert Service Client
-------------------
All client logs can be found inside client container 
Log path:

.. code-block::

  /var/logs

Client exiting with following exit codes:

+-------+------------------------------------------------+
| Code  | Information                                    |
+=======+================================================+
| 0     | Success                                        |
+-------+------------------------------------------------+
| 1     | Invalid client configuration                   |
+-------+------------------------------------------------+
| 2     | Invalid CSR configuration                      |
+-------+------------------------------------------------+
| 3     | Fail in key pair generation                    |
+-------+------------------------------------------------+
| 4     | Fail in CSR generation                         |
+-------+------------------------------------------------+
| 5     | CertService HTTP unsuccessful response         |
+-------+------------------------------------------------+
| 6     | Internal HTTP Client connection problem        |
+-------+------------------------------------------------+
| 7     | Fail in PKCS12 conversion                      |
+-------+------------------------------------------------+
| 8     | Fail in Private Key to PEM Encoding            |
+-------+------------------------------------------------+