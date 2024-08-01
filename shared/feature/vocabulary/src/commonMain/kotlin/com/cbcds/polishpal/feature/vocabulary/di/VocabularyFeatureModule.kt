package com.cbcds.polishpal.feature.vocabulary.di

import com.cbcds.polishpal.data.di.dataModule
import com.cbcds.polishpal.feature.vocabulary.screen.word.WordViewModel
import com.cbcds.polishpal.feature.vocabulary.screen.words.AllWordsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val vocabularyFeatureModule = module {
    includes(dataModule)

    viewModelOf(::AllWordsViewModel)
    viewModelOf(::WordViewModel)
}
