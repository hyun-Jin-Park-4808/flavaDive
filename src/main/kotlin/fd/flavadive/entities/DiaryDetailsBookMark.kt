package fd.flavadive.entities

import jakarta.persistence.*

@Entity
@Table(name = "DIARY_DETAILS_BOOK_MARK")
class DiaryDetailsBookMark (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flava_diary_details_id", nullable = false)
    val flavaDiaryDetails: FlavaDiaryDetails,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
): BaseEntity()