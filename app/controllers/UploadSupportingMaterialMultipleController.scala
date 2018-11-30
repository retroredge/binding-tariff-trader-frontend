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

package controllers

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.UploadSupportingMaterialMultipleFormProvider
import javax.inject.Inject
import models.{FileMetadata, FileWithMetadata, Mode}
import navigation.Navigator
import pages.{CommodityCodeBestMatchPage, UploadSupportingMaterialMultiplePage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.Files.TemporaryFile
import play.api.mvc.{Action, AnyContent, MultipartFormData}
import service.FilestoreService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.uploadSupportingMaterialMultiple

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Future.successful

class UploadSupportingMaterialMultipleController @Inject()(
                                                            appConfig: FrontendAppConfig,
                                                            override val messagesApi: MessagesApi,
                                                            dataCacheConnector: DataCacheConnector,
                                                            navigator: Navigator,
                                                            identify: IdentifierAction,
                                                            getData: DataRetrievalAction,
                                                            requireData: DataRequiredAction,
                                                            formProvider: UploadSupportingMaterialMultipleFormProvider,
                                                            service: FilestoreService
                                                          ) extends FrontendController with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>

      //      val preparedForm = request.userAnswers.get(UploadSupportingMaterialMultiplePage) match {
      //        case None => form
      //        case Some(value) => form //.fill(value)
      //      }

      val existingFiles = request.userAnswers.get(UploadSupportingMaterialMultiplePage).getOrElse(Seq.empty)

      Ok(uploadSupportingMaterialMultiple(appConfig, form, existingFiles, mode))
  }

  def onSubmit(mode: Mode): Action[MultipartFormData[TemporaryFile]] = (identify andThen getData andThen requireData)
    .async(parse.multipartFormData) {
      implicit request =>
        val files: Seq[FileWithMetadata] = request.body.files.map { file =>
          FileWithMetadata(
            file.ref,
            FileMetadata(
              fileName = file.filename,
              mimeType = file.contentType.getOrElse(throw new RuntimeException("Missing file type"))
            )
          )
        }.filter(!_.metadata.fileName.isEmpty)

        if (files.isEmpty) {
          successful(Redirect(navigator.nextPage(CommodityCodeBestMatchPage, mode)(request.userAnswers)))
        } else {
          Future.sequence(files.map(service.upload(_)))
            .flatMap(uploads => {
              val uploaded: Seq[FileMetadata] = request.userAnswers.get(UploadSupportingMaterialMultiplePage).getOrElse(Seq.empty)
              val updated = uploaded ++ uploads
              val updatedAnswers = request.userAnswers.set(UploadSupportingMaterialMultiplePage, updated)
              dataCacheConnector.save(updatedAnswers.cacheMap).map(
                _ =>
                  Redirect(navigator.nextPage(UploadSupportingMaterialMultiplePage, mode)(updatedAnswers))
              )
            })
        }
    }
}
