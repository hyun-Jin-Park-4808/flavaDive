package fd.flavadive.entities

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "RESTAURANT")
class Restaurant (

    @ManyToOne
    val member: Member,

    var name: String,
    var location: String,
    var thumbnailPath: String,
    var description: String,
    var score: Long,
    var isEntrance: Boolean,
    var isAdvancePaymentRequired: Boolean
): BaseEntity()
