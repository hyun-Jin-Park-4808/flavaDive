package fd.flavadive.entities

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "FLAVA_DIARY_DETAILS")
class FlavaDiaryDetails (

    @ManyToOne
    val flavaDiary: FlavaDiary,

    var menuName: String,
    var menuImagePath: String,
    var menuPrice: Long,
    var review: String,
    var creamyLevel: Long,
    var oilyLevel: Long,
    var saltyLevel: Long,
    var sweetLevel: Long,
    var sourLevel: Long,
    var spicyLevel: Long,
    var overallFlavorLevel: Long
): BaseEntity()