/*
 * Copyright 2018 HM Revenue & Customs
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

package utils

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages._
import viewmodels.{AnswerRow, RepeaterAnswerRow, RepeaterAnswerSection}

class CheckYourAnswersHelper(userAnswers: UserAnswers) {

  def registerBusinessRepresenting: Option[AnswerRow] = userAnswers.get(RegisterBusinessRepresentingPage) map {
    x => AnswerRow("registerBusinessRepresenting.checkYourAnswersLabel", s"${x.field1} ${x.field2}", false, routes.RegisterBusinessRepresentingController.onPageLoad(CheckMode).url)
  }

  def selectApplicationType: Option[AnswerRow] = userAnswers.get(SelectApplicationTypePage) map {
    x => AnswerRow("selectApplicationType.checkYourAnswersLabel", s"selectApplicationType.$x", true, routes.SelectApplicationTypeController.onPageLoad(CheckMode).url)
  }

  def whichBestDescribesYou: Option[AnswerRow] = userAnswers.get(WhichBestDescribesYouPage) map {
    x => AnswerRow("whichBestDescribesYou.checkYourAnswersLabel", s"whichBestDescribesYou.$x", true, routes.WhichBestDescribesYouController.onPageLoad(CheckMode).url)
  }

  def registeredAddressForEori: Option[AnswerRow] = userAnswers.get(RegisteredAddressForEoriPage) map {
    x => AnswerRow("registeredAddressForEori.checkYourAnswersLabel", s"${x.field1} ${x.field2}", false, routes.RegisteredAddressForEoriController.onPageLoad(CheckMode).url)
  }
}
