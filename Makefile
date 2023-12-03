include etc/configuration.mk

KEYSTORE_FILE = etc/keystore.file
ANDROID_PLATFORM_PATH = $(ANDROID_PATH)/platforms/$(PLATFORM)
PACKAGE_PATH = $(subst .,/,$(PACKAGE))
ifeq ($(LIBS), )
  LIBS_CLASSPATH =
else
  LIBS_CLASSPATH = -classpath $(LIBS)
endif
PATH += :$(ANDROID_PATH)/tools:$(ANDROID_PATH)/platform-tools:$(ANDROID_PATH)/build-tools/34.0.0

.PHONY: all
all: bin/$(NAME)-$(VERSION).apk

$(KEYSTORE_FILE):
	keytool -genkey -alias $(KEY_ALIAS) -keyalg RSA -dname $(KEY_DNAME) -keysize 2048 -validity 10000 -keypass $(KEY_PASSWORD) -keystore $(KEYSTORE_FILE) -storepass $(KEY_STORE_PASSWORD)

gen/$(PACKAGE_PATH)/R.java: AndroidManifest.xml res/*/*
	mkdir -p gen/$(PACKAGE_PATH)
	aapt p -m -M AndroidManifest.xml -S res -I $(ANDROID_PLATFORM_PATH)/android.jar -J gen

bin/classes: gen/$(PACKAGE_PATH)/R.java src/$(PACKAGE_PATH)/*
	mkdir -p bin/classes
	javac -encoding ascii -source 1.8 -target 1.8 -d bin/classes -bootclasspath $(ANDROID_PLATFORM_PATH)/android.jar $(LIBS_CLASEPATH) gen/$(PACKAGE_PATH)/R.java src/$(PACKAGE_PATH)/*
	touch bin/classes

bin/classes.dex: bin/classes
	d8 --no-desugaring --output bin bin/classes/$(PACKAGE_PATH)/*.class $(LIBS)

bin/$(NAME)-$(VERSION)-raw.apk: AndroidManifest.xml res/*/* bin/classes.dex
	aapt p -f -M AndroidManifest.xml -S res -I $(ANDROID_PLATFORM_PATH)/android.jar -F bin/$(NAME)-$(VERSION)-raw.apk

bin/$(NAME)-$(VERSION)-unzipalign.apk: bin/$(NAME)-$(VERSION)-raw.apk bin/classes.dex
	cp bin/$(NAME)-$(VERSION)-raw.apk bin/$(NAME)-$(VERSION)-unzipalign.apk
	(cd bin; zip $(NAME)-$(VERSION)-unzipalign.apk classes.dex)

bin/$(NAME)-$(VERSION)-unsigned.apk: bin/$(NAME)-$(VERSION)-unzipalign.apk
	zipalign -f 4 bin/$(NAME)-$(VERSION)-unzipalign.apk bin/$(NAME)-$(VERSION)-unsigned.apk

bin/$(NAME)-$(VERSION).apk: $(KEYSTORE_FILE) bin/$(NAME)-$(VERSION)-unsigned.apk
	apksigner sign --append-signature --ks $(KEYSTORE_FILE) --ks-key-alias $(KEY_ALIAS) --ks-pass pass:$(KEY_STORE_PASSWORD) --key-pass pass:$(KEY_PASSWORD) --v2-signing-enabled true --in bin/$(NAME)-$(VERSION)-unsigned.apk --out bin/$(NAME)-$(VERSION).apk

.PHONY: clean
clean:
	rm -rf gen bin

.PHONY: verify
verify: $(KEYSTORE_FILE) bin/$(NAME)-$(VERSION).apk
	zipalign -c -v 4 bin/$(NAME)-$(VERSION).apk
	keytool -list -keystore $(KEYSTORE_FILE) -storepass $(KEY_STORE_PASSWORD)
	apksigner verify --print-certs bin/$(NAME)-$(VERSION).apk

.PHONY: emulator
emulator:
	emulator -no-snapshot-save -avd $(AVD) &

.PHONY: log
log:
	adb logcat

.PHONY: clear-log
clear-log:
	adb logcat -c

.PHONY: shell
shell:
	adb shell

.PHONY: install
install: bin/$(NAME)-$(VERSION).apk
	adb install bin/$(NAME)-$(VERSION).apk

.PHONY: reinstall
reinstall: bin/$(NAME)-$(VERSION).apk
	adb install -r bin/$(NAME)-$(VERSION).apk

.PHONY: start
start:
	adb shell am start -a android.intent.action.MAIN -n $(PACKAGE)/.$(ACTIVITY)

.PHONY: run
run: reinstall start

.PHONY: stop
stop:
	adb shell kill -9 `adb shell ps | grep $(PACKAGE) | tr -s ' ' | cut -d ' ' -f 2`

.PHONY: uninstall
uninstall:
	adb uninstall $(PACKAGE)
