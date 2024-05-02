import type { AppMetadata } from '@genesislcap/foundation-shell/app';

/**
 * @public
 */
export const metadata: AppMetadata = {
  name: '@genesislcap/pbc-reporting',
  description: 'Genesis Reporting PBC',
  version: '1.2.0',
  prerequisites: {
    '@genesislcap/foundation-ui': '14.*',
    gsf: '7.*',
  },
  dependencies: {
    '@genesislcap/pbc-reporting-ui': '1.0.6',
    serverDepId: '7.2.0',
  },
};
