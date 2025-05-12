package com.l0mtick.founditmobile.common.domain.repository

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isConnected: Flow<Boolean>
}