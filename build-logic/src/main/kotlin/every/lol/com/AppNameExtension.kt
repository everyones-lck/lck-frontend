package every.lol.com

import org.gradle.api.Project
import com.android.build.api.dsl.CommonExtension

fun Project.setNamespace(name: String) {
    val androidExtension = extensions.findByName("android") as? CommonExtension<*, *, *, *, *, *>

    if (androidExtension != null) {
        androidExtension.namespace = "every.lol.com.$name"
    } else {
        logger.info("Android extension not found for project: ${project.name}. Skipping namespace setup.")
    }
}