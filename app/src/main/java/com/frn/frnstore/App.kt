package com.frn.frnstore

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.frn.frnstore.data.repo.BannerSliderRepository
import com.frn.frnstore.data.repo.BannerSliderRepositoryImpl
import com.frn.frnstore.data.repo.ProductRepositoryIml
import com.frn.frnstore.data.repo.ProductsRepository
import com.frn.frnstore.data.repo.Source.BannerSliderRemoteDataSource
import com.frn.frnstore.data.repo.Source.ProductLocalDataSource
import com.frn.frnstore.data.repo.Source.ProductRemoteDataSource
import com.frn.frnstore.feature.main.MainViewModel
import com.frn.frnstore.feature.main.ProductListAdapter
import com.frn.frnstore.services.http.ApiService
import com.frn.frnstore.services.http.createApiServiceInstance
import com.frn.frnstore.services.imageLoading.ImageLoadingService
import com.frn.frnstore.services.imageLoading.ImageLoadingServiceImpl
import io.reactivex.Single
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Fresco.initialize(this)



        val myModel = module {
            single<ApiService> { createApiServiceInstance() }
            single<ImageLoadingService> { ImageLoadingServiceImpl() }
            factory<ProductsRepository> {
                ProductRepositoryIml(
                    ProductRemoteDataSource(get()),
                    ProductLocalDataSource()
                )
            }
            factory<BannerSliderRepository>{
                BannerSliderRepositoryImpl(BannerSliderRemoteDataSource(get()))
            }
            factory { ProductListAdapter(get()) }
            viewModel { MainViewModel(get() , get() ) }
        }

        startKoin {
            androidContext(this@App)
            modules(myModel)
        }
    }

}