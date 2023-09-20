import { html } from '@microsoft/fast-element';
import type { Home } from './home';
import { tradeFormCreateSchema, tradeFormUpdateSchema  } from './schemas';

export const HomeTemplate = html<Home>`
  <entity-management
      resourceName="ALL_TRADES"
      title = "Trades"
      entityLabel="Trades"
      createEvent = "EVENT_TRADE_INSERT"
      updateEvent = "EVENT_TRADE_MODIFY"
      deleteEvent = "EVENT_TRADE_DELETE"
      :columns=${x => x.columns}
      :createFormUiSchema=${ () => tradeFormCreateSchema }
      :updateFormUiSchema=${ () => tradeFormUpdateSchema }
  ></entity-management>
`;
