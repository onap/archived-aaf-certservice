package org.onap.aaf.certservice.client.httpclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.client.model.CertServiceResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CertServiceClientHttpClientTest {

    private CertServiceClientHttpClient certServiceClientHttpClient;


    @BeforeEach
    void setUp() {
        String certServiceAddress = "http://localhost:8080";
        int timeoutInSeconds = 30;
        certServiceClientHttpClient = new CertServiceClientHttpClient(certServiceAddress, timeoutInSeconds);
    }

    @Test
    void certClientShouldReturnCorrectListsOfCertificatedChainsAndTrustedCertificates() {

        //given
        final String caName = "TestCA";
        final String CSR = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURSBSRVFVRVNULS0tLS0KTUlJRExEQ0NBaFFDQVFBd2daQXhDekFKQmdOVkJBWVRBbEJNTVJZd0ZBWURWUVFJREExTWIzZGxjaTFUYVd4bGMybGhNUkF3RGdZRFZRUUhEQWRYY205amJHRjNNUTR3REFZRFZRUUtEQVZPYjJ0cFlURU5NQXNHQTFVRUN3d0VUMDVCVURFWE1CVUdBMVVFQXd3T2RHVnpkQzV1YjJ0cFlTNWpiMjB4SHpBZEJna3Foa2lHOXcwQkNRRVdFSFJsYzNSbGNrQnViMnRwWVM1amIyMHdnZ0VpTUEwR0NTcUdTSWIzRFFFQkFRVUFBNElCRHdBd2dnRUtBb0lCQVFERzMwWUZKMDk3bS83dDJQV1pFbExBNmJ5bFc5Z1k0cDNod3NidC9paENqKzFqRG9YRFdpQk0wMXVGd1BqWmNiaXhwR3BQdXdVU3ZWREUzOUtwUDFHS3NCYVcrMHdLZG02Sit4YmN6ZTBEc0N6QUhCTnNYVXJEK3VzZC9jVUxOVm5UeXRZYzZubkF1VSswQzg0U1l6OGVkVHJ4UWVkSmF4MDBaS3YrdHluVnZvWUtyVVFsMlFrTDI4bFhhaWsxdWIzd1FGeFNQdndEM2xuMU81N2k0Wk9hOHlNcWx2NlpsTkxZYng2UFhsc1RqanBWTldPUllPKzdzeWdieEZ0bHYvbEgyN1BISHZJT3BUUmtGd0lVLzRHWXU2blQ0bDBqYkl0VEE0b2dhUFR6b3hodG5jaStLT1VVeVZ4OWk4eWd3cVBUb3d5UFkyNGpSb2xTd3RBQWpDYkJBZ01CQUFHZ1ZqQlVCZ2txaGtpRzl3MEJDUTR4UnpCRk1FTUdBMVVkRVFROE1EcUNEM1JsYzNReUxtNXZhMmxoTG1OdmJZSVBkR1Z6ZEM1dWIyc3VhWFF1WTI5dGdoWjBaWE4wTG1sdWRDNXVaWFF1Ym05cmFXRXVZMjl0TUEwR0NTcUdTSWIzRFFFQkN3VUFBNElCQVFBUmRlNnpiT2R2TXdKSkFETGV0TmlXT0p3TU9Ec0RJeFduUDBjbXkwTVovb21KK21JZFJSb1NZV0t2VDl5OXd3a3A1Sllzb2htMUN4c0RvS1pBZHFWWTloeENMSUJWRktEL2FveUlRUzRhM3prZFBVa0lnWW00UzJxMkI3bTFjT2YxTHpYNzVSQ3BKN3N2SDZ3RFlqV2dEOTBsVW5uamphNUF2VnJTWnRCVUhEQWZsUG5DTmhXU3hMREhTSkZhWHhERkpGbjhpT1FhdDkvUmNERHc2M0lrbWVaLzBWWDhVRjRsaWp2VWcxSGc4WUFrdXVOQnNwTmRDY2FFVFZFUHJwS3BjaFQxdDg1YnA2RnppSHczc3ZCVTM2cmhzUGNQVU5IM1NYT2tVcmZlOXp0RmJzUFB4dmJtZWx1MWEwS2FudmhDbEU0Z1dMT2tWb1k2Q0hlUktYeU0KLS0tLS1FTkQgQ0VSVElGSUNBVEUgUkVRVUVTVC0tLS0t";
        final String PK = "LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2UUlCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktjd2dnU2pBZ0VBQW9JQkFRREczMFlGSjA5N20vN3QyUFdaRWxMQTZieWxXOWdZNHAzaHdzYnQvaWhDaisxakRvWERXaUJNMDF1RndQalpjYml4cEdwUHV3VVN2VkRFMzlLcFAxR0tzQmFXKzB3S2RtNkoreGJjemUwRHNDekFIQk5zWFVyRCt1c2QvY1VMTlZuVHl0WWM2bm5BdVUrMEM4NFNZejhlZFRyeFFlZEpheDAwWkt2K3R5blZ2b1lLclVRbDJRa0wyOGxYYWlrMXViM3dRRnhTUHZ3RDNsbjFPNTdpNFpPYTh5TXFsdjZabE5MWWJ4NlBYbHNUampwVk5XT1JZTys3c3lnYnhGdGx2L2xIMjdQSEh2SU9wVFJrRndJVS80R1l1Nm5UNGwwamJJdFRBNG9nYVBUem94aHRuY2krS09VVXlWeDlpOHlnd3FQVG93eVBZMjRqUm9sU3d0QUFqQ2JCQWdNQkFBRUNnZ0VBTHdIcHFDQXhubk15SUFCdmxSNEtwNFRZVFhIWE01S2xaUTdJUE1zZHN4WVlNNWprTDFmbldLR0ErYTJ5Wkp1SDM1MlFiNFl5WGNxWUErRXdCMGRyTzlBQmx2Q1JlY3VpdDBTOWs3V3ROM2oyS3ZhMzlKNWNwTlJ6ck9RbUprOFhDNFBmZG5oS0RTOEFVdnVUV3k5UVpSK3FyZ280NUZiSVVYRVdZcC9pNkoyMGR3WW44Sm9HamV5WkFBdVhKQktOYzRJNDRndmNQUHJ4ZHBzMUh6dG5WU2RXbE1wL2lDZnc2YnNlRG94aCtkcXYrTXBrbUhROEV6WVZyVUNmbnhsTWNoMmtwenJpOXdTS3ZrZmMzckFieUpTWnhPQ3hYd2ZvTit3M05JQlpOQzh3WStIZC9nVzJPdFNkY2JOaHJ5UWdjMUVWVGtBdVdzTG1jNXZiTjRZQUVRS0JnUUR1WUdCUmVGZ3FUT1BhNHJJVEZhY1BSZm1qdGNmRnpqSEJLNDA5emhSL0VEWERDNGdxVmpxYWZGSkNIWFp4USt2Q1N3UXBvYVpQcTgraWFmc0pVTUdlcjhLNGdQR0xJTmVueFF2ZEF2c2M4NnA2MzZlakYxbFVLdzA4Rmhzam5zL3pyeGxhZnB3eWpGb0RIcFBoWGg2R25sNGx1NmEycGF2bm8wK3dzWGNRZFFLQmdRRFZremhzY1h1SG0wMnZTTE9KN1RZOHEzQSthbUZrb2hBOVY0YmpJYVpBZ2ZoVHZjMS9aSWM4THNRQmZBL2RKZ0R1eldOYjVqckJ5S0NQMWZDU1FZZ2VybmpSYXFRRlB6ZC82bmswSWZzNjUreUJ6bHlBVDVQNjB0Qjk2NEhsSjBYODRnc1owZlMweFlBRTNxWW83UG1QRUNDRXJPQ05FZTlLRXZabjBVanpuUUtCZ1FEYVAyMFFTbkhXVU0yeFl5c05KQjd2Y2U3TlA2cW5aVkRTZnJCemJOSUJQL01wSDg3TWpHUmRld1BKT3JadG4zVWtUNUNCR1ZwdXlXeHlWRHdlWEV6Wm9DeFV4dUhmc3ZNZnpONCt2UEx5bi9sdlJJUjBZdlZMaFpzNWJ6ZnIxZ1NwSktDKzVQclhvUDdzcWp0VTlOcFlBSGxNYk5HSG1vbVlyRUpURVRoazNRS0JnR1FCRm5kNHY4M2tnNENpK3lhSFExRXZPVlNRZldBZ25wZ0todWVObHdvM2tXNnN2aTk3Zy9ORE5wWTNZRG8rRkV1OU1sd1N0c3FNUmRwejQ3eW9JTE8xSUc5MmpxekNTQnVHVUJDQUpPSVZQT0lmSGFNYklBQmZmQzZwK3QyeEFRMkRUbzFkaVVhbi8rVEgyR2ZyWm9OOW1xeGxRcFBycE84N1o5Tis1TGpsQW9HQVhFQXI5Vmo5WmF3bE5yV0VZYjgyN1J6NVNFZkFjSmFFeTVhVzFUVWNzZVc0bWgwYmNuTzVCSHQvYTFmN3VoQmJoY2xQVFlPOTBSNGpLcUtGK2lVa2VDRHV0TmcrM09ncTdKaS9PWk5vajRSaUM1WWV4TWs5d1kxc3NVV0pqbDVoUURpd1BRQU5Zc09TSWxOT3Z2SWpydG81RnhNWVN5UndHamYrYnRSTzIyYz0KLS0tLS1FTkQgUFJJVkFURSBLRVktLS0tLQ==";


        //when
        CertServiceResponse certServiceResponse = certServiceClientHttpClient.sendRequestToCertService(caName, CSR, PK);

        List<String> certificateChain = certServiceResponse.getCertificateChain();
        List<String> trustedCertificate = certServiceResponse.getTrustedCertificates();
        String expectedFirstElementOfCertificateChain = "-----BEGIN CERTIFICATE-----\n" +
                "MIIDjDCCAnSgAwIBAgICEAIwDQYJKoZIhvcNAQELBQAwgYQxCzAJBgNVBAYTAlVT\n" +
                "MRMwEQYDVQQIDApDYWxpZm9ybmlhMRYwFAYDVQQHDA1TYW4tRnJhbmNpc2NvMRkw\n" +
                "FwYDVQQKDBBMaW51eC1Gb3VuZGF0aW9uMQ0wCwYDVQQLDARPTkFQMR4wHAYDVQQD\n" +
                "DBVpbnRlcm1lZGlhdGUub25hcC5vcmcwHhcNMjAwMjEyMDk1MTI2WhcNMjIxMTA4\n" +
                "MDk1MTI2WjB7MQswCQYDVQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQG\n" +
                "A1UEBwwNU2FuLUZyYW5jaXNjbzEZMBcGA1UECgwQTGludXgtRm91bmRhdGlvbjEN\n" +
                "MAsGA1UECwwET05BUDEVMBMGA1UEAwwMdmlkLm9uYXAub3JnMIIBIjANBgkqhkiG\n" +
                "9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw+GIRzJzUOh0gtc+wzFJEdTnn+q5F10L0Yhr\n" +
                "G1xKdjPieHIFGsoiXwcuCU8arNSqlz7ocx62KQRkcA8y6edlOAsYtdOEJvqEI9vc\n" +
                "eyTB/HYsbzw3URPGch4AmibrQkKU9QvGwouHtHn4R2Ft2Y0tfEqv9hxj9v4njq4A\n" +
                "EiDLAFLl5FmVyCZu/MtKngSgu1smcaFKTYySPMxytgJZexoa/ALZyyE0gRhsvwHm\n" +
                "NLGCPt1bmE/PEGZybsCqliyTO0S56ncD55The7+D/UDS4kE1Wg0svlWon/YsE6QW\n" +
                "B3oeJDX7Kr8ebDTIAErevIAD7Sm4ee5se2zxYrsYlj0MzHZtvwIDAQABoxAwDjAM\n" +
                "BgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQCvQ1pTvjON6vSlcJRKSY4r\n" +
                "8q7L4/9ZaVXWJAjzEYJtPIqsgGiPWz0vGfgklowU6tZxp9zRZFXfMil+mPQSe+yo\n" +
                "ULrZSQ/z48YHPueE/BNO/nT4aaVBEhPLR5aVwC7uQVX8H+m1V1UGT8lk9vdI9rej\n" +
                "CI9l524sLCpdE4dFXiWK2XHEZ0Vfylk221u3IYEogVVA+UMX7BFPSsOnI2vtYK/i\n" +
                "lwZtlri8LtTusNe4oiTkYyq+RSyDhtAswg8ANgvfHolhCHoLFj6w1IkG88UCmbwN\n" +
                "d7BoGMy06y5MJxyXEZG0vR7eNeLey0TIh+rAszAFPsIQvrOHW+HuA+WLQAj1mhnm\n" +
                "-----END CERTIFICATE-----";
        String expectedFirstElementOfTrustedCertificates = "-----BEGIN CERTIFICATE-----\n" +
                "MIIDtzCCAp8CFAwqQddh4/iyGfP8UZ3dpXlxfAN8MA0GCSqGSIb3DQEBCwUAMIGX\n" +
                "MQswCQYDVQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2Fu\n" +
                "LUZyYW5jaXNjbzEZMBcGA1UECgwQTGludXgtRm91bmRhdGlvbjENMAsGA1UECwwE\n" +
                "T05BUDERMA8GA1UEAwwIb25hcC5vcmcxHjAcBgkqhkiG9w0BCQEWD3Rlc3RlckBv\n" +
                "bmFwLm9yZzAeFw0yMDAyMTIwOTM0MjdaFw0yMTAyMTEwOTM0MjdaMIGXMQswCQYD\n" +
                "VQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2FuLUZyYW5j\n" +
                "aXNjbzEZMBcGA1UECgwQTGludXgtRm91bmRhdGlvbjENMAsGA1UECwwET05BUDER\n" +
                "MA8GA1UEAwwIb25hcC5vcmcxHjAcBgkqhkiG9w0BCQEWD3Rlc3RlckBvbmFwLm9y\n" +
                "ZzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMCFrnO7/eT6V+7XkPPd\n" +
                "eiL/6xXreuegvit/1/jTVjG+3AOVcmTn2WXwXXRcQLvkWQfJVPoltsY8E3FqFRti\n" +
                "797XjY6cdQJFVDyzNU0+Fb4vJL9FK5wSvnS6EFjBEn3JvXRlENorDCs/mfjkjJoa\n" +
                "Dl74gXQEJYcg4nsTeNIj7cm3Q7VK3mZt1t7LSJJ+czxv69UJDuNJpmQ/2WOKyLZA\n" +
                "gTtBJ+Hyol45/OLsrqwq1dAn9ZRWIFPvRt/XQYH9bI/6MtqSreRVUrdYCiTe/XpP\n" +
                "B/OM6NEi2+p5QLi3Yi70CEbqP3HqUVbkzF+r7bwIb6M5/HxfqzLmGwLvD+6rYnUn\n" +
                "Bm8CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAhXoO65DXth2X/zFRNsCNpLwmDy7r\n" +
                "PxT9ZAIZAzSxx3/aCYiuTrKP1JnqjkO+F2IbikrI4n6sKO49SKnRf9SWTFhd+5dX\n" +
                "vxq5y7MaqxHAY9J7+Qzq33+COVFQnaF7ddel2NbyUVb2b9ZINNsaZkkPXui6DtQ7\n" +
                "/Fb/1tmAGWd3hMp75G2thBSzs816JMKKa9WD+4VGATEs6OSll4sv2fOZEn+0mAD3\n" +
                "9q9c+WtLGIudOwcHwzPb2njtNntQSCK/tVOqbY+vzhMY3JW+p9oSrLDSdGC+pAKK\n" +
                "m/wB+2VPIYcsPMtIhHC4tgoSaiCqjXYptaOh4b8ye8CPBUCpX/AYYkN0Ow==\n" +
                "-----END CERTIFICATE-----";

        //then
        assertNotNull(certServiceResponse);

        assertEquals(certificateChain.size(), 2);
        assertEquals(trustedCertificate.size(), 2);

        assertEquals(expectedFirstElementOfCertificateChain, certificateChain.get(0));
        assertEquals(expectedFirstElementOfTrustedCertificates, trustedCertificate.get(0));

    }
}