package com.zooatlanta.core.coroutines

import com.squareup.anvil.annotations.ContributesBinding
import com.zooatlanta.di.AppScope
import com.zooatlanta.di.SingleInAppScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@SingleInAppScope
class StandardDispatcherProvider @Inject constructor() : DispatcherProvider {
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val main: CoroutineDispatcher = Dispatchers.Main
}
