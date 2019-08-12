import ComponentBuilder from 'ComponentBuilder';
export default class SmartContainer extends ComponentBuilder {
    template() {
        return `
    <div class="common-container">
      <slot></slot>
    </div>
    `;
    }
}
