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

package navigation

import controllers.routes
import javax.inject.{Inject, Singleton}
import models._
import pages.{CommodityCodeRulingReferencePage, UploadWrittenAuthorisationPage, _}
import play.api.mvc.Call

@Singleton
class Navigator @Inject()() {

  private val routeMap: Map[Page, UserAnswers => Call] = Map(
    RegisteredAddressForEoriPage -> (_ => routes.RegisteredAddressForEoriController.onPageLoad(NormalMode)),
    EnterContactDetailsPage -> (_ => routes.EnterContactDetailsController.onPageLoad(NormalMode)),
    WhichBestDescribesYouPage -> (_ => routes.WhichBestDescribesYouController.onPageLoad(NormalMode)),
    SelectApplicationTypePage -> (_ => routes.SelectApplicationTypeController.onPageLoad(NormalMode)),
    RegisterBusinessRepresentingPage -> (_ => routes.RegisterBusinessRepresentingController.onPageLoad(NormalMode)),
    UploadWrittenAuthorisationPage -> (_ => routes.UploadWrittenAuthorisationController.onPageLoad(NormalMode)),
    AcceptItemInformationPage -> (_ => routes.AcceptItemInformationListController.onPageLoad()),
    PreviousCommodityCodePage -> (_ => routes.PreviousCommodityCodeController.onPageLoad(NormalMode)),
    DescribeYourItemPage -> (_ => routes.DescribeYourItemController.onPageLoad(NormalMode)),
    SupportingMaterialFileListPage -> (_ => routes.SupportingMaterialFileListController.onPageLoad(NormalMode)),
    UploadSupportingMaterialMultiplePage -> (_ => routes.UploadSupportingMaterialMultipleController.onPageLoad(NormalMode)),
    ImportOrExportPage -> (_ => routes.ImportOrExportController.onPageLoad(NormalMode)),
    CommodityCodeBestMatchPage -> (_ => routes.CommodityCodeBestMatchController.onPageLoad(NormalMode)),
    CommodityCodeDigitsPage -> (_ => routes.CommodityCodeDigitsController.onPageLoad(NormalMode)),
    WhenToSendSamplePage -> (_ => routes.WhenToSendSampleController.onPageLoad(NormalMode)),
    ReturnSamplesPage -> (_ => routes.ReturnSamplesController.onPageLoad(NormalMode)),
    SimilarItemCommodityCodePage -> (_ => routes.SimilarItemCommodityCodeController.onPageLoad(NormalMode)),
    CommodityCodeRulingReferencePage -> (_ => routes.CommodityCodeRulingReferenceController.onPageLoad(NormalMode)),
    LegalChallengePage -> (_ => routes.LegalChallengeController.onPageLoad(NormalMode)),
    LegalChallengeDetailsPage -> (_ => routes.LegalChallengeDetailsController.onPageLoad(NormalMode)),
    SupportingInformationPage -> (_ => routes.SupportingInformationController.onPageLoad(NormalMode)),
    SupportingInformationDetailsPage -> (_ => routes.SupportingInformationDetailsController.onPageLoad(NormalMode)),
    CheckYourAnswersPage -> (_ => routes.CheckYourAnswersController.onPageLoad()),
    DeclarationPage -> (_ => routes.DeclarationController.onPageLoad(NormalMode)),
    ConfirmationPage -> (_ => routes.ConfirmationController.onPageLoad())
  )

  private val checkRouteMap: Map[Page, UserAnswers => Call] = Map(
    LegalChallengeDetailsPage -> (_ => routes.LegalChallengeDetailsController.onPageLoad(CheckMode)),
    CommodityCodeRulingReferencePage -> (_ => routes.CommodityCodeRulingReferenceController.onPageLoad(CheckMode)),
    CommodityCodeDigitsPage -> (_ => routes.CommodityCodeDigitsController.onPageLoad(CheckMode)),
    ReturnSamplesPage -> (_ => routes.ReturnSamplesController.onPageLoad(CheckMode)),
    SupportingInformationDetailsPage -> (_ => routes.SupportingInformationDetailsController.onPageLoad(CheckMode)),
    PreviousCommodityCodePage -> (_ => routes.PreviousCommodityCodeController.onPageLoad(CheckMode)),
    RegisterBusinessRepresentingPage -> (_ => routes.RegisterBusinessRepresentingController.onPageLoad(CheckMode)),
    UploadWrittenAuthorisationPage -> (_ => routes.UploadWrittenAuthorisationController.onPageLoad(CheckMode))
  )

  def nextPage(page: Page, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode => routeMap.getOrElse(page, _ => routes.IndexController.getApplications())
    case CheckMode => checkRouteMap.getOrElse(page, _ => routes.CheckYourAnswersController.onPageLoad())
  }

}
