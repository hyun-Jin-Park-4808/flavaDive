package fd.flavadive.entities

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "FLAVA_DIARY")
class FlavaDiary (

    @ManyToOne
    val restaurant: Restaurant,

    @ManyToOne
    val member: Member,

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