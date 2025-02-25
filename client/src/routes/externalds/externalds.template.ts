import { html, repeat, when, ref } from '@microsoft/fast-element';
import type { ExternalDS } from './externalds';

export const columnDefs: any[] = [
    {field: 'ENTITY_ID', headerName: 'Id'},
    {field: 'VERSION', headerName: 'Version'},
    {field: 'DESCRIPTION', headerName: 'Description'},
    {field: 'QUANTITY', headerName: 'Quantity'},
];
export const ExternalDSTemplate = html<ExternalDS>`
<div class="split-layout">
    <div class="top-layout">
        <zero-card class="card">
            <span class="card-title">Issuances</span>
            <zero-grid-pro ${ref('grid')} rowHeight="45" only-template-col-defs>
                ${when(x => x.connection.isConnected, html`
                  <grid-pro-genesis-datasource resource-name="ALL_ISSUANCES"></grid-pro-genesis-datasource>
                  ${repeat(() => columnDefs, html`
                    <grid-pro-column :definition="${x => x}" />
                  `)}
                `)}
            </zero-grid-pro>
        </zero-card>
    </div>
</div>
`;
