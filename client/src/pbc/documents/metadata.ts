import type { AppMetadata } from '@genesislcap/foundation-shell/app';

/**
 * @public
 */
export const metadata: AppMetadata = {
  name: '@genesislcap/pbc-documents-ui',
  description: 'Genesis Documents PBC',
  version: '1.2.1',
  prerequisites: {
    '@genesislcap/foundation-ui': '14.*',
    gsf: '7.*',
  },
  dependencies: {
    '@genesislcap/pbc-documents-ui': '0.0.10',
    serverDepId: '7.2.0',
  },
};
