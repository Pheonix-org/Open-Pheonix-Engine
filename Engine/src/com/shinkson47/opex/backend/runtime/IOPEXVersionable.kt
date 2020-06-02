package com.shinkson47.opex.backend.runtime

import com.shinkson47.opex.backend.toolbox.Version

interface IOPEXVersionable {
    fun VERSION(): Version?
}