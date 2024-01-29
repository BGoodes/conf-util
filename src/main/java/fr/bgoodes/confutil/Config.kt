package fr.bgoodes.confutil

import fr.bgoodes.confutil.exceptions.StorageException
import fr.bgoodes.confutil.storage.model.Storage

interface Config {
    @Throws(StorageException::class)
    fun load(storage: Storage)

    @Throws(StorageException::class)
    fun save(storage: Storage)
}
