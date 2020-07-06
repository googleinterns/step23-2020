FROM gitpod/workspace-full

USER gitpod

RUN sudo apt-get -q update && \
    sudo apt-get install -yq clang-format

COPY setup-flutter-linux.sh /tmp/
RUN /tmp/setup-flutter-linux.sh
