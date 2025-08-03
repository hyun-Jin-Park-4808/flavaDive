package fd.flavadive.entities

import jakarta.persistence.*

@Entity
@Table(name = "RESTAURANT_BOOK_MARK")
class RestaurantBookMark (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    val restaurant: Restaurant,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
): BaseEntity()