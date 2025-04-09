package fd.flavadive.entities

import fd.flavadive.common.enums.NotificationType
import fd.flavadive.common.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "DIARY_BOOK_MARK")
class DiaryBookMark (

    @ManyToOne
    val flavaDiary: FlavaDiary,

    @ManyToOne
    val member: Member
): BaseEntity()