See README.md in the parent directory for general information.

Getting Started
---------------

##### Fetch code

```
git clone https://github.com/googlesamples/android-custom-lint-rules.git
cd android-custom-lint-rules
```

##### Build the validator

`./gradlew build`

##### Copy to the lint directory

`cp ./build/libs/android-custom-lint-rules.jar ~/.android/lint/`

##### Verify whether the issues are registered with lint

`lint --show MainActivityDetector`

##### Run lint

`./gradlew lint`

> Note: If you can't run `lint` directly, you may want to include android tools `PATH` in your
 `~/.bash_profile`.
> (i.e. `PATH=$PATH:~/Library/Android/sdk/tools`)
>
> Then run `source ~/.bash_profile`.
