{ pkgs, ... }:
let
  kotlinLspVersion = "0.253.10629";
  androidSdkPath = "/home/user/android-sdk";
in
{
  # Which nixpkgs channel to use.
  channel = "stable-24.05"; # or "unstable"

  # Use https://search.nixos.org/packages to find packages
  packages = [
    pkgs.openjdk21
    pkgs.kotlin
    pkgs.android-tools
    pkgs.android-studio
    # Additional packages from Dockerfile
    pkgs.zsh
    pkgs.unzip
    pkgs.git-lfs
    pkgs.curl
    pkgs.wget
    pkgs.gcc # For build-essential
    pkgs.openssl
    pkgs.webkitgtk
    pkgs.xdotool
    pkgs.libayatana-appindicator
    pkgs.librsvg
    pkgs.fontconfig
  ];

  # Sets environment variables in the workspace
  env = {
    # For Android SDK
    ANDROID_HOME = androidSdkPath;
    ANDROID_SDK_ROOT = androidSdkPath;

    # For signing the Android app
    # Replace with your actual credentials, ideally using IDX secrets
    SIGNING_STORE_PASSWORD = "123456";
    SIGNING_KEY_ALIAS = "key0";
    SIGNING_KEY_PASSWORD = "123456";
  };

  idx = {
    # Search for the extensions you want on https://open-vsx.org/ and use "publisher.id"
    extensions = [
      "vscjava.vscode-gradle"
    ];
    
    # Enable previews
    previews = {
      enable = true;
      previews = {
        # web = {
        #   command = ["./gradlew" "wasmJsBrowserRun" "--no-daemon"];
        #   manager = "web";
        #   env = {
        #     PORT = "$PORT";
        #   };
        # };
        # android = {
        #   command = ["flutter" "run" "--machine" "-d" "android" "-d" "localhost:5555"];
        #   manager = "flutter";
        # };
      };
    };

    # Workspace lifecycle hooks
    workspace = {
      # Runs when a workspace is first created
      onCreate = {
        # Accept Android SDK licenses
        accept-licenses = "yes | sdkmanager --licenses";
        # Install required Android SDK components from Dockerfile
        install-sdk-components = ''
          sdkmanager --install "platform-tools" "platforms;android-35" "platforms;android-36" "build-tools;36.1.0" "cmake;3.22.1" "ndk;26.1.10909125"
        '';
        # Install Oh My Zsh and custom scripts
        install-oh-my-zsh = ''
          git clone https://github.com/ahmedhosnypro/awesome-shell-scripts /tmp/awesome-shell-scripts
          chmod +x /tmp/awesome-shell-scripts/shell/install_ohmyzsh.sh
          /tmp/awesome-shell-scripts/shell/install_ohmyzsh.sh
        '';
        # Download and install the official Kotlin VSIX
        install-kotlin-vsix = ''
          wget -O /tmp/kotlin.vsix "https://download-cdn.jetbrains.com/kotlin-lsp/${kotlinLspVersion}/kotlin-${kotlinLspVersion}.vsix"
          code --install-extension /tmp/kotlin.vsix --force
        '';
        # Add Android SDK tools to the PATH for interactive shells
        add-sdk-to-path = ''
          echo 'export PATH="$PATH:${androidSdkPath}/platform-tools:${androidSdkPath}/emulator"' >> /home/user/.zshrc
        '';
      };
      # Runs when the workspace is (re)started
      onStart = {
        # Example: start a background task to watch and re-build backend code
        # watch-backend = "npm run watch-backend";
      };
    };
  };
}
