import { css } from '@microsoft/fast-element';
import { mixinScreen } from '../../styles';

export const ExternalDSStyles = css`
  :host {
    align-items: center;
    justify-content: center;
    flex-direction: column;

    --neutral-stroke-divider-rest: var(--neutral-fill-stealth-rest);
  }

.split-layout {
      display: flex;
      flex-direction: column;
      flex: 1;
      width: 100%;
      height: 100%;
  }

  .card {
      flex: 1;
      margin: calc(var(--design-unit) * 3px);
  }

  .top-layout {
        height: 90%;
        flex-direction: row;
    }

  .card-title {
      padding: calc(var(--design-unit) * 3px);
      background-color: #22272a;
      font-size: 13px;
      font-weight: bold;
  }

`;
