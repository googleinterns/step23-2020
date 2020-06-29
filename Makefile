
ANGULAR_CLI?=ng
MAVEN_CLI?=mvn
ANGULAR_APP_ROOT?=frontend/ui
MAVEN_FRONTEND_ROOT?=frontend
CLANG_FORMAT?=clang-format

# Is there a better way to do this??
# From https://stackoverflow.com/questions/4036191
rwildcard=$(wildcard $1$2) $(foreach d,$(wildcard $1*),$(call rwildcard,$d/,$2))

format:
	$(CLANG_FORMAT) --style=Google -i --sort-includes $(call rwildcard,frontend/,*.java)
	pushd $(ANGULAR_APP_ROOT) && $(ANGULAR_CLI) lint && popd

clean:
	# TODO: Figure out the right thing to do to clean up angular trash.
	$(MAVEN_CLI) clean --file $(MAVEN_FRONTEND_ROOT)/pom.xml

build-and-copy-angular:
	pushd $(ANGULAR_APP_ROOT) && $(ANGULAR_CLI) build && popd
	cp -r $(ANGULAR_APP_ROOT)/dist/tripmeout/* $(MAVEN_FRONTEND_ROOT)/src/main/webapp/

test-angular:
	pushd $(ANGULAR_APP_ROOT) && $(ANGULAR_CLI) test && popd

test-frontend-server:
	$(MAVEN_CLI) test --file $(MAVEN_FRONTEND_ROOT)/pom.xml

package: build-and-copy-angular
	$(MAVEN_CLI) package --file $(MAVEN_FRONTEND_ROOT)/pom.xml

run-local-frontend: build-and-copy-angular
	$(MAVEN_CLI) package appengine:run --file $(MAVEN_FRONTEND_ROOT)/pom.xml

