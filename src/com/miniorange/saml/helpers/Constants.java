package com.miniorange.saml.helpers;

public class Constants {
	public static String IDP_NAME = "Okta";
	public static String IDP_HOST_NAME = "dev-925013.oktapreview.com";
	public static String IDP_ENTITIY_ID = "http://www.okta.com/exkg857d9oHYjrNSR0h7";
	public static String SINGLE_SIGN_ON_URL = "https://dev-925013.oktapreview.com/app/mycompanydev925013_confluencesaml_1/exkg857d9oHYjrNSR0h7/sso/saml";
	public static String REGEX_NAME = "lokeshnaktode";
	public static String BACKDOOR_URL = "http://localhost:8090/login.action?saml_sso=false ";
	public static String IDP_USERNAME = "lokeshnaktode@hotmail.com";
	public static String IDP_PASSWORD = "@458italiA";
	public static String IDP_USERNAME_2 = "vikasnare1611@gmail.com";
	public static String IDP_PASSWORD_2 = "Admin123";
	public static String IDP_FIRST_NAME = "Vikäš";
	public static String IDP_LAST_NAME = "Náre";
	public static String SECOND_IDP_USERNAME = "lokeshnaktode@hotmail.com";
	public static String SECOND_IDP_PASSWORD = "@458Manit";
	public static String BASE_URL_1 = "http://localhost:8090";
	public static String BASE_URL_2 = "http://localhost:8080";
	public static String BASE_URL_RANDOM = "http://localhost:8090/users/viewmyprofile.action";
	public static String INVALID_ENTITY_ID = "http://www.okta.com/exkg857d9oHYjrNS7";
	public static String INVALID_SINGLE_SIGN_ON_URL = "ht://www.google.com";
	public static String INVALID_SINGLE_LOGOUT_URL = "ht://www.google.com";
	public static String INVALID_IDP_CERTIFICATE = "THISISWRONGIDPCERIFICATE";
	public static String IMPORT_DATA_URL = "https://dev-925013.oktapreview.com/app/exkg857d9oHYjrNSR0h7/sso/saml/metadata";
	public static String INVALID_IMPORT_DATA_URL = "https://dev-924013.oktapreview.com/app/exkg857d9oHYjrNSR0h7/sso/saml/metadata";
	public static String IMPORT_DATA_URL_2 = "https://login.microsoftonline.com/a4addb0d-d477-4c79-bc1c-e0bb067d3c90/federationmetadata/2007-06/federationmetadata.xml";
	public static String INVALID_IMPORT_DATA_URL_2 = "https://login.microsoftonline.com/a5addb0d-d477-4c79-bc1c-e0bb067d3c90/federationmetadata/2007-06/federationmetadata.xml";
	public static String RELAY_STATE_URL = "https://www.google.com/";
	public static String LOGOUT_URL = "https://www.google.com/";

