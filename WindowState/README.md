# WindowState - Surface Duo Compose SDK

**WindowState** is a component for Jetpack Compose that helps you easily get details about the window state of the dual-screen, foldable and large screen devices, using the Google [Jetpack WindowManager](https://developer.android.com/jetpack/androidx/releases/window) library.

The component provides the current window information as a Compose state, including folding position, orientation and window size classes. For dual-screen and foldable devices, combining different folding positions and orientations, we introduce four display postures to take advantage of these new form factors: Dual Portrait, Dual Landscape, Single Portrait, Single Landscape.

![postures](screenshots/postures-overview.png)

And the window size classes are measured based on Google's [Window size classes](https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes#window_size_classes) to help you support different screen sizes of devices, including large screen and regular single screen devices.

## Add to your project

1. Make sure you have **mavenCentral()** repository in your top level **build.gradle** file:

    ```gradle
    allprojects {
        repositories {
            google()
            mavenCentral()
         }
    }
    ```

2. Add dependencies to the module-level **build.gradle** file (current version may be different from what's shown here).

    ```gradle
    implementation "com.microsoft.device.dualscreen:windowstate:1.0.0-alpha09"
    ```

3. Also ensure the compileSdkVersion is set to API 33 and the targetSdkVersion is set to API 32 or newer in the module-level build.gradle file.

    ```gradle
    android { 
        compileSdkVersion 33
        
        defaultConfig { 
            targetSdkVersion 32
        } 
        ... 
    }
    ```

4. Access the info about the window state from **WindowState** to build or adjust your UI. Please refer to the [sample](https://github.com/microsoft/surface-duo-compose-sdk/tree/main/WindowState/sample) for more details.

## API reference

```kotlin
@Composable
fun Activity.rememberWindowState(): WindowState
```

An interface to provide all the relevant info about the device window.

### Large screen and foldable properties

```kotlin
val foldSizeDp: Dp
```

Returns a dp value of the thickness of the hinge of dual-screen device or the folding line of foldable device when the device is in dual-screen mode. If the device is in single screen mode, or the device is a regular single screen device, the return value will be 0.

Must be set before calling `pane1SizeDp` or `pane2SizeDp` for the correct value to be applied.

```kotlin
var largeScreenPane1Weight: Float = 0.5f
```

Proportion of the window that pane 1 should occupy on a large screen. Used when calculating pane size for the `pane1SizeDp` and `pane2SizeDp` properties.

Default value is 0.5 to create equal panes - any new values must be between 0 and 1 or an `IllegalArgumentException` will be thrown.

```kotlin
val pane1SizeDp: DpSize
```

Returns the dp size of the primary pane of any device, including dual-screen, foldable, and large screen devices. This is the recommended value to use when creating dualscreen layouts.

The primary pane is either the top pane or the left/right pane, depending on device orientation and local language layout direction.

If the device is in single screen mode, or the device is a regular single screen device, the return value will be 0.

If the device is a large screen, then the `largeScreenPane1Weight` property will be used to calculate how much space each pane takes up. The property must be set before calling this method for the correct value to be applied.

```kotlin
val pane2SizeDp: DpSize
```

Returns the dp size of the primary pane of any device, including dual-screen, foldable, and large screen devices. This is the recommended value to use when creating dualscreen layouts.

The secondary pane is either the bottom pane or the left/right pane, depending on device orientation and local language layout direction.

If the device is in single screen mode, or the device is a regular single screen device, the return value will be 0.

If the device is a large screen, then the `largeScreenPane1Weight` property will be used to calculate how much space each pane takes up. The property must be set before calling this method for the correct value to be applied.

```kotlin
val foldablePane1SizeDp: DpSize
```

Returns the dp size of the primary pane of the dual-screen or foldable device when the device is in dual-screen mode - note that this does NOT include large screen devices.

The primary pane is either the top pane or the left/right pane, depending on device orientation and local language layout direction.

If the device is in single screen mode, or the device is a regular single screen device, the return value will be 0.

```kotlin
val foldablePane2SizeDp: DpSize
```

Returns the dp size of the secondary pane of the dual-screen or foldable device when the device is in dual-screen mode - note that this does NOT include large screen devices.

The secondary pane is either the bottom pane or the left/right pane, depending on device orientation and local language layout direction.

If the device is in single screen mode, or the device is a regular single screen device, the return value will be 0.

### Window mode properties

```kotlin
val windowMode: WindowMode
```

Returns the display posture of the window mode: **SINGLE_PORTRAIT**, **SINGLE_LANDSCAPE**, **DUAL_PORTRAIT**, **DUAL_LANDSCAPE**.

```kotlin
@Composable
fun isDualScreen(): Boolean
```

Check if the device window is in the dual screen mode, we called it **spanned** for the dual-screen device or **unfolded** for the foldable device.

```kotlin
@Composable
fun isDualPortrait(): Boolean
```

Check if the device window is in the dual portrait posture, with which the hinge or folding line is vertical.

```kotlin
@Composable
fun isDualLandscape(): Boolean
```

Check if the device window is in the dual portrait posture, with which the hinge or folding line is horizontal.

```kotlin
@Composable
fun isSinglePortrait(): Boolean
```

Check if the device window is in the single portrait posture, with which the device is in the single screen mode or the device is a single screen device with the portrait orientation.

```kotlin
@Composable
fun isSingleLandscape(): Boolean
```

Check if the device window is in the single landscape posture, with which the device is in the single screen mode or the device is a single screen device with the landscape orientation.

### Window size properties

```kotlin
@Composable
fun widthSizeClass(): WindowWidthSizeClass
```

Returns the width window size class: **Compact**, **Medium**, **Expanded**, based on the width of the window.

```kotlin
@Composable
fun heightSizeClass(): WindowHeightSizeClass
```

Returns the height window size class: **Compact**, **Medium**, **Expanded**, based on the height of the window.

### Other properties

```kotlin
val hasFold: Boolean = false
```

Returns true if a [FoldingFeature](https://developer.android.com/reference/androidx/window/layout/FoldingFeature) is detected, otherwise returns false.

```kotlin
val foldIsHorizontal: Boolean = false
```

Returns true if a [FoldingFeature](https://developer.android.com/reference/androidx/window/layout/FoldingFeature) is present and in the horizontal orientation, otherwise returns false when a fold is vertical. If no fold is detected, the return value will be false.

Based on the [orientation field](https://developer.android.com/reference/androidx/window/layout/FoldingFeature#orientation()) in [FoldingFeature](https://developer.android.com/reference/androidx/window/layout/FoldingFeature).

```kotlin
val foldBoundsDp: DpRect = DpRect(0.dp, 0.dp, 0.dp, 0.dp)
```

Returns the bounding rectangle of a fold in units of Dp. If no fold is present, the returned rectangle will contain all zeroes.

Based on the [bounds](https://developer.android.com/reference/androidx/window/layout/DisplayFeature#bounds()) field in [DisplayFeature](https://developer.android.com/reference/androidx/window/layout/DisplayFeature).

```kotlin
val foldState: FoldState = FoldState.FLAT
```

Returns the state of a fold: **FLAT** or **HALF_OPENED**. If no fold is present, the returned state is flat.

Based on the [state](https://developer.android.com/reference/androidx/window/layout/FoldingFeature#state()) field in [FoldingFeature](https://developer.android.com/reference/androidx/window/layout/FoldingFeature).

```kotlin
val foldIsSeparating: Boolean = false
```

Returns whether a fold should be thought of as separating the window into distinct sections.

Based on the [isSeparating](https://developer.android.com/reference/androidx/window/layout/FoldingFeature#isSeparating()) field in [FoldingFeature](https://developer.android.com/reference/androidx/window/layout/FoldingFeature).

```kotlin
val foldIsOccluding: Boolean = false
```

Returns whether a fold occludes content in the window.

Based on the [occlusionType](https://developer.android.com/reference/androidx/window/layout/FoldingFeature#occlusionType()) field in [FoldingFeature](https://developer.android.com/reference/androidx/window/layout/FoldingFeature).

```kotlin
val windowWidthDp: Dp = 1.dp
```

Returns the window width in Dp.

```kotlin
val windowHeightDp: Dp = 1.dp
```

Returns the window height in Dp.

## Sample behavior

The table below shows screenshots from the [sample](https://github.com/microsoft/surface-duo-compose-sdk/tree/main/WindowState/sample) running on different large screen and foldable emulators.

| Emulator | Sample screenshot |
|---|:-:|
| Surface Duo 2 (unspanned) | <img src="screenshots/sample_surfaceduo2_unspanned.png" width=500 alt="Sample running on the Surface Duo 2 emulator, unspanned"> |
| Surface Duo 2 (unspanned) | <img src="screenshots/sample_surfaceduo2_unspanned.png" width=500 alt="Sample running on the Surface Duo 2 emulator, unspanned"> |
| Surface Duo 2 (spanned) | <img src="screenshots/sample_surfaceduo2_spanned.png" width=500 alt="Sample running on the Surface Duo 2 emulator, spanned"> |
| 6.7 fold-in | <img src="screenshots/sample_6.7.png" height=500 alt="Sample running on the 6.7 fold-in emulator"> |
| 7.6 fold-in | <img src="screenshots/sample_7.6.png" width=500 alt="Sample running on the 7.6 fold-in emulator"> |
| 8 fold-out | <img src="screenshots/sample_8.png" width=500 alt="Sample running on the 8 fold-out emulator"> |
| Pixel C | <img src="screenshots/sample_pixelc.png" width=500 alt="Sample running on the Pixel C emulator"> |

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
