package fd.flavadive.entities

import jakarta.persistence.*

@Entity
@Table(name = "RESTAURANT")
class Restaurant (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    var member: Member,

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = [CascadeType.ALL], orphanRemoval = true)
    var chatRoom: ChatRoom,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    var reservations: MutableSet<Reservation> = mutableSetOf(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = [CascadeType.ALL], orphanRemoval = true)
    var restaurantBookMarks: MutableSet<RestaurantBookMark> = mutableSetOf(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    var flavaDiaries: MutableSet<FlavaDiary> = mutableSetOf(),

    var name: String,
    var location: String,
    var thumbnailPath: String,
    var description: String,
    var score: Long,
    var isEntrance: Boolean,
    var isAdvancePaymentRequired: Boolean
): BaseEntity()
