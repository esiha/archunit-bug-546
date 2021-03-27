package com.example.project

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

private const val BASE_PACKAGE = "com.oxp"
private const val HEXAGON_PACKAGE = "${BASE_PACKAGE}.things.organization"

@AnalyzeClasses(
    packages = [HEXAGON_PACKAGE],
    importOptions = [DoNotIncludeTests::class]
)
class HexagonalArchitectureTest {
    private val languageExtensions = listOf("ddd", "extensions", "hexagonal")
        .map { "${BASE_PACKAGE}.${it}" }
        .toTypedArray()

    private val language = arrayOf(
        "kotlin..",
        "java..",
        "org.jetbrains.annotations", // The Kotlin compiler adds @NotNull and @Nullable to the code
        "" // Some Kotlin constructs (e.g. when on enums) are implemented in the default package
    )

    private val whiteListedExternalDependencies = arrayOf("arrow.core")

    @ArchTest
    val domainCanOnlyHaveWhiteListedDependencies: ArchRule =
        classes().that().resideInAPackage("${HEXAGON_PACKAGE}.domain..")
            .should().onlyDependOnClassesThat().resideInAnyPackage(
                *language,
                *languageExtensions,
                "${HEXAGON_PACKAGE}.domain..",
                *whiteListedExternalDependencies
            ).because(
                """
                The Domain in Hexagonal Architecture should theoretically not depend on any technology.
                
                However, it needs to be written in a programming language, which itself is a technology. Also, we might
                want to extend the language's capabilities with home-made extensions or some very few & selected external
                dependencies that act as extensions of the language.
                """.trimIndent()
            )

    @ArchTest
    val languageExtensionsCanOnlyHaveWhiteListedDependencies: ArchRule =
        classes().that().resideInAnyPackage(*languageExtensions)
            .should().onlyDependOnClassesThat().resideInAnyPackage(*language, *whiteListedExternalDependencies)
            .because(
                """
                The Domain in Hexagonal Architecture should theoretically not depend on any technology.
                
                However, it needs to be written in a programming language which itself is a technology, and we sometimes
                need to extend that language's expressiveness to write more readable code.
                
                For this reason, we allow ourselves the creation of language extensions which are neither part of the
                Domain (they are unrelated to business concepts and rules) nor part of any Adapter (they won't plug onto
                any of the Domain's Ports.)
                
                Because we consider the language extensions as part of the language, they should only depend on the
                the language itself, or Domain white-listed dependencies when needed.
                """.trimIndent()
            )

    @ArchTest
    val adaptersShouldBeIndependent: ArchRule =
        slices().matching("${HEXAGON_PACKAGE}.(driv*).(*)..")
            .namingSlices("$1 adapter '$2'").should().notDependOnEachOther()
            .because(
                """
                Adapters in Hexagonal Architecture need to be able to evolve independently.
                
                This is only truly possible if there are no dependencies between any of them.
                """.trimIndent()
            )
}