package fd.flavadive.entities

import fd.flavadive.common.enums.NotificationType
import fd.flavadive.common.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "DIARY_DETAILS_BOOK_MARK")
class DiaryDetailsBookMark (

    @ManyToOne
    val flavaDiaryDetails: FlavaDiaryDetails,

    @ManyToOne
    val member: Member
): BaseEntity()