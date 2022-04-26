package com.microsoft.device.dualscreen.twopanelayout


interface TwoPaneLayout {
    class PaneContainer private constructor(private val description: String) {

        override fun toString(): String {
            return description
        }

        companion object {
            val PANE1: PaneContainer = PaneContainer("Pane1")
            val PANE2: PaneContainer = PaneContainer("Pane2")
        }
    }
}