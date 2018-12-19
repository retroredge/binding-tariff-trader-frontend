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

package views

import forms.UploadWrittenAuthorisationFormProvider
import models.NormalMode
import play.api.data.Form
import views.behaviours.StringViewBehaviours
import views.html.uploadWrittenAuthorisation

class UploadWrittenAuthorisationViewSpec extends StringViewBehaviours {

  val messageKeyPrefix = "uploadWrittenAuthorisation"

  val form = new UploadWrittenAuthorisationFormProvider()()

  def createView = () => uploadWrittenAuthorisation(frontendAppConfig, form, None, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => uploadWrittenAuthorisation(frontendAppConfig, form, None, NormalMode)(fakeRequest, messages)

  "UploadWrittenAuthorisation view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)
  }
}