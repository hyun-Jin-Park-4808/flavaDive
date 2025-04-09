package fd.flavadive.entities

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "CHAT_ROOM_MEMBER")
class ChatRoomMember (

    @ManyToOne
    val member: Member,

    @ManyToOne
    val chatRoom: ChatRoom
): BaseEntity()