	public static String IDP_CERTIFICATE = "-----BEGIN CERTIFICATE-----\r\n"
			+ "MIIDpDCCAoygAwIBAgIGAVz3RKDMMA0GCSqGSIb3DQEBCwUAMIGSMQswCQYDVQQGEwJVUzETMBEG\r\n"
			+ "A1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2FuIEZyYW5jaXNjbzENMAsGA1UECgwET2t0YTEU\r\n"
			+ "MBIGA1UECwwLU1NPUHJvdmlkZXIxEzARBgNVBAMMCmRldi05MjUwMTMxHDAaBgkqhkiG9w0BCQEW\r\n"
			+ "DWluZm9Ab2t0YS5jb20wHhcNMTcwNjMwMDQzMDI4WhcNMjcwNjMwMDQzMTI4WjCBkjELMAkGA1UE\r\n"
			+ "BhMCVVMxEzARBgNVBAgMCkNhbGlmb3JuaWExFjAUBgNVBAcMDVNhbiBGcmFuY2lzY28xDTALBgNV\r\n"
			+ "BAoMBE9rdGExFDASBgNVBAsMC1NTT1Byb3ZpZGVyMRMwEQYDVQQDDApkZXYtOTI1MDEzMRwwGgYJ\r\n"
			+ "KoZIhvcNAQkBFg1pbmZvQG9rdGEuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA\r\n"
			+ "jXHtvyGD0QchwoQarAT84ICwfrzrgrWCRt2twTwFAp/EyU5/VTTUtetTJRaVz9fzFzPEZ316STwC\r\n"
			+ "53pqYudO3tBDO8l9LH9HDQH27ySLFdFlAcJdxIfVh91klHlKHwqLSiEcKKnm0xIWd5sAi8o4WZK+\r\n"
			+ "M/47L+reiNWcCRASosuWRmrv8ZZnr6gDh7ETxoxERMoKd33nWBEKyVnJ3vNBx9c1fj/bN2fqv88E\r\n"
			+ "kXvcufImPl/aBGdta+ofyp9NBw7meRBdjQO32TnxFzVHAdlAv/iC3dnL2Gs1rA/0GSetT+U6ZHLl\r\n"
			+ "DNN8UFWIAfuZQ+IXe5NGRnO+MnCTLwl5IVgbqQIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQA7Pcnl\r\n"
			+ "jVrA/gEBH2/9j4I1iNThDSv1mJDRYWZIWATJRhUxzHpxnoYXR3B+KiINh0saxI9izsiOq7EB2aDA\r\n"
			+ "jrbwlgT+XZFx4QajGGm34GMAGt1WtwxELNAsXRNjBP84zwx7UYCl9ydPDKcmkVEWqvz10L+DO0wc\r\n"
			+ "N8Jknl/eaelQEG0euH4QLbo/1e8F83P6dyePdNrJSt0hl9NkpqmZU78jY+zEjfx0FaPObh+WaC7Y\r\n"
			+ "MDFmL7cO3JUoYy2Ur8GR7PGnrX0AhUDwsht1kvkUGWrLKWjnJ8QQTkrl5dkvVpfjoGi8A0twA7T2\r\n"
			+ "iywCMmFAjlkTRxOFDvlbGkzyyHaW65Tk\r\n" + "-----END CERTIFICATE-----";

	public static String SP_PUBLIC_CERTIFICATE = "-----BEGIN CERTIFICATE-----\r\n"
			+ "MIID7TCCAtWgAwIBAgIJAMcsf4R7oVMZMA0GCSqGSIb3DQEBCwUAMIGMMQswCQYD\r\n"
			+ "VQQGEwJJTjELMAkGA1UECAwCTUgxDTALBgNVBAcMBFBVTkUxEzARBgNVBAoMCk1J\r\n"
			+ "TklPUkFOR0UxEzARBgNVBAsMCk1JTklPUkFOR0UxEzARBgNVBAMMCk1JTklPUkFO\r\n"
			+ "R0UxIjAgBgkqhkiG9w0BCQEWE2luZm9AbWluaW9yYW5nZS5jb20wHhcNMTUxMDMw\r\n"
			+ "MTA1NDQ4WhcNMjAxMDI4MTA1NDQ4WjCBjDELMAkGA1UEBhMCSU4xCzAJBgNVBAgM\r\n"
			+ "Ak1IMQ0wCwYDVQQHDARQVU5FMRMwEQYDVQQKDApNSU5JT1JBTkdFMRMwEQYDVQQL\r\n"
			+ "DApNSU5JT1JBTkdFMRMwEQYDVQQDDApNSU5JT1JBTkdFMSIwIAYJKoZIhvcNAQkB\r\n"
			+ "FhNpbmZvQG1pbmlvcmFuZ2UuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB\r\n"
			+ "CgKCAQEAsxulwAiXvaJvT6JEckasFcHY7eME2hjClXPKtGJ6okiPOPQjMAv+zYxZ\r\n"
			+ "2beAUPWxg1pfE7HIdTLh6A0yD2Afnw9ayKmCGiq6rX8TqXzEo8J01M/zGRBXxw+Q\r\n"
			+ "CjB7BpWpHUVcdfagUEJrURHRcx6VXXf/9xprbtv7Wsx/WVhqGl6MCtj4m5tTsHyY\r\n"
			+ "D9BOawxtmaq7dNSECkt9qNUfu+EvTYk3LHI3IoJR4HcMTsYjTbJo6lHNT18FQqRe\r\n"
			+ "WcjNXCTvH17Zit4MaH8WGlL32KV62EyTPZwjqrmUHqoXfj87e+1XOpYk+Z/dApMC\r\n"
			+ "47I6++yq+FlyvVne0w48SAHYt4M1rQIDAQABo1AwTjAdBgNVHQ4EFgQUyihK6rNy\r\n"
			+ "l3Sx9Onzzup0qko7z7QwHwYDVR0jBBgwFoAUyihK6rNyl3Sx9Onzzup0qko7z7Qw\r\n"
			+ "DAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEAPnp6Q5jfZ33/0hbeeVr+\r\n"
			+ "ts5PTxKKdPakoGJWAbgqXzf4h8TuCZMjPBE6g7jk1JqvRFWxg7zx+qhvlWRnwfWl\r\n"
			+ "9yAffY0TbBx+EU3kyTYBg2UnffUaSvoko1UzFK1v4dOP2u+wTP8nM/I+HxBjVVcg\r\n"
			+ "T+7zOK9Y6GXe1spjdQb2ELdBQ2p7NFXFF4uy6jzN9yw2xBid7ZLkJwGeOykZrrd1\r\n"
			+ "YJzGZJoGedpTxrkqbIqRUFnCqKRgB5IzhXO1Xj+xgv8qr0KNJ4oqf58OEHnx2XF2\r\n"
			+ "1RY0F9vpQ6/BPQKqO4pWjEuWanV36AZ5nHw6PeJXEsK2RUnABeA/xzjxH/NT6Fh3\r\n" + "dQ==\r\n"
			+ "-----END CERTIFICATE-----";

