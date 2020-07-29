# Local repo configuration configuration. This can be used to overwrite
# configs/environment variables with persistent defaults. It should not be
# checked into git.
-include environment.mk

CLANG_FORMAT?=clang-format
FIND?=find
FLUTTER_APP_ROOT?=frontend/ui
FLUTTER_CLI?=flutter
MAVEN_CLI?=mvn
MAVEN_FRONTEND_ROOT?=frontend
OPENSSL?=openssl
SHELL=/bin/bash


format:
	$(FIND) frontend -name '*.java' -exec $(CLANG_FORMAT) --style=Google -i --sort-includes '{}' +
	$(FLUTTER_CLI) format $(FLUTTER_APP_ROOT)

test-secrets: encrypted/ui-maps-places-test.enc
	mkdir test-secrets
	$(OPENSSL) aes-256-cbc -d -pbkdf2 -iter 100000 -md sha512 -iv 683df25da352abfd5a5a559505c9034a \
		-in encrypted/ui-maps-places-test.enc -out test-secrets/ui-maps-places-test

instantiate-html-template-test: test-secrets
	sed "s/MAPS_PLACES_API_KEY/$$(cat test-secrets/ui-maps-places-test)/g" \
		$(FLUTTER_APP_ROOT)/web/index.template.html > $(FLUTTER_APP_ROOT)/web/index.html

clean:
	rm -rf test-secrets
	pushd $(FLUTTER_APP_ROOT) && $(FLUTTER_CLI) clean && popd
	$(MAVEN_CLI) clean --file $(MAVEN_FRONTEND_ROOT)/pom.xml

flutter-pub-get:
	pushd $(FLUTTER_APP_ROOT) && $(FLUTTER_CLI) pub get && popd

build-and-copy-flutter-test: instantiate-html-template-test flutter-pub-get
	pushd $(FLUTTER_APP_ROOT) && $(FLUTTER_CLI) build web && popd
	cp -r $(FLUTTER_APP_ROOT)/build/web/* $(MAVEN_FRONTEND_ROOT)/src/main/webapp/

test-flutter: instantiate-html-template-test flutter-pub-get
	pushd $(FLUTTER_APP_ROOT) && $(FLUTTER_CLI) test && popd

test-frontend-server:
	$(MAVEN_CLI) test --file $(MAVEN_FRONTEND_ROOT)/pom.xml

test: test-flutter test-frontend-server

package: build-and-copy-flutter-test
	$(MAVEN_CLI) package --file $(MAVEN_FRONTEND_ROOT)/pom.xml

run-local-frontend: build-and-copy-flutter-test
	$(MAVEN_CLI) package appengine:run --file $(MAVEN_FRONTEND_ROOT)/pom.xml

