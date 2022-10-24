package com.microsoft.device.dualscreen.twopanelayout

/**
 * The TwoPaneMode controls when two panes are shown for TwoPaneLayout.
 *
 * The default pane mode is [TwoPane], which means two panes will be shown whenever there is a separating
 * fold or a large screen. You can choose to show only one pane in different cases by changing the pane mode.
 */
enum class TwoPaneMode {
    /**
     * Always shows two panes, regardless of the orientation
     */
    TwoPane,

    /**
     * Shows one big pane when the fold is horizontal (combines top/bottom panes)
     */
    HorizontalSingle,

    /**
     * Shows one big pane when the fold is vertical (combines left/right panes)
     */
    VerticalSingle,

    /**
     * Always shows one pane, regardless of the orientation
     */
    SinglePane
}
