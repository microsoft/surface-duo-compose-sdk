package com.microsoft.device.dualscreen.testing.sample

import androidx.compose.material.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.microsoft.device.dualscreen.testing.compose.getString
import org.junit.Rule
import org.junit.Test

/**
 * Test class that shows how to use string helper methods with a ComposeTestRule that doesn't
 * have access to an activity
 *
 * To see how to use string helper methods with an AndroidComposeTestRule, refer to
 * [AnnotationTest], [FoldingFeatureTest], or [SwipeTest]
 */
class StringTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Test
    fun string_noArguments() {
        composeTestRule.setContent {
            Text(text = stringResource(R.string.test_string_plain))
        }

        composeTestRule.onNodeWithText(getString(R.string.test_string_plain)).assertIsDisplayed()
    }

    @Test
    fun string_withArguments() {
        composeTestRule.setContent {
            Text(text = stringResource(R.string.test_string_with_args, 1, "argument 2"))
        }

        composeTestRule.onNodeWithText(getString(R.string.test_string_with_args, 1, "argument 2"))
            .assertIsDisplayed()
    }
}
