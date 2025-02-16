package de.artificient.gizo.sdk.sample.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
internal  fun GizoFilledLoadingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Dp = 16.dp,
    small: Boolean = false,
    colors: ButtonColors = GizoButtonDefaults.filledButtonColors(),
    contentPadding: PaddingValues = GizoButtonDefaults.buttonContentPadding(small = small),
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = if (small) {
            modifier.heightIn(min = GizoButtonDefaults.SmallButtonHeight)
        } else {
            modifier
        },
        enabled = enabled,
        shape = RoundedCornerShape(shape),
        colors = colors,
        contentPadding = contentPadding,
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                content()
            }
        }
    )
}

@Composable
internal fun GizoFilledLoadingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Dp = 16.dp,
    small: Boolean = false,
    baseLineColor: Color = MaterialTheme.colorScheme.inversePrimary,
    progressLineColor: Color = MaterialTheme.colorScheme.onBackground,
    colors: ButtonColors = GizoButtonDefaults.filledButtonColors(),
    isLoading: Boolean = false,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    GizoFilledLoadingButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        small = small,
        colors = colors,
        contentPadding = GizoButtonDefaults.buttonContentPadding(
            small = small,
            leadingIcon = leadingIcon != null,
            trailingIcon = trailingIcon != null
        )
    ) {
        GizoButtonContent(
            text = text,
            isLoading = isLoading,
            baseLineColor = baseLineColor,
            progressLineColor = progressLineColor,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }
}


@Composable
internal fun GizoFilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Dp = 16.dp,
    small: Boolean = false,
    colors: ButtonColors = GizoButtonDefaults.filledButtonColors(),
    contentPadding: PaddingValues = GizoButtonDefaults.buttonContentPadding(small = small),
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = if (small) {
            modifier.heightIn(min = GizoButtonDefaults.SmallButtonHeight)
        } else {
            modifier
        },
        enabled = enabled,
        shape = RoundedCornerShape(shape),
        colors = colors,
        contentPadding = contentPadding,
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                content()
            }
        }
    )
}

@Composable
internal fun GizoFilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Dp = 16.dp,
    small: Boolean = false,
    baseLineColor: Color = MaterialTheme.colorScheme.inversePrimary,
    progressLineColor: Color = MaterialTheme.colorScheme.onBackground,
    colors: ButtonColors = GizoButtonDefaults.filledButtonColors(),
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    GizoFilledButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        small = small,
        colors = colors,
        contentPadding = GizoButtonDefaults.buttonContentPadding(
            small = small,
            leadingIcon = leadingIcon != null,
            trailingIcon = trailingIcon != null
        )
    ) {
        GizoButtonContent(
            text = text,
            baseLineColor = baseLineColor,
            progressLineColor = progressLineColor,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }
}

@Composable
internal fun GizoOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    shape: Dp,
    border: BorderStroke? = GizoButtonDefaults.outlinedButtonBorder(enabled = enabled),
    colors: ButtonColors = GizoButtonDefaults.outlinedButtonColors(),
    contentPadding: PaddingValues = GizoButtonDefaults.buttonContentPadding(small = small),
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = if (small) {
            modifier.heightIn(min = GizoButtonDefaults.SmallButtonHeight)
        } else {
            modifier
        },
        shape = RoundedCornerShape(shape),
        enabled = enabled,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                content()
            }
        }
    )
}

@Composable
internal fun GizoOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    shape: Dp = 16.dp,
    border: BorderStroke? = GizoButtonDefaults.outlinedButtonBorder(enabled = enabled),
    colors: ButtonColors = GizoButtonDefaults.outlinedButtonColors(),
    baseLineColor: Color = MaterialTheme.colorScheme.inversePrimary,
    progressLineColor: Color = MaterialTheme.colorScheme.onBackground,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    GizoOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        small = small,
        border = border,
        colors = colors,
        contentPadding = GizoButtonDefaults.buttonContentPadding(
            small = small,
            leadingIcon = leadingIcon != null,
            trailingIcon = trailingIcon != null
        )
    ) {
        GizoButtonContent(
            text = text,
            baseLineColor = baseLineColor,
            progressLineColor = progressLineColor,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }
}

@Composable
internal fun GizoTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    colors: ButtonColors = GizoButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = GizoButtonDefaults.buttonContentPadding(small = small),
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = if (small) {
            modifier.heightIn(min = GizoButtonDefaults.SmallButtonHeight)
        } else {
            modifier
        },
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                content()
            }
        }
    )
}

