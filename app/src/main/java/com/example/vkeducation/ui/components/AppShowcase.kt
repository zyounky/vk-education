package com.example.vkeducation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.vkeducation.R
import com.example.vkeducation.data.SHOWCASE_PAGE_COUNT
import com.example.vkeducation.data.showcasePages

@Composable
fun AppShowcase(
    onAppClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { SHOWCASE_PAGE_COUNT })

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(R.string.showcase_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().testTag("showcase_pager"),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 16.dp,
            verticalAlignment = Alignment.Top,
        ) { page ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                showcasePages[page].forEach { app ->
                    AppItem(
                        app = app,
                        modifier = Modifier.clickable(
                            role = Role.Button,
                            onClickLabel = stringResource(R.string.back_to_apps),
                            onClick = onAppClick,
                        ),
                    )
                }
            }
        }
        Text(
            text = stringResource(
                R.string.showcase_page_number,
                pagerState.currentPage + 1,
                SHOWCASE_PAGE_COUNT,
            ),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally).testTag("showcase_page_number"),
        )
    }
}
