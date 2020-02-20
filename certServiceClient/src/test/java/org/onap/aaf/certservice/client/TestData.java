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

package org.onap.aaf.certservice.client;

public final class TestData {

  private TestData() {}

  // Request parameters
  public static final String CA_NAME = "TestCA";
  public static final String CSR =
      "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURSBSRVFVRVNULS0tLS0KTUlJRExEQ0NBaFFDQVFBd2daQXhDekFKQmdOVkJBWVRBbEJNTVJZd0ZBWURWUVFJREExTWIzZGxjaTFUYVd4bGMybGhNUkF3RGdZRFZRUUhEQWRYY205amJHRjNNUTR3REFZRFZRUUtEQVZPYjJ0cFlURU5NQXNHQTFVRUN3d0VUMDVCVURFWE1CVUdBMVVFQXd3T2RHVnpkQzV1YjJ0cFlTNWpiMjB4SHpBZEJna3Foa2lHOXcwQkNRRVdFSFJsYzNSbGNrQnViMnRwWVM1amIyMHdnZ0VpTUEwR0NTcUdTSWIzRFFFQkFRVUFBNElCRHdBd2dnRUtBb0lCQVFERzMwWUZKMDk3bS83dDJQV1pFbExBNmJ5bFc5Z1k0cDNod3NidC9paENqKzFqRG9YRFdpQk0wMXVGd1BqWmNiaXhwR3BQdXdVU3ZWREUzOUtwUDFHS3NCYVcrMHdLZG02Sit4YmN6ZTBEc0N6QUhCTnNYVXJEK3VzZC9jVUxOVm5UeXRZYzZubkF1VSswQzg0U1l6OGVkVHJ4UWVkSmF4MDBaS3YrdHluVnZvWUtyVVFsMlFrTDI4bFhhaWsxdWIzd1FGeFNQdndEM2xuMU81N2k0Wk9hOHlNcWx2NlpsTkxZYng2UFhsc1RqanBWTldPUllPKzdzeWdieEZ0bHYvbEgyN1BISHZJT3BUUmtGd0lVLzRHWXU2blQ0bDBqYkl0VEE0b2dhUFR6b3hodG5jaStLT1VVeVZ4OWk4eWd3cVBUb3d5UFkyNGpSb2xTd3RBQWpDYkJBZ01CQUFHZ1ZqQlVCZ2txaGtpRzl3MEJDUTR4UnpCRk1FTUdBMVVkRVFROE1EcUNEM1JsYzNReUxtNXZhMmxoTG1OdmJZSVBkR1Z6ZEM1dWIyc3VhWFF1WTI5dGdoWjBaWE4wTG1sdWRDNXVaWFF1Ym05cmFXRXVZMjl0TUEwR0NTcUdTSWIzRFFFQkN3VUFBNElCQVFBUmRlNnpiT2R2TXdKSkFETGV0TmlXT0p3TU9Ec0RJeFduUDBjbXkwTVovb21KK21JZFJSb1NZV0t2VDl5OXd3a3A1Sllzb2htMUN4c0RvS1pBZHFWWTloeENMSUJWRktEL2FveUlRUzRhM3prZFBVa0lnWW00UzJxMkI3bTFjT2YxTHpYNzVSQ3BKN3N2SDZ3RFlqV2dEOTBsVW5uamphNUF2VnJTWnRCVUhEQWZsUG5DTmhXU3hMREhTSkZhWHhERkpGbjhpT1FhdDkvUmNERHc2M0lrbWVaLzBWWDhVRjRsaWp2VWcxSGc4WUFrdXVOQnNwTmRDY2FFVFZFUHJwS3BjaFQxdDg1YnA2RnppSHczc3ZCVTM2cmhzUGNQVU5IM1NYT2tVcmZlOXp0RmJzUFB4dmJtZWx1MWEwS2FudmhDbEU0Z1dMT2tWb1k2Q0hlUktYeU0KLS0tLS1FTkQgQ0VSVElGSUNBVEUgUkVRVUVTVC0tLS0t";
  public static final String PK =
      "LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2UUlCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktjd2dnU2pBZ0VBQW9JQkFRREczMFlGSjA5N20vN3QyUFdaRWxMQTZieWxXOWdZNHAzaHdzYnQvaWhDaisxakRvWERXaUJNMDF1RndQalpjYml4cEdwUHV3VVN2VkRFMzlLcFAxR0tzQmFXKzB3S2RtNkoreGJjemUwRHNDekFIQk5zWFVyRCt1c2QvY1VMTlZuVHl0WWM2bm5BdVUrMEM4NFNZejhlZFRyeFFlZEpheDAwWkt2K3R5blZ2b1lLclVRbDJRa0wyOGxYYWlrMXViM3dRRnhTUHZ3RDNsbjFPNTdpNFpPYTh5TXFsdjZabE5MWWJ4NlBYbHNUampwVk5XT1JZTys3c3lnYnhGdGx2L2xIMjdQSEh2SU9wVFJrRndJVS80R1l1Nm5UNGwwamJJdFRBNG9nYVBUem94aHRuY2krS09VVXlWeDlpOHlnd3FQVG93eVBZMjRqUm9sU3d0QUFqQ2JCQWdNQkFBRUNnZ0VBTHdIcHFDQXhubk15SUFCdmxSNEtwNFRZVFhIWE01S2xaUTdJUE1zZHN4WVlNNWprTDFmbldLR0ErYTJ5Wkp1SDM1MlFiNFl5WGNxWUErRXdCMGRyTzlBQmx2Q1JlY3VpdDBTOWs3V3ROM2oyS3ZhMzlKNWNwTlJ6ck9RbUprOFhDNFBmZG5oS0RTOEFVdnVUV3k5UVpSK3FyZ280NUZiSVVYRVdZcC9pNkoyMGR3WW44Sm9HamV5WkFBdVhKQktOYzRJNDRndmNQUHJ4ZHBzMUh6dG5WU2RXbE1wL2lDZnc2YnNlRG94aCtkcXYrTXBrbUhROEV6WVZyVUNmbnhsTWNoMmtwenJpOXdTS3ZrZmMzckFieUpTWnhPQ3hYd2ZvTit3M05JQlpOQzh3WStIZC9nVzJPdFNkY2JOaHJ5UWdjMUVWVGtBdVdzTG1jNXZiTjRZQUVRS0JnUUR1WUdCUmVGZ3FUT1BhNHJJVEZhY1BSZm1qdGNmRnpqSEJLNDA5emhSL0VEWERDNGdxVmpxYWZGSkNIWFp4USt2Q1N3UXBvYVpQcTgraWFmc0pVTUdlcjhLNGdQR0xJTmVueFF2ZEF2c2M4NnA2MzZlakYxbFVLdzA4Rmhzam5zL3pyeGxhZnB3eWpGb0RIcFBoWGg2R25sNGx1NmEycGF2bm8wK3dzWGNRZFFLQmdRRFZremhzY1h1SG0wMnZTTE9KN1RZOHEzQSthbUZrb2hBOVY0YmpJYVpBZ2ZoVHZjMS9aSWM4THNRQmZBL2RKZ0R1eldOYjVqckJ5S0NQMWZDU1FZZ2VybmpSYXFRRlB6ZC82bmswSWZzNjUreUJ6bHlBVDVQNjB0Qjk2NEhsSjBYODRnc1owZlMweFlBRTNxWW83UG1QRUNDRXJPQ05FZTlLRXZabjBVanpuUUtCZ1FEYVAyMFFTbkhXVU0yeFl5c05KQjd2Y2U3TlA2cW5aVkRTZnJCemJOSUJQL01wSDg3TWpHUmRld1BKT3JadG4zVWtUNUNCR1ZwdXlXeHlWRHdlWEV6Wm9DeFV4dUhmc3ZNZnpONCt2UEx5bi9sdlJJUjBZdlZMaFpzNWJ6ZnIxZ1NwSktDKzVQclhvUDdzcWp0VTlOcFlBSGxNYk5HSG1vbVlyRUpURVRoazNRS0JnR1FCRm5kNHY4M2tnNENpK3lhSFExRXZPVlNRZldBZ25wZ0todWVObHdvM2tXNnN2aTk3Zy9ORE5wWTNZRG8rRkV1OU1sd1N0c3FNUmRwejQ3eW9JTE8xSUc5MmpxekNTQnVHVUJDQUpPSVZQT0lmSGFNYklBQmZmQzZwK3QyeEFRMkRUbzFkaVVhbi8rVEgyR2ZyWm9OOW1xeGxRcFBycE84N1o5Tis1TGpsQW9HQVhFQXI5Vmo5WmF3bE5yV0VZYjgyN1J6NVNFZkFjSmFFeTVhVzFUVWNzZVc0bWgwYmNuTzVCSHQvYTFmN3VoQmJoY2xQVFlPOTBSNGpLcUtGK2lVa2VDRHV0TmcrM09ncTdKaS9PWk5vajRSaUM1WWV4TWs5d1kxc3NVV0pqbDVoUURpd1BRQU5Zc09TSWxOT3Z2SWpydG81RnhNWVN5UndHamYrYnRSTzIyYz0KLS0tLS1FTkQgUFJJVkFURSBLRVktLS0tLQ";

