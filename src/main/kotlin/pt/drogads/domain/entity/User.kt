package pt.drogads.domain.entity

import io.quarkus.mongodb.panache.common.MongoEntity
import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoCompanion
import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoEntityBase
import io.quarkus.security.identity.SecurityIdentity
import io.smallrye.mutiny.Uni
import org.bson.types.ObjectId
import org.eclipse.microprofile.openapi.annotations.media.Schema
import javax.json.bind.annotation.JsonbTransient
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@MongoEntity(collection = "users")
class User : ReactivePanacheMongoEntityBase() {

    @JsonbTransient
    var id: ObjectId? = null

    @NotBlank
    @Size(max = 20)
    @Schema(title="User name", required = true)
    lateinit var username: String

    @NotBlank
    @Size(max = 120)
    @Schema(title="User password", required = true)
    lateinit var password: String

    @NotBlank
    @Email
    @Size(max = 50)
    @Schema(title="User email", required = true)
    lateinit var email: String

    @JsonbTransient
    var active: Boolean = true

    @JsonbTransient
    val roles: Set<Roles> = setOf(Roles.USER)

    enum class Roles {
        ADMIN, USER
    }

    companion object : ReactivePanacheMongoCompanion<User> {
        fun findByUsername(username: String) = find("username", username).firstResult()
        fun softDeleteUser(username: String): Uni<User> = findByUsername(username).chain { user ->
            user?.active =false
            user?.update()}

    }

    override fun toString(): String {
        return "User(id=$id, username='$username', email='$email', active=$active, roles=$roles)"
    }
}