# ComposeTesting - Surface Duo Compose SDK

**ComposeTesting** provides some helper functions that help you easily test your application on dual-screen, foldable, and large screen devices by simulating folding features (fold/hinge) and device gestures.

The library is based on the [testing-kotlin](https://github.com/microsoft/surface-duo-sdk/tree/main/utils/test-utils) library and exposes those utility methods as well, so if your project uses a combination of composables and views, you only need to import **ComposeTesting**.

## Add to your project

1. Make sure you have **mavenCentral()** repository in your top level **build.gradle** or **settings.gradle** file:

    ```gradle
    repositories {
        google()
        mavenCentral()
    }
    ```

2. Add dependencies to the module-level **build.gradle** file (current version may be different from what's shown here).

    ```gradle
    implementation "com.microsoft.device.dualscreen.testing:testing-compose:1.0.0-alpha03"
    ```

3. Also ensure the compileSdkVersion and targetSdkVersion are set to API 31 or newer in the module-level build.gradle file.

    ```gradle
    android { 
        compileSdkVersion 31
        
        defaultConfig { 
            targetSdkVersion 31
        } 
        ... 
    }
    ```

4. Access the testing functions from **ComposeTesting** to test your application. Please refer to the [sample](https://github.com/microsoft/surface-duo-compose-sdk/tree/main/ComposeTesting/sample) for more details.

## API reference

For API reference information for **testing-kotlin** , refer to these resources:

- [UiDevice Extensions](https://github.com/microsoft/surface-duo-sdk/tree/main/utils/test-utils#uidevice-extensions)
- [FoldingFeatureHelper](https://github.com/microsoft/surface-duo-sdk/tree/main/utils/test-utils#foldingfeaturehelper)
- [DeviceModel](https://github.com/microsoft/surface-duo-sdk/tree/main/utils/test-utils#devicemodel)
- [WindowLayoutInfoConsumer](https://github.com/microsoft/surface-duo-sdk/tree/main/utils/test-utils#windowlayoutinfoconsumer)
- [CurrentActivityDelegate](https://github.com/microsoft/surface-duo-sdk/tree/main/utils/test-utils#currentactivitydelegate)
- [ForceClick](https://github.com/microsoft/surface-duo-sdk/tree/main/utils/test-utils#forceclick)
- [ViewMatcher](https://github.com/microsoft/surface-duo-sdk/tree/main/utils/test-utils#viewmatcher)

In addition to the resources above, **ComposeTesting** also offers the APIs described below.

> **Note:** When updating to version `1.0.0-alpha03` and beyond, the import statements for utility functions change from `com.microsoft.device.dualscreen.testing.<function>` to `com.microsoft.device.dualscreen.testing.compose.<function>`. The `compose` part was added to the package name to avoid conflicts with the **testing-kotlin** library.

### FoldingFeatureHelper

These functions provide Compose wrappers for the [FoldingFeatureHelper](https://github.com/microsoft/surface-duo-sdk/tree/main/utils/test-utils#foldingfeaturehelper) methods from **testing-kotlin**. The methods can be used in foldable UI tests to simulate the present of vertical and
horizontal folding features (folds/hinges). The folding features are simulated using `TestWindowLayoutInfo` from Google's [Jetpack WindowManager Testing](https://developer.android.com/reference/androidx/window/testing/layout/package-summary) library.

As described in the **testing-kotlin** documentation, the first step is to create a test rule with the `createWindowLayoutInfoPublisherRule()` function.

```kotlin
fun <A : ComponentActivity> TestRule.simulateVerticalFoldingFeature(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
)
```

Simulate a vertical folding feature in a Compose test. The default parameters create a vertical folding feature that is centered, 0px wide, and in the half-opened state, but these properties can be changed by passing in different parameter values.

```kotlin
fun <A : ComponentActivity> TestRule.simulateHorizontalFoldingFeature(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
)
```

Simulate a horizontal foldingFeature in a Compose test. The default parameters create a horizontal folding feature that is centered, 0px tall, and in the half-opened state, but these properties can be changed by passing in different parameter values.

### StringHelper

These functions can be used for string operations in UI tests to simplify testing code.

```kotlin
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.getString(@StringRes id: Int): String
```

Get resource string inside Compose test with resource id

```kotlin
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.getString(@StringRes id: Int, vararg formatArgs: Any): String
```

Get resource string inside Compose test with resource id and arguments

## Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.opensource.microsoft.com.

When you submit a pull request, a CLA bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., status check, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

## License

Copyright (c) Microsoft Corporation.

MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
