package model


case class MergedTimetableElement(longName: String, backColor: String, alternatename: String, name: String, id: Int, typee: Int)

object MergedTimetableElement{
  def apply(element: TimetableElement): MergedTimetableElement = MergedTimetableElement(element.longName, element.backColor, element.alternatename, element.name, element.id, element.typee)
}


case class MergedTimetablePeriodElement(missing: Boolean, orgId: Int, id: Int, state: String, typee: Int, elementInfo: MergedTimetableElement, orgElementInfo: Option[MergedTimetableElement])

object MergedTimetablePeriodElement{
  def apply(periodElement: TimetablePeriodElement, element: TimetableElement, origElement: Option[TimetableElement]): MergedTimetablePeriodElement = {
    MergedTimetablePeriodElement(
      periodElement.missing,
      periodElement.orgId,
      periodElement.id,
      periodElement.state,
      periodElement.typee,
      MergedTimetableElement(element),
      origElement.map(MergedTimetableElement(_))
    )
  }
}


case class MergedTimetablePeriod(startTime: Int,
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
                               elements: List[MergedTimetablePeriodElement],
                               code: Int,
                               priority: Int,
                               date: Int)

object MergedTimetablePeriod{
  def apply(period: TimetablePeriod, elements: List[TimetableElement]): MergedTimetablePeriod = {
    val d = period.elements.map{ e =>
      val element = elements.filter(elem => elem.id == e.id).head
      val origElement = elements.filter(elem => (elem.id == e.orgId)).headOption
      MergedTimetablePeriodElement(e, element, origElement)
    }
    MergedTimetablePeriod(period.startTime, period.endTime, period.lessonId, period.lessonNumber, period.hasInfo, period.cellState, period.is, period.lessonCode, period.lessonText, period.periodText, period.hasPeriodText, d, period.code, period.priority, period.date)
  }
}