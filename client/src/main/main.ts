import { Connect, ConnectConfig, defaultConnectConfig } from '@genesislcap/foundation-comms';
import { Navigation } from '@genesislcap/foundation-header';
import { baseLayerLuminance, StandardLuminance } from '@microsoft/fast-components';
import { FASTElement, customElement, observable, DOM } from '@microsoft/fast-element';
import { Container, inject, Registration } from '@microsoft/fast-foundation';
import { DefaultRouteRecognizer } from '@microsoft/fast-router';
import { DynamicTemplate as template, LoadingTemplate, MainTemplate } from './main.template';
import { MainStyles as styles } from './main.styles';
import { MainRouterConfig } from '../routes';
import * as Components from '../components';
import { logger } from '../utils';
import { importPBCAssets } from '@genesislcap/foundation-shell/pbc';
import { App } from '@genesislcap/foundation-shell/app';
import { configureDesignSystem } from '@genesislcap/foundation-ui';
import designTokens from '../styles/design-tokens.json';
import { Store, StoreEventDetailMap } from '../store';


const name = 'blank-app';

// eslint-disable-next-line
declare var API_HOST: string;

@customElement({
  name,
  template,
  styles,
})
export class MainApplication extends FASTElement {
  
  @inject(MainRouterConfig) config!: MainRouterConfig;
  @inject(Navigation) navigation!: Navigation;
  
  @App app: App;
  @Connect connect!: Connect;
  @Container container!: Container;
  @Store store: Store;
  @observable provider!: any;
  @observable ready: boolean = false;
  @observable data: any = null;

  async connectedCallback() {
    this.registerDIDependencies();
    super.connectedCallback();
    this.addEventListeners();
    this.readyStore();
    await this.loadPBCs();
    await this.loadRemotes();
    DOM.queueUpdate(() => {
      configureDesignSystem(this.provider, designTokens);
    });
  }

  async loadPBCs() {
    /**
     * Import PBC assets that may have been added to the ./pbc directory by genx or by hand
     */
    const pbcAssets = await importPBCAssets();
    /**
     * Register bulk assets
     */
    this.app.registerAssets(pbcAssets);
    /**
     * Register the top-level route collection
     */
    this.app.registerRouteCollection(this.config.routes);
  }

  disconnectedCallback() {
    super.disconnectedCallback();
    this.removeEventListeners();
    this.disconnectStore();
  }

  onDarkModeToggle() {
    baseLayerLuminance.setValueFor(
      this.provider,
      baseLayerLuminance.getValueFor(this.provider) === StandardLuminance.DarkMode
        ? StandardLuminance.LightMode
        : StandardLuminance.DarkMode
    );
  }

  async loadRemotes() {
    const { registerComponents } = Components;
    await registerComponents();
    this.ready = true;
  }

  /**
   * You can use various directives in templates like when(), which enables conditional rendering,
   * and you can also split your templates up and return them based on some state.
   */
  public selectTemplate() {
    return this.ready ? MainTemplate : LoadingTemplate;
  }

  private registerDIDependencies() {
    this.container.register(
      Registration.transient(DefaultRouteRecognizer, DefaultRouteRecognizer),
      /**
       * Register custom configs for core services and micro frontends. Note this can also be done via
       * provideDesignSystem().register(...) if preferred.
       *
       * Registration.instance<CredentialManagerConfig>(CredentialManagerConfig, {
       *  ...defaultCredentialManagerConfig,
       *  cookie: {
       *    ...defaultCredentialManagerConfig.cookie,
       *    path: '/login',
       *  },
       * }),
       */
      Registration.instance<ConnectConfig>(ConnectConfig, {
        ...defaultConnectConfig,
        connect: {
          ...defaultConnectConfig.connect,
          heartbeatInterval: 15_000,
        },
      }),
      /**
       * // example of setting grid options for all grids from app level
       * Registration.instance<GridOptionsConfig>(GridOptionsConfig, {
       *  defaultCsvExportParams: csvExportParams,
       * }),
       */
    );
  }

  protected addEventListeners() {
    this.addEventListener('store-connected', this.store.onConnected);
  }

  protected removeEventListeners() {
    this.removeEventListener('store-connected', this.store.onConnected);
  }

  protected readyStore() {
    // @ts-ignore
    this.$emit('store-connected', this);
    this.$emit('store-ready', true);
  }

  protected disconnectStore() {
    this.$emit('store-disconnected');
  }
}
