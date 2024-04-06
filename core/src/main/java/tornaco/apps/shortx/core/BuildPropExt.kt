package tornaco.apps.shortx.core

object BuildPropExt {
    var isDebug = false

    inline fun onDebugBuild(action: () -> Unit) {
        if (isDebug) {
            action()
        }
    }

    inline fun onReleaseBuild(action: () -> Unit) {
        if (isDebug) {
            action()
        }
    }
}