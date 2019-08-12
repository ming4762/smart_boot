import ResizeHandler from '../utils/ResizeHandler.js';
export default {
    data() {
        return {
            clientHeight: 0
        };
    },
    computed: {
        computedTableHeight() {
            return this.clientHeight - 30;
        }
    },
    beforeMount() {
        const $this = this;
        $this.clientHeight = document.body.clientHeight;
        ResizeHandler.bind(() => {
            $this.clientHeight = document.body.clientHeight;
        });
    }
};
