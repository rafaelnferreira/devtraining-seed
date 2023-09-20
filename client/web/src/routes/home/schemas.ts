const conditionalSchemaEntry = (predicate: boolean, entry) => {
  return predicate ? [entry] : [];
};

const tradeFormSchema = (editing?: boolean) => ({
  type: 'VerticalLayout',
  elements: [
    ...conditionalSchemaEntry(editing, {
      type: 'Control',
      label: 'ID',
      scope: '#/properties/TRADE_ID',
      options: {
        readonly: true,
      },
    }),
    {
        "type": "Control",
        "label": "Counterparty",
        "scope": "#/properties/COUNTERPARTY_ID",
        "options": {
            allOptionsResourceName: "ALL_COUNTERPARTIES",
            valueField: "COUNTERPARTY_ID",
            labelField: "COUNTERPARTY_NAME",
            data: null,
        },
    },
    {
        "type": "Control",
        "label": "Direction",
        "scope": "#/properties/DIRECTION"
    },
    {
        "type": "Control",
        "label": "Instrument",
        "scope": "#/properties/INSTRUMENT_ID",
        "options": {
            allOptionsResourceName: "ALL_INSTRUMENTS",
            valueField: "INSTRUMENT_ID",
            labelField: "INSTRUMENT_NAME",
            data: null,
        },
    },
    {
        "type": "Control",
        "label": "Price",
        "scope": "#/properties/PRICE"
    },
    {
        "type": "Control",
        "label": "Quantity",
        "scope": "#/properties/QUANTITY"
    },
    {
        "type": "Control",
        "label": "Symbol",
        "scope": "#/properties/SYMBOL"
    },
    {
            "type": "Control",
            "label": "TradeDate",
            "scope": "#/properties/TRADE_DATE"
    },
    {
            "type": "Control",
            "label": "EnteredBy",
            "scope": "#/properties/ENTERED_BY"
    },
    {
            "type": "Control",
            "label": "TradeStatus",
            "scope": "#/properties/TRADE_STATUS"
    },
  ],
});
export const tradeFormCreateSchema = tradeFormSchema(false);
export const tradeFormUpdateSchema = tradeFormSchema(true);
