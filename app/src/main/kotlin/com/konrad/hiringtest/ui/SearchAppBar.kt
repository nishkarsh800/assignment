package com.konrad.hiringtest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

/**
 * An app bar that contains a search icon, text field and clear/close button.
 *
 * The search app bar will automatically request focus for its text field.
 *
 * @param placeholderText The text to display when there is no search query and search is active.
 * @param inactiveText The text to display when search is inactive.
 * @param modifier The [Modifier] to use to control layout parameters.
 */
@Composable
fun SearchAppBar(
    placeholderText: String,
    inactiveText: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isSearchActive by remember { mutableStateOf(false) }
    val focusRequester = FocusRequester()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1.0f)
            .background(MaterialTheme.colorScheme.surface, RectangleShape)
            .windowInsetsPadding(WindowInsets.statusBars)
            .then(modifier),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    PaddingValues(
                        start = 16.dp,
                        end = 4.dp
                    )
                )
                .height(56.dp), // Height of AppBar
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSearchActive) {
                    SearchTextField(
                        value = value,
                        onValueChange = onValueChange,
                        placeholderText = placeholderText,
                        focusRequester = focusRequester
                    )
                    SideEffect {
                        focusRequester.requestFocus()
                    }
                } else {
                    Text(
                        text = inactiveText,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (!isSearchActive) {
                    IconButton(
                        onClick = {
                            isSearchActive = true
                        }
                    ) {
                        Icon(
                            imageVector = Rounded.Search,
                            contentDescription = "Search",
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            if (value.isNotEmpty()) {
                                onValueChange("")
                            } else {
                                isSearchActive = false
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Rounded.Close,
                            contentDescription = "Clear",
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    focusRequester: FocusRequester
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        enabled = true,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        decorationBox = { innerTextField ->
            Row(modifier = Modifier.fillMaxWidth()) {
                if (value.isEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = placeholderText,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            innerTextField()
        }
    )
}