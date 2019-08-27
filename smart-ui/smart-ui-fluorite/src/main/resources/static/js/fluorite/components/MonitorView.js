import CommonUtils from '../../utils/CommonUtils.js';
export default {
    props: {
        data: {
            type: Object,
            required: true
        },
        token: {
            type: String,
            required: true
        },
        hd: {
            type: Boolean,
            default: false
        },
        height: {
            type: Number,
            default: 600
        },
        width: {
            type: Number,
            default: 800
        },
        autoplay: {
            type: Boolean,
            default: true
        },
        lazyInit: {
            type: Boolean,
            default: false
        },
        decoderPath: {
            type: String,
            default: CommonUtils.withContextPath('js/ezuikit')
        }
    },
    data() {
        return {
            player: null
        };
    },
    mounted() {
        if (!this.lazyInit) {
            this.initPlayer();
        }
    },
    computed: {
        getUrl() {
            return this.hd ? this.data.hdUrl : this.data.url;
        },
        getPlayDivStyle() {
            return `width:${this.width}px;height:${this.height}px;`;
        }
    },
    watch: {
        data: {
            deep: true,
            handler: function () {
                this.initPlayer();
            }
        }
    },
    methods: {
        play() {
            if (this.player) {
                this.player.play();
            }
        },
        stop() {
            if (this.player) {
                this.player.stop();
            }
        },
        initPlayer(token) {
            if (!window.EZUIKit) {
                CommonUtils.loadJS(CommonUtils.withContextPath('js/ezuikit/ezuikit.js'))
                    .then(() => {
                    this.createPlayer(token);
                });
            }
            else {
                this.createPlayer(token);
            }
        },
        createPlayer(token) {
            let useToken = this.token;
            if (token)
                useToken = token;
            this.player = new window.EZUIKit.EZUIPlayer({
                id: 'playerDiv',
                url: this.getUrl,
                autoplay: this.autoplay,
                accessToken: useToken,
                decoderPath: this.decoderPath,
                width: this.width,
                height: this.height
            });
        }
    },
    template: `
  <div :style="getPlayDivStyle" id="playerDiv"></div>
  `
};
