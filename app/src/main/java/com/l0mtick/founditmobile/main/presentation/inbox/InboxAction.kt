package com.l0mtick.founditmobile.main.presentation.inbox

sealed interface InboxAction {
    data object UpdateChats : InboxAction
}