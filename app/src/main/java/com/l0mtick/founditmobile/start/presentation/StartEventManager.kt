package com.l0mtick.founditmobile.start.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object StartEventManager {
    private val eventChannel = Channel<StartEvent>(Channel.BUFFERED)

    val eventsFlow = eventChannel.receiveAsFlow()

    fun triggerEvent(event: StartEvent) {
        CoroutineScope(Dispatchers.Default).launch { eventChannel.send(event) }
    }

    /**
     * A sealed class representing all possible types of application events.
     * This allows for strongly typed event handling with a clear structure.
     */
    sealed interface StartEvent {
        data object OnNavigateToMain: StartEvent
        data object OnNavigateToMainAsGuest: StartEvent
    }
}