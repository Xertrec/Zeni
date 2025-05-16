package com.zeni.core.domain.utils.extensions

import android.net.Uri

val Uri.completeFileName
    get() = this.path!!.substringAfterLast(delimiter = "/")