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

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import javax.inject.Inject
import models.requests.OptionalDataRequest
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Results}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future
import scala.concurrent.Future.successful

class SignOutController @Inject()(val appConfig: FrontendAppConfig,
                                  dataCacheConnector: DataCacheConnector,
                                  identify: IdentifierAction,
                                  getData: DataRetrievalAction,
                                  requireData: DataRequiredAction,
                                  val messagesApi: MessagesApi) extends FrontendController with I18nSupport {

  def startFeedbackSurvey: Action[AnyContent] = (identify andThen getData).async { implicit request =>
    clearDataCache(request)
    successful(SeeOther(appConfig.feedbackSurvey).withNewSession)
  }

  def forceSignOut: Action[AnyContent] = (identify andThen getData).async { implicit request =>
    clearDataCache(request)
    successful(Results.Redirect(routes.SessionExpiredController.onPageLoad()).withNewSession)
  }

  def keepAlive(): Action[AnyContent] = Action.async {
    implicit request => Future.successful(Ok("OK"))
  }

  private def clearDataCache(request: OptionalDataRequest[AnyContent]) = {
    request.userAnswers map { answer => dataCacheConnector.remove(answer.cacheMap) }
  }
}