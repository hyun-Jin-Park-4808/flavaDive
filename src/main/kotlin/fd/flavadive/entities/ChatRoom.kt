package fd.flavadive.entities

import jakarta.persistence.*

@Entity
@Table(name = "CHAT_ROOM")
class ChatRoom (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    var member: Member,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false, unique = true)
    val restaurant: Restaurant,

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true)
    var chatRoomMembers: MutableList<ChatRoomMember> = mutableListOf(),

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true)
    var chatMessage: MutableList<ChatMessage> = mutableListOf(),

    var announcement: String
): BaseEntity()
