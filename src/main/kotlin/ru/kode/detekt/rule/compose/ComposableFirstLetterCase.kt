package ru.kode.detekt.rule.compose

import io.gitlab.arturbosch.detekt.api.*

class ComposableFirstLetterCase(config: Config = Config.empty) : Rule(config) {
  override val issue = Issue(
    javaClass.simpleName,
    Severity.Defect,
    "Checks if Composable top level function " +
        "or class method with Unit " +
        "return type starts with upper case letter and lower case in any other type",
    Debt.FIVE_MINS
  )

}