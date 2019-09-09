var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import ApiService from '../../utils/ApiService.js';
import CollectionUtils from '../../utils/CollectionUtils.js';
const queryDictItemURL = 'public/sys/dictItem/list';
export default class SysApiUtils {
    static getDictItem(...dictCode) {
        return __awaiter(this, void 0, void 0, function* () {
            const parameter = {
                'dictCode@in': dictCode,
                sortName: 'seq'
            };
            const itemList = yield ApiService.postAjax(queryDictItemURL, parameter);
            let result = {};
            if (itemList && itemList.length > 0) {
                const group = CollectionUtils.group(itemList, 'dictCode');
                if (dictCode.length === 1) {
                    result = CollectionUtils.mapToObject(CollectionUtils.listToMap(group.values().next().value, 'itemCode', 'itemValue'));
                }
                else {
                    group.forEach((value, key) => {
                        result[key] = CollectionUtils.mapToObject(CollectionUtils.listToMap(value, 'itemCode', 'itemValue'));
                    });
                }
            }
            return result;
        });
    }
    static getDictItemList(...dictCode) {
        return __awaiter(this, void 0, void 0, function* () {
            const parameter = {
                'dictCode@in': dictCode,
                sortName: 'seq'
            };
            const itemList = yield ApiService.postAjax(queryDictItemURL, parameter);
            let result = [];
            if (itemList && itemList.length > 0) {
                const dealItemList = itemList.map(item => {
                    return {
                        label: item.itemValue,
                        value: item.itemCode,
                        dictCode: item.dictCode
                    };
                });
                const group = CollectionUtils.group(itemList, 'dictCode');
                if (dictCode.length === 1) {
                    result = dealItemList;
                }
                else {
                    group.forEach((value, key) => {
                        result[key] = value;
                    });
                }
            }
            return result;
        });
    }
}
