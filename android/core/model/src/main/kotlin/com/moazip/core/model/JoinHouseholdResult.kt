package com.moazip.core.model

sealed interface JoinHouseholdResult {
    data object Joined : JoinHouseholdResult
    data object AlreadyMemberOfHousehold : JoinHouseholdResult
    data object RequiresHouseholdSwitch : JoinHouseholdResult
}
