import { registerPlugin } from '@capacitor/core';

import type { WelcomeSoundPlugin } from './definitions';

const WelcomeSound = registerPlugin<WelcomeSoundPlugin>('WelcomeSound', {
  web: () => import('./web').then(m => new m.WelcomeSoundWeb()),
});

export * from './definitions';
export { WelcomeSound };
