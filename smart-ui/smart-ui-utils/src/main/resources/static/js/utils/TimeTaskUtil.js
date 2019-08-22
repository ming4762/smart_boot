export default class TimeTaskUtil {
    static delay(timeout, ...parameters) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(parameters);
            }, timeout);
        });
    }
    static loop(targetFunction, times, delay, ...parameter) {
        let num = 0;
        const loop = setInterval(() => {
            try {
                targetFunction(loop, ...parameter);
            }
            catch (error) {
                console.error(error);
            }
            num++;
            if (num === times) {
                clearInterval(loop);
            }
        }, delay);
    }
}
