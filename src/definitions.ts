/// <reference types="@capacitor/cli" />

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    /**
     * These configuration values are available:
     */
    WelcomeSound?: {
      /**
       * Configure of the sound filename from startup.
       *
       * Only available for Android and iOS.
       *
       * @default welcome.aac
       * @example startup.mp3
       */
      fileName?: string;
    };
  }
}

export interface EnableOptions {
  /**
   *  Filename of sound
   *
   * @since 1.0.0
   */
  fileName?: string
}
export interface WelcomeSoundPlugin {
  /**
   *  Enable playing sound on app start
   *
   * @since 1.0.0
   */
  enable(options?: EnableOptions): Promise<any>;

  /**
   *  Disable playing sound on app start
   *
   * @since 1.0.0
   */
  disable(): Promise<any>;
}
