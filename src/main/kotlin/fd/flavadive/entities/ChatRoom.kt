package fd.flavadive.entities

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "CHAT_ROOM")
class ChatRoom (

    @ManyToOne
    @JoinColumn(name = "HOST_ID")
    val member: Member,

    @ManyToOne
    val restaurant: Restaurant,

    var announcement: String
): BaseEntity()
