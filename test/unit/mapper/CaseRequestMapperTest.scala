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

package mapper

import models._
import pages._
import play.api.libs.json.{JsValue, Json, Writes}
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.test.UnitSpec

class CaseRequestMapperTest extends UnitSpec {

  private val mapper = new CaseRequestMapper()

  "Mapper" should {

    "Fail when mandatory fields are missing" in {
      intercept[IllegalStateException] {
        mapper.map(missingGoodDetails())
      }
    }

    "Map Mandatory Fields" in {
      // When
      val response = mapper.map(mandatoryAnswers())

      // Then Mandatory fields should be present
      response.attachments shouldBe Seq.empty

      val application = response.application

      val holder: EORIDetails = application.holder
      holder.eori shouldBe "Trader EORI"
      holder.businessName shouldBe "Trader Business Name"
      holder.addressLine1 shouldBe "Trader Address Line 1"
      holder.addressLine2 shouldBe "Trader Town"
      holder.postcode shouldBe "Trader Post Code"
      holder.country shouldBe "Trader Country"

      val contact: Contact = application.contact
      contact.name shouldBe "Name"
      contact.email shouldBe "Email"
      contact.phone shouldBe None

      application.offline shouldBe false
      application.goodName shouldBe "Good Name"
      application.goodDescription shouldBe "Good Description"
      application.sampleToBeProvided shouldBe true
      application.sampleToBeReturned shouldBe false

      // Then optional Fields should be blank
      application.agent shouldBe None
      application.confidentialInformation shouldBe None
      application.otherInformation shouldBe None
      application.reissuedBTIReference shouldBe None
      application.relatedBTIReference shouldBe None
      application.knownLegalProceedings shouldBe None
      application.envisagedCommodityCode shouldBe None
    }

    "Map Optional Fields" in {
      // When
      val response = mapper.map(allAnswers())

      // Then Mandatory fields should be present
      response.attachments shouldBe Seq.empty

      val application = response.application

      val holder: EORIDetails = application.holder
      holder.eori shouldBe "Trader Eori"
      holder.businessName shouldBe "Trader Business Name"
      holder.addressLine1 shouldBe "Trader Address Line 1"
      holder.addressLine2 shouldBe "Trader Town"
      holder.postcode shouldBe "Trader Post Code"
      holder.country shouldBe "Trader Country"

      val contact: Contact = application.contact
      contact.name shouldBe "Name"
      contact.email shouldBe "Email"
      contact.phone shouldBe Some("12345")

      val agent: AgentDetails = application.agent.get
      agent.eoriDetails.eori shouldBe "Agent EORI"
      agent.eoriDetails.businessName shouldBe "Agent Business Name"
      agent.eoriDetails.addressLine1 shouldBe "Agent Address Line 1"
      agent.eoriDetails.addressLine2 shouldBe "Agent Town"
      agent.eoriDetails.postcode shouldBe "Agent Post Code"
      agent.eoriDetails.country shouldBe "Agent Country"

      application.agent shouldBe Some(agent)
      application.offline shouldBe false
      application.goodName shouldBe "Good Name"
      application.goodDescription shouldBe "Good Description"
      application.sampleToBeProvided shouldBe true
      application.sampleToBeReturned shouldBe true

      // Then optional Fields should be blank
      application.confidentialInformation shouldBe Some("Confidential Info")
      application.otherInformation shouldBe Some("Other Info")
      application.reissuedBTIReference shouldBe Some("Reissued BTI Reference")
      application.relatedBTIReference shouldBe Some("Related BTI Reference")
      application.knownLegalProceedings shouldBe Some("Known Legal Proceedings")
      application.envisagedCommodityCode shouldBe Some("Envisaged Commodity Code")
      application.importOrExport shouldBe Some("IMPORT")
    }
  }

  // agent filling the form
  def allAnswers(): UserAnswers = {
    UserAnswers(
      CacheMap(
        "id",
        Map(
          RegisteredAddressForEoriPage.toString -> js(
            RegisteredAddressForEori(
              "Agent EORI",
              "Agent Business Name",
              "Agent Address Line 1",
              "Agent Town",
              "Agent Post Code",
              "Agent Country"
            )
          ),
          EnterContactDetailsPage.toString -> js(
            EnterContactDetails(
              "Name",
              "Email",
              Some("12345")
            )
          ),
          DescribeYourItemPage.toString -> js(
            DescribeYourItem(
              "Good Name",
              "Good Description",
              Some("Confidential Info")
            )
          ),
          WhichBestDescribesYouPage.toString -> js(
            WhichBestDescribesYou.BusinessRepresentative
          ),
          ImportOrExportPage.toString -> js(
            ImportOrExport.Import
          ),
          RegisterBusinessRepresentingPage.toString -> js(
            RegisterBusinessRepresenting(
              "Trader Eori",
              "Trader Business Name",
              "Trader Address Line 1",
              "Trader Town",
              "Trader Post Code",
              "Trader Country"
            )
          ),
          SupportingInformationDetailsPage.toString -> js("Other Info"),
          PreviousCommodityCodePage.toString -> js(
            PreviousCommodityCode(
              "Reissued BTI Reference"
            )
          ),
          CommodityCodeRulingReferencePage.toString -> js("Related BTI Reference"),
          LegalChallengeDetailsPage.toString -> js("Known Legal Proceedings"),
          CommodityCodeDigitsPage.toString -> js("Envisaged Commodity Code"),
          WhenToSendSamplePage.toString -> js(true),
          ReturnSamplesPage.toString -> js(
            ReturnSamples.Yes
          )
        )
      )
    )
  }

  // trader filling the form
  def mandatoryAnswers(): UserAnswers = {
    UserAnswers(
      CacheMap(
        "id",
        Map(
          RegisteredAddressForEoriPage.toString -> js(
            RegisteredAddressForEori(
              "Trader EORI",
              "Trader Business Name",
              "Trader Address Line 1",
              "Trader Town",
              "Trader Post Code",
              "Trader Country"
            )
          ),
          EnterContactDetailsPage.toString -> js(
            EnterContactDetails(
              "Name",
              "Email",
              None
            )
          ),
          DescribeYourItemPage.toString -> js(
            DescribeYourItem(
              "Good Name",
              "Good Description",
              None
            )
          ),
          WhenToSendSamplePage.toString -> js(true)
        )
      )
    )
  }

  def missingGoodDetails(): UserAnswers = {
    UserAnswers(
      CacheMap(
        "id",
        Map(
          RegisteredAddressForEoriPage.toString -> js(
            RegisteredAddressForEori(
              "Trader EORI",
              "Trader Business Name",
              "Trader Address Line 1",
              "Trader Town",
              "Trader Post Code",
              "Trader Country"
            )
          ),
          EnterContactDetailsPage.toString -> js(
            EnterContactDetails(
              "Name",
              "Email",
              Some("Phone")
            )
          ),
          WhenToSendSamplePage.toString -> js(true)
        )
      )
    )
  }

  def js[T](obj: T)(implicit writes: Writes[T]): JsValue = Json.toJson(obj)

}
