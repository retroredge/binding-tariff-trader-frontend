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

import pages.{Page, WhichBestDescribesYouPage}
import play.api.libs.json._
import viewmodels.RadioOption

sealed trait WhichBestDescribesYou extends Page

object WhichBestDescribesYou {

  case object BusinessOwner extends WithName("businessOwner") with WhichBestDescribesYou
  case object BusinessRepresentative extends WithName("businessRepresentative") with WhichBestDescribesYou

  val values: Set[WhichBestDescribesYou] = Set(BusinessOwner, BusinessRepresentative)

  val options: Set[RadioOption] = values.map {
    value => RadioOption("whichBestDescribesYou", value.toString)
  }

  implicit val enumerable: Enumerable[WhichBestDescribesYou] =
    Enumerable(values.toSeq.map(v => v.toString -> v): _*)

  implicit object WhichBestDescribesYouWrites extends Writes[WhichBestDescribesYou] {
    def writes(whichBestDescribesYou: WhichBestDescribesYou): JsValue = Json.toJson(whichBestDescribesYou.toString)
  }

  implicit object WhichBestDescribesYouReads extends Reads[WhichBestDescribesYou] {
    override def reads(json: JsValue): JsResult[WhichBestDescribesYou] = json match {
      case JsString(BusinessOwner.toString) => JsSuccess(BusinessOwner)
      case JsString(BusinessRepresentative.toString) => JsSuccess(BusinessRepresentative)
      case _ => JsError("Unknown whichBestDescribesYou")
    }
  }

  def theUserIsAnAgent(answers: UserAnswers): Boolean = {
    answers.get(WhichBestDescribesYouPage).exists(_.isInstanceOf[WhichBestDescribesYou.BusinessRepresentative.type])
  }

}
