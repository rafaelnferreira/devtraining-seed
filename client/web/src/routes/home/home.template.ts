import { html } from '@microsoft/fast-element';
import type { Home } from './home';

export const HomeTemplate = html<Home>`
  <entity-management
      resourceName="ALL_TRADES"
      title = "Trades"
      entityLabel="Trades"
      createEvent = "EVENT_TRADE_INSERT"
      :columns=${x => x.columns}
  ></entity-management>
`;