	public static String SP_PRIVATE_CERTIFICATE = "-----BEGIN PRIVATE KEY-----\r\n"
			+ "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCzG6XACJe9om9P\r\n"
			+ "okRyRqwVwdjt4wTaGMKVc8q0YnqiSI849CMwC/7NjFnZt4BQ9bGDWl8Tsch1MuHo\r\n"
			+ "DTIPYB+fD1rIqYIaKrqtfxOpfMSjwnTUz/MZEFfHD5AKMHsGlakdRVx19qBQQmtR\r\n"
			+ "EdFzHpVdd//3Gmtu2/tazH9ZWGoaXowK2Pibm1OwfJgP0E5rDG2Zqrt01IQKS32o\r\n"
			+ "1R+74S9NiTcscjciglHgdwxOxiNNsmjqUc1PXwVCpF5ZyM1cJO8fXtmK3gxofxYa\r\n"
			+ "UvfYpXrYTJM9nCOquZQeqhd+Pzt77Vc6liT5n90CkwLjsjr77Kr4WXK9Wd7TDjxI\r\n"
			+ "Adi3gzWtAgMBAAECggEAXxvSU3v8n92OwDnJoOk4XkFWoVQI8ottYJFhVZJpzp07\r\n"
			+ "OKIjwVLNVncuCzevMs6VGcw2aq3gZaPEZEYFjDad9AfiTGX+yUhhYtn1Je40OUV8\r\n"
			+ "ZrmaPHf6NKFl+pMt8Rwt9nYrmBSDorryY9VTZI8b/8tGKABmhjLYcangAiC6Dw+7\r\n"
			+ "KEPYqrNHfz6W7Xc8jydReWF/Cl0+qu9gVU2yiRh7/MDyE6NLs7DdkQY2E0OwKkSF\r\n"
			+ "DLeGZe/Bn99rSWuZKgWJRTe/oatoeXmwP+oyutlw1jktLihXKDH0fHtgFSUc/M6D\r\n"
			+ "aAjnag+W1yEA0vPEUi02mFwCYazax8e5d+bqFKqJfQKBgQDthutSu2t43sh6RimP\r\n"
			+ "7vVM98QTgdh4oeLxgt8vcE/4lcXg3xlcKObl77KgYKp/R1KH186lSJPV7Nv4so9y\r\n"
			+ "FkS3jdUpqW2VrR+C/muCjFCJTus56H1mP2MPDRUcH7/60KoMJ+lGWDMA1EOzR6iY\r\n"
			+ "99xaYL+5eCIuwVDyySdH1I2sdwKBgQDBCaAm3lOEByUkSGGWz3NgNqgPT5apk94y\r\n"
			+ "+PaQqtJFRDa4DZ5NSgFWsmV9gfmSSEP5ofPHDg2TuCQawwfvEyDUu85QNDejBD3k\r\n"
			+ "nhyBdv75ljHLzGkIyTsEhHaefK9j2LSJ/FjhssVa1UerFycKZGNYTBQFXUNOnGI3\r\n"
			+ "dCe5bd4L+wKBgQCAykoolsTkSe/sFMUObSQGssVXSm1ko2qZRzvazgwenp8nmfB3\r\n"
			+ "PxpLjNePDDsGPlbqn0JF7n9yXDa9t5v94UMrP7VYHKz1nmRas7b5lzlH6kmzIXN1\r\n"
			+ "EGOW0qIimLiQt7msU6ux37rv9SgsOmuZXbPWMWVjnFb0gQ9yRU2OLhsPawKBgQCs\r\n"
			+ "3rPfWwarm8J1RaSva8RFC21hmMKxxkwclbwpPfDkOvxNw1RansWoIEanKPiZOcI2\r\n"
			+ "EgS/5CPgf+1FUhcO0LB8Elvmk3ynrirHS5j3j9szJIAfpEUDq0IA/6dCsyJWZkD3\r\n"
			+ "uVbXeEMo3ws5ephpxBD7h9X+H9Eg2wdR5eGVO4C4dQKBgFPv0IBl2LAYz+pDTbq9\r\n"
			+ "Undfb7fZ3OwJq9Y5QChj8zIhgf5EQZdk0YkPGvc2n2QbVjNUPAZ4wKeXfOUvj8zR\r\n"
			+ "x0F/+RsJfG1TrDtFe0/8pWnt5oXYxLN0/vHEUU/GXaHNiuXvy3FcGSWMjtg2Ekm4\r\n" + "LWf07xNzN+vCkgBQdU8iAKoP\r\n"
			+ "-----END PRIVATE KEY-----";

