#!/usr/bin/env bash

if ! which flutter; then
  wget -P /tmp/ https://storage.googleapis.com/flutter_infra/releases/stable/linux/flutter_linux_1.17.4-stable.tar.xz
  tar xf /tmp/flutter_linux_1.17.4-stable.tar.xz -C ~/
  rm /tmp/flutter_linux_1.17.4-stable.tar.xz
  cat << EOF >> ~/.bashrc
export FLUTTER_HOME=\$HOME/flutter
if [[ -d \$FLUTTER_HOME/bin ]]; then
  export PATH=\$PATH:\$FLUTTER_HOME/bin
fi
EOF
  export FLUTTER_HOME=$HOME/flutter
  export PATH=$PATH:$FLUTTER_HOME/bin
  flutter channel beta
  flutter upgrade
  flutter config --enable-web
fi
