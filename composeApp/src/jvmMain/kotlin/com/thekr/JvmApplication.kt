package com.thekr

import ca.gosyer.appdirs.AppDirs
import com.thekr.database.AppContainer
import com.thekr.database.AppDataContainer
import com.thekr.database.getDatabaseBuilder
import com.thekr.di.DatabaseProvider
import com.thekr.di.appStorage
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.PosixFilePermissions

object JvmApplication {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    var container: AppContainer = AppDataContainer()

    val userDataDir = AppDirs {
        appName = "thekr"
        appAuthor = "thekr"
    }.getUserDataDir()

    init {
        // Settings and the Room DB are written in plaintext into userDataDir;
        // create/tighten the directory to owner-only on POSIX systems so other
        // local accounts cannot read the user's data. Best-effort: never
        // blocks the boot.
        runCatching {
            val dir = Path.of(userDataDir)
            Files.createDirectories(dir)
            if (FileSystems.getDefault().supportedFileAttributeViews().contains("posix")) {
                Files.setPosixFilePermissions(
                    dir,
                    PosixFilePermissions.fromString("rwx------"),
                )
            }
        }

        // Initialize database
        DatabaseProvider.initDatabase(getDatabaseBuilder())

        appStorage = userDataDir
    }
}
