package ru.kode.detekt.rule.compose

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.lint
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldHaveSize
import kotlin.io.path.Path

class FunctionParameterSuffixTest : ShouldSpec({
  should("test") {
    val code = """
      fun kek(width: Int, sven: Param,) {}
        """.trimIndent()
    val config = TestConfig(
      values = mutableMapOf(
        "containsWords" to setOf("w"),
        "haveTypes" to setOf("w"),
        "suffixes" to setOf("w"),
      )
    )
    val result = FunctionParameterSuffix(config).lint(code)
    result shouldHaveSize 1
  }
})