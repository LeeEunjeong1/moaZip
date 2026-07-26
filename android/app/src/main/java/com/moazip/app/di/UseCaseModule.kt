package com.moazip.app.di

import com.moazip.core.domain.repository.AssetRepository
import com.moazip.core.domain.repository.DashboardRepository
import com.moazip.core.domain.repository.HouseholdRepository
import com.moazip.core.domain.usecase.AddAssetUseCase
import com.moazip.core.domain.usecase.CreateHouseholdUseCase
import com.moazip.core.domain.usecase.GetLatestInviteCodeUseCase
import com.moazip.core.domain.usecase.GetHouseholdMembersUseCase
import com.moazip.core.domain.usecase.HasJoinedHouseholdUseCase
import com.moazip.core.domain.usecase.JoinHouseholdWithInviteCodeUseCase
import com.moazip.core.domain.usecase.ObserveDashboardSummary
import com.moazip.core.domain.usecase.ReissueInviteCodeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideCreateHouseholdUseCase(repository: HouseholdRepository) =
        CreateHouseholdUseCase(repository)

    @Provides
    fun provideHasJoinedHouseholdUseCase(repository: HouseholdRepository) =
        HasJoinedHouseholdUseCase(repository)

    @Provides
    fun provideJoinHouseholdWithInviteCodeUseCase(repository: HouseholdRepository) =
        JoinHouseholdWithInviteCodeUseCase(repository)

    @Provides
    fun provideReissueInviteCodeUseCase(repository: HouseholdRepository) =
        ReissueInviteCodeUseCase(repository)

    @Provides
    fun provideGetLatestInviteCodeUseCase(repository: HouseholdRepository) =
        GetLatestInviteCodeUseCase(repository)

    @Provides
    fun provideGetHouseholdMembersUseCase(repository: HouseholdRepository) =
        GetHouseholdMembersUseCase(repository)

    @Provides
    fun provideAddAssetUseCase(repository: AssetRepository) =
        AddAssetUseCase(repository)

    @Provides
    fun provideObserveDashboardSummary(repository: DashboardRepository) =
        ObserveDashboardSummary(repository)
}
