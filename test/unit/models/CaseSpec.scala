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
import java.time.temporal.ChronoUnit

import org.scalatest.{MustMatchers, WordSpec}

class CaseSpec extends WordSpec with MustMatchers {

  private def decisionWithEndDate(date: Instant) = {
    val c = oCase.btiCaseWithDecision
    c.copy(decision = Some(c.decision.get.copy(effectiveEndDate = Some(date))))
  }

  "Case class" must {

    val futureDate = Instant.now().plus(1, ChronoUnit.DAYS)
    val pastDate = Instant.now().minus(1, ChronoUnit.DAYS)

    "active decision returns true when end date is in the future" in {
      decisionWithEndDate(futureDate).hasActiveDecision mustBe true
    }

    "active decision returns false when end date is in the past" in {
      decisionWithEndDate(pastDate).hasActiveDecision mustBe false
    }

    "expired decision returns false when end date is in the future" in {
      decisionWithEndDate(futureDate).hasExpiredDecision mustBe false
    }

    "expired decision returns true when end date is in the past" in {
      decisionWithEndDate(pastDate).hasExpiredDecision mustBe true
    }

    "hasEori returns true for trader" in {
      val traderCase = oCase.btiCaseExample.copy(application = oCase.btiApplicationExample.copy(agent = None))
      traderCase.hasEoriNumber(traderCase.application.holder.eori) mustBe true
    }

    "hasEori returns true for agent" in {
      val agentCase = oCase.btiCaseExample
      agentCase.hasEoriNumber(agentCase.application.agent.get.eoriDetails.eori) mustBe true
    }

    "hasEori returns false for another user" in {
      oCase.btiCaseExample.hasEoriNumber("????") mustBe false
    }

    "hasRuling returns false for case with no decision" in {
      oCase.btiCaseExample.hasRuling mustBe false
    }

    "hasRuling returns false for case with wrong status" in {
      oCase.btiCaseWithDecision.copy(status = CaseStatus.OPEN).hasRuling mustBe false
    }

    "hasRuling returns true for case with decision" in {
      oCase.btiCaseWithDecision.hasRuling mustBe true
    }
  }
}
