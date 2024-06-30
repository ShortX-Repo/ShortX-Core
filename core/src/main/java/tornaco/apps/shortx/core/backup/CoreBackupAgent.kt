package tornaco.apps.shortx.core.backup

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tornaco.apps.shortx.core.shortXManager
import tornaco.apps.shortx.core.store.BaseDirectActionSetsStore
import tornaco.apps.shortx.core.store.BaseDirectActionSettingsStore
import tornaco.apps.shortx.core.store.BaseGlobalVarStore
import tornaco.apps.shortx.core.store.BasePkgSetsStore
import tornaco.apps.shortx.core.store.BaseRuleSetsStore
import tornaco.apps.shortx.core.store.BaseRuleSettingsStore
import tornaco.apps.shortx.core.store.BaseToggleSettingsStore
import tornaco.apps.shortx.core.util.DateUtils
import tornaco.apps.shortx.core.util.Logger
import tornaco.apps.shortx.core.util.ZipUtils
import java.io.File

fun autoGenBackupFileName() =
    "ShortX-Backup-${DateUtils.formatForFileName(System.currentTimeMillis())}"

interface BackupSettings {
    val cacheDir: File
    val destDir: File
    val versionCode: Long
}

open class CoreBackupAgent(private val settings: BackupSettings) {
    private val logger = Logger("CoreBackupAgent")

    suspend fun backup(fileName: String? = null): File {
        return withContext(Dispatchers.IO) {
            val backupTempDir = File(settings.cacheDir, "backup-${System.currentTimeMillis()}")

            // Rules
            val allRules = shortXManager.getAllRules()
            val ruleStore = BaseRuleSettingsStore(this, File(backupTempDir, RULE_FILE_NAME))
            ruleStore.addRules(rules = allRules, write = false)
            ruleStore.write()

            // DAs
            val allDAs = shortXManager.getAllDirectAction()
            val daStore = BaseDirectActionSettingsStore(this, File(backupTempDir, DA_FILE_NAME))
            daStore.addDirectActions(das = allDAs, write = false)
            daStore.write()

            // Toggles
            val allToggles = shortXManager.getAllToggles()
            val toggleStore = BaseToggleSettingsStore(this, File(backupTempDir, TOGGLE_FILE_NAME))
            toggleStore.addToggles(das = allToggles, write = false)
            toggleStore.write()

            // PkgSets
            val allPkgSets = shortXManager.getAllPkgSets(true)
            val pkgSetStore = BasePkgSetsStore(this, File(backupTempDir, PKGSET_FILE_NAME))
            pkgSetStore.addAllPkgSets(pkgSets = allPkgSets, write = false)
            pkgSetStore.write()

            // GV
            val allGV = shortXManager.getAllGlobalVars().filterNot { it.isSecret }
            val gvStore = BaseGlobalVarStore(this, File(backupTempDir, GV_FILE_NAME))
            gvStore.addAllGlobalVars(globalVars = allGV, write = false)
            gvStore.write()

            // DA Sets
            val allDASets = shortXManager.getAllDASets(false)
            val daSetsStore =
                BaseDirectActionSetsStore(this, File(backupTempDir, DA_SET_FILE_NAME))
            daSetsStore.addDirectActionSets(directActionSets = allDASets, write = false)
            daSetsStore.write()

            // Rule Sets
            val allRuleSets = shortXManager.getAllRuleSets(false)
            val ruleSetStore = BaseRuleSetsStore(this, File(backupTempDir, RULE_SET_FILE_NAME))
            ruleSetStore.addRuleSets(ruleSets = allRuleSets, write = false)
            ruleSetStore.write()

            // Meta
            val metaFile = File(backupTempDir, META_FILE_NAME)
            val meta = "${settings.versionCode}"
            metaFile.writeText(meta)

            val backupZipRootDir = settings.destDir

            val name = (fileName ?: autoGenBackupFileName()) + ".zip"

            ZipUtils.zip(backupTempDir.absolutePath, backupZipRootDir.absolutePath, name)

            backupTempDir.deleteRecursively()

            return@withContext File(backupZipRootDir, name)
        }
    }

    suspend fun resetAllData() {
        logger.w("resetAllData")
        withContext(Dispatchers.IO) {
            shortXManager.getAllRules().forEach {
                logger.w("Delete rule: ${it.title}")
                shortXManager.deleteRule(it.id)
            }
            shortXManager.getAllDirectAction().forEach {
                logger.w("Delete da: ${it.title}")
                shortXManager.deleteDirectAction(it.id)
            }
            shortXManager.getAllToggles().forEach {
                logger.w("Delete toggle: ${it.title}")
                shortXManager.deleteToggle(it.id)
            }
            shortXManager.getAllGlobalVars().forEach {
                logger.w("Delete gv: ${it.name}")
                shortXManager.deleteGlobalVar(it.name)
            }
            shortXManager.getAllPkgSets(withPkgList = false).forEach {
                logger.w("Delete pkg set: ${it.label}")
                shortXManager.deletePkgSet(it.label)
            }
            shortXManager.getAllRuleSets(false).forEach {
                logger.w("Delete rule set: ${it.title}")
                shortXManager.deleteRuleSet(it.id)
            }
            shortXManager.getAllDASets(false).forEach {
                logger.w("Delete da set: ${it.title}")
                shortXManager.deleteDASet(it.id)
            }
            shortXManager.clearEvaluateRecords()
        }
    }

}