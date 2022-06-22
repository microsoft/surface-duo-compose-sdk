# Navigation Rail

This sample is built with Jetpack Compose, the new UI framework in Android.

## Getting Started

To learn how to load apps on the Surface Duo emulator, see the [documentation](https://docs.microsoft.com/dual-screen/android), and follow [the blog](https://devblogs.microsoft.com/surface-duo).

## Features

The sample showcases how to incorporate the [NavigationRail](https://material.io/components/navigation-rail) component into a Jetpack Compose application for large screen and foldable devices.

### Dynamic navigation components

When the app is spanned and the screen size is larger, it uses a **NavigationRail**.

![Lakes gallery dual portrait](screenshots/dual_portrait_lakes_placeholder.png)
![Animals gallery dual landscape](screenshots/dual_landscape_animals.png)

When the app is unspanned, however, it switches back to a **BottomNavigation** bar.

![Birds gallery single portrait](screenshots/single_portrait_birds.png)
![Rocks gallery single landscape](screenshots/single_landscape_rocks.png)

### Hinge-aware content drawer

The app features a custom content drawer component, similar to [BottomSheetScaffold](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#BottomSheetScaffold(kotlin.Function1,androidx.compose.ui.Modifier,androidx.compose.material.BottomSheetScaffoldState,kotlin.Function0,kotlin.Function1,kotlin.Function0,androidx.compose.material.FabPosition,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.ui.unit.Dp,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,kotlin.Function1,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.ui.unit.Dp,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,kotlin.Function1)), that adds a `Spacer` to the drawer content if it's being rendered across the hinge.

![Demo of content drawer](screenshots/content_drawer_animation.gif)

### Navigation hierarchy

The app uses two sources of navigation.

To switch between gallery and item views, we use the built in navigation support from `TwoPaneLayout`.

![TwoPaneLayout navigation logic](screenshots/twopanelayout.png)

To switch between the gallery categories, we use a `NavHostController` and `NavHost`paried with the current navigation component.

![NavHost navigation logic](screenshots/navhost.png)

## Attributions

The images used in the NavigationRail sample are licensed under [© eakarat], [© nerudol], [© DedMityay], [© tibor13], [© Harry Collins], [© Tony Baggett], [© WildMedia], [© Vlasto Opatovsky], [© Lakeview Images], [© viktoriya89], [© dudlajzov], [© zkbld], [© muraviov], [© phototrip.cz], [© gpisano71], [© Mike Mareen], [	© Fokke], [© ronnybas], [© Piotr Krzeslak], [© shaiith], [© aiisha], [© Andrew Mayovskyy], [© Andrey Armyagov], [© Наталья Босяк] / stock.adobe.com

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
