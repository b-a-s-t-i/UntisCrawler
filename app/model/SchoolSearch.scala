package model


case class SchoolSearchResult(address: String, server: String, displayName: String, loginName: String)

case class SchoolSearchSchoolList(schools: List[SchoolSearchResult])

case class SchoolSearchResponse(result: SchoolSearchSchoolList)