  // Correct response data

  public static final String CORRECT_RESPONSE =
      "{\"certificateChain\":[\"-----BEGIN CERTIFICATE-----\\nMIIDjDCCAnSgAwIBAgICEAIwDQYJKoZIhvcNAQELBQAwgYQxCzAJBgNVBAYTAlVT\\nMRMwEQYDVQQIDApDYWxpZm9ybmlhMRYwFAYDVQQHDA1TYW4tRnJhbmNpc2NvMRkw\\nFwYDVQQKDBBMaW51eC1Gb3VuZGF0aW9uMQ0wCwYDVQQLDARPTkFQMR4wHAYDVQQD\\nDBVpbnRlcm1lZGlhdGUub25hcC5vcmcwHhcNMjAwMjEyMDk1MTI2WhcNMjIxMTA4\\nMDk1MTI2WjB7MQswCQYDVQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQG\\nA1UEBwwNU2FuLUZyYW5jaXNjbzEZMBcGA1UECgwQTGludXgtRm91bmRhdGlvbjEN\\nMAsGA1UECwwET05BUDEVMBMGA1UEAwwMdmlkLm9uYXAub3JnMIIBIjANBgkqhkiG\\n9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw+GIRzJzUOh0gtc+wzFJEdTnn+q5F10L0Yhr\\nG1xKdjPieHIFGsoiXwcuCU8arNSqlz7ocx62KQRkcA8y6edlOAsYtdOEJvqEI9vc\\neyTB/HYsbzw3URPGch4AmibrQkKU9QvGwouHtHn4R2Ft2Y0tfEqv9hxj9v4njq4A\\nEiDLAFLl5FmVyCZu/MtKngSgu1smcaFKTYySPMxytgJZexoa/ALZyyE0gRhsvwHm\\nNLGCPt1bmE/PEGZybsCqliyTO0S56ncD55The7+D/UDS4kE1Wg0svlWon/YsE6QW\\nB3oeJDX7Kr8ebDTIAErevIAD7Sm4ee5se2zxYrsYlj0MzHZtvwIDAQABoxAwDjAM\\nBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQCvQ1pTvjON6vSlcJRKSY4r\\n8q7L4/9ZaVXWJAjzEYJtPIqsgGiPWz0vGfgklowU6tZxp9zRZFXfMil+mPQSe+yo\\nULrZSQ/z48YHPueE/BNO/nT4aaVBEhPLR5aVwC7uQVX8H+m1V1UGT8lk9vdI9rej\\nCI9l524sLCpdE4dFXiWK2XHEZ0Vfylk221u3IYEogVVA+UMX7BFPSsOnI2vtYK/i\\nlwZtlri8LtTusNe4oiTkYyq+RSyDhtAswg8ANgvfHolhCHoLFj6w1IkG88UCmbwN\\nd7BoGMy06y5MJxyXEZG0vR7eNeLey0TIh+rAszAFPsIQvrOHW+HuA+WLQAj1mhnm\\n-----END CERTIFICATE-----\",\"-----BEGIN CERTIFICATE-----\\nMIIDqTCCApGgAwIBAgICEAAwDQYJKoZIhvcNAQELBQAwgZcxCzAJBgNVBAYTAlVT\\nMRMwEQYDVQQIDApDYWxpZm9ybmlhMRYwFAYDVQQHDA1TYW4tRnJhbmNpc2NvMRkw\\nFwYDVQQKDBBMaW51eC1Gb3VuZGF0aW9uMQ0wCwYDVQQLDARPTkFQMREwDwYDVQQD\\nDAhvbmFwLm9yZzEeMBwGCSqGSIb3DQEJARYPdGVzdGVyQG9uYXAub3JnMB4XDTIw\\nMDIxMjA5NDAxMloXDTIyMTEwODA5NDAxMlowgYQxCzAJBgNVBAYTAlVTMRMwEQYD\\nVQQIDApDYWxpZm9ybmlhMRYwFAYDVQQHDA1TYW4tRnJhbmNpc2NvMRkwFwYDVQQK\\nDBBMaW51eC1Gb3VuZGF0aW9uMQ0wCwYDVQQLDARPTkFQMR4wHAYDVQQDDBVpbnRl\\ncm1lZGlhdGUub25hcC5vcmcwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIB\\nAQC1oOYMZ6G+2DGDAizYnzdCNiogivlht1s4oqgem7fM1XFPxD2p31ATIibOdqr/\\ngv1qemO9Q4r1xn6w1Ufq7T1K7PjnMzdSeTqZefurE2JM/HHx2QvW4TjMlz2ILgaD\\nL1LN60kmMQSOi5VxKJpsrCQxbOsxhvefd212gny5AZMcjJe23kUd9OxUrtvpdLEv\\nwI3vFEvT7oRUnEUg/XNz7qeg33vf1C39yMR+6O4s6oevgsEebVKjb+yOoS6zzGtz\\n72wZjm07C54ZlO+4Uy+QAlMjRiU3mgWkKbkOy+4CvwehjhpTikdBs2DX39ZLGHhn\\nL/0a2NYtGulp9XEqmTvRoI+PAgMBAAGjEDAOMAwGA1UdEwQFMAMBAf8wDQYJKoZI\\nhvcNAQELBQADggEBADcitdJ6YswiV8jAD9GK0gf3+zqcGegt4kt+79JXlXYbb1sY\\nq3o6prcB7nSUoClgF2xUPCslFGpM0Er9FCSFElQM/ru0l/KVmJS6kSpwEHvsYIH3\\nq5anta+Pyk8JSQWAAw+qrind0uBQMnhR8Tn13tgV+Kjvg/xlH/nZIEdN5YtLB1cA\\nbeVsZRyRfVL9DeZU8s/MZ5wC3kgcEp5A4m5lg7HyBxBdqhzFcDr6xiy6OGqW8Yep\\nxrwfc8Fw8a/lOv4U+tBeGNKPQDYaL9hh+oM+qMkNXsHXDqdJsuEGJtU4i3Wcwzoc\\nXGN5NWV//4bP+NFmwgcn7AYCdRvz04A8GU/0Cwg\\u003d\\n-----END CERTIFICATE-----\"],\"trustedCertificates\":[\"-----BEGIN CERTIFICATE-----\\nMIIDtzCCAp8CFAwqQddh4/iyGfP8UZ3dpXlxfAN8MA0GCSqGSIb3DQEBCwUAMIGX\\nMQswCQYDVQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2Fu\\nLUZyYW5jaXNjbzEZMBcGA1UECgwQTGludXgtRm91bmRhdGlvbjENMAsGA1UECwwE\\nT05BUDERMA8GA1UEAwwIb25hcC5vcmcxHjAcBgkqhkiG9w0BCQEWD3Rlc3RlckBv\\nbmFwLm9yZzAeFw0yMDAyMTIwOTM0MjdaFw0yMTAyMTEwOTM0MjdaMIGXMQswCQYD\\nVQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2FuLUZyYW5j\\naXNjbzEZMBcGA1UECgwQTGludXgtRm91bmRhdGlvbjENMAsGA1UECwwET05BUDER\\nMA8GA1UEAwwIb25hcC5vcmcxHjAcBgkqhkiG9w0BCQEWD3Rlc3RlckBvbmFwLm9y\\nZzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMCFrnO7/eT6V+7XkPPd\\neiL/6xXreuegvit/1/jTVjG+3AOVcmTn2WXwXXRcQLvkWQfJVPoltsY8E3FqFRti\\n797XjY6cdQJFVDyzNU0+Fb4vJL9FK5wSvnS6EFjBEn3JvXRlENorDCs/mfjkjJoa\\nDl74gXQEJYcg4nsTeNIj7cm3Q7VK3mZt1t7LSJJ+czxv69UJDuNJpmQ/2WOKyLZA\\ngTtBJ+Hyol45/OLsrqwq1dAn9ZRWIFPvRt/XQYH9bI/6MtqSreRVUrdYCiTe/XpP\\nB/OM6NEi2+p5QLi3Yi70CEbqP3HqUVbkzF+r7bwIb6M5/HxfqzLmGwLvD+6rYnUn\\nBm8CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAhXoO65DXth2X/zFRNsCNpLwmDy7r\\nPxT9ZAIZAzSxx3/aCYiuTrKP1JnqjkO+F2IbikrI4n6sKO49SKnRf9SWTFhd+5dX\\nvxq5y7MaqxHAY9J7+Qzq33+COVFQnaF7ddel2NbyUVb2b9ZINNsaZkkPXui6DtQ7\\n/Fb/1tmAGWd3hMp75G2thBSzs816JMKKa9WD+4VGATEs6OSll4sv2fOZEn+0mAD3\\n9q9c+WtLGIudOwcHwzPb2njtNntQSCK/tVOqbY+vzhMY3JW+p9oSrLDSdGC+pAKK\\nm/wB+2VPIYcsPMtIhHC4tgoSaiCqjXYptaOh4b8ye8CPBUCpX/AYYkN0Ow\\u003d\\u003d\\n-----END CERTIFICATE-----\",\"-----BEGIN CERTIFICATE-----\\nMIIDvzCCAqcCFF5DejiyfoNfPiiMmBXulniBewBGMA0GCSqGSIb3DQEBCwUAMIGb\\nMQswCQYDVQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2Fu\\nLUZyYW5jaXNjbzEZMBcGA1UECgwQTGludXgtRm91bmRhdGlvbjENMAsGA1UECwwE\\nT05BUDEVMBMGA1UEAwwMbmV3Lm9uYXAub3JnMR4wHAYJKoZIhvcNAQkBFg90ZXN0\\nZXJAb25hcC5vcmcwHhcNMjAwMjEyMDk1OTM3WhcNMjEwMjExMDk1OTM3WjCBmzEL\\nMAkGA1UEBhMCVVMxEzARBgNVBAgMCkNhbGlmb3JuaWExFjAUBgNVBAcMDVNhbi1G\\ncmFuY2lzY28xGTAXBgNVBAoMEExpbnV4LUZvdW5kYXRpb24xDTALBgNVBAsMBE9O\\nQVAxFTATBgNVBAMMDG5ldy5vbmFwLm9yZzEeMBwGCSqGSIb3DQEJARYPdGVzdGVy\\nQG9uYXAub3JnMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtF4FXeDV\\nng/inC/bTACmZnLC9IiC7PyG/vVbMxxN1bvQLRAwC/Hbl3i9zD68Vs/jPPr/SDr9\\n2rgItdDdUY1V30Y3PT06F11XdEaRb+t++1NX0rDf1AqPaBZgnBmB86s1wbqHdJTr\\nwEImDZ5xMPfP3fiWy/9Yw/U7iRMIi1/oI0lWuHJV0bn908shuJ6dvInpRCoDnoTX\\nYP/FiDSZCFVewQcq4TigB7kRqZrDcPZWbSlqHklDMXRwbCxAiFSziuX6TBwru9Rn\\nHhIeXVSgMU1ZSSopVbJGtQ4zSsU1nvTK5Bhc2UHGcAOZy1xTN5D9EEbTqh7l+Wtx\\ny8ojkEXvFG8lVwIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQAE+bUphwHit78LK8sb\\nOMjt4DiEu32KeSJOpYgPLeBeAIynaNsa7sQrpuxerGNTmQWIcw6olXI0J+OOwkik\\nII7elrYtd5G1uALxXWdamNsaY0Du34moVL1YjexJ7qQ4oBUxg2tuY8NAQGDK+23I\\nnCA+ZwzdTJo73TYS6sx64d/YLWkX4nHGUoMlF+xUH34csDyhpuTSzQhC2quB5N8z\\ntSFdpe4z2jqx07qo2EBFxi03EQ8Q0ex6l421QM2gbs7cZQ66K0DkpPcF2+iHZnyx\\nxq1lnlsWHklElF2bhyXTn3fPp5wtan00P8IolKx7CAWb92QjkW6M0RvTW/xuwIzh\\n0rTO\\n-----END CERTIFICATE-----\"]}";

