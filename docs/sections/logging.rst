.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2020 NOKIA

.. _cert_logs:

Logging
========

CertService API
--------------------------
To see CertService console logs use:

- Docker:

.. code-block:: bash

    docker logs <cert-service-container-name>
    docker logs aafcert-service

- Kubernetes:

.. code-block:: bash

    kubectl logs <cert-service-pod-name>
    kubectl logs $(kubectl get pods | grep cert-service | awk '{print $1}')

Console logs contains logs for logging levels from **DEBUG** to **ERROR**.

CertService logs for different logging levels are available in the container:

- Docker:

.. code-block:: bash

    docker exec -it <cert-service-container-name> bash
    docker exec -it aafcert-service bash

- Kubernetes:

.. code-block:: bash

    kubectl exec -it <cert-service-pod-name> bash
    kubectl exec -it $(kubectl get pods | grep cert-service | awk '{print $1}') -- bash

Path to logs:

    /var/log/onap/aaf/certservice

Available log files:

    - audit.log - contains logs for **INFO** logging level
    - debug.log - contains logs for logging levels from **DEBUG** to **ERROR**
    - error.log - contains logs for **ERROR** logging level

User cannot change logging levels.



CertService client
----------------------------
To see CertService client console logs use :

- Docker: 

.. code-block:: bash
   
    docker logs <cert-service-client-container-name>
    docker logs aafcert-client

- Kubernetes: 
  CertService client is used as init container in other components. In the following example:
    - *<some-component-pod-name>* refers to the component that uses CertService client as init container
    - *<cert-service-client-init-container-name>* refers to name of init container used by the mentioned component. It can be found by executing *'kubectl descrine pod <some-component-pod-name>'* and looking into 'Init Containers section'

.. code-block:: bash

    kubectl logs <some-component-pod-name> -c <cert-service-client-init-container-name>
    kubectl logs <some-component-pod-name> -c cert-service-client



| Container stops after execution, so all available logs are printed on console.
| User cannot change logging levels.

Client application exits with following exit codes:


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
| 9     | Wrong TLS configuration                        |
+-------+------------------------------------------------+
