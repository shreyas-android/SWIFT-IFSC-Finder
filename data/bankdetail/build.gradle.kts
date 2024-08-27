import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-parcelize")
    id("maven-publish")
    id("app.cash.sqldelight") version "2.0.1"

}

version = "1.0.0"
group = "com.jackson.shared.data.bankdetail"

kotlin {

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
        publishLibraryVariants("release")
    }

    val xcf = XCFramework()


    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "bankdetail"
            xcf.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.sql.delight.adaper)
                implementation(libs.sql.delight.runtime)
                implementation(libs.sql.delight.coroutine)
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.atomicfu)
                implementation(project(":common:shared"))
                api("app.cash.sqldelight:androidx-paging3-extensions:2.0.0")

                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.sql.delight.android)
                implementation(project(":common:shared"))
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(libs.sql.delight.native)
                implementation(project(":common:shared"))
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.jackson.shared.data.bankdetail"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
    }
}


sqldelight {
    databases {
        create("BankDatabase"){
            packageName.set("com.jackson.shared.data.bankdetail.database")
            srcDirs.setFrom("src/main/sqldelight")
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
            //            schemaOutputDirectory.set(file("src/commonMain/db/databases"))
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
            migrationOutputDirectory.set(file("src/main/db/migrations"))
            // generateAsync.set(true)

        }
    }
}

task("testClasses").doLast {
    println("This is a dummy testClasses task")
}
