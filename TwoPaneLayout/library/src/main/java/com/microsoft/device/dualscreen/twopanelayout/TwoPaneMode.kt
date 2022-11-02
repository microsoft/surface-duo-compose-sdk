package com.microsoft.device.dualscreen.twopanelayout

/**
 * The TwoPaneMode controls when two panes are shown for TwoPaneLayout.
 *
 * The default pane mode is [TwoPane], which means whenever there is a **separating fold** or a **large window**,
 * two panes will be shown, but you can choose to show only one pane in these cases by changing the pane mode.
 *
 *
 * A **separating fold** means there's a [FoldingFeature](https://developer.android.com/reference/kotlin/androidx/window/layout/FoldingFeature)
 * present that returns true for the [isSeparating](https://developer.android.com/reference/kotlin/androidx/window/layout/FoldingFeature#isSeparating())
 * property.
 *
 *
 * A **large window** is one with a width [WindowSizeClass](https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes#window_size_classes)
 * of `EXPANDED` and a height size classs of at least `MEDIUM`.
 */
enum class TwoPaneMode {
    /**
     * Always shows two panes when there is a **separating fold** or **large window**, regardless of the orientation
     */
    TwoPane,

    /**
     * Shows one big pane when there is a **horizontal separating fold** or a **portrait large window** (combines top/bottom panes)
     */
    HorizontalSingle,

    /**
     * Shows one big pane when there is a **vertical separating fold** or a **landscape large window** (combines left/right panes)
     */
    VerticalSingle,

    /**
     * Always shows one pane, regardless of any window features and the orientation
     */
    SinglePane
}
