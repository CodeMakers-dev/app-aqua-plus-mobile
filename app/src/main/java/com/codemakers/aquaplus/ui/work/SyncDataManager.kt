package com.codemakers.aquaplus.ui.work

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.codemakers.aquaplus.domain.repository.UserRepository
import com.codemakers.aquaplus.domain.usecases.HasUnsyncedReadingFormDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

const val SYNC_DATA_WORK = "SYNC_DATA_WORK"

class SyncDataManager(
    private val workManager: WorkManager,
    private val userRepository: UserRepository,
    private val hasUnsyncedReadingFormDataUseCase: HasUnsyncedReadingFormDataUseCase,
) : CoroutineScope by MainScope() {

    val workState: LiveData<List<WorkInfo>>
        get() = workManager.getWorkInfosByTagLiveData(SYNC_DATA_WORK)

    fun start() = launch {
        val hasUnsyncedData = hasUnsyncedReadingFormDataUseCase()
        Log.d("SyncDataManager", "hasUnsyncedData: $hasUnsyncedData")
        // TODO: Check if user is logged
        //val isUserLogged = userRepository.isUserLogged()
        val isWorkFinished = workState.value?.firstOrNull()?.state?.isFinished != false
        Log.d("SyncDataManager", "workState.value?.firstOrNull()?.state: ${workState.value?.firstOrNull()?.state}")
        Log.d("SyncDataManager", "isWorkFinished: $isWorkFinished")
        if (isWorkFinished /*&& isUserLogged*/ && hasUnsyncedData) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build()
            val syncDataWorker: OneTimeWorkRequest =
                OneTimeWorkRequestBuilder<SyncDataWorker>()
                    .setConstraints(constraints)
                    .addTag(SYNC_DATA_WORK).build()

            workManager.beginUniqueWork(SYNC_DATA_WORK, ExistingWorkPolicy.KEEP, syncDataWorker)
                .enqueue()
        }
    }
}