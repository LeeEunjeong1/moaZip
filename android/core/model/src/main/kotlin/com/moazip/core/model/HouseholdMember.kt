package com.moazip.core.model

data class HouseholdMember(
    val userId: String,
    val displayName: String?,
    val role: HouseholdMemberRole = HouseholdMemberRole.MEMBER,
)

enum class HouseholdMemberRole { OWNER, MEMBER }
