package pt.drogads.domain

import io.quarkus.mongodb.panache.common.MongoEntity
import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoEntity
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@MongoEntity(collection = "user")
data class User(
    @NotBlank
    val username: String,
    @NotBlank
    val password: String,
    @Email
    val email: String,
): ReactivePanacheMongoEntity()