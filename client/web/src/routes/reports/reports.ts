import {ReportTemplate as template} from "./reports.template";
import { customElement, FASTElement, observable } from '@microsoft/fast-element';
import { Connect } from '@genesislcap/foundation-comms';

const name = 'reports-route';

@customElement({
    name,
    template,
})
export class Reports extends FASTElement {



    @Connect connection: Connect;


    constructor() {
        super();
    }
}
