/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import java.time.Instant
import java.util.UUID

object oCase {
  val fileAttachment = Attachment(id = UUID.randomUUID().toString)
  val eoriDetailsExample = EORIDetails("eoriTrader", "Trader Business Name", "line1", "line2", "line3", "postcode", "country")
  val eoriAgentDetailsExample = AgentDetails(EORIDetails("eoriAgent", "Agent Business Name", "line1", "line2", "line3", "postcode", "country"),
    Some(fileAttachment))
  val contactExample = Contact("name", "email", Some("phone"))
  val btiApplicationExample = Application(
    "BTI",
    eoriDetailsExample,
    contactExample,
    Some(eoriAgentDetailsExample),
    offline = false,
    "Laptop",
    "Personal Computer",
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    sampleToBeProvided = false,
    sampleToBeReturned = false
  )

  def decisionExample(exp : Option[String] = Some("This is some explanation")) = {
    Decision(
      bindingCommodityCode = "commodity-code-123",
      effectiveStartDate = Some(Instant.now),
      effectiveEndDate = Some(Instant.now),
      justification = "justification-content",
      goodsDescription = "goodsDescription-content",
      methodCommercialDenomination = Some("commercial-denomination-content"),
      explanation = exp
    )
  }


  val btiCaseWithDecision = Case(reference = "ref", status = CaseStatus.COMPLETED, application = btiApplicationExample, decision = Some(decisionExample()))
  val btiCaseWithDecisionNoExplanation = Case(reference = "ref", status = CaseStatus.COMPLETED, application = btiApplicationExample, decision = Some(decisionExample(None)))

  val btiCaseExample = Case(reference = "ref", status = CaseStatus.OPEN, application = btiApplicationExample)
  val newBtiCaseExample = NewCaseRequest(btiApplicationExample, Seq.empty)

}
