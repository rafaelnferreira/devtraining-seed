import { FASTElement, customElement, html } from "@microsoft/fast-element";
import type { ViewTemplate } from "@microsoft/fast-element";

const template: ViewTemplate<ApproveTradeButton> = html`
  <div>
    <fast-text-field type="number" @change=${(x) => x.onTradeInput()}
      >Trade ID</fast-text-field
    >
    <fast-button appearence="primary" @click=${(x) => x.send()}
      >Send</fast-button
    >
  </div>
`;

@customElement({ name: "approve-trade-button", template })
export class ApproveTradeButton extends FASTElement {
  private tradeId: number;

  onTradeInput() {
    const input = this.shadowRoot?.querySelector(
      "fast-text-field"
    ) as HTMLInputElement;
    this.tradeId = +input.value;
  }

  send() {
    this.$emit("tradeSubmitted", { tradeId: this.tradeId });
  }
}
