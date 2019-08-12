import Md5 from './Md5.js';
export default class Md5Utils {
    static md5(value, hashIterations = 1) {
        let hash = Md5.hashStr(value).toString();
        if (hashIterations > 1) {
            for (let i = 1; i < hashIterations; i++) {
                hash = Md5.hashStr(hash).toString();
            }
        }
        return hash;
    }
}
