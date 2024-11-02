package com.example.cinemagic.Data

import com.example.cinemagic.DetailScreen.data.DetailsCastService
import com.example.cinemagic.DetailScreen.data.DetailsService
import com.example.cinemagic.FavoriteScreen.data.FavoriteService
import com.example.cinemagic.HomeScreen.data.HomeApiService
import com.example.cinemagic.NotificationScreen.data.NotificationService
import com.example.cinemagic.SearchScreen.data.SearchService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule { // Consider renaming to NetworkModule if it provides for the entire network layer

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun provideMoviesApiService(retrofit: Retrofit): HomeApiService { // Provide MoviesApiService
        return retrofit.create(HomeApiService::class.java)
    }

    @Provides
    fun provideFavorite(retrofit: Retrofit): FavoriteService {
        return retrofit.create(FavoriteService::class.java)
    }

    @Provides
    fun provideNotifications(retrofit: Retrofit): NotificationService {
        return retrofit.create(NotificationService::class.java)
    }

    @Provides
    fun provideSearch(retrofit: Retrofit): SearchService {
        return retrofit.create(SearchService::class.java)
    }

    @Provides
    fun provideDetails(retrofit: Retrofit): DetailsService {
        return retrofit.create(DetailsService::class.java)
    }

    @Provides
    fun provideDetailsCast(retrofit: Retrofit): DetailsCastService {
        return retrofit.create(DetailsCastService::class.java)
    }

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