	public static String SP_PUBLIC_CERTIFICATE_2 = "-----BEGIN CERTIFICATE-----\r\n"
			+ "MIID2zCCAsOgAwIBAgIJAIAWKY2nKJUwMA0GCSqGSIb3DQEBCwUAMIGDMQswCQYD\r\n"
			+ "VQQGEwJJTjELMAkGA1UECAwCTUgxDTALBgNVBAcMBFBVTkUxEzARBgNVBAoMCk1p\r\n"
			+ "bmlPcmFuZ2UxEDAOBgNVBAsMB1RFU1RJTkcxDTALBgNVBAMMBERFTU8xIjAgBgkq\r\n"
			+ "hkiG9w0BCQEWE3JhdmlAbWluaW9yYW5nZS5jb20wHhcNMTgwOTIxMDcxNzMyWhcN\r\n"
			+ "MTkwOTIxMDcxNzMyWjCBgzELMAkGA1UEBhMCSU4xCzAJBgNVBAgMAk1IMQ0wCwYD\r\n"
			+ "VQQHDARQVU5FMRMwEQYDVQQKDApNaW5pT3JhbmdlMRAwDgYDVQQLDAdURVNUSU5H\r\n"
			+ "MQ0wCwYDVQQDDARERU1PMSIwIAYJKoZIhvcNAQkBFhNyYXZpQG1pbmlvcmFuZ2Uu\r\n"
			+ "Y29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0ou2G4quB5Y9Gkzd\r\n"
			+ "8JVm8A4n5R2Srnd/gnLY3AYMkfpxWTpdoAbk8AeKHPStaymaZZdpgYmZLoqS4DtC\r\n"
			+ "uhOsiOyWW4fxmwH/t6FMNMSBemLX6jWpHC6+ofnQSgxuEXj1D98XfonbZSsDGdcU\r\n"
			+ "Bcu8sIVw7pAxrxlpQPznd+NAIVh5jf+oQh81FbMYnKqhYOmL0tgIRxnOREI4nSq5\r\n"
			+ "gVLoBn8spUq1S5EoPKAiWr/7SyblooUGfB8bFMyAF1VT5XmjQm/l4p6RYaHMZL3l\r\n"
			+ "EzeRuLpf4mc+MCebgKaRbGsaNhVIBwo5dnfIdDt5lzxWBDuvh5o1SKMcwxlXy3hn\r\n"
			+ "kOhLiwIDAQABo1AwTjAdBgNVHQ4EFgQUqQRHALxxZnxRZyVBAXm90gC9soIwHwYD\r\n"
			+ "VR0jBBgwFoAUqQRHALxxZnxRZyVBAXm90gC9soIwDAYDVR0TBAUwAwEB/zANBgkq\r\n"
			+ "hkiG9w0BAQsFAAOCAQEABr5rwX/XBkR+MR1RVBk6XC78MNwcRTLAju7q0MYxFZzy\r\n"
			+ "PV3HfZj3s6QtitBjtCylgKQVOnORJDGjpZM7My2u6UhfwBOlOKScjI9sCQAq7ZCj\r\n"
			+ "0kSxGymBhwKvJrExlaxI1UORHy54UQoIZ7UZRnUMk1iQhiR8XEenQTBECrI+Jp5o\r\n"
			+ "pqCxqUjQYk3sHYwcl4oYqPBzZcv6yIkg4HLANdfYTO/ekhtxmjbZHk8q9U+/egmq\r\n"
			+ "LASw0vUn1SaBD/1E88xS++ri7tRll/WBRtM2ORq6ufs9f2T1kJnEr1maCJvUbtML\r\n"
			+ "7gTOgptn5dr4aUJXKWUQlA/tbP6PZ3zYfc/Hs0bf+A==\r\n" + "-----END CERTIFICATE-----";

