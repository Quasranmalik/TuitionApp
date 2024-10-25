package com.example.myapplication.hilt.di

import com.example.myapplication.data.student.StudentRepository
import com.example.myapplication.data.student.implementation.RoomStudentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideRepository(roomStudentRepository: RoomStudentRepository):StudentRepository
}