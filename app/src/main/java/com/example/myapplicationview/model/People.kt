package com.example.myapplicationview.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class People(
    var names: String,
    var ages: Int
): Parcelable