// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'

export default class SmartContainer extends ComponentBuilder {

  protected template () {
    return `
    <div class="common-container">
      <slot></slot>
    </div>
    `
  }
}