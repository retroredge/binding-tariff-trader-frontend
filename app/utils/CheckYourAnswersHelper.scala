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

package utils

import controllers.routes
import models.requests.DataRequest
import models.{CheckMode, UserAnswers}
import pages._
import viewmodels.AnswerRow

class CheckYourAnswersHelper(userAnswers: UserAnswers) {

  def uploadWrittenAuthorisation: Option[AnswerRow] = userAnswers.get(UploadWrittenAuthorisationPage) map {
    x => AnswerRow("uploadWrittenAuthorisation.checkYourAnswersLabel", x.name, false, routes.UploadWrittenAuthorisationController.onPageLoad(CheckMode).url)
  }

  def supportingInformationDetails: Option[AnswerRow] = userAnswers.get(SupportingInformationDetailsPage) map {
    x => AnswerRow("supportingInformationDetails.checkYourAnswersLabel", s"$x", false, routes.SupportingInformationDetailsController.onPageLoad(CheckMode).url)
  }

  def supportingInformation: Option[AnswerRow] = userAnswers.get(SupportingInformationPage) map {
    x => AnswerRow("supportingInformation.checkYourAnswersLabel", yesNoAnswer(x), true, routes.SupportingInformationController.onPageLoad(CheckMode).url)
  }

  def legalChallengeDetails: Option[AnswerRow] = userAnswers.get(LegalChallengeDetailsPage) map {
    x => AnswerRow("legalChallengeDetails.checkYourAnswersLabel", s"$x", false, routes.LegalChallengeDetailsController.onPageLoad(CheckMode).url)
  }

  def legalChallenge: Option[AnswerRow] = userAnswers.get(LegalChallengePage) map {
    x => AnswerRow("legalChallenge.checkYourAnswersLabel", yesNoAnswer(x), true, routes.LegalChallengeController.onPageLoad(CheckMode).url)
  }

  def commodityCodeRulingReference: Option[AnswerRow] = userAnswers.get(CommodityCodeRulingReferencePage) map {
    x => AnswerRow("commodityCodeRulingReference.checkYourAnswersLabel", s"$x", false, routes.CommodityCodeRulingReferenceController.onPageLoad(CheckMode).url)
  }

  def similarItemCommodityCode: Option[AnswerRow] = userAnswers.get(SimilarItemCommodityCodePage) map {
    x => AnswerRow("similarItemCommodityCode.checkYourAnswersLabel", yesNoAnswer(x), true, routes.SimilarItemCommodityCodeController.onPageLoad(CheckMode).url)
  }

  def returnSamples: Option[AnswerRow] = userAnswers.get(ReturnSamplesPage) map {
    x => AnswerRow("returnSamples.checkYourAnswersLabel", s"returnSamples.$x", true, routes.ReturnSamplesController.onPageLoad(CheckMode).url)
  }

  def whenToSendSample: Option[AnswerRow] = userAnswers.get(WhenToSendSamplePage) map {
    x => AnswerRow("whenToSendSample.checkYourAnswersLabel", yesNoAnswer(x), true, routes.WhenToSendSampleController.onPageLoad(CheckMode).url)
  }

  def commodityCodeDigits: Option[AnswerRow] = userAnswers.get(CommodityCodeDigitsPage) map {
    x => AnswerRow("commodityCodeDigits.checkYourAnswersLabel", s"$x", false, routes.CommodityCodeDigitsController.onPageLoad(CheckMode).url)
  }

  def commodityCodeBestMatch: Option[AnswerRow] = userAnswers.get(CommodityCodeBestMatchPage) map {
    x => AnswerRow("commodityCodeBestMatch.checkYourAnswersLabel", yesNoAnswer(x), true, routes.CommodityCodeBestMatchController.onPageLoad(CheckMode).url)
  }

  def supportingMaterialFileList: Option[AnswerRow] = {

    def constructRow: Seq[String] => AnswerRow = { content =>
      AnswerRow("supportingMaterialFileList.checkYourAnswersLabel", content, false, routes.SupportingMaterialFileListController.onPageLoad(CheckMode).url)
    }

    userAnswers.get(SupportingMaterialFileListPage) map {
      case filenames if filenames.nonEmpty => constructRow(filenames.map(_.name))
      case _ => constructRow(Seq("No files attached"))
    }
  }

  def describeYourItem: Option[AnswerRow] = userAnswers.get(DescribeYourItemPage) map {
    x => AnswerRow("describeYourItem.checkYourAnswersLabel", Seq(x.name, x.description, x.confidentialInformation.orNull), false, routes.DescribeYourItemController.onPageLoad(CheckMode).url)
  }

  def previousCommodityCode: Option[AnswerRow] = userAnswers.get(PreviousCommodityCodePage) map {
    x => AnswerRow("previousCommodityCode.checkYourAnswersLabel", s"${x.field1}", false, routes.PreviousCommodityCodeController.onPageLoad(CheckMode).url)
  }

  def enterContactDetails: Option[AnswerRow] = userAnswers.get(EnterContactDetailsPage) map {
    x => AnswerRow("enterContactDetails.checkYourAnswersLabel", Seq(x.field1, x.field2, x.field3.orNull), false, routes.EnterContactDetailsController.onPageLoad(CheckMode).url)
  }

  def registerBusinessRepresenting: Option[AnswerRow] = userAnswers.get(RegisterBusinessRepresentingPage) map {
    x =>
      AnswerRow("registerBusinessRepresenting.checkYourAnswersLabel",
        Seq(x.eoriNumber, x.businessName, x.addressLine1, x.town, x.postCode, x.country),
        false,
        routes.RegisterBusinessRepresentingController.onPageLoad(CheckMode).url)
  }

  def selectApplicationType: Option[AnswerRow] = userAnswers.get(SelectApplicationTypePage) map {
    x => AnswerRow("selectApplicationType.checkYourAnswersLabel", s"selectApplicationType.$x", true, routes.SelectApplicationTypeController.onPageLoad(CheckMode).url)
  }

  def importOrExport: Option[AnswerRow] = userAnswers.get(ImportOrExportPage) map {
    x => AnswerRow("importOrExport.checkYourAnswersLabel", s"importOrExport.$x", true, routes.ImportOrExportController.onPageLoad(CheckMode).url)
  }

  def whichBestDescribesYou: Option[AnswerRow] = userAnswers.get(WhichBestDescribesYouPage) map {
    x => AnswerRow("whichBestDescribesYou.checkYourAnswersLabel", s"whichBestDescribesYou.$x", true, routes.WhichBestDescribesYouController.onPageLoad(CheckMode).url)
  }

  def registeredAddressForEori(implicit request: DataRequest[_]): Option[AnswerRow] = userAnswers.get(RegisteredAddressForEoriPage) map { x =>
    val fields = if (request.eoriNumber.isDefined) {
      Seq(x.businessName, x.addressLine1, x.townOrCity, x.postcode, x.country)
    } else {
      Seq(x.eori, x.businessName, x.addressLine1, x.townOrCity, x.postcode, x.country)
    }
    AnswerRow("registeredAddressForEori.checkYourAnswersLabel", fields, false, routes.RegisteredAddressForEoriController.onPageLoad(CheckMode).url)
  }

  private def yesNoAnswer(x: Boolean) = if (x) "site.yes" else "site.no"

}
