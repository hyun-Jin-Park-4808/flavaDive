package fd.flavadive.entities

import fd.flavadive.common.enums.NotificationType
import fd.flavadive.common.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "DIARY_BOOK_MARK")
class DiaryBookMark (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flava_diary_id", nullable = false)
    val flavaDiary: FlavaDiary,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
): BaseEntity()