package model


case class TimetableUserData(personType: Int, personId: Int, klasseId: Int)

case class TimetableUserDataResponse(result: TimetableUserData)
