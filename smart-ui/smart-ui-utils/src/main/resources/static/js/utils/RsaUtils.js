define(["require", "exports", "jsencrypt/3.0.0/jsencrypt.min"], function (require, exports, jsencrypt_min_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class RsaUtils {
        static createKey() {
            const jSEncrypt = new jsencrypt_min_1.default();
            return new RsaKey(jSEncrypt.getPublicKeyB64(), jSEncrypt.getPrivateKeyB64());
        }
        static rsaEncrypt(publicKey, data) {
            this.GLOBAL_KEY.setPublicKey(publicKey);
            return this.GLOBAL_KEY.encrypt(data);
        }
        static rsaDecrypt(privateKey, ciphertext) {
            this.GLOBAL_KEY.setPrivateKey(privateKey);
            return this.GLOBAL_KEY.decrypt(ciphertext);
        }
    }
    RsaUtils.GLOBAL_KEY = new jsencrypt_min_1.default();
    exports.default = RsaUtils;
    class RsaKey {
        constructor(pubKey, priKey) {
            this.pubKey = pubKey;
            this.priKey = priKey;
        }
    }
});
