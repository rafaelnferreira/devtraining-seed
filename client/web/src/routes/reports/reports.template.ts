import { html, repeat, when, ref } from '@microsoft/fast-element';
import type { Reports } from './reports';

export const ReportTemplate = html<Reports>`
<div>
    

        <zero-card>
            <span>Trade View</span>
            <zero-grid-pro rowHeight="45">
                ${when(x => x.connection.isConnected, html`
                  <grid-pro-genesis-datasource resource-name="ALL_TRADES">
                      
                  </grid-pro-genesis-datasource>
                `)}
            </zero-grid-pro>
        </zero-card>
    
</div>
`;
