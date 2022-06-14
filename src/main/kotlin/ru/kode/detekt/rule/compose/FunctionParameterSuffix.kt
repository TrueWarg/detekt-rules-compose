package ru.kode.detekt.rule.compose

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.google.gwt.dev.js.rhino.Context.reportError
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtParameter

class FunctionParameterSuffix(config: Config = Config.empty) : Rule(config) {
  override val issue = Issue(
    javaClass.simpleName,
    Severity.Defect,
    "Checks if parameters of functions with defined name and types contain defined suffix",
    Debt.FIVE_MINS
  )

  override fun visitNamedFunction(function: KtNamedFunction) {
    val words = valueOrNull<Set<String>>("containsWords")?.map { it.lowercase() }
    val types = valueOrNull<Set<String>>("haveTypes")
    val suffixes = valueOrNull<Set<String>>("suffixes")?.map { it.lowercase() }
    require(!words.isNullOrEmpty()) { "containsWords field must not be empty" }
    require(!types.isNullOrEmpty()) { "haveTypes field must not be empty" }
    require(!suffixes.isNullOrEmpty()) { "suffixes field must not be empty" }
    for (param in function.valueParameters) {
      val name = param.name.orEmpty().lowercase()
      val type = param.text.split(":")[1].trim()
      val incorrect = words.any { name.contains(it) } && types.contains(type) && suffixes.all { !name.contains(it) }
      if (incorrect) reportError(param)
    }
  }

  private fun reportError(node: KtParameter) {
    val usesPastTense = node.name!!.startsWith("on") && node.name!!.endsWith("ed")
    report(
      CodeSmell(
        issue,
        Entity.from(node),
        if (usesPastTense) {
          "Invalid event parameter name. Do not use past tense. For example: \"onClicked\" → \"onClick\""
        } else {
          "Invalid event parameter name. Use names like \"onClick\", \"onValueChange\" etc"
        }
      )
    )
  }
}