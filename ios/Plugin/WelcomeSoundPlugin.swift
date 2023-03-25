import Foundation
import Capacitor
import AVFAudio

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(WelcomeSoundPlugin)
public class WelcomeSoundPlugin: CAPPlugin {
    private var isEnabled = true
    private lazy var config = welcomeSoundConfig()

    var player: AVAudioPlayer?

    override public func load() {
        self.isEnabled = config.enable
        play()
    }

    @objc func play() {
        guard self.isEnabled else {
            return
        }

        let parsed = parseFileName(config.fileName)
        let name = parsed?.name
        let ext = parsed?.ext

        let audioSession = AVAudioSession.sharedInstance()

        do {
            try audioSession.setCategory(.playback)
            try audioSession.setActive(true)
        } catch {
            print("Failed to configure audio session: \(error)")
        }

        guard let url = Bundle.main.url(forResource: name, withExtension: ext) else {
            print("File : \(name ?? "").\(ext ?? "") not found")
            return
        }

        do {
            player = try AVAudioPlayer(contentsOf: url)
            player?.prepareToPlay()
            player?.play()
            if ((player) != nil){
                DispatchQueue.main.asyncAfter(deadline: .now() + player!.duration) {
                    do {
                        try audioSession.setActive(false, options: .notifyOthersOnDeactivation)
                    } catch {
                        print("Failed to deactivate audio session: \(error)")
                    }
                }
            }
        } catch {
            print("Failed to play sound: \(error)")
        }
    }

    @objc func enable(_ call: CAPPluginCall) {
        isEnabled = true
        let fileName = call.getString("fileName", config.fileName)
        config.enable = true
        config.fileName = fileName
        setWelcomeSoundConfig(config: config)
        call.resolve()
    }

    @objc func disable(_ call: CAPPluginCall) {
        isEnabled = false
        config.enable = false
        setWelcomeSoundConfig(config: config)
        call.resolve()
    }

    private func setWelcomeSoundConfig(config: WelcomeSoundConfig) {
        let preferences = UserDefaults.standard
        preferences.set(config.enable, forKey: "welcomeSoundEnable")
        preferences.set(config.fileName, forKey: "welcomeSoundFilename")
    }

    private func parseFileName(_ fileName: String) -> (name: String, ext: String)? {
        guard let url = URL(string: fileName) else {
            return nil
        }
        let name = url.deletingPathExtension().lastPathComponent
        let ext = url.pathExtension
        return (name, ext)
    }


    private func welcomeSoundConfig() -> WelcomeSoundConfig {
        var config = WelcomeSoundConfig()
        let preferences = UserDefaults.standard
        if let enable = preferences.object(forKey: "welcomeSoundEnable") as? Bool {
            config.enable = enable
        }
        if let fileName = preferences.string(forKey: "welcomeSoundFilename") {
            config.fileName = fileName
        }
        return config
    }
}
