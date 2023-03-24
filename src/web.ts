import { WebPlugin } from '@capacitor/core';

import type { WelcomeSoundPlugin } from './definitions';

export class WelcomeSoundWeb extends WebPlugin implements WelcomeSoundPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
