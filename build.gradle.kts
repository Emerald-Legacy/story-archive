import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

plugins {
    id("org.asciidoctor.jvm.base") version "4.0.5"
    id("org.asciidoctor.jvm.convert") version "4.0.5"
    id("org.asciidoctor.jvm.pdf") version "4.0.5"
}

repositories {
    mavenCentral()
}

asciidoctorj {
    setVersion("3.0.0")
}

abstract class RenderStoryTask @Inject constructor(
    we: WorkerExecutor
) : AsciidoctorTask(we) {

    init {
        setSourceDir(File("./docs/"))
        setBaseDir(File("./docs/"))

        setOutputDir(File("./build/"))
        outputOptions {
            setSeparateOutputDirs(false)
            backends("pdf", "html")
        }

        attributes(
            mapOf(
                "icons" to "font",
                "revnumber" to "",
                "revdate" to "",
                "version-label" to "",
                "language" to "EN",
                "imagesdir" to "images",
                "pdf-themesdir" to "pdf-theme/themes",
                "pdf-fontsdir" to "pdf-theme/fonts",
                "pdf-theme" to "el"
            )
        )
    }
}

val renderStories = tasks.register<RenderStoryTask>("renderStories") {
    group = "Stories"
    description = "Render all story files to PDF and HTML"
    sources {
        include("stories/*.adoc")
    }
}

val renderChapterParts = tasks.register<RenderStoryTask>("renderChapterParts") {
    group = "Stories"
    description = "Render all chapter part files to PDF and HTML"
    sources {
        include("stories/chapter_parts/*.adoc")
    }
}

val renderIndex = tasks.register<RenderStoryTask>("renderIndex") {
    group = "Stories"
    description = "Render the index file to PDF and HTML"
    sources {
        include("index.adoc")
    }
}

tasks.register("buildStoryArchive") {
    group = "Stories"
    description = "Build all story files (index, stories, and chapter parts)"
    dependsOn(renderStories, renderChapterParts, renderIndex)
}

defaultTasks("buildStoryArchive")
