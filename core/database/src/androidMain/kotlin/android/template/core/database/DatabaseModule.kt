package android.template.core.database

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val databaseModule: Module = module {
    single { createDatabase(androidContext()) }
    single { get<AppDatabase>().myModelDao() }
}

fun createDatabase(context: Context): AppDatabase =
    Room.databaseBuilder<AppDatabase>(context, "MyModel.db")
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
