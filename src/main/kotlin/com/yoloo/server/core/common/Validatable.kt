package com.yoloo.server.core.common

import com.googlecode.objectify.annotation.OnSave
import javax.validation.ConstraintViolationException
import javax.validation.Validation

interface Validatable {

    @OnSave
    fun validate() {
        val violations = Validation.buildDefaultValidatorFactory().validator.validate(this)

        if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }
    }
}