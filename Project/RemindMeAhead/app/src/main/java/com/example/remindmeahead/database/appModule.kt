package com.example.remindmeahead.database

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class appModule {
    @Singleton
    @Provides
    fun provideRepo(dao: dbDao):repo{
        return repo(dao)
    }

    @Singleton
    @Provides
    fun provideDB(app: Application):dataBase{
        return dataBase.getInstance(app)
    }

    @Singleton
    @Provides
    fun provideDao(DataBase:dataBase):dbDao{
        return DataBase.daos()
    }
}