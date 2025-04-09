package fd.flavadive.entities

import fd.flavadive.common.enums.NotificationType
import fd.flavadive.common.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "NOTIFICATION")
class Notification (
    @Enumerated(EnumType.STRING)
    val notificationType: NotificationType,

    @ManyToOne
    val member: Member,

    var content: String
): BaseEntity()