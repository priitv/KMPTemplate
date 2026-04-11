import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kspAllTargets(notation: Any) {
    add("kspCommonMainMetadata", notation)
    add("kspAndroid", notation)
    add("kspIosArm64", notation)
    add("kspIosSimulatorArm64", notation)
    add("kspIosX64", notation)
}
