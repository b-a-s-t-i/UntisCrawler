package model

import com.fasterxml.jackson.annotation.JsonProperty


case class TimetableElement(longName: String, backColor: String, alternatename: String, name: String, id: Int, @JsonProperty("type")typee: Int)

case class TimetablePeriodElement(missing: Boolean, orgId: Int, id: Int, state: String, @JsonProperty("type")typee: Int)

case class TimetablePeriod( startTime: Int,
                            endTime: Int,
                            lessonId: Int,
                            lessonNumber: Int,
                            hasInfo: Boolean,
                            cellState: String,
                            is: Map[String, Boolean],
                            lessonCode: String,
                            lessonText: String,
                            periodText: String,
                            hasPeriodText: Boolean,
                            elements: List[TimetablePeriodElement],
                            code: Int,
                            priority: Int,
                            date: Int
                            )

case class TimetableData(elementPeriods: Map[String, List[TimetablePeriod]],elements: List[TimetableElement], elementIds: List[Int])

case class TimetableResponse(lastImportTimestamp: Long, data: TimetableData)

case class TimetableResponseWrapper(result: TimetableResponse, isSessionTimeout: Boolean)


case class TimetableElementList(elementType: Int, elementTypeLabel: String, elements: List[TimetableElement])

case class TimetableUserData(personType: Int, personId: Int, klasseId: Int)

case class TimetableUserDataResponse(result: TimetableUserData)
