/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.social.com.di.components


import android.content.Context
import dagger.Component
import owlsdevelopers.com.owlsweather.WeatherRcvService
import owlsdevelopers.com.owlsweather.ui.AddTownActivity
import owlsdevelopers.com.owlsweather.ui.HomeActivity
import owlsdevelopers.com.owlsweather.ui.SettingsActivity
import owlsdevelopers.com.owlsweather.ui.TownFragment
import owlsdevelopers.com.owlsweather.ui.di.modules.DataModule
import owlsdevelopers.com.owlsweather.widgets.WidgetBig
import owlsdevelopers.com.owlsweather.widgets.WidgetSmall
import javax.inject.Singleton

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = arrayOf(DataModule::class))
interface ApplicationComponent {
    fun inject(fragment: TownFragment)
    fun inject(activity: AddTownActivity)
    fun inject(activity: SettingsActivity)
    fun inject(activity: HomeActivity)
    fun inject(widget: WidgetBig.UpdateService)
    fun inject(widget: WidgetSmall.UpdateService)
    fun inject(service: WeatherRcvService)



}
