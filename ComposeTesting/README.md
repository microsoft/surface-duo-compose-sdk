# ComposeTesting - Surface Duo Compose SDK

**ComposeTesting** provides some helper functions that help you easily test your application on the dual-screen, foldable and large screen devices, by simulating the foldingFeatures(fold/hinge), device gestures, screenshot comparing and zooming.

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
    implementation "com.microsoft.device.dualscreen.testing:testing-compose:1.0.0-alpha01"
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



### FoldingFeature Helper

These functions can be used in foldable UI tests to simulate the present of vertical and
horizontal foldingFeatures(folds/hinges). The foldingFeatures are simulated using TestWindowLayoutInfo, using the Google [Jetpack WindowManager Testing](https://developer.android.com/reference/androidx/window/testing/layout/package-summary) library.

```kotlin
fun createWindowLayoutInfoPublisherRule(): TestRule
```

Return WindowLayoutInfoPublisherRule which allows you to simulate the different foldingFeature by pushing through different WindowLayoutInfo values.

```kotlin
fun <A : ComponentActivity> TestRule.simulateVerticalFoldingFeature(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
)
```

Simulate a vertical foldingFeature in a Compose test

```kotlin
fun <A : ComponentActivity> TestRule.simulateHorizontalFoldingFeature(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
)
```

Simulate a horizontal foldingFeature in a Compose test

```kotlin
fun <A : ComponentActivity> TestRule.simulateFoldingFeature(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    center: Int,
    size: Int,
    state: FoldingFeature.State,
    orientation: FoldingFeature.Orientation,
) 
```

Simulate a foldingFeature with the given properties in a Compose test

### Swipe Helper

These functions can be used in dualscreen UI tests to simulate swipe gestures that affect
app display. The swipes are simulated using UiDevice, and the coordinates are calculated based
on the display width/height of the testing device.

```kotlin
fun UiDevice.spanFromStart()
```

Span app from the top/left pane

```kotlin
fun UiDevice.spanFromEnd()
```

Span app from the bottom/right pane

```kotlin
fun UiDevice.unspanToStart()
```

Unspan app to the top/left pane

```kotlin
fun UiDevice.unspanToEnd()
```

Unspan app to bottom/right pane

```kotlin
fun UiDevice.switchToStart()
```

Switch app from bottom/right pane to top/left pane

```kotlin
fun UiDevice.switchToEnd() 
```

Switch app from top/left pane to bottom/right pane

```kotlin
fun UiDevice.closeStart() 
```

Close app from top/left pane

```kotlin
fun UiDevice.closeEnd()
```

Close app from bottom/right pane


### Device model

 The DeviceModel class and related helper functions can be used in dualscreen UI tests to help
calculate coordinates for simulated swipe gestures. Device properties are determined using
UiDevice.

```kotlin
fun UiDevice.isSurfaceDuo(): Boolean
```

Checks whether a device is a Surface Duo model

```kotlin
fun UiDevice.getFoldSize(): Int
```

Returns a pixel value of the hinge/fold size of a foldable or dual-screen device

```kotlin
fun UiDevice.getDeviceModel(): DeviceModel
```

Returns the model of a device based on display width and height


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
