package fd.flavadive.entities

import jakarta.persistence.*

@Entity
@Table(name = "FLAVA_DIARY")
class FlavaDiary (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    val restaurant: Restaurant,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @OneToMany(mappedBy = "flavaDiary", cascade = [CascadeType.ALL], orphanRemoval = true)
    var diaryBookMarks: MutableList<DiaryBookMark> = mutableListOf(),

    @OneToMany(mappedBy = "flavaDiary", cascade = [CascadeType.ALL], orphanRemoval = true)
    var flavaDiaryDetails: MutableList<FlavaDiaryDetails> = mutableListOf(),

    var isPublic: Boolean,
    var isGlobal: Boolean,
    var receiptImagePath: String,
    var thumbnailPath: String,
    var overallReview: String,
    var serviceScore: Long,
    var speedScore: Long,
    var kindnessScore: Long,
    var cleanlinessScore: Long
): BaseEntity()