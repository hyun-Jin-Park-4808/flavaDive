package fd.flavadive.entities

import fd.flavadive.common.enums.NotificationType
import fd.flavadive.common.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "RESTAURANT_BOOK_MARK")
class RestaurantBookMark (

    @ManyToOne
    val restaurant: Restaurant,

    @ManyToOne
    val member: Member
): BaseEntity()