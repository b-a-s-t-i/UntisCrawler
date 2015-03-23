package model

import java.util.UUID

case class GenericResponse[A](userId: UUID, data: A)

case class MobileTimetableUserDataResponse(timetableElements: List[TimetableElementList], userData: TimetableUserData)