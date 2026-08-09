package com.moazip.core.model

data class HouseholdDetails(
    val id: String,
    val name: String,
    val inviteCode: String?,
    val members: List<HouseholdMember>,
)
