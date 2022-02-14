package com.microsoft.device.dualscreen.windowstate

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class WindowStateComposeTest {
    @get: Rule
    val composeTestRule = createComposeRule()
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private val largeScreen = WindowState(windowWidthDp = 1000.dp, windowHeightDp = 1000.dp)
    private val largeScreenFoldNotSeparating =
        WindowState(
            hasFold = true,
            foldBoundsDp = DpRect(490.dp, 0.dp, 510.dp, 1000.dp),
            windowWidthDp = 1000.dp,
            windowHeightDp = 1000.dp
        )
    private val compactScreen = WindowState()
    private val compactScreenFoldNotSeparating = WindowState(hasFold = true)
    private val horizontalFold = WindowState(
        hasFold = true,
        foldIsHorizontal = true,
        foldBoundsDp = DpRect(0.dp, 490.dp, 1000.dp, 510.dp),
        foldIsSeparating = true,
        windowWidthDp = 1000.dp,
        windowHeightDp = 1000.dp
    )
    private val verticalFold = WindowState(
        hasFold = true,
        foldBoundsDp = DpRect(100.dp, 0.dp, 180.dp, 500.dp),
        foldIsSeparating = true,
        windowWidthDp = 500.dp,
        windowHeightDp = 500.dp
    )

    private val sizeZero = DpSize.Zero

    /**
     * Tests that the window mode properties/functions are all correct for windows with different properties
     * when in portrait orientation
     */
    @Test
    fun potrait_window_modes_are_calculated_correctly() {
        // Lock device in portrait orientation
        device.setOrientationNatural()

        composeTestRule.setContent {
            // Assert large screen with no fold/non-separating fold is in dual landscape mode
            largeScreen.assertWindowModeEquals(WindowMode.DUAL_LANDSCAPE)
            largeScreenFoldNotSeparating.assertWindowModeEquals(WindowMode.DUAL_LANDSCAPE)

            // Assert compact screen with no fold/non-separating fold is in single portrait mode
            compactScreen.assertWindowModeEquals(WindowMode.SINGLE_PORTRAIT)
            compactScreenFoldNotSeparating.assertWindowModeEquals(WindowMode.SINGLE_PORTRAIT)

            // Assert window with horizontal fold is in dual landscape mode
            horizontalFold.assertWindowModeEquals(WindowMode.DUAL_LANDSCAPE)

            // Assert window with vertical fold is in dual portrait mode
            verticalFold.assertWindowModeEquals(WindowMode.DUAL_PORTRAIT)
        }

        // Unlock device orientation
        device.unfreezeRotation()
    }

    /**
     * Tests that the window mode properties/functions are all correct for windows with different properties
     * when in landscape orientation
     */
    @Test
    fun landscape_window_modes_are_calculated_correctly() {
        // Lock device in landscape orientation
        device.setOrientationLeft()

        composeTestRule.setContent {
            // Assert large screen with no fold/non-separating fold is in dual portrait mode
            largeScreen.assertWindowModeEquals(WindowMode.DUAL_PORTRAIT)
            largeScreenFoldNotSeparating.assertWindowModeEquals(WindowMode.DUAL_PORTRAIT)

            // Assert compact screen with no fold/non-separating fold is in single landscape mode
            compactScreen.assertWindowModeEquals(WindowMode.SINGLE_LANDSCAPE)
            compactScreenFoldNotSeparating.assertWindowModeEquals(WindowMode.SINGLE_LANDSCAPE)

            // Assert window with horizontal fold is in dual landscape mode
            horizontalFold.assertWindowModeEquals(WindowMode.DUAL_LANDSCAPE)

            // Assert window with vertical fold is in dual portrait mode
            verticalFold.assertWindowModeEquals(WindowMode.DUAL_PORTRAIT)
        }

        // Unlock device orientation
        device.unfreezeRotation()
    }

    /**
     * Tests that pane and foldable pane sizes are calculated for windows with different properties
     * when in portrait orientation
     */
    @Test
    fun portrait_pane_sizes_are_calculated_correctly() {
        // Lock device in portrait orientation
        device.setOrientationNatural()

        composeTestRule.setContent {
            // Assert large screen with no fold/non-separating fold has equal panes or weighted panes and size
            // zero foldable panes
            largeScreen.assertPaneSizesEquals(pane1 = DpSize(1000.dp, 500.dp), pane2 = DpSize(1000.dp, 500.dp))
            largeScreenFoldNotSeparating.largeScreenPane1Weight = 0.3f
            largeScreenFoldNotSeparating.assertPaneSizesEquals(
                pane1 = DpSize(1000.dp, 300.dp),
                pane2 = DpSize(1000.dp, 700.dp),
            )

            // Assert compact screen with no fold/non-separating fold has panes and foldable panes of size zero
            compactScreen.largeScreenPane1Weight = 0.3f
            compactScreen.assertPaneSizesEquals(pane1 = sizeZero, pane2 = sizeZero)
            compactScreenFoldNotSeparating.assertPaneSizesEquals(pane1 = sizeZero, pane2 = sizeZero)

            // Assert window with horizontal fold ignores weight and divides based on fold boundaries
            horizontalFold.largeScreenPane1Weight = 0.3f
            horizontalFold.assertPaneSizesEquals(
                pane1 = DpSize(1000.dp, 490.dp),
                pane2 = DpSize(1000.dp, 490.dp),
                checkFoldable = true
            )

            // Assert window with vertical fold divides into unequal panes according to fold boundaries
            // Note: assume LTR local layout direction
            verticalFold.assertPaneSizesEquals(
                pane1 = DpSize(100.dp, 500.dp), pane2 = DpSize(320.dp, 500.dp), checkFoldable = true
            )
        }

        // Unlock device orientation
        device.unfreezeRotation()
    }

    /**
     * Tests that pane and foldable pane sizes are calculated for windows with different properties
     * when in landscape orientation
     */
    @Test
    fun landscape_pane_sizes_are_calculated_correctly() {
        // Lock device in landscape orientation
        device.setOrientationLeft()

        composeTestRule.setContent {
            // Assert large screen with no fold/non-separating fold has equal panes or weighted panes and size
            // zero foldable panes
            largeScreen.assertPaneSizesEquals(pane1 = DpSize(500.dp, 1000.dp), pane2 = DpSize(500.dp, 1000.dp))
            largeScreenFoldNotSeparating.largeScreenPane1Weight = 0.3f
            largeScreenFoldNotSeparating.assertPaneSizesEquals(
                pane1 = DpSize(300.dp, 1000.dp),
                pane2 = DpSize(700.dp, 1000.dp),
            )

            // Assert compact screen with no fold/non-separating fold has panes and foldable panes of size zero
            compactScreen.assertPaneSizesEquals(pane1 = sizeZero, pane2 = sizeZero)
            compactScreen.largeScreenPane1Weight = 0.6f
            compactScreenFoldNotSeparating.assertPaneSizesEquals(pane1 = sizeZero, pane2 = sizeZero)

            // Assert window with horizontal fold ignores weight and divides based on fold boundaries
            horizontalFold.largeScreenPane1Weight = 0.3f
            horizontalFold.assertPaneSizesEquals(
                pane1 = DpSize(1000.dp, 490.dp),
                pane2 = DpSize(1000.dp, 490.dp),
                checkFoldable = true
            )

            // Assert window with vertical fold divides into unequal panes according to fold boundaries
            // Note: assume LTR local layout direction
            verticalFold.assertPaneSizesEquals(
                pane1 = DpSize(100.dp, 500.dp), pane2 = DpSize(320.dp, 500.dp), checkFoldable = true
            )
        }

        // Unlock device orientation
        device.unfreezeRotation()
    }

    /**
     * Helper method to assert all window mode API properties/functions return the correct value for the given
     * expected window mode
     *
     * @param expected: the expected window mode for the WindowState object
     */
    @Composable
    private fun WindowState.assertWindowModeEquals(expected: WindowMode) {
        assertEquals(expected, windowMode)

        val singlePortrait = expected == WindowMode.SINGLE_PORTRAIT
        val singleLandscape = expected == WindowMode.SINGLE_LANDSCAPE
        val dualPortrait = expected == WindowMode.DUAL_PORTRAIT
        val dualLandscape = expected == WindowMode.DUAL_LANDSCAPE
        val dualScreen = dualPortrait || dualLandscape

        assertEquals(dualScreen, isDualScreen())
        assertEquals(dualPortrait, isDualPortrait())
        assertEquals(dualLandscape, isDualLandscape())
        assertEquals(singlePortrait, isSinglePortrait())
        assertEquals(singleLandscape, isSingleLandscape())
    }

    @Composable
    private fun WindowState.assertPaneSizesEquals(
        pane1: DpSize,
        pane2: DpSize,
        checkFoldable: Boolean = false
    ) {
        // Check pane sizes
        assertEquals(pane1, pane1SizeDp)
        assertEquals(pane2, pane2SizeDp)

        val foldablePane1 = if (checkFoldable) pane1 else DpSize.Zero
        val foldablePane2 = if (checkFoldable) pane2 else DpSize.Zero

        // Check foldable pane sizes
        assertEquals(foldablePane1, foldablePane1SizeDp)
        assertEquals(foldablePane2, foldablePane2SizeDp)
    }
}
