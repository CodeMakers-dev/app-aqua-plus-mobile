package com.codemakers.aquaplus.ui.work

import androidx.lifecycle.LiveData
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.codemakers.aquaplus.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

const val SYNC_DATA_WORK = "SYNC_DATA_WORK"

class SyncDataManager(
    private val workManager: WorkManager,
    private val userRepository: UserRepository,
) : CoroutineScope by MainScope() {

    val workState: LiveData<List<WorkInfo>>
        get() = workManager.getWorkInfosByTagLiveData(SYNC_DATA_WORK)

    fun start() = launch {
        if (workState.value?.firstOrNull()?.state?.isFinished != false && userRepository.isUserLogged()) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build()
            val syncDataWorker: OneTimeWorkRequest = OneTimeWorkRequestBuilder<com.codemakers.aquaplus.ui.work.SyncDataWorker>()
                .setConstraints(constraints).addTag(SYNC_DATA_WORK).build()

            workManager.beginUniqueWork(SYNC_DATA_WORK, ExistingWorkPolicy.KEEP, syncDataWorker)
                .enqueue()
        }
    }
}