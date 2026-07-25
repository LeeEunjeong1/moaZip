package com.moazip.feature.assets.addasset.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moazip.feature.assets.R
import com.moazip.feature.assets.addasset.contract.OwnerSelection

@Composable
fun OwnerSelector(
    memberNames: List<String>,
    selectedOwner: OwnerSelection,
    onOwnerSelected: (OwnerSelection) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        SectionLabel(text = stringResource(R.string.add_asset_owner_label))
        OwnerRadioItem(
            label = stringResource(R.string.add_asset_owner_common),
            selected = selectedOwner == OwnerSelection.Common,
            onClick = { onOwnerSelected(OwnerSelection.Common) },
        )
        memberNames.forEach { memberName ->
            val selection = OwnerSelection.Member(memberName)
            OwnerRadioItem(
                label = memberName,
                selected = selectedOwner == selection,
                onClick = { onOwnerSelected(selection) },
            )
        }
    }
}
