package com.moazip.app.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moazip.core.data.InMemoryDashboardRepository
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.repository.AuthRepository
import com.moazip.core.domain.repository.AssetRepository
import com.moazip.core.domain.repository.DashboardRepository
import com.moazip.core.domain.repository.HouseholdRepository
import com.moazip.core.domain.repository.UserRepository
import com.moazip.core.domain.usecase.AddAssetUseCase
import com.moazip.core.domain.usecase.CreateHouseholdUseCase
import com.moazip.core.domain.usecase.GetLatestInviteCodeUseCase
import com.moazip.core.domain.usecase.HasJoinedHouseholdUseCase
import com.moazip.core.domain.usecase.JoinHouseholdWithInviteCodeUseCase
import com.moazip.core.domain.usecase.ObserveDashboardSummary
import com.moazip.core.domain.usecase.ReissueInviteCodeUseCase
import com.moazip.core.firebase.FirebaseAssetRepository
import com.moazip.core.firebase.FirebaseAuthRepository
import com.moazip.core.firebase.FirebaseCurrentUserProvider
import com.moazip.core.firebase.FirebaseHouseholdRepository
import com.moazip.core.firebase.FirebaseUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideCurrentUserProvider(firebaseAuth: FirebaseAuth): CurrentUserProvider =
        FirebaseCurrentUserProvider(firebaseAuth)

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository =
        FirebaseUserRepository(firestore)

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        userRepository: UserRepository,
    ): AuthRepository = FirebaseAuthRepository(firebaseAuth, userRepository)

    @Provides
    @Singleton
    fun provideHouseholdRepository(firestore: FirebaseFirestore): HouseholdRepository =
        FirebaseHouseholdRepository(firestore)

    @Provides
    @Singleton
    fun provideAssetRepository(firestore: FirebaseFirestore): AssetRepository =
        FirebaseAssetRepository(firestore)

    @Provides
    @Singleton
    fun provideDashboardRepository(): DashboardRepository = InMemoryDashboardRepository()

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
    fun provideAddAssetUseCase(repository: AssetRepository) =
        AddAssetUseCase(repository)

    @Provides
    fun provideObserveDashboardSummary(repository: DashboardRepository) =
        ObserveDashboardSummary(repository)
}
