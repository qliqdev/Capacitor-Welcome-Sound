export interface WelcomeSoundPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
