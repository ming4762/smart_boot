define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var TreeUtils = (function () {
        function TreeUtils() {
        }
        TreeUtils.convertList2Tree = function (list, treeCode, topParentCode) {
            if (list == null) {
                return null;
            }
            if (!topParentCode) {
                topParentCode = '0';
            }
            var treeList = [];
            if (treeCode.length !== 2) {
                console.error('请指明treeCode', treeCode);
            }
            var code = treeCode[0];
            var parentCode = treeCode[1];
            for (var _i = 0, list_1 = list; _i < list_1.length; _i++) {
                var value = list_1[_i];
                var parentId = value[parentCode];
                if (parentId === null || parentId === topParentCode) {
                    treeList.push(value);
                    continue;
                }
                for (var _a = 0, list_2 = list; _a < list_2.length; _a++) {
                    var parent_1 = list_2[_a];
                    var id = parent_1[code];
                    if (id === parentId) {
                        if (!parent_1[this.CHILDREN]) {
                            parent_1[this.CHILDREN] = [];
                        }
                        parent_1[this.CHILDREN].push(value);
                        parent_1[this.HAS_CHILD] = true;
                        value[this.HAS_PARENT] = true;
                        continue;
                    }
                }
            }
            return treeList;
        };
        TreeUtils.CHILDREN = 'children';
        TreeUtils.HAS_CHILD = 'hasChild';
        TreeUtils.HAS_PARENT = 'hasParent';
        return TreeUtils;
    }());
    exports.default = TreeUtils;
});
