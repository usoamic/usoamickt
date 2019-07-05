package io.usoamic.testcli.di

import io.usoamic.cli.core.Usoamic
import dagger.Module
import dagger.Provides
import io.usoamic.cli.other.Config
import javax.inject.Singleton

@Module
class UsoamicModule {
    @Provides
    @Singleton
    fun provideContract(): Usoamic {
        return Usoamic("test_account.json")
    }
}