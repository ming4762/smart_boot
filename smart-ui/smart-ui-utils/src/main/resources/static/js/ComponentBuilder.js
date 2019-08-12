export default class ComponentBuilder {
    data() {
        return {};
    }
    props() {
        return {};
    }
    template() {
        return '';
    }
    methods() {
        return {};
    }
    components() {
        return {};
    }
    computed() {
        return {};
    }
    mixins() {
        return [];
    }
    watch() { }
    created() { }
    build() {
        const component = {
            template: this.template(),
            data: this.data,
            methods: this.methods(),
            components: this.components(),
            computed: this.computed(),
            mixins: this.mixins(),
            props: this.props(),
            watch: this.watch()
        };
        if (this['beforeMount']) {
            component['beforeMount'] = this['beforeMount'];
        }
        if (this['created']) {
            component['created'] = this['created'];
        }
        if (this['mounted']) {
            component['mounted'] = this['mounted'];
        }
        if (this['beforeUpdate']) {
            component['beforeUpdate'] = this['beforeUpdate'];
        }
        return component;
    }
}
