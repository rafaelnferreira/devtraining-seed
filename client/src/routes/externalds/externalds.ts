import { customElement, FASTElement, observable } from '@microsoft/fast-element';
import { ExternalDSTemplate as template } from './externalds.template';
import {ExternalDSStyles as styles} from './externalds.styles';
import { ZeroGridPro } from '@genesislcap/foundation-zero-grid-pro';
import { Connect } from '@genesislcap/foundation-comms';

const name = 'externalds-route';

@customElement({
  name,
  template,
  styles,
})
export class ExternalDS extends FASTElement {

  public grid!: ZeroGridPro;

  @Connect connection: Connect;

  constructor() {
    super();
  }
}
