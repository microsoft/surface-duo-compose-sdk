# TwoPaneLayout - Surface Duo Compose SDK

**TwoPaneLayout** is a UI component for Jetpack Compose, which contains the layouts that help you create UI for dual-screen, foldable, and large-screen devices. TwoPaneLayout provides a two-pane layout for use at the top level of a UI. The component will place two panes side-by-side when the app is spanned on dual-screen, foldable and large-screen devices. The two panes can be horizontal or vertical, based on the orientation of the device, unless `paneMode` is configured. 

When the app is spanned across a vertical hinge or fold, or when the width is larger than height of screen on large-screen device, pane1 will be placed on the left, while pane2 will be on the right. If the device rotates, the app is spanned across a horizontal hinge or fold, or the width is smaller than the height of screen on large-screen device, pane1 will be placed on the top and pane2 will be on the bottom. The TwoPaneLayout is able to assign children widths or heights according to their weights provided using the `TwoPaneScope.weight` modifier. If no weight is provided, the two panes will be displayed equally.

If running on regular single-screen device, there will be only one pane visible. The other pane will be overlayed and navigation will be available to switch between two panes.


```
@Composable
fun TwoPaneLayout(
    modifier: Modifier = Modifier,
    paneMode: TwoPaneMode = TwoPaneMode.TwoPane,
    pane1: @Composable TwoPaneScope.() -> Unit,
    pane2: @Composable TwoPaneScope.() -> Unit
)

fun navigateToPane1()

fun navigateToPane2() 
```

Please refer to [TwoPaneLayout](https://docs.microsoft.com/dual-screen/android/jetpack/compose/two-pane-layout) on Microsoft Dual-screen document for more details.
About some common use case for the two panes, please check out [user interface patterns](https://docs.microsoft.com/dual-screen/introduction#dual-screen-app-patterns).

## 

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
    implementation "com.microsoft.device.dualscreen:twopanelayout:1.0.0-alpha08"
    ```
3. Also ensure the compileSdkVersion and targetSdkVersion are set to API 31 or newer in the module-level build.gradle file.

    ```
    android { 
        compileSdkVersion 31
        
        defaultConfig { 
            targetSdkVersion 31
        } 
        ... 
    }
    ```

4. Build layout with **TwoPaneLayout**. Please refer to the [sample](https://github.com/microsoft/surface-duo-compose-samples/tree/main/TwoPaneLayout/sample) for more details.

- Dual-screen device(Surface Duo device, 1:1)

![surfaceduo](screenshots/surfaceduo.png)

- Foldable device(1:1)

![foldable](screenshots/foldable.png)

- Tablet device(3:7)

![tablet](screenshots/tablet.png)

- VerticalSingle on Foldable

![verticalSingle](screenshots/single-vertical.png)

- HorizontalSingle on Dual-screen device

![horizontal](screenshots/single-horizontal.png)

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