@Composable
internal fun GizoTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    baseLineColor: Color = MaterialTheme.colorScheme.inversePrimary,
    progressLineColor: Color = MaterialTheme.colorScheme.onBackground,
    colors: ButtonColors = GizoButtonDefaults.textButtonColors(),
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    GizoTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        small = small,
        colors = colors,
        contentPadding = GizoButtonDefaults.buttonContentPadding(
            small = small,
            leadingIcon = leadingIcon != null,
            trailingIcon = trailingIcon != null
        )
    ) {
        GizoButtonContent(
            text = text,
            baseLineColor = baseLineColor,
            progressLineColor = progressLineColor,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }
}

@Composable
internal fun RowScope.GizoButtonContent(
    text: @Composable () -> Unit,
    isLoading: Boolean = false,
    baseLineColor: Color = MaterialTheme.colorScheme.inversePrimary,
    progressLineColor: Color = MaterialTheme.colorScheme.onBackground,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
) {
    if (leadingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = GizoButtonDefaults.ButtonIconSize)) {
            leadingIcon()
        }
    }
    Box(
        Modifier
            .padding(
                start = if (leadingIcon != null) {
                    GizoButtonDefaults.ButtonContentSpacing
                } else {
                    0.dp
                },
                end = if (trailingIcon != null) {
                    GizoButtonDefaults.ButtonContentSpacing
                } else {
                    0.dp
                }
            )
    ) {
        if (isLoading)
            GizoLoadingButton(
                baseLineColor = baseLineColor,
                progressLineColor = progressLineColor,
                contentDesc = "Loading button"
            )
        else
            text()
    }
    if (trailingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = GizoButtonDefaults.ButtonIconSize)) {
            trailingIcon()
        }
    }
}

/**
 * button with default values.
 */
internal object GizoButtonDefaults {
    val SmallButtonHeight = 32.dp
    const val DisabledButtonContainerAlpha = 0.5f
    const val DisabledButtonContentAlpha = 0.38f
    private val ButtonHorizontalPadding = 24.dp
    private val ButtonHorizontalIconPadding = 16.dp
    private val ButtonVerticalPadding = 8.dp
    private val SmallButtonHorizontalPadding = 16.dp
    private val SmallButtonHorizontalIconPadding = 12.dp
    private val SmallButtonVerticalPadding = 7.dp
    val ButtonContentSpacing = 8.dp
    val ButtonIconSize = 28.dp
    fun buttonContentPadding(
        small: Boolean,
        leadingIcon: Boolean = false,
        trailingIcon: Boolean = false,
    ): PaddingValues {
        return PaddingValues(
            start = when {
                small && leadingIcon -> SmallButtonHorizontalIconPadding
                small -> SmallButtonHorizontalPadding
                leadingIcon -> ButtonHorizontalIconPadding
                else -> ButtonHorizontalPadding
            },
            top = if (small) SmallButtonVerticalPadding else ButtonVerticalPadding,
            end = when {
                small && trailingIcon -> SmallButtonHorizontalIconPadding
                small -> SmallButtonHorizontalPadding
                trailingIcon -> ButtonHorizontalIconPadding
                else -> ButtonHorizontalPadding
            },
            bottom = if (small) SmallButtonVerticalPadding else ButtonVerticalPadding
        )
    }

    @Composable
    fun filledButtonColors(
        containerColor: Color = MaterialTheme.colorScheme.primary,
        contentColor: Color = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor: Color = MaterialTheme.colorScheme.primary.copy(
            alpha = DisabledButtonContainerAlpha
        ),
        disabledContentColor: Color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = DisabledButtonContentAlpha
        ),
    ) = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )

    @Composable
    fun outlinedButtonBorder(
        enabled: Boolean,
        width: Dp = 1.dp,
        color: Color = Color.White,
        disabledColor: Color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = DisabledButtonContainerAlpha
        ),
    ): BorderStroke = BorderStroke(
        width = width,
        color = if (enabled) color else disabledColor
    )

    @Composable
    fun outlinedButtonColors(
        containerColor: Color = Color.Transparent,
        contentColor: Color = MaterialTheme.colorScheme.onBackground,
        disabledContainerColor: Color = Color.Transparent,
        disabledContentColor: Color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = DisabledButtonContentAlpha
        ),
    ) = ButtonDefaults.outlinedButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )

    @Composable
    fun textButtonColors(
        containerColor: Color = Color.Transparent,
        contentColor: Color = MaterialTheme.colorScheme.onBackground,
        disabledContainerColor: Color = Color.Transparent,
        disabledContentColor: Color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = DisabledButtonContentAlpha
        ),
    ) = ButtonDefaults.textButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )
}
