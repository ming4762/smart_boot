'use strict';
export default class RsaUtils {
    constructor() {
        try {
            if (JSEncrypt) {
                this.GLOBAL_KEY = new JSEncrypt();
            }
        }
        catch (e) {
        }
    }
    createKey() {
        const jSEncrypt = new JSEncrypt();
        return new RsaKey(jSEncrypt.getPublicKeyB64(), jSEncrypt.getPrivateKeyB64());
    }
    rsaEncrypt(publicKey, data) {
        this.GLOBAL_KEY.setPublicKey(publicKey);
        return this.GLOBAL_KEY.encrypt(data);
    }
    rsaDecrypt(privateKey, ciphertext) {
        this.GLOBAL_KEY.setPrivateKey(privateKey);
        return this.GLOBAL_KEY.decrypt(ciphertext);
    }
}
class RsaKey {
    constructor(pubKey, priKey) {
        this.pubKey = pubKey;
        this.priKey = priKey;
    }
}
