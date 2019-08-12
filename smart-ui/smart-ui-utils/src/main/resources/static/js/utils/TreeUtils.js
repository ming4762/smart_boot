export default class TreeUtils {
    static convertList2Tree(list, treeCode, topParentCode) {
        if (list == null) {
            return null;
        }
        if (!topParentCode) {
            topParentCode = '0';
        }
        let treeList = [];
        if (treeCode.length !== 2) {
            console.error('请指明treeCode', treeCode);
        }
        let code = treeCode[0];
        let parentCode = treeCode[1];
        for (let value of list) {
            let parentId = value[parentCode];
            if (parentId === null || parentId === topParentCode) {
                treeList.push(value);
                continue;
            }
            for (let parent of list) {
                let id = parent[code];
                if (id === parentId) {
                    if (!parent[this.CHILDREN]) {
                        parent[this.CHILDREN] = [];
                    }
                    parent[this.CHILDREN].push(value);
                    parent[this.HAS_CHILD] = true;
                    value[this.HAS_PARENT] = true;
                }
            }
        }
        return treeList;
    }
}
TreeUtils.CHILDREN = 'children';
TreeUtils.HAS_CHILD = 'hasChild';
TreeUtils.HAS_PARENT = 'hasParent';
