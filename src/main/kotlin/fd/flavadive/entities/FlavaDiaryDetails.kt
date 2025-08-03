package fd.flavadive.entities

import jakarta.persistence.*

@Entity
@Table(name = "FLAVA_DIARY_DETAILS")
class FlavaDiaryDetails (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flava_diary_id", nullable = false)
    val flavaDiary: FlavaDiary,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flavaDiaryDetails", cascade = [CascadeType.ALL], orphanRemoval = true)
    val diaryDetailsBookMarks: MutableList<DiaryDetailsBookMark> = mutableListOf(),

    var menuName: String,
    var menuImagePath: String,
    var menuPrice: Long,
    var review: String,
    var creamyLevel: Long,
    var oilyLevel: Long,
    var saltyLevel: Long,
    var sweetLevel: Long,
    var sourLevel: Long,
    var spicyLevel: Long,
    var overallFlavorLevel: Long
): BaseEntity()