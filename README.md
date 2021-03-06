![build-test-check](https://github.com/microsoft/surface-duo-compose-sdk/actions/workflows/build_test_check.yml/badge.svg) ![Compose Version](https://img.shields.io/badge/Jetpack%20Compose-1.1.1-brightgreen)

# Surface Duo Jetpack Compose SDK

This repo contains components built with Jetpack Compose for the Microsoft Surface Duo and other large screen and foldable devices.

Please read the [code of conduct](CODE_OF_CONDUCT.md) and [contribution guidelines](CONTRIBUTING.md).

## Prerequisites

- Jetpack Compose version: `1.1.1`
- Jetpack Window Manager version: `1.0.0`
- Android Studio version: Chipmunk `2021.2.1`

## Getting Started

When importing the component source code into Android Studio, use the specific component folder (ex: **TwoPaneLayout**) as the base directory of the project.

To learn how to load apps on the Surface Duo emulator, see the [documentation](https://docs.microsoft.com/dual-screen/android), and follow [our blog](https://devblogs.microsoft.com/surface-duo).

Please check out our page on [Jetpack Compose for Microsoft Surface Duo](https://docs.microsoft.com/dual-screen/android/jetpack/compose/) for more details.

## Contents

### [TwoPaneLayout](https://github.com/microsoft/surface-duo-compose-sdk/tree/main/TwoPaneLayout)

A UI component that helps you build two-pane layout for dual-screen, foldable, and large-screen devices.

#### Latest Update: [1.0.1-alpha02](https://github.com/microsoft/surface-duo-compose-sdk/releases/tag/twopanelayout_20220525_1.0.1-alpha02) (May 25th, 2022)

### [WindowState](https://github.com/microsoft/surface-duo-compose-sdk/tree/main/WindowState)

A utility component that helps you easily get details about the window state of dual-screen, foldable, and large-screen devices.

#### Latest Update: [1.0.0-alpha04](https://github.com/microsoft/surface-duo-compose-sdk/releases/tag/windowstate_20220214_alpha04) (February 14th, 2022)

### [ComposeTesting](https://github.com/microsoft/surface-duo-compose-sdk/tree/main/ComposeTesting)

Helper functions that help you easily test your application on the dual-screen, foldable and large screen devices.

#### Latest Update: [1.0.0-alpha04](https://github.com/microsoft/surface-duo-compose-sdk/releases/tag/composetesting_20220705_alpha04) (July 5th, 2022)

### [DragAndDrop](https://github.com/microsoft/surface-duo-compose-sdk/tree/main/DragAndDrop)

A Compose component that helps you build drag and drop feature on any Android devices, especially dual-screen, foldable and large-screen devices.

#### Latest Update: [1.0.0-alpha01](https://github.com/microsoft/surface-duo-compose-sdk/releases/tag/draganddrop_20220707_alpha01) (July 7th, 2022)

## Social links

| Blog post | Video |
|---|---|
| [Video+Chat and Calculator samples for Jetpack Compose](https://devblogs.microsoft.com/surface-duo/jetpack-compose-video-calculator-samples/) | [Twitch #77: New Jetpack Compose samples](https://www.twitch.tv/videos/1519558235) |
| [Jetpack Compose TwoPaneLayout update](https://devblogs.microsoft.com/surface-duo/jetpack-compose-foldable-twopanelayout/) | [Twitch #76: Jetpack Compose TwoPaneLayout update](https://www.youtube.com/watch?v=cI73qh_mTOo)|
| [Write foldable tests quickly with Test Kit](https://devblogs.microsoft.com/surface-duo/foldable-ui-test-kit/) | [Twitch #63: Test Kit for foldable apps](https://www.youtube.com/watch?v=3I0qU5SeUBM) |
| [Jetpack Compose UI testing](https://devblogs.microsoft.com/surface-duo/jetpack-compose-ui-test/) | [Twitch #59: Jetpack Compose testing](https://www.youtube.com/watch?v=Q3lDz7PjO7U) |
| [Jetpack Compose WindowState preview](https://devblogs.microsoft.com/surface-duo/jetpack-compose-windowstate-preview/) | [Twitch #53: Jetpack Compose WindowState for foldable devices](https://www.youtube.com/watch?v=qOIliow-uS4) |
| [Get started with Jetpack Compose](https://devblogs.microsoft.com/surface-duo/get-started-with-jetpack-compose/) | [Twitch #44: Get started with Jetpack Compose](https://www.youtube.com/watch?v=ijXDWDtdiIE) |
| [Jetpack Compose Navigation Rail](https://devblogs.microsoft.com/surface-duo/jetpack-compose-navigation-rail/) | [Twitch #42: Jetpack Compose Navigation Rail](https://www.youtube.com/watch?v=pdoIyOU7Suk)
| [New TwoPaneLayout Compose library preview](https://devblogs.microsoft.com/surface-duo/jetpack-compose-twopanelayout-preview/) | [Twitch #25: TwoPaneLayout preview for Jetpack Compose](https://www.youtube.com/watch?v=Q66bR2jKdrg) |
| [Jetpack Compose foldable and dual-screen development](https://devblogs.microsoft.com/surface-duo/jetpack-compose-foldable-samples) | [Twitch #9: Jetpack Compose samples](https://www.youtube.com/watch?v=m8bMjFhBbN8) |
| [Jetpack Compose on Microsoft Surface Duo](https://devblogs.microsoft.com/surface-duo/jetpack-compose-dual-screen-sample/) | N/A|

## Related links

- [Compose samples](https://github.com/microsoft/surface-duo-compose-samples/)
- [Jetpack Window Manager samples](https://github.com/microsoft/surface-duo-window-manager-samples)
- [SDK samples (Java)](https://github.com/microsoft/surface-duo-sdk-samples)
- [SDK samples (Kotlin)](https://github.com/microsoft/surface-duo-sdk-samples-kotlin)
- [Unity samples](https://github.com/microsoft/surface-duo-sdk-unity-samples)
- [Xamarin samples](https://github.com/microsoft/surface-duo-sdk-xamarin-samples)
- [Flutter samples](https://github.com/microsoft/surface-duo-sdk-samples-flutter)
- [React Native samples](https://github.com/microsoft/react-native-dualscreen)
- [Web samples](https://docs.microsoft.com/dual-screen/web/samples)

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

## Trademarks

This project may contain trademarks or logos for projects, products, or services. Authorized use of Microsoft trademarks or logos is subject to and must follow
[Microsoft's Trademark & Brand Guidelines](https://www.microsoft.com/en-us/legal/intellectualproperty/trademarks/usage/general).
Use of Microsoft trademarks or logos in modified versions of this project must not cause confusion or imply Microsoft sponsorship.
Any use of third-party trademarks or logos are subject to those third-party's policies.
