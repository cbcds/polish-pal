package com.cbcds.polishpal

import com.cbcds.polishpal.feature.settings.model.AppInfo

class PolishPalAppInfo : AppInfo {

    override val applicationId: String = BuildConfig.APPLICATION_ID

    override val applicationVersion: String = BuildConfig.VERSION_NAME

    override val contactEmail: String = CONTACT_EMAIL

    private companion object {

        private const val CONTACT_EMAIL = "cbcds.dev@gmail.com"
    }
}
