import { WebPlugin } from '@capacitor/core';

import type { WelcomeSoundPlugin } from './definitions';

export class WelcomeSoundWeb extends WebPlugin implements WelcomeSoundPlugin {
  async enable(): Promise<any> {
    throw this.unimplemented('Not implemented on web.');
  }

  async disable(): Promise<any> {
    throw this.unimplemented('Not implemented on web.');
  }
}
