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

package controllers

import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import connectors.DataCacheConnector
import controllers.actions._
import config.FrontendAppConfig
import forms.AskForUploadSupportingMaterialFormProvider
import models.Mode
import pages._
import navigation.Navigator
import views.html.askForUploadSupportingMaterial

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AskForUploadSupportingMaterialController @Inject()(appConfig: FrontendAppConfig,
                                         override val messagesApi: MessagesApi,
                                         dataCacheConnector: DataCacheConnector,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: AskForUploadSupportingMaterialFormProvider
                                         ) extends FrontendController with I18nSupport {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(mode: Mode) = (identify andThen getData andThen requireData) {
    implicit request =>

      val preparedForm = request.userAnswers.get(AskForUploadSupportingMaterialPage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(askForUploadSupportingMaterial(appConfig, preparedForm, mode))
  }

  def onSubmit(mode: Mode) = (identify andThen getData andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(askForUploadSupportingMaterial(appConfig, formWithErrors, mode))),
        value => {
          val updatedAnswers = request.userAnswers.set(AskForUploadSupportingMaterialPage, value)

          val nextPage = if (value) UploadSupportingMaterialMultiplePage else CommodityCodeBestMatchPage

          dataCacheConnector.save(updatedAnswers.cacheMap).map(
            _ =>
              Redirect(navigator.nextPage(nextPage, mode)(updatedAnswers))
          )
        }
      )
  }
}