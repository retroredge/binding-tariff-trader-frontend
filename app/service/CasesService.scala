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

package service

import connectors.{BindingTariffClassificationConnector, EmailConnector}
import javax.inject.{Inject, Singleton}
import models.CaseStatus.CaseStatus
import models._
import play.api.Logger
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class CasesService @Inject()(connector: BindingTariffClassificationConnector, emailConnector: EmailConnector) {

  def create(c: NewCaseRequest)(implicit hc: HeaderCarrier): Future[Case] = {
    for {
      c <- connector.createCase(c)

      email = ApplicationSubmittedEmail(
        to = Seq(c.application.contact.email),
        parameters = ApplicationSubmittedParameters(
          c.application.contact.name,
          c.reference
        )
      )

      _ <- emailConnector.send(email) recover loggingAnError(c.reference)
    } yield c
  }

  private def loggingAnError(ref: String): PartialFunction[Throwable, Unit] = {
    case t: Throwable => Logger.error(s"Failed to send email for Application [$ref]", t)
  }

  def getCases(eori: String, statuses: Set[CaseStatus], pagination: Pagination, sort: Sort)
              (implicit hc: HeaderCarrier): Future[Paged[Case]] = {
    connector.findCasesBy(eori, statuses, pagination, sort)
  }

  def getCaseForUser(userEori: String, reference: String)(implicit hc: HeaderCarrier): Future[Case] = {
    getCase(reference, _.hasEoriNumber(userEori))
  }

  def getCaseWithRulingForUser(userEori: String, reference: String)(implicit hc: HeaderCarrier): Future[Case] = {
    getCase(reference, c => c.hasEoriNumber(userEori) && c.hasRuling)
  }


  private def getCase(reference: String, caseFilter: Case => Boolean)(implicit hc: HeaderCarrier): Future[Case] = {
    connector.findCase(reference).map(_.filter(caseFilter)) flatMap {
      case Some(c) => Future.successful(c)
      case _ => Future.failed(new RuntimeException("Case not found"))
    }
  }

}
