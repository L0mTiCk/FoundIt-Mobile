package com.l0mtick.founditmobile.main.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object MainEventManager {
    private val eventChannel = Channel<MainEvent>(Channel.BUFFERED)

    val eventsFlow = eventChannel.receiveAsFlow()

    fun triggerEvent(event: MainEvent) {
        CoroutineScope(Dispatchers.Default).launch { eventChannel.send(event) }
    }

    /**
     * A sealed class representing all possible types of application events.
     * This allows for strongly typed event handling with a clear structure.
     */
    sealed interface MainEvent {
        data object OnDarkThemeChanged : MainEvent
    }
}