  public static final String EXPECTED_FIRST_ELEMENT_OF_CERTIFICATE_CHAIN =
      "-----BEGIN CERTIFICATE-----\n"
          + "MIIDjDCCAnSgAwIBAgICEAIwDQYJKoZIhvcNAQELBQAwgYQxCzAJBgNVBAYTAlVT\n"
          + "MRMwEQYDVQQIDApDYWxpZm9ybmlhMRYwFAYDVQQHDA1TYW4tRnJhbmNpc2NvMRkw\n"
          + "FwYDVQQKDBBMaW51eC1Gb3VuZGF0aW9uMQ0wCwYDVQQLDARPTkFQMR4wHAYDVQQD\n"
          + "DBVpbnRlcm1lZGlhdGUub25hcC5vcmcwHhcNMjAwMjEyMDk1MTI2WhcNMjIxMTA4\n"
          + "MDk1MTI2WjB7MQswCQYDVQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQG\n"
          + "A1UEBwwNU2FuLUZyYW5jaXNjbzEZMBcGA1UECgwQTGludXgtRm91bmRhdGlvbjEN\n"
          + "MAsGA1UECwwET05BUDEVMBMGA1UEAwwMdmlkLm9uYXAub3JnMIIBIjANBgkqhkiG\n"
          + "9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw+GIRzJzUOh0gtc+wzFJEdTnn+q5F10L0Yhr\n"
          + "G1xKdjPieHIFGsoiXwcuCU8arNSqlz7ocx62KQRkcA8y6edlOAsYtdOEJvqEI9vc\n"
          + "eyTB/HYsbzw3URPGch4AmibrQkKU9QvGwouHtHn4R2Ft2Y0tfEqv9hxj9v4njq4A\n"
          + "EiDLAFLl5FmVyCZu/MtKngSgu1smcaFKTYySPMxytgJZexoa/ALZyyE0gRhsvwHm\n"
          + "NLGCPt1bmE/PEGZybsCqliyTO0S56ncD55The7+D/UDS4kE1Wg0svlWon/YsE6QW\n"
          + "B3oeJDX7Kr8ebDTIAErevIAD7Sm4ee5se2zxYrsYlj0MzHZtvwIDAQABoxAwDjAM\n"
          + "BgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQCvQ1pTvjON6vSlcJRKSY4r\n"
          + "8q7L4/9ZaVXWJAjzEYJtPIqsgGiPWz0vGfgklowU6tZxp9zRZFXfMil+mPQSe+yo\n"
          + "ULrZSQ/z48YHPueE/BNO/nT4aaVBEhPLR5aVwC7uQVX8H+m1V1UGT8lk9vdI9rej\n"
          + "CI9l524sLCpdE4dFXiWK2XHEZ0Vfylk221u3IYEogVVA+UMX7BFPSsOnI2vtYK/i\n"
          + "lwZtlri8LtTusNe4oiTkYyq+RSyDhtAswg8ANgvfHolhCHoLFj6w1IkG88UCmbwN\n"
          + "d7BoGMy06y5MJxyXEZG0vR7eNeLey0TIh+rAszAFPsIQvrOHW+HuA+WLQAj1mhnm\n"
          + "-----END CERTIFICATE-----";

