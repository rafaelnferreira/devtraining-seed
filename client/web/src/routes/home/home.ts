import { customElement, FASTElement, observable } from '@microsoft/fast-element';
import { HomeTemplate as template } from './home.template';
import { HomeStyles as styles } from './home.styles';
import {EntityManagement} from '@genesislcap/foundation-entity-management';
import { ZeroGridPro } from '@genesislcap/foundation-zero-grid-pro';
import { Connect } from '@genesislcap/foundation-comms';

EntityManagement;

const defaultColumnConfig = {
  enableCellChangeFlash: true,
  enableRowGroup: true,
  enablePivot: true,
  enableValue: true,
};

const COLUMNS = [
  {
    ...defaultColumnConfig,
    field: 'TRADE_ID',
    headerName: 'Id',
  },
  {
      ...defaultColumnConfig,
      field: 'COUNTERPARTY_NAME',
      headerName: 'Counterparty',
  },
  {
    ...defaultColumnConfig,
    field: 'QUANTITY',
    headerName: 'Quantity',
  },
  {
    ...defaultColumnConfig,
    field: 'PRICE',
    headerName: 'Price',
  },
  {
      ...defaultColumnConfig,
      field: 'INSTRUMENT_NAME',
      headerName: 'Instrument Name',
    },
  {
    ...defaultColumnConfig,
    field: 'SYMBOL',
    headerName: 'Symbol',
  },
  {
      ...defaultColumnConfig,
      field: 'CURRENCY',
      headerName: 'Currency',
  },
  {
    ...defaultColumnConfig,
    field: 'DIRECTION',
    headerName: 'Direction',
  },
  {
    ...defaultColumnConfig,
    field: 'TRADE_DATE',
    headerName: 'Trade Date',
  },
  {
    ...defaultColumnConfig,
    field: 'ENTERED_BY',
    headerName: 'Entered By',
  },
  {
    ...defaultColumnConfig,
    field: 'TRADE_STATUS',
    headerName: 'Status',
  },
];

const name = 'home-route';

@customElement({
  name,
  template,
  styles,
})
export class Home extends FASTElement {
  @observable columns: any = COLUMNS;

  public positionsGrid!: ZeroGridPro;

  @Connect connection: Connect;

  constructor() {
    super();
  }
}
