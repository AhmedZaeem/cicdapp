# cicdapp - Shopping App CI/CD

android shopping app with CI pipeline using github actions. built with jetpack compose.

## how to run

open in android studio and run, or `./gradlew assembleDebug` from terminal

## pipeline

4 jobs:
- code quality (lint + detekt)
- unit tests + jacoco coverage
- instrumented tests on emulator (api 30, 33, 34)  
- build debug apk

## issues i ran into

- actions/upload-artifact v3 was deprecated, github just straight up fails the whole workflow if you use it. had to upgrade evrything to v4 (checkout, setup-java, upload-artifact, codecov). wasted like 20 min figuring out why it kept failing
- detekt default rules dont work with compose at all. composable funcitons start with uppercase which breaks FunctionNaming rule. had to change the pattern to `[a-zA-Z][a-zA-Z0-9]*`. also bumped LongMethod threshold to 150 becuase compose functions are long with all the nesting
- detekt config had a `formatting` section that doesnt exist without the formatting plugin, removed it
- String.format without Locale.US was flagged by lint, fixed all of them
- jacoco needed explicit path to `tmp/kotlin-classes/debug` for kotlin class files, not the usual java path
- had to set `unitTests.isReturnDefaultValues = true` otherwise some tests crash
- branch was on main but repo default was master, had to sort that out
- workflow had JDK 17 but gradle daemon needs JDK 21 (its in gradle-daemon-jvm.properties), changed all jobs to use java 21
- emualtor tests were completly broken because `macos-latest` is now arm64 (apple silicon M1 runners). the x86_64 emualtor images cant run on arm hosts at all, it just says `FATAL | Avd's CPU Architecture 'x86_64' is not supported by the QEMU2 emulator on aarch64 host` and then sits there timing out for 11 minutes. switched to `macos-13` which is still intel so x86_64 works fine
- also changed api 28 to 29 because 28 was super slow and kept timing out even on intel, added emulator-boot-timeout of 600 seconds just in case
- then macos-13 got deprecated too lol. github removed it entirely, gives `configuration 'macos-13-us-default' is not supported` error. so had to go back to `macos-latest` but this time use `arm64-v8a` arch instead of x86_64 since all macos runners are apple silicon now. also removed the Nexus 6 profile and dropped api 29 cuz theres no arm64 system image for it, swapped in api 34
- STILL didnt work. emulator kept saying device not found and timing out. turns out `google_apis` target doesnt have arm64 system images for most api lvls. changed target to `default` which actually has arm64 images. also added force-avd-creation, disable-animations, explicit emulator-options with -no-snapshot-save and swiftshader_indirect gpu, and bumped boot timeout to 900s. switched api 30 to 31 too cuz 30 default arm64 image is flakey
- ok arm emulators on macos runners are just completly broken, none of the arm64 images boot at all. gave up on macos entirely. moved the whole instrumented-tests job to `ubuntu-latest` with KVM hardware accelaration enabled. back to x86_64 arch and google_apis target which actually work with KVM on linux. had to add a udev rule step to enable /dev/kvm permissions. this finaly boots the emulator properly
- emulator booted but `addToCartUpdatesCount` test failed with "component is not displayed". the add to cart button wasnt visible on the small emualtor screen so the click did nothing. added `performScrollTo()` before the click and `waitForIdle()` + `waitUntil` after so it actualy waits for the UI to update before checking the cart count

## tests

unit tests: CartViewModel (add, remove, update qty, totals, clear) and ProductRepository (list, lookup, validation)

instrumented: product list shows up, cart button works, add to cart updates count, empty cart msg, cart shows items

