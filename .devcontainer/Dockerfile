# Devcontainer for building Android target of the Compose Multiplatform project without Android Studio
FROM mcr.microsoft.com/devcontainers/java:21-bookworm

ARG ANDROID_SDK_VERSION=11076708
ARG KOTLIN_LSP_VERSION=0.253.10629
ENV ANDROID_HOME=/opt/android-sdk \
    ANDROID_SDK_ROOT=/opt/android-sdk \
    PATH=$PATH:/opt/android-sdk/cmdline-tools/latest/bin:/opt/android-sdk/platform-tools:/opt/android-sdk/emulator

USER root
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
    unzip git-lfs \
    nano \
    libwebkit2gtk-4.1-dev \
    build-essential \
    curl \
    wget \
    file \
    libxdo-dev \
    libssl-dev \
    libayatana-appindicator3-dev \
    librsvg2-dev \
    zsh \
    git \
    fontconfig \
    unzip \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*


# Clone awesome-shell-scripts repo and use its scripts for Oh My Zsh and Bun
RUN git clone https://github.com/ahmedhosnypro/awesome-shell-scripts /opt/awesome-shell-scripts

# Install Oh My Zsh, Powerlevel10k, and plugins using the repo's script
RUN chmod +x /opt/awesome-shell-scripts/shell/install_ohmyzsh.sh && \
    /opt/awesome-shell-scripts/shell/install_ohmyzsh.sh

# Set zsh as default shell
ENV SHELL=/usr/bin/zsh
SHELL ["/usr/bin/zsh", "-c"]

# Install Android command line tools and required components
RUN mkdir -p /opt/android-sdk/cmdline-tools \
    && cd /tmp \
    && wget -q https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_SDK_VERSION}_latest.zip \
    && unzip -q commandlinetools-linux-${ANDROID_SDK_VERSION}_latest.zip \
    && rm commandlinetools-linux-${ANDROID_SDK_VERSION}_latest.zip \
    && mkdir -p /opt/android-sdk/cmdline-tools/latest \
    && mv cmdline-tools/* /opt/android-sdk/cmdline-tools/latest/ \
    && rm -rf /tmp/cmdline-tools \
    && yes | /opt/android-sdk/cmdline-tools/latest/bin/sdkmanager --sdk_root=/opt/android-sdk --licenses \
    && /opt/android-sdk/cmdline-tools/latest/bin/sdkmanager --sdk_root=/opt/android-sdk \
        "platform-tools" \
        "platforms;android-35" \
        "platforms;android-36" \
        "build-tools;36.1.0" \
        "cmake;3.22.1" \
        "ndk;26.1.10909125"

# Install Bun using the awesome-shell-scripts repo script
RUN chmod +x /opt/awesome-shell-scripts/shell/install_bun.sh && \
    /opt/awesome-shell-scripts/shell/install_bun.sh

# Cache Kotlin VS Code VSIX and standalone LSP zip for offline installs
RUN KOTLIN_LSP_DIR=/opt/kotlin-lsp \
    && mkdir -p ${KOTLIN_LSP_DIR} \
    && cd /tmp \
    && wget -q https://download-cdn.jetbrains.com/kotlin-lsp/${KOTLIN_LSP_VERSION}/kotlin-${KOTLIN_LSP_VERSION}.vsix \
    && wget -q https://download-cdn.jetbrains.com/kotlin-lsp/${KOTLIN_LSP_VERSION}/kotlin-${KOTLIN_LSP_VERSION}.zip \
    && mv kotlin-${KOTLIN_LSP_VERSION}.vsix kotlin-${KOTLIN_LSP_VERSION}.zip ${KOTLIN_LSP_DIR}/

# Prepare gradle cache directory with root ownership
RUN mkdir -p /root/.gradle && \
    mkdir -p /opt/android-sdk

USER root