  public static final String EXPECTED_FIRST_ELEMENT_OF_TRUSTED_CERTIFICATES =
      "-----BEGIN CERTIFICATE-----\n"
          + "MIIDtzCCAp8CFAwqQddh4/iyGfP8UZ3dpXlxfAN8MA0GCSqGSIb3DQEBCwUAMIGX\n"
          + "MQswCQYDVQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2Fu\n"
          + "LUZyYW5jaXNjbzEZMBcGA1UECgwQTGludXgtRm91bmRhdGlvbjENMAsGA1UECwwE\n"
          + "T05BUDERMA8GA1UEAwwIb25hcC5vcmcxHjAcBgkqhkiG9w0BCQEWD3Rlc3RlckBv\n"
          + "bmFwLm9yZzAeFw0yMDAyMTIwOTM0MjdaFw0yMTAyMTEwOTM0MjdaMIGXMQswCQYD\n"
          + "VQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2FuLUZyYW5j\n"
          + "aXNjbzEZMBcGA1UECgwQTGludXgtRm91bmRhdGlvbjENMAsGA1UECwwET05BUDER\n"
          + "MA8GA1UEAwwIb25hcC5vcmcxHjAcBgkqhkiG9w0BCQEWD3Rlc3RlckBvbmFwLm9y\n"
          + "ZzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMCFrnO7/eT6V+7XkPPd\n"
          + "eiL/6xXreuegvit/1/jTVjG+3AOVcmTn2WXwXXRcQLvkWQfJVPoltsY8E3FqFRti\n"
          + "797XjY6cdQJFVDyzNU0+Fb4vJL9FK5wSvnS6EFjBEn3JvXRlENorDCs/mfjkjJoa\n"
          + "Dl74gXQEJYcg4nsTeNIj7cm3Q7VK3mZt1t7LSJJ+czxv69UJDuNJpmQ/2WOKyLZA\n"
          + "gTtBJ+Hyol45/OLsrqwq1dAn9ZRWIFPvRt/XQYH9bI/6MtqSreRVUrdYCiTe/XpP\n"
          + "B/OM6NEi2+p5QLi3Yi70CEbqP3HqUVbkzF+r7bwIb6M5/HxfqzLmGwLvD+6rYnUn\n"
          + "Bm8CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAhXoO65DXth2X/zFRNsCNpLwmDy7r\n"
          + "PxT9ZAIZAzSxx3/aCYiuTrKP1JnqjkO+F2IbikrI4n6sKO49SKnRf9SWTFhd+5dX\n"
          + "vxq5y7MaqxHAY9J7+Qzq33+COVFQnaF7ddel2NbyUVb2b9ZINNsaZkkPXui6DtQ7\n"
          + "/Fb/1tmAGWd3hMp75G2thBSzs816JMKKa9WD+4VGATEs6OSll4sv2fOZEn+0mAD3\n"
          + "9q9c+WtLGIudOwcHwzPb2njtNntQSCK/tVOqbY+vzhMY3JW+p9oSrLDSdGC+pAKK\n"
          + "m/wB+2VPIYcsPMtIhHC4tgoSaiCqjXYptaOh4b8ye8CPBUCpX/AYYkN0Ow==\n"
          + "-----END CERTIFICATE-----";

  // Error response data
  public static String MISSING_PK_RESPONSE = "{\n" +
          "    \"timestamp\": \"2020-02-24T09:58:35.967+0000\",\n" +
          "    \"status\": 400,\n" +
          "    \"error\": \"Bad Request\",\n" +
          "    \"message\": \"Missing request header 'PK' for method parameter of type String\",\n" +
          "    \"path\": \"//v1/certificate/sss\"\n" +
          "}";

}
