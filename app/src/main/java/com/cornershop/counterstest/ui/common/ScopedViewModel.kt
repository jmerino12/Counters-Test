package com.cornershop.counterstest.ui.common

import androidx.fragment.app.DialogFragment
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

abstract class ScopeDialogFragment : DialogFragment(), KoinScopeComponent {

    override val scope: Scope by lazy { createScope(this) }

    override fun onDestroy() {
        closeScope()
        super.onDestroy()
    }

}