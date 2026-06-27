package com.jigsaw.di

import com.jigsaw.data.repository.ChallengeRepositoryImpl
import com.jigsaw.data.repository.GameRepositoryImpl
import com.jigsaw.data.repository.PreferencesRepositoryImpl
import com.jigsaw.data.repository.ProgressionRepositoryImpl
import com.jigsaw.domain.repository.ChallengeRepository
import com.jigsaw.domain.repository.GameRepository
import com.jigsaw.domain.repository.PreferencesRepository
import com.jigsaw.domain.repository.ProgressionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindGameRepository(impl: GameRepositoryImpl): GameRepository
    @Binds @Singleton abstract fun bindChallengeRepository(impl: ChallengeRepositoryImpl): ChallengeRepository
    @Binds @Singleton abstract fun bindProgressionRepository(impl: ProgressionRepositoryImpl): ProgressionRepository
    @Binds @Singleton abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository
}
