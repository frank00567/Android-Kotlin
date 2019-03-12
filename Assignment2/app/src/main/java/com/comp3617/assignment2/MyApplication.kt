package com.comp3617.assignment2

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
            .initialData { realm -> realm.createObject(TaskModel::class.java) }
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(realmConfig)
    }
}