	public static String SP_PRIVATE_CERTIFICATE_2 = "-----BEGIN PRIVATE KEY-----\r\n"
			+ "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDSi7Ybiq4Hlj0a\r\n"
			+ "TN3wlWbwDiflHZKud3+CctjcBgyR+nFZOl2gBuTwB4oc9K1rKZpll2mBiZkuipLg\r\n"
			+ "O0K6E6yI7JZbh/GbAf+3oUw0xIF6YtfqNakcLr6h+dBKDG4RePUP3xd+idtlKwMZ\r\n"
			+ "1xQFy7ywhXDukDGvGWlA/Od340AhWHmN/6hCHzUVsxicqqFg6YvS2AhHGc5EQjid\r\n"
			+ "KrmBUugGfyylSrVLkSg8oCJav/tLJuWihQZ8HxsUzIAXVVPleaNCb+XinpFhocxk\r\n"
			+ "veUTN5G4ul/iZz4wJ5uAppFsaxo2FUgHCjl2d8h0O3mXPFYEO6+HmjVIoxzDGVfL\r\n"
			+ "eGeQ6EuLAgMBAAECggEAVSdgbd8256ah6+Mu2ED0sx5mkEnXq2x1S8M7ZjGkH95U\r\n"
			+ "vavXdgOT0GicOX3ULcVTxsboSxXluGcfZEVThYKzg2V8omRnGTRDwncgJS9c1qzn\r\n"
			+ "erhdPyLAwy4QSgKg+bwcEWE/VVRYQaCVznuog3eVRyeRN0tmbNUwNrKSw3BL3a/5\r\n"
			+ "GIKuxvgvLWiqIngQYQQWkr3N9cG9T32lZzazxsXe5JDKRZXMG7skm8CeelgAZEvo\r\n"
			+ "NE1H9am9sqs3h5fn9aNgjDbERi1kK1y7DFNQhfuIspJ0vqYVVcndzDdF6tL6Fh7s\r\n"
			+ "V3u9uvrkenDuzlKxB3Mm586FdsxUYI8drLQfb9QesQKBgQDroHwqKKJ3IM9w8AyY\r\n"
			+ "3xH68rQTR4zb3jsi5em3ju7gsPzXqHO/2591pggejL7UcBLFw3DuhAeSbXmJuMR7\r\n"
			+ "xx6tIzhQRmq6uHKPmN5d3I8aNNLN9dDKm1eMux/d/Fich0rbReXuXsAC3zd/mg0i\r\n"
			+ "5vwX0FmZ5zMEPNkIn7micTVdJQKBgQDkwBB4bITkYXzMtk/tzTDUtOggnm5DHB0s\r\n"
			+ "lAAiNdPfrFvOmZdpKpEhud97tiTmOGSmT7efo2R6JJ7ftBW51bts9dQohX1acwNA\r\n"
			+ "CAVXaYlwLIt08F2fczW+PaT36dWFCB6GHOnAwtXuDmy3mxTR2tsztKZzMJa3mwVu\r\n"
			+ "QK+k2Lwe7wKBgC1FuJWhGRYqh7D7bnAw7BN7bn4F0VqtaE9cPXu3Ss6BMxN8mkDS\r\n"
			+ "weCo9gZopIHMxs6YrqolVA8eWavMhHQD3jEGA1vpK7iUl+5fQNpn9lbir15DTMqg\r\n"
			+ "7kFr8sOEKMJqfEeFNpiGO/5RRSiPQNbj6qvorE6VzYeS6WBu0AwWFFrBAoGAdLq5\r\n"
			+ "7OnbqrtAtyo1gXEbAQBMEBEozSA0lwHzS4HSW6qVssjX5fet1dsG+xdInx6Z5fB6\r\n"
			+ "EIGNjXLQi/FWwN0bZo/PstnjzNsyBmPRbfjSBKcGhyb9b5eYDjtgyo3NwrQQ93kb\r\n"
			+ "o50JFBDSU6NUrX4UK798zP03e/wkek2SE/nxjYsCgYEAh6LuZAw9pqLDM/ftIXpl\r\n"
			+ "zWh4OB/WPMCUXpGq8/hIBhfQ3BODG19O9Sp5P56Jf/abFwlSFK67Q5Qgl9EHcOF6\r\n"
			+ "pGjZji8iwBcV1KVMMCqczgvdCyNRs7YieJdMoKGSnTD58xDPxE5Mj0jugku/tKW5\r\n" + "JY2AZFY3snPLV2+o/hGwSh8=\r\n"
			+ "-----END PRIVATE KEY-----";

	public static String SP_PUBLIC_CERTIFICATE_2_STRING = "MIID2zCCAsOgAwIBAgIJAIAWKY2nKJUwMA0GCSqGSIb3DQEBCwUAMIGDMQswCQYD"
			+ "VQQGEwJJTjELMAkGA1UECAwCTUgxDTALBgNVBAcMBFBVTkUxEzARBgNVBAoMCk1p"
			+ "bmlPcmFuZ2UxEDAOBgNVBAsMB1RFU1RJTkcxDTALBgNVBAMMBERFTU8xIjAgBgkq"
			+ "hkiG9w0BCQEWE3JhdmlAbWluaW9yYW5nZS5jb20wHhcNMTgwOTIxMDcxNzMyWhcN"
			+ "MTkwOTIxMDcxNzMyWjCBgzELMAkGA1UEBhMCSU4xCzAJBgNVBAgMAk1IMQ0wCwYD"
			+ "VQQHDARQVU5FMRMwEQYDVQQKDApNaW5pT3JhbmdlMRAwDgYDVQQLDAdURVNUSU5H"
			+ "MQ0wCwYDVQQDDARERU1PMSIwIAYJKoZIhvcNAQkBFhNyYXZpQG1pbmlvcmFuZ2Uu"
			+ "Y29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0ou2G4quB5Y9Gkzd"
			+ "8JVm8A4n5R2Srnd/gnLY3AYMkfpxWTpdoAbk8AeKHPStaymaZZdpgYmZLoqS4DtC"
			+ "uhOsiOyWW4fxmwH/t6FMNMSBemLX6jWpHC6+ofnQSgxuEXj1D98XfonbZSsDGdcU"
			+ "Bcu8sIVw7pAxrxlpQPznd+NAIVh5jf+oQh81FbMYnKqhYOmL0tgIRxnOREI4nSq5"
			+ "gVLoBn8spUq1S5EoPKAiWr/7SyblooUGfB8bFMyAF1VT5XmjQm/l4p6RYaHMZL3l"
			+ "EzeRuLpf4mc+MCebgKaRbGsaNhVIBwo5dnfIdDt5lzxWBDuvh5o1SKMcwxlXy3hn"
			+ "kOhLiwIDAQABo1AwTjAdBgNVHQ4EFgQUqQRHALxxZnxRZyVBAXm90gC9soIwHwYD"
			+ "VR0jBBgwFoAUqQRHALxxZnxRZyVBAXm90gC9soIwDAYDVR0TBAUwAwEB/zANBgkq"
			+ "hkiG9w0BAQsFAAOCAQEABr5rwX/XBkR+MR1RVBk6XC78MNwcRTLAju7q0MYxFZzy"
			+ "PV3HfZj3s6QtitBjtCylgKQVOnORJDGjpZM7My2u6UhfwBOlOKScjI9sCQAq7ZCj"
			+ "0kSxGymBhwKvJrExlaxI1UORHy54UQoIZ7UZRnUMk1iQhiR8XEenQTBECrI+Jp5o"
			+ "pqCxqUjQYk3sHYwcl4oYqPBzZcv6yIkg4HLANdfYTO/ekhtxmjbZHk8q9U+/egmq"
			+ "LASw0vUn1SaBD/1E88xS++ri7tRll/WBRtM2ORq6ufs9f2T1kJnEr1maCJvUbtML"
			+ "7gTOgptn5dr4aUJXKWUQlA/tbP6PZ3zYfc/Hs0bf+A==";

}
